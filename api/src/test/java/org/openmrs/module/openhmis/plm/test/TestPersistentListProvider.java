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

package org.openmrs.module.openhmis.plm.test;

import org.openmrs.module.openhmis.plm.PersistentList;
import org.openmrs.module.openhmis.plm.PersistentListProvider;
import org.openmrs.module.openhmis.plm.model.PersistentListItemModel;

public class TestPersistentListProvider implements PersistentListProvider {

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public void add(PersistentListItemModel item) {
	}

	@Override
	public boolean remove(PersistentListItemModel item) {
		return false;
	}

	@Override
	public void clear(PersistentList list) {

	}

	@Override
	public PersistentListItemModel[] getItems(PersistentList list) {
		return new PersistentListItemModel[0];
	}
}
