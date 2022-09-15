package com.unascribed.lib39.conflagration.api;

import java.util.Optional;
import java.util.function.Consumer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public interface ConfigValueType<T, W extends ClickableWidget> {

	/**
	 * Unmarshal the given string into the expected type, or an empty Optional if parsing fails.
	 */
	Optional<T> unmarshal(String s);
	/**
	 * Marshal the given instance into a string for storage in the config.
	 */
	String marshal(T value);
	
	/**
	 * Return a newly constructed widget that can be used to configure this value in the GUI, or an
	 * empty Optional if doing so is not practical.
	 * <p>
	 * <b><i>This method must be annotated as <code>@Environment(EnvType.CLIENT)</code>!</i></b>
	 * @param desc the Narrator description of the key
	 * @param x the x coordinate of the widget
	 * @param y the y coordinate of the widget
	 * @param width the maximum width of the widget
	 * @param height the maximum height of the widget
	 * @param currentValue the current value of the option that should be used to initialize the widget
	 * @param updateCallback a consumer that must be called when the widget's state is modified
	 */
	@Environment(EnvType.CLIENT)
	Optional<W> createWidget(Text desc, int x, int y, int width, int height, T currentValue, Consumer<T> updateCallback);
	
}
