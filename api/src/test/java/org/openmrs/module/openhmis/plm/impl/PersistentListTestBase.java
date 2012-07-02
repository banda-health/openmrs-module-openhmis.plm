package org.openmrs.module.openhmis.plm.impl;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.openhmis.plm.*;
import org.openmrs.module.openhmis.plm.test.TestPersistentListProvider;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.hasItems;

public abstract class PersistentListTestBase {
	protected PersistentListProvider provider;
	protected PersistentList list;

	protected abstract PersistentList createList(PersistentListProvider provider);

	@Before
	public void before() {
		provider = new TestPersistentListProvider();
		list = createList(provider);
	}

	/**
	 * @verifies Return the number of items
	 * @see org.openmrs.module.openhmis.plm.PersistentList#getSize()
	 */
	@Test
	public void getSize_shouldReturnNumberOfItems() {
		Assert.assertEquals(0, list.getSize());

		list.add(new PersistentListItem("1", null));
		Assert.assertEquals(1, list.getSize());

		list.add(new PersistentListItem("2", null));
		Assert.assertEquals(2, list.getSize());

		list.add(new PersistentListItem("3", null));
		Assert.assertEquals(3, list.getSize());
	}

	/**
	 * @verifies Return an empty array when there are no items
	 * @see org.openmrs.module.openhmis.plm.PersistentList#getSize()
	 */
	@Test
	public void getSize_shouldReturnEmptyArrayWhenNoItems() {
		Assert.assertEquals(0, list.getSize());

		PersistentListItem[] items =  list.getItems();
		Assert.assertNotNull(items);
		Assert.assertEquals(0, items.length);
	}

	/**
	 * @verifies Return items that have been added
	 * @see org.openmrs.module.openhmis.plm.PersistentList#getItems()
	 */
	@Test
	public void getItems_shouldReturnItemsThatHaveBeenAdded() {
		PersistentListItem item = new PersistentListItem("1", null);
		list.add(item);

		Assert.assertEquals(1, list.getSize());

		PersistentListItem[] items = list.getItems();
		Assert.assertNotNull(items);
		Assert.assertEquals(1, items.length);
		Assert.assertEquals(item, items[0]);
	}

	/**
	 * @verifies Add a single item
	 * @see org.openmrs.module.openhmis.plm.PersistentList#add(org.openmrs.module.openhmis.plm.PersistentListItem...)
	 */
	@Test
	public void add_shouldAddASingleItem() {
		PersistentListItem item = new PersistentListItem("1", null);
		list.add(item);

		Assert.assertEquals(1, list.getSize());

		PersistentListItem[] items = list.getItems();
		Assert.assertNotNull(items);
		Assert.assertEquals(1, items.length);
		assertEquals(item.getKey(), items[0].getKey());
	}

	/**
	 * @verifies Add multiple items
	 * @see org.openmrs.module.openhmis.plm.PersistentList#add(org.openmrs.module.openhmis.plm.PersistentListItem...)
	 */
	@Test
	public void add_shouldAddMultipleItems() {
		PersistentListItem item = new PersistentListItem("1", null);
		PersistentListItem item2 = new PersistentListItem("2", null);
		list.add(item, item2);

		Assert.assertEquals(2, list.getSize());

		PersistentListItem[] items = list.getItems();
		Assert.assertNotNull(items);
		Assert.assertEquals(2, items.length);

		// Just check that the items are in the list, order is not important
		List<PersistentListItem> itemList = Arrays.asList(items);
		assertThat(itemList, hasItems(item, item2));
	}

	/**
	 * @verifies throw PersistentListException when duplicate items are added
	 * @see org.openmrs.module.openhmis.plm.PersistentList#add(org.openmrs.module.openhmis.plm.PersistentListItem...)
	 */
	@Test(expected = PersistentListException.class)
	public void shouldThrowExceptionWhenDuplicateItemsAreAdded() {
		PersistentListItem item1 = new PersistentListItem("1", null);
		PersistentListItem item2 = new PersistentListItem("1", null);

		list.add(item1);
		list.add(item2);
	}

	/**
	 * @verifies Return all list items
	 * @see org.openmrs.module.openhmis.plm.PersistentList#getItems()
	 */
	@Test
	public void getItems_shouldReturnAllListItems() {
		PersistentListItem item1 = new PersistentListItem("1", null);
		PersistentListItem item2 = new PersistentListItem("2", null);
		PersistentListItem item3 = new PersistentListItem("3", null);

		list.add(item1, item2, item3);

		Assert.assertEquals(3, list.getSize());

		PersistentListItem[] items = list.getItems();
		Assert.assertNotNull(items);
		Assert.assertEquals(3, items.length);

		// Just check that the items are in the list, order is not important
		List<PersistentListItem> itemList = Arrays.asList(items);
		assertThat(itemList, hasItems(item1, item2, item3));
	}

	/**
	 * @verifies Remove the item
	 * @see org.openmrs.module.openhmis.plm.PersistentList#remove(org.openmrs.module.openhmis.plm.PersistentListItem)
	 */
	@Test
	public void remove_shouldRemoveItem() {
		PersistentListItem item = new PersistentListItem("1", null);
		list.add(item);

		PersistentListItem[] items = list.getItems();

		Assert.assertNotNull(items);
		Assert.assertEquals(1, items.length);
		Assert.assertEquals(item, items[0]);

		list.remove(item);

		Assert.assertEquals(0, list.getSize());
	}

	/**
	 * @verifies Return true if the item was removed
	 * @see org.openmrs.module.openhmis.plm.PersistentList#remove(org.openmrs.module.openhmis.plm.PersistentListItem)
	 */
	@Test
	public void remove_shouldReturnTrueIfItemWasRemoved() {
		PersistentListItem item = new PersistentListItem("1", null);
		list.add(item);

		PersistentListItem[] items = list.getItems();

		Assert.assertNotNull(items);
		Assert.assertEquals(1, items.length);

		boolean result = list.remove(item);

		Assert.assertEquals(true, result);
	}

	/**
	 * @verifies Return false if the item was not removed
	 * @see org.openmrs.module.openhmis.plm.PersistentList#remove(org.openmrs.module.openhmis.plm.PersistentListItem)
	 */
	@Test
	public void remove_shouldReturnFalseIfItemWasNotRemoved() {
		boolean result = list.remove(new PersistentListItem("1", null));

		Assert.assertEquals(0, list.getSize());
		Assert.assertEquals(false, result);
	}

	/**
	 * @verifies Remove all items
	 * @see org.openmrs.module.openhmis.plm.PersistentList#clear()
	 */
	@Test
	public void clear_shouldRemoveAllItems() {
		PersistentListItem item = new PersistentListItem("1", null);
		PersistentListItem item2 = new PersistentListItem("2", null);
		list.add(item);
		list.add(item2);

		Assert.assertEquals(2, list.getSize());

		list.clear();

		Assert.assertEquals(0, list.getSize());
	}

	/**
	 * @verifies Not throw an exception when list is empty
	 * @see org.openmrs.module.openhmis.plm.PersistentList#clear()
	 */
	@Test
	public void clear_shouldNotThrowAnExceptionWhenListIsEmpty() {
		Assert.assertEquals(0, list.getSize());
		list.clear();
		Assert.assertEquals(0, list.getSize());
	}

	/**
	 * @verifies Not remove item from list
	 * @see org.openmrs.module.openhmis.plm.PersistentList#getNext()
	 */
	@Test
	public void getNext_shouldNotRemoveItemFromList() {
		PersistentListItem item = new PersistentListItem("1", null);
		list.add(item);

		assertEquals(1, list.getSize());
		item = list.getNext();

		assertNotNull(item);
		assertEquals(1, list.getSize());
	}

	/**
	 * @verifies Return null when list is empty
	 * @see org.openmrs.module.openhmis.plm.PersistentList#getNext()
	 */
	@Test
	public void getNext_shouldReturnNullWhenListIsEmpty() {
		PersistentListItem item = list.getNext();

		assertNull(item);
	}

	/**
	 * @verifies Return and remove item
	 * @see org.openmrs.module.openhmis.plm.PersistentList#getNextAndRemove()
	 */
	@Test
	public void getNextAndRemove_shouldReturnAndRemoveItem() {
		PersistentListItem item = new PersistentListItem("1", null);
		list.add(item);

		assertEquals(1, list.getSize());

		item = list.getNextAndRemove();

		assertNotNull(item);
		assertEquals(0, list.getSize());
	}

	/**
	 * @verifies Return null when list is empty
	 * @see org.openmrs.module.openhmis.plm.PersistentList#getNextAndRemove()
	 */
	@Test
	public void getNextAndRemoveShouldReturnNullWhenEmpty() {
		PersistentListItem item = list.getNextAndRemove();

		assertNull(item);
	}

	/**
	 * @verifies Fire the itemAdded event
	 * @see org.openmrs.module.openhmis.plm.PersistentList#add(org.openmrs.module.openhmis.plm.PersistentListItem...)
	 */
	@Test
	public void add_shouldFireTheItemAddedEvent() {
		TestListEventListener listener = new TestListEventListener();
		list.addEventListener(listener);

		PersistentListItem item = new PersistentListItem("key", null);
		list.add(item);

		Assert.assertEquals(1, listener.added);
		Assert.assertEquals(0, listener.removed);
		Assert.assertEquals(0, listener.cleared);
	}

	/**
	 * @verifies Fire the itemAdded event for each item added
	 * @see org.openmrs.module.openhmis.plm.PersistentList#add(org.openmrs.module.openhmis.plm.PersistentListItem...)
	 */
	@Test
	public void add_shouldFireAddEventForEachItemAdded() {
		TestListEventListener listener = new TestListEventListener();
		list.addEventListener(listener);

		PersistentListItem item = new PersistentListItem("key", null);
		PersistentListItem item2 = new PersistentListItem("key2", null);
		PersistentListItem item3 = new PersistentListItem("key3", null);
		list.add(item, item2, item3);

		Assert.assertEquals(3, listener.added);
		Assert.assertEquals(0, listener.removed);
		Assert.assertEquals(0, listener.cleared);
	}

	/**
	 * @verifies Reference the correct list and item when firing the itemAdded event
	 * @see org.openmrs.module.openhmis.plm.PersistentList#add(org.openmrs.module.openhmis.plm.PersistentListItem...)
	 */
	@Test
	public void add_shouldReferenceCorrectListAndItemWhenItemAddedEvent() {
		final PersistentListItem item = new PersistentListItem("key", null);

		list.addEventListener(new ListEventListenerAdapter() {
			@Override
			public void itemAdded(ListEvent event) {
				Assert.assertEquals(list, event.getSource());
				Assert.assertEquals(item, event.getItem());
				Assert.assertEquals(ListEvent.ListOperation.ADDED, event.getOperation());
			}
		});

		list.add(item);
	}

	/**
	 * @verifies Fire the itemRemoved event
	 * @see org.openmrs.module.openhmis.plm.PersistentList#remove(org.openmrs.module.openhmis.plm.PersistentListItem)
	 */
	@Test
	public void remove_shouldFireRemoveEvent() {
		PersistentListItem item = new PersistentListItem("key", null);
		list.add(item);

		TestListEventListener listener = new TestListEventListener();
		list.addEventListener(listener);

		list.remove(item);

		Assert.assertEquals(0, listener.added);
		Assert.assertEquals(1, listener.removed);
		Assert.assertEquals(0, listener.cleared);
	}

	/**
	 * @verifies Not fire the itemRemoved event for items not found in the list
	 * @see org.openmrs.module.openhmis.plm.PersistentList#remove(org.openmrs.module.openhmis.plm.PersistentListItem)
	 */
	@Test
	public void remove_shouldNotFireRemoveEventForItemsNotInList() {
		TestListEventListener listener = new TestListEventListener();
		list.addEventListener(listener);

		PersistentListItem item = new PersistentListItem("key", null);
		list.remove(item);

		Assert.assertEquals(0, listener.added);
		Assert.assertEquals(0, listener.removed);
		Assert.assertEquals(0, listener.cleared);
	}

	/**
	 * @verifies Reference the correct list and item when firing the itemRemoved event
	 * @see org.openmrs.module.openhmis.plm.PersistentList#remove(org.openmrs.module.openhmis.plm.PersistentListItem)
	 */
	@Test
	public void shouldReferenceCorrectListAndItemWhenItemRemovedEvent() {
		final PersistentListItem item = new PersistentListItem("key", null);

		list.addEventListener(new ListEventListenerAdapter() {
			@Override
			public void itemRemoved(ListEvent event) {
				Assert.assertEquals(list, event.getSource());
				Assert.assertEquals(item, event.getItem());
				Assert.assertEquals(ListEvent.ListOperation.REMOVED, event.getOperation());
			}
		});

		list.remove(item);
	}

	/**
	 * @verifies Fire the listCleared event
	 * @see org.openmrs.module.openhmis.plm.PersistentList#clear()
	 */
	@Test
	public void clear_shouldFireTheListClearedEvent() {
		TestListEventListener listener = new TestListEventListener();
		list.addEventListener(listener);

		list.clear();

		Assert.assertEquals(0, listener.added);
		Assert.assertEquals(0, listener.removed);
		Assert.assertEquals(1, listener.cleared);
	}

	/**
	 * @verifies Reference the correct list when firing the listCleared event
	 * @see org.openmrs.module.openhmis.plm.PersistentList#clear()
	 */
	@Test
	public void shouldReferenceCorrectListAndNullItemWhenListClearedEvent() {
		list.addEventListener(new ListEventListenerAdapter() {
			@Override
			public void listCleared(ListEvent event) {
				Assert.assertEquals(list, event.getSource());
				Assert.assertNull(event.getItem());
				Assert.assertEquals(ListEvent.ListOperation.CLEARED, event.getOperation());
			}
		});

		list.clear();
	}

	/**
	 * @verifies Fire events for added listeners
	 * @see org.openmrs.module.openhmis.plm.PersistentList#addEventListener(ListEventListener)
	 */
	@Test
	public void addEventListener_shouldFireEventsForAddedListeners() {
		TestListEventListener listener1 = new TestListEventListener();
		TestListEventListener listener2 = new TestListEventListener();

		list.addEventListener(listener1);
		list.addEventListener(listener2);

		PersistentListItem item = new PersistentListItem("key", null);
		list.add(item);

		Assert.assertEquals(1, listener1.added);
		Assert.assertEquals(1, listener2.added);
		Assert.assertEquals(0, listener1.removed);
		Assert.assertEquals(0, listener2.removed);
		Assert.assertEquals(0, listener1.cleared);
		Assert.assertEquals(0, listener2.cleared);

		list.remove(item);

		Assert.assertEquals(1, listener1.added);
		Assert.assertEquals(1, listener2.added);
		Assert.assertEquals(1, listener1.removed);
		Assert.assertEquals(1, listener2.removed);
		Assert.assertEquals(0, listener1.cleared);
		Assert.assertEquals(0, listener2.cleared);

		list.clear();

		Assert.assertEquals(1, listener1.added);
		Assert.assertEquals(1, listener2.added);
		Assert.assertEquals(1, listener1.removed);
		Assert.assertEquals(1, listener2.removed);
		Assert.assertEquals(1, listener1.cleared);
		Assert.assertEquals(1, listener2.cleared);
	}

	/**
	 * @verifies Not fire events for removed listeners
	 * @see org.openmrs.module.openhmis.plm.PersistentList#removeEventListener(ListEventListener)
	 */
	@Test
	public void removeEventListener_shouldNotFireEventsForRemovedListeners() {
		TestListEventListener listener1 = new TestListEventListener();
		TestListEventListener listener2 = new TestListEventListener();

		list.addEventListener(listener1);
		list.addEventListener(listener2);

		PersistentListItem item = new PersistentListItem("key", null);
		list.add(item);

		Assert.assertEquals(1, listener1.added);
		Assert.assertEquals(1, listener2.added);
		Assert.assertEquals(0, listener1.removed);
		Assert.assertEquals(0, listener2.removed);
		Assert.assertEquals(0, listener1.cleared);
		Assert.assertEquals(0, listener2.cleared);

		list.removeEventListener(listener1);

		list.remove(item);

		Assert.assertEquals(1, listener1.added);
		Assert.assertEquals(1, listener2.added);
		Assert.assertEquals(0, listener1.removed);
		Assert.assertEquals(1, listener2.removed);
		Assert.assertEquals(0, listener1.cleared);
		Assert.assertEquals(0, listener2.cleared);

		list.removeEventListener(listener2);

		list.clear();

		Assert.assertEquals(1, listener1.added);
		Assert.assertEquals(1, listener2.added);
		Assert.assertEquals(0, listener1.removed);
		Assert.assertEquals(1, listener2.removed);
		Assert.assertEquals(0, listener1.cleared);
		Assert.assertEquals(0, listener2.cleared);
	}

	private class TestListEventListener implements ListEventListener {
		public int added;
		public int removed;
		public int cleared;

		public void reset() {
			added = 0;
			removed = 0;
			cleared = 0;
		}

		@Override
		public void itemAdded(ListEvent event) {
			added++;
		}

		@Override
		public void itemRemoved(ListEvent event) {
			removed++;
		}

		@Override
		public void listCleared(ListEvent event) {
			cleared++;
		}
	}
}
