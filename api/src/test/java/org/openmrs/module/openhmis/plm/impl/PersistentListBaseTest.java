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
import org.openhmis.commons.Initializable;
import org.openhmis.commons.Utility;
import org.openmrs.module.openhmis.plm.PersistentList;
import org.openmrs.module.openhmis.plm.PersistentListItem;
import org.openmrs.module.openhmis.plm.PersistentListProvider;
import org.openmrs.module.openhmis.plm.PersistentListTest;
import org.openmrs.module.openhmis.plm.model.PersistentListItemModel;

import java.util.Date;

import static org.mockito.Mockito.when;

public abstract class PersistentListBaseTest extends PersistentListTest {
	protected abstract PersistentList createList(PersistentListProvider mockedProvider);

	@Override
	protected PersistentList loadList(PersistentListProvider mockedProvider) {
		PersistentList result = createList(mockedProvider);

		Initializable init = Utility.as(Initializable.class, result);
		if (init != null) {
			when(mockedProvider.getItems(result)).thenReturn(new PersistentListItemModel[0]);

			init.initialize();
		}

		return result;
	}

	/**
	 * @verifies Load all items from the provider and add them to the list
	 * @see PersistentListBase#initialize()
	 */
	@Test
	public void initialize_shouldLoadAllItemsFromTheProviderAndAddThemToTheList() throws Exception {
		Assert.assertEquals(0, list.getSize());

		PersistentListItemModel model1 = new PersistentListItemModel(0, "1", 0, null, null, new Date());
		model1.setItemId(0);
		PersistentListItemModel model2 = new PersistentListItemModel(0, "2", 1, null, null, new Date());
		model2.setItemId(1);
		PersistentListItemModel model3 = new PersistentListItemModel(0, "3", 2, null, null, new Date());
		model3.setItemId(2);

		when(mockedProvider.getItems(list)).thenReturn(new PersistentListItemModel[] { model1, model2, model3 });

		Initializable init = Utility.as(Initializable.class, list);
		if (init == null) {
			Assert.fail("Unexpected list type: " + list.getClass().getName());
		} else {
			init.initialize();
		}

		Assert.assertEquals(3, list.getSize());

		PersistentListItem item = list.getItemAt(0);
		Assert.assertNotNull(item);
		Assert.assertEquals("1", item.getKey());

		item = list.getItemAt(1);
		Assert.assertNotNull(item);
		Assert.assertEquals("2", item.getKey());

		item = list.getItemAt(2);
		Assert.assertNotNull(item);
		Assert.assertEquals("3", item.getKey());
	}

	/**
	 * @verifies update each moved item via the list provider
	 * @see PersistentList#insert(PersistentListItem, int)
	 */
	@Test
	public void insert_shouldUpdateEachMovedItemViaTheListProvider() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}
}
