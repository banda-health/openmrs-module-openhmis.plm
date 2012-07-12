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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhmis.commons.EventRaiser;
import org.openhmis.commons.FireableEventListenerList;
import org.openhmis.commons.Initializable;
import org.openmrs.module.openhmis.plm.*;
import org.openmrs.module.openhmis.plm.model.PersistentListItemModel;
import org.openmrs.module.openhmis.plm.model.PersistentListModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Base type for Persistent List Manager lists.  Provides a thread-safe list implementation base that caches the items
 * in the defined Collection<PersistentListItem> subtype.
 *
 * @param <T> The collection type for the list implementation.
 */
public abstract class PersistentListBase<T extends Collection<PersistentListItem>> implements PersistentList, Initializable {
	public final static int MAX_ITEM_KEY_LENGTH = 250;

	private Log log = LogFactory.getLog(PersistentListBase.class);

	protected final Object syncLock = new Object();

	protected Integer id;
	protected String key;
	protected String description;
	protected PersistentListProvider provider;
	protected T cachedItems;
	protected List<String> itemKeys = new ArrayList<String>();
	private FireableEventListenerList listenerList = new FireableEventListenerList();

	protected PersistentListBase() {
	}

	protected PersistentListBase(String key, PersistentListProvider provider) {
		this(null, key, provider);
	}

	protected PersistentListBase(Integer id, String key, PersistentListProvider provider) {
		if (key == null || key.trim().equals("")) {
			throw new IllegalArgumentException("Key has no content.");
		}
		if (provider == null) {
			throw new IllegalArgumentException("Provider is not defined.");
		}

		this.id = id;
		this.key = key;
		this.provider = provider;
	}

	@Override
	public abstract PersistentListItem getNext();

	@Override
	public abstract PersistentListItem getNextAndRemove();

	protected abstract T initializeCache();
	protected abstract int getItemIndex(PersistentListItem item);

	@Override
	public void initialize() {
		log.debug("Initializing the '" + key + "' list...");

		synchronized (syncLock) {
			// Initialize the cache object, as determined by the subtype.
			cachedItems = initializeCache();

			// Load the items into the cache
			Collections.addAll(cachedItems, loadList());

			// Load the item keys into the key cache
			for (PersistentListItem item : cachedItems) {
				itemKeys.add(item.getKey());
			}
		}

		log.debug("The '" + key + "' has been initialized.");
	}

	@Override
	public void load(PersistentListModel model) {
		this.id = model.getListId();
		this.key = model.getKey();
		this.description = model.getDescription();
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public PersistentListProvider getProvider() {
		return provider;
	}

	@Override
	public void setProvider(PersistentListProvider provider) {
		this.provider = provider;
	}

	@Override
	public int getSize() {
		return cachedItems.size();
	}

	@Override
	public void add(PersistentListItem... items) {
		PersistentListItem item = null;
		try {
			synchronized (syncLock) {
				for (PersistentListItem listItem : items) {
					// Store the reference to the current item (in case of an exception)
					item = listItem;

					if (item.getKey().length() > MAX_ITEM_KEY_LENGTH) {
						throw new IllegalArgumentException("The item key must be " + MAX_ITEM_KEY_LENGTH + " characters or less.");
					}
					if (itemKeys.contains(item.getKey())) {
						throw new IllegalArgumentException("An item with the key '" + item.getKey() + "' has already been added to this persistent list.");
					}

					// Add the item to the cached items
					itemKeys.add(item.getKey());
					cachedItems.add(item);

					// Add the item to the serviceProvider at the specified index
					PersistentListItemModel modelItem = createItemModel(item);
					provider.add(modelItem);
				}
			}

			// Fire the add events outside of the synchronized block
			for (PersistentListItem listItem : items) {
				fireListEvent(new ListEvent(this, listItem, ListEvent.ListOperation.ADDED));
			}
		} catch (Exception ex) {
			// If there was an exception while trying to add an item ensure that it is no longer in the cache.
			cachedItems.remove(item);

			/*
			TODO: Should the item be removed from the serviceProvider as well, or it is safe to assume that exceptions
				can only occur before the item is added to the serviceProvider?
			*/

			/*
			 TODO: Given that a serviceProvider could throw pretty much any exception type (DB, file system, network, etc),
			    is it ok to just rethrow it as an Exception?
			*/

			throw new PersistentListException(ex);
		}
	}

	@Override
	public boolean remove(PersistentListItem item) {
		Boolean wasRemovedFromProvider, wasRemovedFromCache;
		synchronized (syncLock) {
			wasRemovedFromProvider = provider.remove(createItemModel(item));
			wasRemovedFromCache = cachedItems.remove(item);
			itemKeys.remove(item.getKey());
		}

		// Fire the remove event outside of the synchronized block
		if (wasRemovedFromProvider || wasRemovedFromCache) {
			fireListEvent(new ListEvent(this, item, ListEvent.ListOperation.REMOVED));

			return true;
		} else {
			return false;
		}
	}

	@Override
	public void clear() {
		synchronized (syncLock) {
			provider.clear(this);
			cachedItems.clear();
			itemKeys.clear();
		}

		fireListEvent(new ListEvent(this, null, ListEvent.ListOperation.CLEARED));
	}

	@Override
	public PersistentListItem[] getItems() {
		return cachedItems.toArray(new PersistentListItem[cachedItems.size()]);
	}

	@Override
	public void addEventListener(ListEventListener listener) {
		listenerList.add(ListEventListener.class, listener);
	}

	@Override
	public void removeEventListener(ListEventListener listener) {
		listenerList.remove(ListEventListener.class, listener);
	}

	protected PersistentListItem[] loadList() {
		PersistentListItemModel[] modelItems = provider.getItems(this);

		PersistentListItem[] items = new PersistentListItem[modelItems.length];
		int i = 0;
		for (PersistentListItemModel model : modelItems) {
			items[i++] = createItem(model);
		}

		return items;
	}

	protected PersistentListItem createItem(PersistentListItemModel model) {
		return new PersistentListItem(model.getItemId(), model.getItemKey(),
				model.getCreator(), model.getDateCreated());
	}

	protected PersistentListItemModel createItemModel(PersistentListItem item) {
		return new PersistentListItemModel(this, item.getKey(), getItemIndex(item), item.getCreator(),
				item.getCreatedOn());
	}

	protected void fireListEvent(final ListEvent event) {
		listenerList.fire(ListEventListener.class, new EventRaiser<ListEventListener>() {
			@Override
			public void fire(ListEventListener listener) {
				switch (event.getOperation()) {
					case ADDED:
						listener.itemAdded(event);
						break;
					case REMOVED:
						listener.itemRemoved(event);
						break;
					case CLEARED:
						listener.listCleared(event);
						break;
				}
			}
		});
	}
}

