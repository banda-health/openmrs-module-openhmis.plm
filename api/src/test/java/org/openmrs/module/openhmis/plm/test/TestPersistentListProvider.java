package org.openmrs.module.openhmis.plm.test;

import org.openmrs.module.openhmis.plm.PersistentList;
import org.openmrs.module.openhmis.plm.PersistentListProvider;
import org.openmrs.module.openhmis.plm.model.PersistentListItemModel;

public class TestPersistentListProvider implements PersistentListProvider {

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public void add(PersistentListItemModel item) {
	}

	@Override
	public boolean remove(PersistentListItemModel item) {
		return false;
	}

	@Override
	public void clear(PersistentList list) {

	}

	@Override
	public PersistentListItemModel[] getItems(PersistentList list) {
		return new PersistentListItemModel[0];
	}
}
