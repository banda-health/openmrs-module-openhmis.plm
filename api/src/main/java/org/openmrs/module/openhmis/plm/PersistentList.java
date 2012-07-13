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
 * Represents classes that can persist items and return them according to some data structure.
 */
public interface PersistentList {
	/**
	 * Gets the list key.
	 * @return The list key.
	 */
	String getKey();

	/**
	 * Gets the list description.
	 * @return The list description.
	 */
	String getDescription();

	/**
	 * Sets the list description.
	 * @param description The list description.
	 */
	void setDescription(String description);

	/**
	 * Gets the list id or <code>null</code> if the list has not yet been persisted.
	 * @return The list id.
	 */
	Integer getId();

	/**
	 * Gets the {@link PersistentListProvider}.
	 * @return The {@link PersistentListProvider}.
	 */
	PersistentListProvider getProvider();

	/**
	 * Sets the {@link PersistentListProvider}
	 * @param provider The {@link PersistentListProvider}
	 */
	void setProvider(PersistentListProvider provider);

	/**
	 * Loads the settings from the specified {@link PersistentListModel}.
	 * @param model The {@link PersistentListModel} to load from.
	 */
	void load(PersistentListModel model);

	/**
	 * Adds new {@link PersistentListItem} to the list.
	 * @param items The {@link PersistentListItem}'s to add.
	 * @throws PersistentListException
	 * @throws IllegalArgumentException
	 * @should Add a single item
	 * @should Add multiple items
	 * @should throw PersistentListException when duplicate items are added
	 * @should Fire the itemAdded event
	 * @should Fire the itemAdded event for each item added
	 * @should Reference the correct list and item when firing the itemAdded event
	 * @should Allow a key that is less than 250 characters
	 * @should Throw IllegalArgumentException if item key is longer than 250 characters
	 * @should Block list operations on other threads until complete
	 */
	void add(PersistentListItem... items);

	/**
	 * Inserts a new {@link PersistentListItem} to the list at the specified index.
	 * @param item The {@link PersistentListItem} to insert.
	 * @param index The index where the item will be inserted.
	 * @should throw IllegalArgumentException if the index is less than zero
	 * @should insert the item at the end of the list if the index is larger than the list size
	 * @should insert the item at the specified index and move the existing items
	 * @should update each moved item via the list provider
	 * @should throw PersistentListException when a duplicate item is inserted
	 * @should Fire the itemAdded event with the index
	 * @should Reference the correct list and item when firing the itemAdded event
	 * @should Allow a key that is less than 250 characters
	 * @should Throw IllegalArgumentException if item key is longer than 250 characters
	 * @should Block list operations on other threads until complete
	 */
	void insert(PersistentListItem item, int index);

	/**
	 * Removes the specified {@link PersistentListItem} from the list.
	 * @param item The {@link PersistentListItem} to remove.
	 * @return {@code true} if removed; otherwise, {@code false}.
	 * @should Remove the item
	 * @should Return true if the item was removed
	 * @should Return false if the item was not removed
	 * @should Fire the itemRemoved event
	 * @should Not fire the itemRemoved event for items not found in the list
	 * @should Reference the correct list and item when firing the itemRemoved event
	 * @should Block list operations on other threads until complete
	 */
	boolean remove(PersistentListItem item);

	/**
	 * Clears all items from the list.
	 * @should Remove all items
	 * @should Not throw an exception when list is empty
	 * @should Fire the listCleared event
	 * @should Reference the correct list when firing the listCleared event
	 * @should Block list operations on other threads until complete
	 */
	void clear();

	/**
	 * Gets all the {@link PersistentListItem}'s in the list in the proper list order.
	 * @return The list {@link PersistentListItem}'s.
	 * @should Return items that have been added
	 * @should Return all list items
	 * @should Block list operations on other threads until complete
	 */
	PersistentListItem[] getItems();

	/**
	 * Gets the {@link PersistentListItem} at the specified index.
	 * @param index The index of the item to get.
	 * @return The {@link PersistentListItem} or {@code null} if there is no item at the specified index.
	 * @should Return the item at the specified index
	 * @should Return null when there is no item at the specified index
	 * @should Throw IllegalArgumentException when the index is less than zero
	 * @should Return null if the index is larger than or equal to the list size
	 * @should Block list operations on other threads until complete
	 */
	PersistentListItem getItemAt(int index);

	/**
	 * Gets the next {@link PersistentListItem} as defined by the list implementation without removing the item
	 * from the list.
	 * @return The next {@link PersistentListItem} or {@code null} if no items are defined.
	 * @should Not remove item from list
	 * @should Return null when list is empty
	 * @should Block list operations on other threads until complete
	 */
	PersistentListItem getNext();

	/**
	 * Gets the next {@link PersistentListItem} as defined by the list implementation and removes it from the list.
	 * @return The next {@link PersistentListItem}.
	 * @should Return and remove item
	 * @should Return null when list is empty
	 * @should Fire the itemRemoved event
	 * @should Block list operations on other threads until complete
	 */
	PersistentListItem getNextAndRemove();

	/**
	 * Gets the number of items currently in the list.
	 * @return The number of items currently in the list.
	 * @should Return the number of items
	 * @should Return an empty array when there are no items
	 * @should Not block list operations on other threads
	 */
	int getSize();

	/**
	 * Adds a listener to the list events.
	 * @param listener The listener instance to add.
	 * @should Fire events for added listeners
	 */
	void addEventListener(ListEventListener listener);

	/**
	 * Removes a listener from the list events.
	 * @param listener The listener instance to remove.
	 * @should Not fire events for removed listeners
	 */
	void removeEventListener(ListEventListener listener);
}

