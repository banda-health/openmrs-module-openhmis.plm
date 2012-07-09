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
	 * @should Add a single item
	 * @should Add multiple items
	 * @should throw PersistentListException when duplicate items are added
	 * @should Fire the itemAdded event
	 * @should Fire the itemAdded event for each item added
	 * @should Reference the correct list and item when firing the itemAdded event
	 * @should Allow a key that is less than 250 characters
	 * @should Throw IllegalArgumentException if item key is longer than 250 characters
	 */
	void add(PersistentListItem... items);

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
	 */
	boolean remove(PersistentListItem item);

	/**
	 * Clears all items from the list.
	 * @should Remove all items
	 * @should Not throw an exception when list is empty
	 * @should Fire the listCleared event
	 * @should Reference the correct list when firing the listCleared event
	 */
	void clear();

	/**
	 * Gets all the {@link PersistentListItem}'s in the list in the proper list order.
	 * @return The list {@link PersistentListItem}'s.
	 * @should Return items that have been added
	 * @should Return all list items
	 */
	PersistentListItem[] getItems();

	/**
	 * Gets the next {@link PersistentListItem} as defined by the list implementation without removing the item
	 * from the list.
	 * @return The next {@link PersistentListItem} or {@code null} if no items are defined.
	 * @should Not remove item from list
	 * @should Return null when list is empty
	 */
	PersistentListItem getNext();

	/**
	 * Gets the next {@link PersistentListItem} as defined by the list implementation and removes it from the list.
	 * @return The next {@link PersistentListItem}.
	 * @should Return and remove item
	 * @should Return null when list is empty
	 */
	PersistentListItem getNextAndRemove();

	/**
	 * Gets the number of items currently in the list.
	 * @return The number of items currently in the list.
	 * @should Return the number of items
	 * @should Return an empty array when there are no items
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

