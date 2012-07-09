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

public class PriorityPersistentListItem extends PersistentListItem {
	private int priority;
	private Integer order;

	PriorityPersistentListItem() {
	}

	public PriorityPersistentListItem(PersistentListItemModel model) {
		super(model);

		this.setPriority(model.getSecondaryOrder());
		this.setOrder(model.getTertiaryOrder());
	}

	public PriorityPersistentListItem(String key) {
		this(null, key, Context.getAuthenticatedUser(), new Date(), 0, null);
	}

	public PriorityPersistentListItem(String key, User createdBy) {
		this(null, key, createdBy, new Date(), 0, null);
	}

	public PriorityPersistentListItem(String key, User createdBy, Date createdOn) {
		this(null, key, createdBy, createdOn, 0, null);
	}

	public PriorityPersistentListItem(Integer id, String key, User createdBy, Date createdOn, int priority, Integer order) {
		super(id, key, createdBy, createdOn);

		this.priority = priority;
		this.order = order;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
}
