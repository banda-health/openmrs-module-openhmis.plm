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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.openhmis.plm.*;
import org.openmrs.module.openhmis.plm.model.PersistentListModel;
import org.openmrs.module.openhmis.plm.test.TestPersistentList;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ModuleFactory.class)
public class PersistentListServiceImplTest {
	protected PersistentListServiceProvider serviceProvider;
	protected PersistentListProvider listProvider;
	protected PersistentListService service;

	@Before
	public void before() {
		serviceProvider = mock(PersistentListServiceProvider.class);
		listProvider = mock(PersistentListProvider.class);

		service= new PersistentListServiceImpl(serviceProvider, listProvider);
	}

	/**
	 * @verifies set isLoaded to true when complete
	 * @see PersistentListServiceImpl#onStartup()
	 */
	@Test
	public void onStartup_shouldSetIsLoadedToTrueWhenComplete() throws Exception {
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[0]);

		PersistentListServiceImpl impl = (PersistentListServiceImpl)service;
		Assert.assertFalse(impl.getIsLoaded());
		service.onStartup();
		Assert.assertTrue(impl.getIsLoaded());
	}

	/**
	 * @verifies attempt to load lists
	 * @see PersistentListServiceImpl#onStartup()
	 */
	@Test
	public void onStartup_shouldAttemptToLoadLists() throws Exception {
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[0]);

		service.onStartup();

		verify(serviceProvider).getLists();
	}

	/**
	 * @verifies add any existing lists to the lists map
	 * @see PersistentListServiceImpl#onStartup()
	 */
	@Test
	public void onStartup_shouldAddAnyExistingListsToTheListsMap() throws Exception {
		PersistentListModel model = new PersistentListModel(1, "test1", TestPersistentList.class.getName(),
				"Description 1", new Date());
		PersistentListModel model2 = new PersistentListModel(2, "test2", TestPersistentList.class.getName(),
				"Description 2", new Date());
		PersistentListModel model3 = new PersistentListModel(3, "test3", TestPersistentList.class.getName(),
				"Description 3", new Date());
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[] { model, model2, model3 });

		service.onStartup();

		PersistentList[] lists = service.getLists();
		Assert.assertEquals(3, lists.length);

		PersistentList list = service.getList(model.getKey());
		Assert.assertNotNull(list);
		assertListModel(model, list);

		list = service.getList(model2.getKey());
		Assert.assertNotNull(list);
		assertListModel(model2, list);

		list = service.getList(model3.getKey());
		Assert.assertNotNull(list);
		assertListModel(model3, list);
	}

	/**
	 * @verifies add a new list
	 * @see PersistentListServiceImpl#createList(Class, String, String)
	 */
	@Test
	public void createList_shouldAddANewList() {
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[0]);
		service.onStartup();

		String key = "test";
		String desc = "desc";

		Assert.assertEquals(0, service.getLists().length);
		service.createList(TestPersistentList.class, key, desc);
		assertList(TestPersistentList.class, key, desc);
	}

	/**
	 * @verifies fire the list added event
	 * @see PersistentListServiceImpl#createList(Class, String, String)
	 */
	@Test
	public void createList_shouldFireTheListAddedEvent() {
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[0]);
		service.onStartup();

		TestListServiceEventListener listener = new TestListServiceEventListener();
		service.addEventListener(listener);

		service.createList(TestPersistentList.class, "test", null);

		Assert.assertEquals(1, listener.added);
		Assert.assertEquals(0, listener.removed);
	}

	/**
	 * @verifies reference the correct service and list on list added event
	 * @see PersistentListServiceImpl#createList(Class, String, String)
	 */
	@Test
	public void createList_shouldReferenceTheCorrectServiceAndListOnListAddedEvent() {
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[0]);
		service.onStartup();

		final String key = "test";

		service.addEventListener(new ListServiceEventListenerAdapter() {
			@Override
			public void listAdded(ListServiceEvent event) {
				Assert.assertEquals(ListServiceEvent.ServiceOperation.ADDED, event.getOperation());
				Assert.assertEquals(service, event.getSource());
				Assert.assertEquals(TestPersistentList.class, event.getList().getClass());
				Assert.assertEquals(key, event.getList().getKey());
			}
		});

		service.createList(TestPersistentList.class, key, null);
	}

	/**
	 * @verifies allow a key that is less than 250 characters
	 * @see PersistentListServiceImpl#createList(Class, String, String)
	 */
	@Test
	public void createList_shouldAllowAKeyThatIsLessThan250Characters() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalStateException when called before service is loaded
	 * @see PersistentListServiceImpl#createList(Class, String, String)
	 */
	@Test(expected = IllegalStateException.class)
	public void createList_shouldThrowIllegalStateExceptionWhenCalledBeforeServiceIsLoaded() throws Exception {
		PersistentListServiceImpl impl = (PersistentListServiceImpl)service;

		Assert.assertEquals(false, impl.getIsLoaded());
		impl.createList(TestPersistentList.class, "test", "");
	}

	/**
	 * @verifies throw IllegalArgumentException if key is longer than 250 characters
	 * @see PersistentListServiceImpl#createList(Class, String, String)
	 */
	@Test
	public void createList_shouldThrowIllegalArgumentExceptionIfKeyIsLongerThan250Characters() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies add new list when not existing
	 * @see PersistentListServiceImpl#ensureList(Class, String, String)
	 */
	@Test
	public void ensureList_shouldAddNewListWhenNotExisting() {
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[0]);
		service.onStartup();

		String key = "test";
		String desc = "desc";

		PersistentList search = service.getList(key);
		Assert.assertNull(search);
		Assert.assertEquals(0, service.getLists().length);

		service.ensureList(TestPersistentList.class, key, desc);

		assertList(TestPersistentList.class, key, desc);
	}

	/**
	 * @verifies not add or update new list when existing key
	 * @see PersistentListServiceImpl#ensureList(Class, String, String)
	 */
	@Test
	public void ensureList_shouldNotAddOrUpdateNewListWhenExistingKey() {
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[0]);
		service.onStartup();

		String key = "test";
		String desc = "desc";

		// Add the list to the service
		service.ensureList(TestPersistentList.class, key, desc);
		Assert.assertEquals(1, service.getLists().length);
		PersistentList list = assertList(TestPersistentList.class, key, desc);

		// Re-add the list to the service, no change should be made
		service.ensureList(TestPersistentList.class, key, "something else");
		Assert.assertEquals(1, service.getLists().length);
		Assert.assertEquals(list, service.getList(key));
		Assert.assertEquals(desc, service.getList(key).getDescription());
	}

	/**
	 * @verifies return list by key
	 * @see PersistentListServiceImpl#ensureList(Class, String, String)
	 */
	@Test
	public void ensureList_shouldReturnListByKey() {
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[0]);
		service.onStartup();

		String key = "test";

		// Add the list to the service
		service.ensureList(TestPersistentList.class, key, null);
		assertList(TestPersistentList.class, key, null);
	}

	/**
	 * @verifies return null for undefined keys
	 * @see PersistentListServiceImpl#ensureList(Class, String, String)
	 */
	@Test
	public void ensureList_shouldReturnNullForUndefinedKeys() {
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[0]);
		service.onStartup();

		String key = "test";

		// Add the list to the service
		service.ensureList(TestPersistentList.class, key, null);
		Assert.assertEquals(1, service.getLists().length);
		Assert.assertNull(service.getList("other"));
	}

	/**
	 * @verifies allow a key that is less than 250 characters
	 * @see PersistentListServiceImpl#ensureList(Class, String, String)
	 */
	@Test
	public void ensureList_shouldAllowAKeyThatIsLessThan250Characters() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException with empty key
	 * @see PersistentListServiceImpl#ensureList(Class, String, String)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void ensureList_shouldThrowIllegalArgumentExceptionWithEmptyKey() throws Exception {
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[0]);
		service.onStartup();

		// This should throw
		service.ensureList(TestPersistentList.class, "", null);
	}

	/**
	 * @verifies throw IllegalArgumentException with null key
	 * @see PersistentListServiceImpl#ensureList(Class, String, String)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void ensureList_shouldThrowIllegalArgumentExceptionWithNullKey() throws Exception {
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[0]);
		service.onStartup();

		// This should throw
		service.ensureList(TestPersistentList.class, null, null);
	}

	/**
	 * @verifies throw IllegalStateException when called before service is loaded
	 * @see PersistentListServiceImpl#ensureList(Class, String, String)
	 */
	@Test(expected = IllegalStateException.class)
	public void ensureList_shouldThrowIllegalStateExceptionWhenCalledBeforeServiceIsLoaded() throws Exception {
		PersistentListServiceImpl impl = (PersistentListServiceImpl)service;

		Assert.assertEquals(false, impl.getIsLoaded());
		impl.ensureList(TestPersistentList.class, "test", "");
	}

	/**
	 * @verifies not fire listAdded event when list already exists
	 * @see PersistentListServiceImpl#ensureList(Class, String, String)
	 */
	@Test
	public void ensureList_shouldNotFireListAddedEventWhenListAlreadyExists() {
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[0]);
		service.onStartup();

		// First, create list
		service.createList(TestPersistentList.class, "test", null);

		// Now attach the listener
		TestListServiceEventListener listener = new TestListServiceEventListener();
		service.addEventListener(listener);

		// And attempt to add a list with the same key (this won't do anything)
		service.ensureList(TestPersistentList.class, "test", null);

		// No additions should have been fired
		Assert.assertEquals(0, listener.added);
		Assert.assertEquals(0, listener.removed);
	}

	/**
	 * @verifies throw IllegalArgumentException if key is longer than 250 characters
	 * @see PersistentListServiceImpl#ensureList(Class, String, String)
	 */
	@Test
	public void ensureList_shouldThrowIllegalArgumentExceptionIfKeyIsLongerThan250Characters() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies return empty list when no lists defined
	 * @see PersistentListServiceImpl#getLists()
	 */
	@Test
	public void getLists_shouldReturnEmptyListWhenNoListsDefined() {
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[0]);
		service.onStartup();

		Assert.assertNotNull(service.getLists());
		Assert.assertEquals(0, service.getLists().length);
	}

	/**
	 * @verifies return all defined lists
	 * @see PersistentListServiceImpl#getLists()
	 */
	@Test
	public void getLists_shouldReturnAllDefinedLists() {
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[0]);
		service.onStartup();

		String key = "test";
		String key2 = "test2";
		String key3 = "test3";

		// Add the lists to the service
		service.ensureList(TestPersistentList.class, key, null);
		service.ensureList(TestPersistentList.class, key2, null);
		service.ensureList(TestPersistentList.class, key3, null);

		PersistentList[] lists = service.getLists();

		Assert.assertNotNull(lists);
		Assert.assertEquals(3, lists.length);
	}

	/**
	 * @verifies not return reference to internal collection
	 * @see PersistentListServiceImpl#getLists()
	 */
	@Test
	public void getLists_shouldNotReturnReferenceToInternalCollection() {
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[0]);
		service.onStartup();

		String key = "test";
		String key2 = "test2";
		String key3 = "test3";

		// Add the lists to the service
		service.ensureList(TestPersistentList.class, key, null);
		service.ensureList(TestPersistentList.class, key2, null);

		PersistentList[] lists = service.getLists();

		Assert.assertNotNull(lists);
		Assert.assertEquals(2, lists.length);

		// Now add another list to the service
		service.ensureList(TestPersistentList.class, key3, null);

		// The list should not be in the list previously returned
		Assert.assertEquals(2, lists.length);
	}

	/**
	 * @verifies throw IllegalStateException when called before service is loaded
	 * @see PersistentListServiceImpl#getLists()
	 */
	@Test(expected = IllegalStateException.class)
	public void getLists_shouldThrowIllegalStateExceptionWhenCalledBeforeServiceIsLoaded() throws Exception {
		PersistentListServiceImpl impl = (PersistentListServiceImpl)service;

		Assert.assertEquals(false, impl.getIsLoaded());
		impl.getLists();
	}

	/**
	 * @verifies return the list by key
	 * @see PersistentListServiceImpl#getList(String)
	 */
	@Test
	public void getList_shouldReturnTheListByKey() throws Exception {
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[0]);
		service.onStartup();

		String key = "test";

		// Add the list to the service
		service.createList(TestPersistentList.class, key, null);

		// Assert that the list has been added and is returned from getList
		assertList(TestPersistentList.class, key, null);
	}

	/**
	 * @verifies return null for undefined keys
	 * @see PersistentListServiceImpl#getList(String)
	 */
	@Test
	public void getList_shouldReturnNullForUndefinedKeys() throws Exception {
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[0]);
		service.onStartup();

		String key = "test";

		// Add the list to the service
		service.createList(TestPersistentList.class, key, null);

		PersistentList list = service.getList("notfound");
		Assert.assertNull(list);
	}

	/**
	 * @verifies throw IllegalArgumentException with empty key
	 * @see PersistentListServiceImpl#getList(String)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getList_shouldThrowIllegalArgumentExceptionWithEmptyKey() throws Exception {
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[0]);
		service.onStartup();

		// This should throw
		service.getList("");
	}

	/**
	 * @verifies throw IllegalArgumentException with null key
	 * @see PersistentListServiceImpl#getList(String)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getList_shouldThrowIllegalArgumentExceptionWithNullKey() throws Exception {
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[0]);
		service.onStartup();

		// This should throw
		service.getList(null);
	}

	/**
	 * @verifies throw IllegalStateException when called before service is loaded
	 * @see PersistentListServiceImpl#getList(String)
	 */
	@Test(expected = IllegalStateException.class)
	public void getList_shouldThrowIllegalStateExceptionWhenCalledBeforeServiceIsLoaded() throws Exception {
		PersistentListServiceImpl impl = (PersistentListServiceImpl)service;

		Assert.assertEquals(false, impl.getIsLoaded());
		impl.getList("test");
	}

	/**
	 * @verifies remove list when existing
	 * @see PersistentListServiceImpl#removeList(String)
	 */
	@Test
	public void removeList_shouldRemoveListWhenExisting() {
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[0]);
		service.onStartup();

		String key = "test";

		// Add the list to the service
		service.ensureList(TestPersistentList.class, key, null);
		PersistentList list = assertList(TestPersistentList.class, key, null);

		// Now remove that list
		service.removeList(list.getKey());
		Assert.assertEquals(0, service.getLists().length);
		Assert.assertNull(service.getList(key));
	}

	/**
	 * @verifies not throw when removing missing list
	 * @see PersistentListServiceImpl#removeList(String)
	 */
	@Test
	public void removeList_shouldNotThrowWhenRemovingMissingList() {
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[0]);
		service.onStartup();

		service.removeList("test");
	}

	/**
	 * @verifies fire listRemoved event
	 * @see PersistentListServiceImpl#removeList(String)
	 */
	@Test
	public void removeList_shouldFireListRemovedEvent() {
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[0]);
		service.onStartup();

		service.createList(TestPersistentList.class, "test", null);

		TestListServiceEventListener listener = new TestListServiceEventListener();
		service.addEventListener(listener);

		service.removeList("test");

		Assert.assertEquals(0, listener.added);
		Assert.assertEquals(1, listener.removed);
	}

	/**
	 * @verifies not fire listRemoved event when list not found
	 * @see PersistentListServiceImpl#removeList(String)
	 */
	@Test
	public void removeList_shouldNotFireListRemovedEventWhenListNotFound() {
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[0]);
		service.onStartup();

		TestListServiceEventListener listener = new TestListServiceEventListener();
		service.addEventListener(listener);

		service.removeList("test");

		Assert.assertEquals(0, listener.added);
		Assert.assertEquals(0, listener.removed);
	}

	/**
	 * @verifies reference correct service and list in listRemoved event
	 * @see PersistentListServiceImpl#removeList(String)
	 */
	@Test
	public void removeList_shouldReferenceCorrectServiceAndListInListRemovedEvent() {
		when(serviceProvider.getLists()).thenReturn(new PersistentListModel[0]);
		service.onStartup();

		final String key = "test";
		service.createList(TestPersistentList.class, key, null);

		service.addEventListener(new ListServiceEventListenerAdapter() {
			@Override
			public void listRemoved(ListServiceEvent event) {
				Assert.assertEquals(ListServiceEvent.ServiceOperation.REMOVED, event.getOperation());
				Assert.assertEquals(service, event.getSource());
				Assert.assertEquals(TestPersistentList.class, event.getList().getClass());
				Assert.assertEquals(key, event.getList().getKey());
			}
		});

		service.removeList(key);
	}

	/**
	 * @verifies throw IllegalStateException when called before service is loaded
	 * @see PersistentListServiceImpl#removeList(String)
	 */
	@Test(expected = IllegalStateException.class)
	public void removeList_shouldThrowIllegalStateExceptionWhenCalledBeforeServiceIsLoaded() throws Exception {
		PersistentListServiceImpl impl = (PersistentListServiceImpl)service;

		Assert.assertEquals(false, impl.getIsLoaded());
		impl.removeList("test");
	}

	private void assertListModel(PersistentListModel model, PersistentList list) {
		Assert.assertEquals(model.getListId(), list.getId());
		Assert.assertEquals(model.getKey(), list.getKey());
		Assert.assertEquals(model.getListClass(), list.getClass().getName());
		Assert.assertEquals(model.getDescription(), list.getDescription());
	}

	private PersistentList assertList(Class cls, String key, String desc) {
		PersistentList list = service.getList(key);
		Assert.assertNotNull(list);
		Assert.assertEquals(cls, list.getClass());
		Assert.assertEquals(key, list.getKey());
		Assert.assertEquals(desc, list.getDescription());

		return list;
	}

	public class TestListServiceEventListener implements ListServiceEventListener {
		public int added;
		public int removed;

		@Override
		public void listAdded(ListServiceEvent event) {
			added++;
		}

		@Override
		public void listRemoved(ListServiceEvent event) {
			removed++;
		}
	}
}
