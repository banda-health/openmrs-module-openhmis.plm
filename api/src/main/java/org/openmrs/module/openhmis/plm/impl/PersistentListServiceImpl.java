package org.openmrs.module.openhmis.plm.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhmis.commons.EventRaiser;
import org.openhmis.commons.FireableEventListenerList;
import org.openhmis.commons.Initializable;
import org.openhmis.commons.Utility;
import org.openmrs.module.openhmis.plm.*;
import org.openmrs.module.openhmis.plm.model.PersistentListModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
	This type is a synchronized list manager.  Read operations are not synchronized so that they operate as fast as
	possible while ensureList may acquire a lock if the list is new and removeList will always acquire a lock if the
	list is found.
 */
public class PersistentListServiceImpl implements PersistentListService {
	private final Log log = LogFactory.getLog(PersistentListServiceImpl.class);
	private final Object syncLock = new Object();
	private final Object eventLock = new Object();

	private Map<String, PersistentList> lists = new HashMap<String, PersistentList>();
	private FireableEventListenerList listenerList = new FireableEventListenerList();
	private boolean isLoaded = false;

	protected PersistentListServiceProvider serviceProvider;
	protected PersistentListProvider listProvider;

	@Autowired
	public PersistentListServiceImpl(PersistentListServiceProvider serviceProvider, PersistentListProvider listProvider) {
		this.serviceProvider = serviceProvider;
		this.listProvider = listProvider;
		this.isLoaded = false;
	}

	/**
	 * Initializes the {@link PersistentListService}.
	 * @should set isLoaded to true when complete
	 * @should attempt to load lists
	 * @should add any existing lists to the lists map
	 */
	@Override
	public void onStartup() {
		// Load lists from the database
		loadLists();
	}

	@Override
	public void onShutdown() {
	}

	/**
	 * Checks that the specified list exists and if it does not, creates it.
	 * @param listClass The list type to create.
	 * @param key The list key.
	 * @param description An optional description of the list purpose.
	 * @param <T> A type that implements the {@link PersistentList} interface.
	 * @return The existing or newly created {@link PersistentList}.
	 * @should add new list when not existing
	 * @should not add or update new list when existing key
	 * @should return list by key
	 * @should return null for undefined keys
	 * @should throw IllegalArgumentException with empty key
	 * @should throw IllegalArgumentException with null key
	 * @should throw IllegalStateException when called before service is loaded
	 * @should not fire listAdded event when list already exists
	 */
	@Override
    public <T extends PersistentList> PersistentList ensureList(Class<T> listClass, String key, String description) {
        if (!isLoaded) {
	        throw new IllegalStateException("The service must be loaded via onStartup() before being used.");
        }

		if (listClass == null) {
	        throw new IllegalArgumentException("The list class must be defined.");
        }
	    if (StringUtils.isEmpty(key)) {
		    throw new IllegalArgumentException("The list must have a key.");
	    }

        //Check to see if plm has already been defined
	    PersistentList list = lists.get(key);

        //If not defined, synchronize and check again (double-check locking)
        if (list == null) {
	        synchronized (syncLock){
		        list = lists.get(key);
		        if (list == null) {
			        log.debug("Could not find the '" + key + "' list.  Creating a new list...");

			        createList(listClass, key, description);

			        log.debug("The '" + key + "' list was created.");
		        }
	        }
        }

	    return list;
    }

	/**
	 * Creates a new list of the specified type with the specified key.
	 * @param listClass The list type to create.
	 * @param key The list key.
	 * @param description An optional description of the list purpose.
	 * @param <T> A type that implements the {@link PersistentList} interface.
	 * @return The newly created {@link PersistentList}.
	 * @should add a new list
	 * @should fire the list added event
	 * @should reference the correct service and list on list added event
	 * @should throw IllegalStateException when called before service is loaded
	 */
	@Override
	public <T extends PersistentList> PersistentList createList(Class<T> listClass, String key, String description) {
		if (!isLoaded) {
			throw new IllegalStateException("The service must be loaded via onStartup() before being used.");
		}

		if (listClass == null) {
			throw new IllegalArgumentException("The list class must be defined.");
		}
		if (StringUtils.isEmpty(key)) {
			throw new IllegalArgumentException("The list must have a key.");
		}

		log.debug("Creating the '" + key + "' list...");

		PersistentList list = null;
		synchronized (syncLock) {
			// Make sure no other list with the specified key exists
			if (lists.containsKey(key)) {
				throw new IllegalArgumentException("A list with the key '" + key + "'" +
						" has already been added to this service.");
			}

			// Create list model
			PersistentListModel model = new PersistentListModel(null, key, listClass.getName(), description,
					new Date());

			// Persist the list model
			serviceProvider.addList(model);

			// Create list instance and load properties from model
			list = createList(model);

			// Add the list to the service list and key caches
			lists.put(list.getKey(), list);
			lists.put(key, list);
		}

		fireServiceEvent(new ListServiceEvent(this, list, ListServiceEvent.ServiceOperation.ADDED));

		log.debug("The '" + key + "' was created.");

		return list;
	}

	/**
	 * Removes the list and associated items.
	 * @param key The key of the list to remove.
	 * @should remove list when existing
	 * @should not throw when removing missing list
	 * @should fire listRemoved event
	 * @should not fire listRemoved event when list not found
	 * @should reference correct service and list in listRemoved event
	 * @should throw IllegalStateException when called before service is loaded
	 */
	@Override
    public void removeList(String key) {
		if (!isLoaded) {
			throw new IllegalStateException("The service must be loaded via onStartup() before being used.");
		}

		PersistentList list = lists.get(key);
	    if (list != null) {
		    log.debug("Deleting the " + key + "list...");

		    synchronized (syncLock) {
			    serviceProvider.removeList(key);
			    lists.remove(key);
		    }

		    fireServiceEvent(new ListServiceEvent(this, list, ListServiceEvent.ServiceOperation.REMOVED));

		    log.debug("The '" + key + "' list was deleted.");
	    }
    }

	/**
	 * Gets the lists that are currently defined in the system.
	 * @return An array containing the lists or an empty array if no lists are defined.
	 * @should return empty list when no lists defined
	 * @should return all defined lists
	 * @should not return reference to internal collection
	 * @should throw IllegalStateException when called before service is loaded
	 */
	@Override
    public PersistentList[] getLists() {
		if (!isLoaded) {
			throw new IllegalStateException("The service must be loaded via onStartup() before being used.");
		}

		return lists.values().toArray(new PersistentList[lists.values().size()]);
    }

	/**
	 * Gets the specified list or <code>null</code> if this list is not found.
	 * @param key The list key.
	 * @return The list or <code>null</code> if the list cannot be found.
	 * @should return the list by key
	 * @should return null for undefined keys
	 * @should throw IllegalArgumentException with empty key
	 * @should throw IllegalArgumentException with null key
	 * @should throw IllegalStateException when called before service is loaded
	 */
	@Override
    public PersistentList getList(String key) {
		if (!isLoaded) {
			throw new IllegalStateException("The service must be loaded via onStartup() before being used.");
		}

		if (StringUtils.isEmpty(key)) {
	        throw new IllegalArgumentException("The list key must be defined");
        }
	    if (!isLoaded) {
		    return null;
	    }

	    return lists.get(key);
    }

	@Override
	public void addEventListener(ListServiceEventListener listener) {
		listenerList.add(ListServiceEventListener.class, listener);
	}

	@Override
	public void removeEventListener(ListServiceEventListener listener) {
		listenerList.remove(ListServiceEventListener.class, listener);
	}

	public Boolean getIsLoaded() {
		return isLoaded;
	}

	public void setServiceProvider(PersistentListServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public void setListProvider(PersistentListProvider listProvider) {
		this.listProvider = listProvider;
	}

	private void fireServiceEvent(final ListServiceEvent event) {
		listenerList.fire(ListServiceEventListener.class, new EventRaiser<ListServiceEventListener>() {
			@Override
			public void fire(ListServiceEventListener listener) {
				switch (event.getOperation()) {
					case ADDED:
						listener.listAdded(event);
						break;
					case REMOVED:
						listener.listRemoved(event);
						break;
				}
			}
		});
	}

	protected void loadLists() {
		log.debug("Loading the configured lists from the serviceProvider...");

		// Lock access so that list requests will not proceed until loaded
		synchronized (syncLock) {
			PersistentListModel[] listModels = serviceProvider.getLists();
			for (PersistentListModel listModel : listModels) {
				PersistentList list = createList(listModel);

				if (list != null) {
					lists.put(list.getKey(), list);
				}
			}

			isLoaded = true;
		}

		log.debug("Loaded " + lists.size() + " lists.");
	}

	protected PersistentList createList(PersistentListModel model) {
		Class listClass = null;

		// Load the list class
		try {
			listClass = Class.forName(model.getListClass());
		} catch (Exception ex) {
			log.error("Could not load the '" + model.getListClass() + "' list type.", ex);
		}

		if (listClass == null) {
			// The list class could not be loaded so just return null
			return null;
		}

		PersistentList list = null;
		try {
			list = (PersistentList)listClass.newInstance();
			list.load(model);
			list.setProvider(listProvider);

			Initializable init = Utility.as(Initializable.class, list);
			if (init != null) {
				init.initialize();
			}
		} catch (InstantiationException e) {
			log.error("Could not create new instance of " + listClass.getName() +
					" because the class could not be instantiated", e);

			list = null;
		} catch (IllegalAccessException e) {
			log.error("Could not create new instance of " + listClass.getName() +
					" because of an access exception", e);

			list = null;
		}

		return list;

	}
}

