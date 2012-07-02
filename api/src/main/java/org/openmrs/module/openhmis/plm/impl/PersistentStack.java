package org.openmrs.module.openhmis.plm.impl;

import org.openmrs.module.openhmis.plm.PersistentListItem;
import org.openmrs.module.openhmis.plm.PersistentListProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * A persistent list which is implemented as a stack (first in, last out) data structure.
 */
public class PersistentStack extends PersistentListBase<Stack<PersistentListItem>> {
	public PersistentStack(String key, PersistentListProvider provider) {
		super(key, provider);
	}

	public PersistentStack(int id, String key, PersistentListProvider provider) {
		super(id, key, provider);
	}

	/**
	 * Gets the next {@link PersistentListItem} without removing the item from the list.
	 * @return The next {@link PersistentListItem} or {@code null} if no items are defined.
	 * @should Return items in last in first out order
	 */
	@Override
	public PersistentListItem getNext() {
		if (cachedItems.size() == 0) {
			return null;
		} else {
			return cachedItems.peek();
		}
	}

	/**
	 * Gets the next {@link PersistentListItem} and removes it from the list.
	 * @return The next {@link PersistentListItem}.
	 * @should Return items in last in first out order
	 */
	@Override
	public PersistentListItem getNextAndRemove() {
		if (cachedItems.size() == 0) {
			return null;
		} else {
			return cachedItems.pop();
		}
	}

	/**
	 * Gets all the {@link PersistentListItem}'s in the list in last-in, first-out order.
	 * @return The list {@link PersistentListItem}'s.
	 * @should Return the items in last in first out order
	 */
	@Override
	public PersistentListItem[] getItems() {
		//The items must be reversed so that the first item is at index 0
		List<PersistentListItem> list = Arrays.asList(super.getItems());
		Collections.reverse(list);

		return list.toArray(new PersistentListItem[list.size()]);
	}

	@Override
	protected Stack<PersistentListItem> initializeCache() {
		return new Stack<PersistentListItem>();
	}

	@Override
	protected int getItemIndex(PersistentListItem item) {
		/*
			New items are added to the front of the list.
		    Rather than have to reorder each item order every time a new item is added
		    simply negate the size:
		        Item 1: 0
				Item 2: -1
				Item 3: -2
		*/
		int index = cachedItems.indexOf(item);
		if (index < 0) {
			// New items go to the end of the queue
			index = cachedItems.size();
		}

		return index * -1;
	}
}
