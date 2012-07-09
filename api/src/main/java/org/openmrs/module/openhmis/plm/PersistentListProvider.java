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

import org.openmrs.module.openhmis.plm.model.PersistentListItemModel;

/**
 *  Represents classes that handle loading and saving list items.
 */
public interface PersistentListProvider {
	/**
	 * Gets the name of the serviceProvider.
	 * @return The name of the serviceProvider.
	 */
    String getName();

	/**
	 * Gets the description of the serviceProvider.
	 * @return The description of the serviceProvider.
	 */
    String getDescription();

	/**
	 * Adds a new item to the list.
	 * @param item The item to add.
	 */
    void add(PersistentListItemModel item);

	/**
	 * Removes the specified item from the list.
	 * @param item The item to remove.
	 * @return {@code true} if the item was removed; otherwise, {@code false}.
	 */
    boolean remove(PersistentListItemModel item);

	/**
	 * Removes all items from the specified list.
	 * @param list The list to clear.
	 */
    void clear(PersistentList list);

	/**
	 * Gets all the items from the list in order.
	 * @param list The @see PersistentList to get.
	 * @return The items in the list.
	 */
	PersistentListItemModel[] getItems(PersistentList list);
}

