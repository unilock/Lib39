import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.BasicFileAttributeView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.regex.Pattern

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

return {
	TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
	def zip = it.toPath()

	def fs = FileSystems.newFileSystem(zip, new HashMap<>())
	try {
		def time = FileTime.fromMillis(Long.parseLong('git show -s --format=%ct HEAD'.execute().text.trim())*1000)
		fs.getRootDirectories().each { d ->
			Files.walk(d).forEach {
				try {
					Files.getFileAttributeView(it, BasicFileAttributeView.class).setTimes(time, time, time)
				} catch (e) {}
			}
		}
		def refmap = fs.getPath("lib39-refmap.json")
		if (Files.exists(refmap)) {
			def gson = new GsonBuilder().setPrettyPrinting().create()
			def obj = gson.fromJson(new String(Files.readAllBytes(refmap), StandardCharsets.UTF_8), JsonObject.class)
			def objs = new HashMap<>()
			def splitApart = { subj, path ->
				subj.entrySet().each { inner ->
					def ik = inner.key
					def module = ik.split('/')[3]
					def io = objs.get(module)
					if (!io) {
						io = new JsonObject()
						objs.put(module, io)
					}
					path.each {
						def step = io.get(it)
						if (!step) {
							step = new JsonObject()
							io.add(it, step)
						}
						io = step
					}
					io.add(ik, inner.value)
				}
			}
			splitApart(obj.get('mappings'), ['mappings'])
			obj.get('data').entrySet().each { en ->
				splitApart(en.value, ['data', en.key])
			}
			def sorter
			sorter = {
				List<String> keys = new ArrayList<>()
				// no keySet, can't addAll... thanks google
				it.entrySet().each { en ->
					keys.add(en.key)
				}
				Collections.sort(keys)
				def out = new JsonObject()
				keys.each { k ->
					def ele = it.get(k)
					if (ele.isJsonObject()) {
						ele = sorter(ele)
					}
					out.add(k, ele)
				}
				return out
			}
			objs.entrySet().each {
				def sortedObj = sorter(it.value)
				Files.write(fs.getPath(it.key+'/lib39-'+it.key+'-refmap.json'), gson.toJson(it.value).getBytes(StandardCharsets.UTF_8))
			}
			Files.delete(refmap)
		}
		def aw = fs.getPath("lib39.accesswidener")
		if (Files.exists(aw)) {
			def txt = new String(Files.readAllBytes(aw), StandardCharsets.UTF_8)
			def m = Pattern.compile('accessible\tmethod\tlib39/DummyEntry\t(.*?)\t\\(\\)V\n(.*?)\naccessible\tmethod\tlib39/DummyEntry\tend\t\\(\\)V', Pattern.DOTALL).matcher(txt)
			while (m.find()) {
				def mod = m.group(1)
				def out = fs.getPath(mod+'/lib39-'+mod+'.accesswidener')
				Files.write(out, ("accessWidener v1 intermediary\n"+m.group(2)).getBytes(StandardCharsets.UTF_8))
			}
		}
	} finally {
		fs.close()
	}
}
