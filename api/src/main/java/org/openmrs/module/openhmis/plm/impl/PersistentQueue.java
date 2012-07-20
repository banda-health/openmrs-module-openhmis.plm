/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

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

	@Override
	protected LinkedList<PersistentListItem> initializeCache() {
		return new LinkedList<PersistentListItem>();
	}

	@Override
	protected void insertItem(int index, PersistentListItem item) {
		cachedItems.add(index, item);
	}

	/**
	 * Gets the next {@link PersistentListItem} without removing the item from the list.
	 * @return The next {@link PersistentListItem} or {@code null} if no items are defined.
	 */
	@Override
	protected PersistentListItem getNextItem() {
		return cachedItems.peek();
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

	@Override
	protected PersistentListItem getItemByIndex(int index) {
		return cachedItems.get(index);
	}
}

