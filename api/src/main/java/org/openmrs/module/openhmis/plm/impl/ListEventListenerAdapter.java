package org.openmrs.module.openhmis.plm.impl;

import org.openmrs.module.openhmis.plm.ListEvent;
import org.openmrs.module.openhmis.plm.ListEventListener;

/**
 * Adapter implementation of an {@link ListEventListener}.
 */
public class ListEventListenerAdapter implements ListEventListener {
	public void itemAdded(ListEvent event) { }
	public void itemRemoved(ListEvent event) { }
	public void listCleared(ListEvent event) { }
}
