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

import java.util.EventListener;

/**
 * Represents types that can handle {@link ListEvent}'s.
 */
public interface ListEventListener extends EventListener {
	/**
	 * Called when a {@link PersistentListItem} is added to a list.
	 * @param event The event information.
	 */
	void itemAdded(ListEvent event);

	/**
	 * Called when a {@link PersistentListItem} is removed from a list.
	 * @param event The event information.
	 */
	void itemRemoved(ListEvent event);

	/**
	 * Called when a {@link PersistentList} is cleared of all items.
	 * @param event The event information.
	 */
	void listCleared(ListEvent event);
}

