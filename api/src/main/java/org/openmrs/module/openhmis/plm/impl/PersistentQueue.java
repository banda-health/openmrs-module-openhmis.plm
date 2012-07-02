package org.openmrs.module.openhmis.plm.impl;

import org.openmrs.module.openhmis.plm.PersistentListItem;
import org.openmrs.module.openhmis.plm.PersistentListProvider;

import java.util.LinkedList;

/**
 * A persistent list which is implemented as a queue (first in, first out) data structure.
 */
public class PersistentQueue extends PersistentListBase<LinkedList<PersistentListItem>> {
	public PersistentQueue(String key, PersistentListProvider provider) {
		super(key, provider);
	}

	public PersistentQueue(int id, String key, PersistentListProvider provider) {
		super(id, key, provider);
	}

	/**
	 * Gets the next {@link PersistentListItem} without removing the item from the list.
	 * @return The next {@link PersistentListItem} or {@code null} if no items are defined.
	 * @should Return the next item in first in first out order
	 */
	@Override
	public PersistentListItem getNext() {
		return cachedItems.peek();
	}

	/**
	 * Gets the next {@link PersistentListItem} and removes it from the list.
	 * @return The next {@link PersistentListItem} or {@code null} if no items are defined.
	 * @should Return the next item in first in first out order
	 */
	@Override
	public PersistentListItem getNextAndRemove() {
		if (cachedItems.size() == 0) {
			return null;
		} else {
			return cachedItems.pop();
		}
	}

	@Override
	protected LinkedList<PersistentListItem> initializeCache() {
		return new LinkedList<PersistentListItem>();
	}

	@Override
	protected int getItemIndex(PersistentListItem item) {
		int index = cachedItems.indexOf(item);
		if (index < 0) {
			// New items go to the end of the queue
			index = cachedItems.size();
		}

		return index;
	}
}

