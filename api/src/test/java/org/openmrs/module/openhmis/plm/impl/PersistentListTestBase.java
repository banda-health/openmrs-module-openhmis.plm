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

import org.openmrs.module.openhmis.plm.PersistentList;
import org.openmrs.module.openhmis.plm.PersistentListProvider;
import org.openmrs.module.openhmis.plm.PersistentListTest;
import org.openmrs.module.openhmis.plm.test.TestPersistentListProvider;

public abstract class PersistentListTestBase extends PersistentListTest {
	protected PersistentListProvider provider;

	protected abstract PersistentList createList(PersistentListProvider provider);

	@Override
	protected PersistentList loadList() {
		provider = new TestPersistentListProvider();

		return createList(provider);
	}
}
