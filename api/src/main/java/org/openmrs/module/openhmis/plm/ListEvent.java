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

import java.util.EventObject;

/**
 * The type that contains the information about a list event.
 */
public class ListEvent extends EventObject {
	/**
	 * The list operation types.
	 */
	public enum ListOperation {
		/**
		 * Occurs when an item is added to the list.
		 */
		ADDED,
		/**
		 * Occurs when an item is removed from the list.
		 */
		REMOVED,
		/**
		 * Occurs when all the items are cleared from the list.
		 */
		CLEARED
	}

	private transient PersistentListItem item;
	private transient ListOperation operation;

	/**
	 * Constructs the list event.
	 *
	 * @param list The {@link PersistentList} on which the event initially occurred.
	 * @param item The {@link PersistentListItem} that the operation occurred upon.
	 * @param operation The {@link ListOperation} that occurred.
	 * @throws IllegalArgumentException if list is null or if the item is null and the operation is not CLEARED.
	 */
	public ListEvent(PersistentList list, PersistentListItem item, ListOperation operation) {
		super(list);

		if (operation != ListOperation.CLEARED && item == null) {
			throw new IllegalArgumentException("The item must be defined when the operation is not CLEARED.");
		}

		this.item = item;
		this.operation = operation;
	}

	/**
	 * Gets the {@link PersistentListItem} for this event.
	 * @return The {@link PersistentListItem} for this event.
	 */
	public PersistentListItem getItem() {
		return item;
	}

	/**
	 * Gets the operation that occurred to fire this event.
	 * @return The {@link ListOperation} that occurred.
	 */
	public ListOperation getOperation() {
		return operation;
	}
}

