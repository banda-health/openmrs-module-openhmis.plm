package org.openmrs.module.openhmis.plm.model;

import java.util.Date;

public class PersistentListModel {
	private Integer listId;
	private String key;
	private String listClass;
	private String description;
	private Date createdOn;

	PersistentListModel() {
	}

	public PersistentListModel(Integer listId, String key, String listClass, String description,
	                           Date createdOn) {
		this.listId = listId;
		this.key = key;
		this.listClass = listClass;
		this.description = description;

		this.createdOn = createdOn;
	}

	public String getListClass() {
		return listClass;
	}

	public void setListClass(String listClass) {
		this.listClass = listClass;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getListId() {
		return listId;
	}

	public void setListId(Integer listId) {
		this.listId = listId;
	}
}

