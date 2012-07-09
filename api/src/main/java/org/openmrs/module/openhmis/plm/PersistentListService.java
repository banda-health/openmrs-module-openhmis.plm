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

import org.openmrs.api.OpenmrsService;

import java.util.Collection;

/**
 * Represents classes that can create and remove persistent lists.
 */
public interface PersistentListService extends OpenmrsService {
	/**
	 * Checks that the specified list exists and if it does not, creates it.
	 * @param listClass The list type to create.
	 * @param key The list key.
	 * @param description An optional description of the list purpose.
	 * @param <T> A type that implements the {@link PersistentList} interface.
	 * @return The existing or newly created {@link PersistentList}.
	 */
	<T extends PersistentList> PersistentList ensureList(Class<T> listClass, String key, String description);

	/**
	 * Creates a new list of the specified type with the specified key.
	 * @param listClass The list type to create.
	 * @param key The list key.
	 * @param description An optional description of the list purpose.
	 * @param <T> A type that implements the {@link PersistentList} interface.
	 * @return The newly created {@link PersistentList}.
	 */
	<T extends PersistentList> PersistentList createList(Class<T> listClass, String key, String description);

	/**
	 * Removes the list and associated items.
	 * @param key The key of the list to remove.
	 */
    void removeList(String key);

	/**
	 * Gets the lists that are currently defined in the system.
	 * @return An array containing the lists or an empty array if no lists are defined.
	 */
    PersistentList[] getLists();

	/**
	 * Gets the specified list or <code>null</code> if this list is not found.
	 * @param key The list key.
	 * @return The list or <code>null</code> if the list cannot be found.
	 */
    PersistentList getList(String key);

	void addEventListener(ListServiceEventListener listener);

	void removeEventListener(ListServiceEventListener listener);
}

