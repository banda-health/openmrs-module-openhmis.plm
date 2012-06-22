package org.openmrs.module.openhmis.plm.impl;

import org.openmrs.module.openhmis.plm.ListServiceEvent;
import org.openmrs.module.openhmis.plm.ListServiceEventListener;

public class ListServiceEventListenerAdapter implements ListServiceEventListener {
	@Override
	public void listAdded(ListServiceEvent event) { }

	@Override
	public void listRemoved(ListServiceEvent event) { }
}
