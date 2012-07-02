package org.openmrs.module.openhmis.plm.impl;

import org.junit.Test;
import org.openmrs.module.openhmis.plm.PersistentList;
import org.openmrs.module.openhmis.plm.PersistentListItem;
import org.openmrs.module.openhmis.plm.PersistentListProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PersistentQueueTest extends PersistentListTestBase {
	@Override
	protected PersistentList createList(PersistentListProvider provider) {
		PersistentQueue queue = new PersistentQueue(1, "test", provider);
		queue.initialize();

		return queue;
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
		assertNotNull(items);
		assertEquals(3, items.length);

		assertEquals(item1, items[0]);
		assertEquals(item2, items[1]);
		assertEquals(item3, items[2]);
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
		assertNotNull(items);
		assertEquals(3, items.length);

		PersistentListItem item = list.getNextAndRemove();
		assertNotNull(item);
		assertEquals(item1, item);

		item = list.getNextAndRemove();
		assertNotNull(item);
		assertEquals(item2, item);

		item = list.getNextAndRemove();
		assertNotNull(item);
		assertEquals(item3, item);
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
		assertNotNull(items);
		assertEquals(2, items.length);

		PersistentListItem item = list.getNext();
		assertNotNull(item);
		assertEquals(item1, item);

		list.remove(item);
		item = list.getNext();
		assertNotNull(item);
		assertEquals(item2, item);
	}
}

