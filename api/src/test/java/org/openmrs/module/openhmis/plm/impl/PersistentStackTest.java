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

import junit.framework.Assert;
import org.junit.Test;
import org.openmrs.module.openhmis.plm.PersistentList;
import org.openmrs.module.openhmis.plm.PersistentListItem;
import org.openmrs.module.openhmis.plm.PersistentListProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PersistentStackTest extends PersistentListBaseTest {
	@Override
	protected PersistentList createList(PersistentListProvider mockedProvider) {
		return new PersistentStack(1, "test", mockedProvider);
	}

	@Override
	public void insert_shouldInsertTheItemAtTheSpecifiedIndexAndMoveTheExistingItems() throws Exception {
		Assert.fail("Not implemented");
	}

	/**
	 * @verifies Return the items in last in first out order
	 * @see PersistentStack#getItems()
	 */
	@Test
	public void getItems_shouldReturnTheItemsInLastInFirstOutOrder() throws Exception {
		PersistentListItem item1 = new PersistentListItem("1", null);
		PersistentListItem item2 = new PersistentListItem("2", null);
		PersistentListItem item3 = new PersistentListItem("3", null);

		list.add(item1, item2, item3);

		PersistentListItem[] items = list.getItems();
		assertNotNull(items);
		assertEquals(3, items.length);

		assertEquals(item3, items[0]);
		assertEquals(item2, items[1]);
		assertEquals(item1, items[2]);
	}

	/**
	 * @verifies Return items in last in first out order
	 * @see PersistentStack#getNextAndRemove()
	 */
	@Test
	public void getNextAndRemove_shouldReturnItemsInLastInFirstOutOrder() throws Exception {
		PersistentListItem item1 = new PersistentListItem("1", null);
		PersistentListItem item2 = new PersistentListItem("2", null);
		PersistentListItem item3 = new PersistentListItem("3", null);

		list.add(item1, item2, item3);

		PersistentListItem[] items = list.getItems();
		assertNotNull(items);
		assertEquals(3, items.length);

		PersistentListItem item = list.getNextAndRemove();
		assertNotNull(item);
		assertEquals(item3, item);

		item = list.getNextAndRemove();
		assertNotNull(item);
		assertEquals(item2, item);

		item = list.getNextAndRemove();
		assertNotNull(item);
		assertEquals(item1, item);
	}

	/**
	 * @verifies Return items in last in first out order
	 * @see PersistentStack#getNext()
	 */
	@Test
	public void getNext_shouldReturnItemsInLastInFirstOutOrder() throws Exception {
		PersistentListItem item1 = new PersistentListItem("1", null);
		PersistentListItem item2 = new PersistentListItem("2", null);

		list.add(item1, item2);

		PersistentListItem[] items = list.getItems();
		assertNotNull(items);
		assertEquals(2, items.length);

		PersistentListItem item = list.getNext();
		assertNotNull(item);
		assertEquals(item2, item);

		list.remove(item2);
		item = list.getNext();
		assertNotNull(item);
		assertEquals(item1, item);
	}
}
