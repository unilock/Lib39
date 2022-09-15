package com.unascribed.lib39.mesh.api;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.unascribed.lib39.core.Lib39Log;
import com.unascribed.lib39.util.api.NBTUtils;

import com.google.common.base.Ascii;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.Graphs;
import com.google.common.graph.MutableGraph;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class BlockNetwork<N extends BlockNetworkNode, T extends BlockNetworkNodeType> {

	private final BlockNetworkManager owner;
	private final BlockNetworkType<N, T> type;
	private final UUID id;
	private final MutableGraph<N> members = GraphBuilder.undirected()
			.allowsSelfLoops(true)
			.nodeOrder(ElementOrder.unordered())
			.build();
	
	private final Map<BlockPos, N> membersByPos = Maps.newHashMap();
	private final Multimap<BlockNetworkNodeType, N> membersByType = HashMultimap.create();

	
	protected BlockNetwork(BlockNetworkManager owner, BlockNetworkType<N, T> type, UUID id) {
		this.owner = owner;
		this.type = type;
		this.id = id;
	}

	/**
	 * <b>Dangerous.</b> Use sparingly.
	 */
	public void removeNodeDirectly(N n) {
		members.removeNode(n);
		membersByPos.remove(n.getPos(), n);
		membersByType.remove(n.getType(), n);
		owner.networksByPos.remove(n.getPos(), this);
	}
	
	/**
	 * <b>Dangerous.</b> Use sparingly.
	 */
	public void addNodeDirectly(N n) {
		if (membersByPos.containsKey(n.getPos())) {
			removeNodeDirectly(membersByPos.get(n.getPos()));
		}
		members.addNode(n);
		membersByPos.put(n.getPos(), n);
		membersByType.put(n.getType(), n);
		owner.networksByPos.put(n.getPos(), type, this);
	}
	
	public void removeNodeAt(BlockPos pos) {
		Lib39Log.trace("Removing {} from network {}", pos, id);
		if (membersByPos.containsKey(pos)) {
			N node = membersByPos.get(pos);
			Set<N> neighbors = Sets.newHashSet(members.adjacentNodes(node));
			removeNodeDirectly(node);
			if (neighbors.size() > 1) {
				for (N a : neighbors) {
					if (!members.nodes().contains(a)) continue;
					for (N b : neighbors) {
						if (!members.nodes().contains(b)) continue;
						if (a == b) continue;
						if (!isReachable(a, b)) {
							BlockNetwork<N, T> other = type.construct(owner, UUID.randomUUID());
							Lib39Log.debug("Splitting network {} of size {} off into {}", id, size(), other.id);
							Set<N> reachable = Graphs.reachableNodes(members, b);
							for (int p = 0; p < 3; p++) {
								for (N n : reachable) {
									if (p == 0) {
										other.addNodeDirectly(n);
									} else if (p == 1) {
										for (N n2 : members.adjacentNodes(n)) {
											other.members.putEdge(n, n2);
										}
									} else if (p == 2) {
										removeNodeDirectly(n);
									}
								}
							}
							other.update();
							Lib39Log.debug("Network {} is now of size {}, and {} is of size {}", id, size(), other.id, other.size());
							owner.addNetwork(other);
						}
					}
				}
			}
			update();
			owner.markDirty();
			if (isEmpty()) {
				Lib39Log.debug("Destroying empty network {}", id);
				owner.removeNetwork(this);
			}
		}
	}
	
	public void addNode(N node) {
		N cur = membersByPos.get(node.getPos());
		if (cur != null && cur.getType() == node.getType()) return;
		Set<BlockPos> neighbors = BlockNetworkManager.neighbors(node.getPos());
		if (cur != null && !isEmpty() && neighbors.stream().noneMatch(membersByPos::containsKey)) {
			throw new IllegalArgumentException("Cannot add orphan node at "+node.getPos().toShortString()+" to non-empty network");
		}
		addNodeDirectly(node);
		Lib39Log.debug("Adding {} to network {}", node.getPos().toShortString(), id);
		for (BlockPos neighbor : neighbors) {
			N n = membersByPos.get(neighbor);
			if (n != null) {
				members.putEdge(node, n);
			} else {
				@SuppressWarnings("unchecked") // the type argument ensures the generic matches
				BlockNetwork<N, T> other = (BlockNetwork<N, T>)owner.networksByPos.get(neighbor, type);
				if (other != null) {
					N originator = other.membersByPos.get(neighbor);
					if (originator != null) {
						Lib39Log.debug("Joining network {} of size {} and network {} of size {}", id, size(), other.id, other.size());
						for (int p = 0; p < 2; p++) {
							for (N no : other.members.nodes()) {
								if (p == 0) {
									addNodeDirectly(no);
								} else if (p == 1) {
									for (N no2 : other.members.adjacentNodes(no)) {
										members.putEdge(no, no2);
									}
								}
							}
						}
						members.putEdge(node, originator);
						Lib39Log.debug("Network {} is now of size {}. Destroying {}", id, size(), other.id);
						owner.removeNetwork(other);
					}
				}
			}
		}
		update();
		owner.markDirty();
	}
	
	public void clear() {
		for (BlockPos bp : membersByPos.keySet()) {
			owner.networksByPos.row(bp).remove(type, this);
		}
		members.nodes().clear();
		membersByPos.clear();
		membersByType.clear();
		update();
		owner.markDirty();
	}

	public void tick() {
		type.tick(this);
	}
	
	public void update() {
		type.update(this);
	}
	
	public BlockNetworkManager getOwner() {
		return owner;
	}
	
	public ServerWorld getWorld() {
		return owner.world;
	}
	
	public BlockNetworkType<N, T> getType() {
		return type;
	}
	
	public UUID getId() {
		return id;
	}
	
	private boolean isReachable(N from, N to) {
		return Graphs.reachableNodes(members, from).contains(to);
	}

	public int size() {
		return members.nodes().size();
	}
	
	public boolean isEmpty() {
		return members.nodes().isEmpty();
	}
	
	public MutableGraph<N> getMembers() {
		return members;
	}
	
	public Map<BlockPos, N> getMembersByPos() {
		return membersByPos;
	}
	
	public Multimap<BlockNetworkNodeType, N> getMembersByType() {
		return membersByType;
	}

	public void onAdded() {
		for (BlockPos bp : membersByPos.keySet()) {
			owner.networksByPos.put(bp, type, this);
		}
	}

	public void onRemoved() {
		for (BlockPos bp : membersByPos.keySet()) {
			owner.networksByPos.row(bp).remove(type, this);
		}
	}
	
	public void readNbt(NbtCompound compound) {
		NbtList li = compound.getList("Nodes", NbtType.COMPOUND);
		List<N> nodes = Lists.newArrayList();
		List<Map.Entry<N, int[]>> conns = Lists.newArrayList();
		clear();
		for (int i = 0; i < li.size(); i++) {
			NbtCompound en = li.getCompound(i);
			BlockPos pos = NBTUtils.listToBlockPos(en.getList("Pos", NbtType.INT));
			String typeStr = Ascii.toUpperCase(en.getString("Type"));
			T nodeType = null;
			for (T nt : type.getNodeTypes()) {
				if (nt.name().equals(typeStr)) {
					nodeType = nt;
					break;
				}
			}
			if (nodeType == null) {
				Lib39Log.warn("Unknown node type {} found in network {} at {} during deserialization. Discarding!", typeStr, id, pos.toShortString());
				break;
			}
			N n = type.deserializeNode(pos, nodeType, en);
			nodes.add(n);
			conns.add(Maps.immutableEntry(n, en.getIntArray("Conn")));
			addNodeDirectly(n);
		}
		for (Map.Entry<N, int[]> en : conns) {
			N n = en.getKey();
			int[] conn = en.getValue();
			for (int c : conn) {
				N cn = nodes.get(c);
				members.putEdge(n, cn);
			}
		}
		update();
	}
	
	public void writeNbt(NbtCompound compound) {
		Identifier typeId = BlockNetworkManager.getId(type);
		if (typeId == null) {
			Lib39Log.warn("Unregistered network type {} encountered while attempting to save network {}. Discarding!", type, id);
			return;
		}
		List<N> nodes = Lists.newArrayList(members.nodes());
		NbtList li = new NbtList();
		for (int i = 0; i < nodes.size(); i++) {
			N n = nodes.get(i);
			NbtCompound en = new NbtCompound();
			en.put("Pos", NBTUtils.blockPosToList(n.getPos()));
			en.putString("Type", Ascii.toLowerCase(n.getType().name()));
			IntArrayList conn = new IntArrayList();
			for (N ne : members.adjacentNodes(n)) {
				conn.add(nodes.indexOf(ne));
			}
			en.put("Conn", new NbtIntArray(conn.toIntArray()));
			li.add(en);
		}
		compound.put("Nodes", li);
		compound.putString("Type", typeId.toString());
	}
	
}
