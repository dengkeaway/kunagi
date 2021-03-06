package scrum.client.common;

import java.util.HashSet;
import java.util.Set;

public class BlockListSelectionManager {

	private Set<BlockListWidget> lists = new HashSet<BlockListWidget>();

	public void clear() {
		lists.clear();
	}

	public void add(BlockListWidget list) {
		lists.add(list);
	}

	public void deselectAll() {
		for (BlockListWidget list : lists)
			list.collapseAll();
	}

	public boolean select(Object object) {
		for (BlockListWidget list : lists) {
			if (list.contains(object) && list.showObject(object)) return true;
		}
		return false;
	}
}
