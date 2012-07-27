package org.openmrs.module.openhmis.plm.db;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.openhmis.plm.impl.PersistentListBase;
import org.openmrs.module.openhmis.plm.impl.PersistentQueue;
import org.openmrs.module.openhmis.plm.model.PersistentListModel;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

public class DatabaseServiceProviderTest extends BaseModuleContextSensitiveTest {
	private Log log = LogFactory.getLog(PersistentListBase.class);
	private static String TEST_DATABASE_FILE = "db/PlmBasicTest.xml";

	@Autowired
	private SessionFactory sessionFactory;
	
	private DatabaseServiceProvider service;

	@Before
	public void before() {
		try {
			executeDataSet(TEST_DATABASE_FILE);
		} catch (Exception e) {
			Assert.fail("Couldn't load test database from " + TEST_DATABASE_FILE);
		}
		service = new DatabaseServiceProvider(sessionFactory);
	}
	
	/**
	 * @see DatabaseServiceProvider#getLists()
	 * @verifies return existing lists
	 * 
	 * Based on a test database with one list with key Test
	 */
	@Test
	public void getLists_shouldReturnExistingLists() throws Exception {
		PersistentListModel[] lists = service.getLists();
		Assert.assertEquals(1, lists.length);
		PersistentListModel testList = lists[0];
		Assert.assertEquals("Test", testList.getKey());
	}

	/**
	 * @see DatabaseServiceProvider#addList(PersistentListModel)
	 * @verifies add a new list
	 */
	@Test
	public void addList_shouldAddANewList() throws Exception {
		PersistentListModel newList = new PersistentListModel(
			null,
			"Test2",
			PersistentQueue.class.getName(),
			"Second test",
			new Date(System.currentTimeMillis()));
		service.addList(newList);
		PersistentListModel[] lists = service.getLists();
		Assert.assertEquals(2, lists.length);
		PersistentListModel list = lists[1];
		Assert.assertEquals(newList.getKey(), list.getKey());
	}

	/**
	 * @see DatabaseServiceProvider#removeList(String)
	 * @verifies remove a list
	 */
	@Test
	public void removeList_shouldRemoveAList() throws Exception {
		service.removeList("Test");
		PersistentListModel[] lists = service.getLists();
		Assert.assertEquals(0, lists.length);		
	}
}