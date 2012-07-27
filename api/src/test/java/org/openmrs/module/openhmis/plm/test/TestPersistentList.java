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

package org.openmrs.module.openhmis.plm.test;

import org.apache.commons.lang.NotImplementedException;
import org.openmrs.module.openhmis.plm.ListEventListener;
import org.openmrs.module.openhmis.plm.PersistentList;
import org.openmrs.module.openhmis.plm.PersistentListItem;
import org.openmrs.module.openhmis.plm.PersistentListProvider;
import org.openmrs.module.openhmis.plm.model.PersistentListModel;

public class TestPersistentList implements PersistentList {
	public TestPersistentList() {
	}

	public TestPersistentList(String key) {
		this(key, new TestPersistentListProvider(), 0);
	}

	public TestPersistentList(String key, PersistentListProvider provider) {
		this(key, provider, 0);
	}

	public TestPersistentList(String key, PersistentListProvider provider, int count) {
		this.key = key;
		this.provider = provider;
		this.count = count;
	}

	Integer id;
	String key;
	String description;
	PersistentListProvider provider;
	int count;

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		return count;
	}

	@Override
	public void addEventListener(ListEventListener listener) {
	}

	@Override
	public void removeEventListener(ListEventListener listener) {
	}

	@Override
	public void load(PersistentListModel model) {
		key = model.getKey();
		id = model.getListId();
		description = model.getDescription();
	}

	@Override
	public void add(PersistentListItem... items) {
		throw new NotImplementedException();
	}

	@Override
	public void insert(int index, PersistentListItem item) {
		throw new NotImplementedException();
	}

	@Override
	public boolean remove(PersistentListItem item) {
		throw new NotImplementedException();
	}

	@Override
	public void clear() {
		throw new NotImplementedException();
	}

	@Override
	public PersistentListItem[] getItems() {
		throw new NotImplementedException();
	}

	@Override
	public PersistentListItem getItemAt(int index) {
		throw new NotImplementedException();
	}

	@Override
	public PersistentListItem getNext() {
		throw new NotImplementedException();
	}

	@Override
	public PersistentListItem getNextAndRemove() {
		throw new NotImplementedException();
	}
}
