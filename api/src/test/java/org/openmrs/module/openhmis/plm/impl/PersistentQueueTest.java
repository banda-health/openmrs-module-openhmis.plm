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

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.openhmis.plm.PersistentList;
import org.openmrs.module.openhmis.plm.PersistentListItem;
import org.openmrs.module.openhmis.plm.PersistentListProvider;

public class PersistentQueueTest extends PersistentListBaseTest {
	@Override
	protected PersistentList createList(PersistentListProvider mockedProvider) {
		return new PersistentQueue(1, "test", mockedProvider);
	}

	/**
	 * @verifies insert the item at the specified index and move the existing items
	 * @see PersistentList#insert(int, org.openmrs.module.openhmis.plm.PersistentListItem)
	 */
	@Test
	@Override
	public void insert_shouldInsertTheItemAtTheSpecifiedIndexAndMoveTheExistingItems() throws Exception {
		PersistentListItem item1 = new PersistentListItem("1", null);
		PersistentListItem item2 = new PersistentListItem("2", null);
		PersistentListItem item3 = new PersistentListItem("3", null);
		PersistentListItem item4 = new PersistentListItem("4", null);
		PersistentListItem item5 = new PersistentListItem("5", null);

		// Add some items to the list
		list.add(item1, item2, item3);
		Assert.assertEquals(3, list.getSize());

		// Insert the item in the middle
		list.insert(1, item4);

		// Now check the order
		PersistentListItem[] items = list.getItems();
		Assert.assertEquals(4, items.length);
		Assert.assertEquals(item1, items[0]);
		Assert.assertEquals(item4, items[1]);
		Assert.assertEquals(item2, items[2]);
		Assert.assertEquals(item3, items[3]);

		list.insert(3, item5);

		items = list.getItems();
		Assert.assertEquals(5, items.length);
		Assert.assertEquals(item1, items[0]);
		Assert.assertEquals(item4, items[1]);
		Assert.assertEquals(item2, items[2]);
		Assert.assertEquals(item5, items[3]);
		Assert.assertEquals(item3, items[4]);
	}

	@Test
	@Override
	public void insert_shouldInsertTheItemProperlyAtTheBeginningOfTheList() throws Exception {
		PersistentListItem item1 = new PersistentListItem("1", null);
		PersistentListItem item2 = new PersistentListItem("2", null);
		PersistentListItem item3 = new PersistentListItem("3", null);
		PersistentListItem item4 = new PersistentListItem("4", null);

		// Add some items to the list
		list.add(item1, item2, item3);
		Assert.assertEquals(3, list.getSize());

		// Insert the item at the start
		list.insert(0, item4);

		// Now check the order
		PersistentListItem[] items = list.getItems();
		Assert.assertEquals(4, items.length);
		Assert.assertEquals(item4, items[0]);
		Assert.assertEquals(item1, items[1]);
		Assert.assertEquals(item2, items[2]);
		Assert.assertEquals(item3, items[3]);
	}

	@Test
	@Override
	public void insert_shouldInsertTheItemProperlyAtTheEndOfTheList() throws Exception {
		PersistentListItem item1 = new PersistentListItem("1", null);
		PersistentListItem item2 = new PersistentListItem("2", null);
		PersistentListItem item3 = new PersistentListItem("3", null);
		PersistentListItem item4 = new PersistentListItem("4", null);

		// Add some items to the list
		list.add(item1, item2, item3);
		Assert.assertEquals(3, list.getSize());

		// Insert the item at the end
		list.insert(3, item4);

		// Now check the order
		PersistentListItem[] items = list.getItems();
		Assert.assertEquals(4, items.length);
		Assert.assertEquals(item1, items[0]);
		Assert.assertEquals(item2, items[1]);
		Assert.assertEquals(item3, items[2]);
		Assert.assertEquals(item4, items[3]);
	}

	/**
	 * @verifies Return items in first in first out order
	 * @see PersistentQueue#getNext()
	 */
	@Test
	public void getNext_shouldReturnItemsInFirstInFirstOutOrder() throws Exception {
		PersistentListItem item1 = new PersistentListItem("1", null);
		PersistentListItem item2 = new PersistentListItem("2", null);
		PersistentListItem item3 = new PersistentListItem("3", null);

		list.add(item1, item2, item3);

		PersistentListItem[] items = list.getItems();
		Assert.assertNotNull(items);
		Assert.assertEquals(3, items.length);

		Assert.assertEquals(item1, items[0]);
		Assert.assertEquals(item2, items[1]);
		Assert.assertEquals(item3, items[2]);
	}

	/**
	 * @verifies Return the next item in first in first out order
	 * @see org.openmrs.module.openhmis.plm.impl.PersistentQueue#getNextAndRemove()
	 */
	@Test
	public void getNextAndRemove_shouldReturnTheNextItemInFirstInFirstOutOrder() {
		PersistentListItem item1 = new PersistentListItem("1", null);
		PersistentListItem item2 = new PersistentListItem("2", null);
		PersistentListItem item3 = new PersistentListItem("3", null);

		list.add(item1, item2, item3);

		PersistentListItem[] items = list.getItems();
		Assert.assertNotNull(items);
		Assert.assertEquals(3, items.length);

		PersistentListItem item = list.getNextAndRemove();
		Assert.assertNotNull(item);
		Assert.assertEquals(item1, item);

		item = list.getNextAndRemove();
		Assert.assertNotNull(item);
		Assert.assertEquals(item2, item);

		item = list.getNextAndRemove();
		Assert.assertNotNull(item);
		Assert.assertEquals(item3, item);
	}

	/**
	 * @verifies Return the next item in first in first out order
	 * @see PersistentQueue#getNext()
	 */
	@Test
	public void getNext_shouldReturnTheNextItemInFirstInFirstOutOrder() throws Exception {
		PersistentListItem item1 = new PersistentListItem("1", null);
		PersistentListItem item2 = new PersistentListItem("2", null);

		list.add(item1, item2);

		PersistentListItem[] items = list.getItems();
		Assert.assertNotNull(items);
		Assert.assertEquals(2, items.length);

		PersistentListItem item = list.getNext();
		Assert.assertNotNull(item);
		Assert.assertEquals(item1, item);

		list.remove(item);
		item = list.getNext();
		Assert.assertNotNull(item);
		Assert.assertEquals(item2, item);
	}

	/**
	 * @verifies Return the item at the specified index
	 * @see PersistentList#getItemAt(int)
	 */
	@Test
	@Override
	public void getItemAt_shouldReturnTheItemAtTheSpecifiedIndex() throws Exception {
		PersistentListItem item1 = new PersistentListItem("1", null);
		PersistentListItem item2 = new PersistentListItem("2", null);
		PersistentListItem item3 = new PersistentListItem("3", null);
		PersistentListItem item4 = new PersistentListItem("4", null);

		list.add(item1, item2, item3, item4);

		PersistentListItem item = list.getItemAt(0);
		Assert.assertEquals(item1, item);

		item = list.getItemAt(1);
		Assert.assertEquals(item2, item);

		item = list.getItemAt(3);
		Assert.assertEquals(item4, item);
	}
}

