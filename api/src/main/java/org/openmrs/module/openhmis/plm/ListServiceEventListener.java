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
 * Represents types that can handle {@link ListServiceEvent}'s.
 */
public interface ListServiceEventListener extends EventListener {
	/**
	 * Called when a {@link PersistentList} is added to the list service.
	 * @param event The event information.
	 */
	void listAdded(ListServiceEvent event);

	/**
	 * Called when a {@link PersistentList} is removed from the list service.
	 * @param event The event information.
	 */
	void listRemoved(ListServiceEvent event);
}

