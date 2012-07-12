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

package org.openmrs.module.openhmis.plm.model;

import org.openmrs.User;
import org.openmrs.module.openhmis.plm.PersistentList;

import java.util.Date;

public class PersistentListItemModel {
	private int listId;
	private Integer itemId;
	private int itemOrder;
	private String itemKey;
	private User creator;
	private Date dateCreated;

	public PersistentListItemModel(PersistentList list, String key, int itemOrder, User creator) {
		this(list.getId(), key, itemOrder, creator, new Date());
	}

	public PersistentListItemModel(int listId, String key, int itemOrder, User creator) {
		this(listId, key, itemOrder, creator, new Date());
	}

	public PersistentListItemModel(PersistentList list, String key, int itemOrder, User creator, Date dateCreated) {
		this(list.getId(), key, itemOrder, creator, dateCreated);
	}

	public PersistentListItemModel(int listId, String key, int itemOrder, User creator, Date dateCreated) {
		this.listId = listId;
		this.itemKey = key;
		this.itemOrder = itemOrder;
		this.creator = creator;
		this.dateCreated = dateCreated;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getListId() {
		return listId;
	}

	public void setListId(int listId) {
		this.listId = listId;
	}

	public int getItemOrder() {
		return itemOrder;
	}

	public void setItemOrder(int itemOrder) {
		this.itemOrder = itemOrder;
	}

	public String getItemKey() {
		return itemKey;
	}

	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
}
