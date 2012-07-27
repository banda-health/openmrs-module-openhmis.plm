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

package org.openmrs.module.openhmis.plm.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.openhmis.plm.PersistentListException;
import org.openmrs.module.openhmis.plm.impl.PersistentListServiceImpl;
import org.openmrs.module.openhmis.plm.PersistentListServiceProvider;
import org.openmrs.module.openhmis.plm.model.PersistentListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

@Component
public class DatabaseServiceProvider implements PersistentListServiceProvider {
	private Log log = LogFactory.getLog(PersistentListServiceImpl.class);
	private SessionFactory sessionFactory;

	@Autowired
	public DatabaseServiceProvider(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Gets all existing lists from the database.
	 * @return Array of existing {@link PersistentListModel}s
	 * @should return existing lists
	 */
	@Override
	public PersistentListModel[] getLists() {
		Session session = sessionFactory.getCurrentSession();

		try {
			Criteria search = session.createCriteria(PersistentListModel.class);

			return new ArrayList<PersistentListModel>(search.list()).toArray(new PersistentListModel[0]);
		} catch (Exception ex) {
			throw new PersistentListException("An exception occurred while attempting to get the lists.", ex);
		} finally {
			//session.close();
		}
	}

	/**
	 * Adds a list to the database.
	 * @return Integer The primary key of the new database record
	 * @should add a new list
	 */
	@Override
	public Integer addList(PersistentListModel list) {
		Session session = sessionFactory.getCurrentSession();
		Integer listId = null;

		try {
			listId = (Integer) session.save(list);
		} catch (Exception ex) {
			throw new PersistentListException("An exception occurred while attempting to add a list.", ex);
		} finally {
			//session.close();
		}
		return listId;
	}

	/**
	 * Removes a list from the database by its key
	 * @should remove a list
	 */
	@Override
	public void removeList(String key) {
		Session session = sessionFactory.getCurrentSession();

		try {
			// Find the lists with the specified key (should only be one)
			Criteria search = session.createCriteria(PersistentListModel.class)
					.add(Restrictions.eq("key", key));

			// Delete the lists
			for (Object list : search.list()) {
				session.delete(list);
			}
			session.flush();
		} catch (Exception ex) {
			throw new PersistentListException("An exception occurred while attempting to add a list.", ex);
		} finally {
			//session.close();
		}
	}
}
