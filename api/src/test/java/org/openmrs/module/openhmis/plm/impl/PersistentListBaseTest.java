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

import org.junit.Test;
import org.openhmis.commons.Initializable;
import org.openhmis.commons.Utility;
import org.openmrs.module.openhmis.plm.PersistentList;
import org.openmrs.module.openhmis.plm.PersistentListItem;
import org.openmrs.module.openhmis.plm.PersistentListProvider;
import org.openmrs.module.openhmis.plm.PersistentListTest;
import org.openmrs.module.openhmis.plm.model.PersistentListItemModel;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

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
	public abstract void initialize_shouldLoadAllItemsFromTheProviderAndAddThemToTheList() throws Exception;

	/**
	 * @verifies update each moved item via the list provider
	 * @see PersistentList#insert(int, org.openmrs.module.openhmis.plm.PersistentListItem)
	 */
	@Test
	public void insert_shouldUpdateEachMovedItemViaTheListProvider() throws Exception {
		PersistentListItem item1 = new PersistentListItem("1", null);
		PersistentListItem item2 = new PersistentListItem("2", null);
		PersistentListItem item3 = new PersistentListItem("3", null);
		PersistentListItem item4 = new PersistentListItem("4", null);

		list.add(item1, item2,  item3);

		reset(mockedProvider);

		list.insert(1, item4);

		verify(mockedProvider).add(any(PersistentListItemModel.class));
	}
}
