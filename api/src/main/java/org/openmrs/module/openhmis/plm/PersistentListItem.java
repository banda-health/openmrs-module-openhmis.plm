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

import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.plm.model.PersistentListItemModel;

import java.util.Date;

public class PersistentListItem {
	private Integer id;
	private String key;
	private User creator;
	private Date createdOn;

	PersistentListItem() {
	}

	public PersistentListItem(PersistentListItemModel model) {
		this(model.getItemId(), model.getItemKey(), model.getCreator(), model.getCreatedOn());
	}

	public PersistentListItem(String key) {
		this(null, key, Context.getAuthenticatedUser(), new Date());
	}

	public PersistentListItem(String key, User creator) {
		this(null, key, creator, new Date());
	}

	public PersistentListItem(String key, User creator, Date createdOn) {
		this(null, key, creator, createdOn);
	}

	public PersistentListItem(Integer id, String key, User creator, Date createdOn) {
		this.id = id;
		this.key = key;
		this.creator = creator;
		this.createdOn = createdOn;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	void setCreatedOn(Date createdAt) {
		this.createdOn = createdAt;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}
}

