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

package org.openmrs.module.openhmis.plm;

import org.openmrs.module.openhmis.plm.model.PersistentListModel;

/**
 * Represents classes that handle loading and saving {@link PersistentList}'s.
 */
public interface PersistentListServiceProvider {
	/**
	 * Gets all the {@link PersistentList}'s that are currently defined.
	 * @return The lists.
	 */
	PersistentListModel[] getLists();

	/**
	 * Adds a {@link PersistentList}.
	 * @param list The {@link PersistentList} to persist.
	 * @return Integer The list ID from the database
	 */
	Integer addList(PersistentListModel list);

	/**
	 * Removes a {@link PersistentList}.
	 * @param key The key of the {@link PersistentList} to remove.
	 */
	void removeList(String key);
}
