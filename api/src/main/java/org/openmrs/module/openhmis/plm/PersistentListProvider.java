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

