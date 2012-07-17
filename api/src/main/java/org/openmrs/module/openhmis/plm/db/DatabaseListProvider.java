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
import org.hibernate.*;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.openmrs.module.openhmis.plm.*;
import org.openmrs.module.openhmis.plm.impl.PersistentListServiceImpl;
import org.openmrs.module.openhmis.plm.model.PersistentListItemModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link PersistentListProvider} which stores items in a database.
 */
@Component
public class DatabaseListProvider implements PersistentListProvider {
	private static final String ADD_SQL =
		"UPDATE plm_list_items SET primary_order = primary_order + 1 WHERE list_id = ? AND item_order >= ?";
	private static final String REMOVE_SQL =
		"UPDATE plm_list_items SET primary_order = primary_order - 1 WHERE list_id = ? AND item_order >= ?";
	private static final String CLEAR_SQL = "DELETE FROM plm_list_items WHERE list_id = ?";

	private final Log log = LogFactory.getLog(PersistentListServiceImpl.class);
	private final Object syncLock = new Object();

	private SessionFactory sessionFactory;

	/**
	 * Creates a new DatabaseListProvider instance with the specified hibernate {@link SessionFactory}.
	 * @param sessionFactory The {@link SessionFactory} used to create the connection to the database.
	 */
	@Autowired
	public DatabaseListProvider(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Gets the name of the serviceProvider.
	 * @return The name of the serviceProvider.
	 */
	@Override
	public String getName() {
		return "Database List Provider";
	}

	/**
	 * Gets the description of the serviceProvider.
	 * @return The description of the serviceProvider.
	 */
	@Override
	public String getDescription() {
		return "A persistent list serviceProvider that stores items in an OpenMRS database.";
	}

	/**
	 * Adds a new item to the list.
	 * @param item The item to add.
	 * @should add the item to the database
	 * @should correctly update the index of the other items in the table
	 * @should throw PersistentListException when the command returns 0 rows updated
	 * @should roll back the transaction when the operation fails
	 * @shold commit the transaction if no operations fail
	 */
	@Override
	public void add(final PersistentListItemModel item) {
		Session session = sessionFactory.getCurrentSession();
		Transaction trans = null;

		try
		{
			// Start transaction
			trans = session.beginTransaction();

			// Update all items >= index to be their current index + 1
			// TODO: Can this type of query be handled by Hibernate without needing to use hardcoded SQL?
			session.doWork(new Work() {
				public void execute(Connection connection) {
					try {
						PreparedStatement cmd = connection.prepareStatement(ADD_SQL);
						cmd.setInt(1, item.getListId());
						cmd.setInt(2, item.getItemOrder());

						cmd.executeUpdate();
					} catch (SQLException sex) {
						throw new PersistentListException(sex);
					}
				}
			});

			// Insert item with index
			session.save(item);
			session.flush();

			// Commit transaction
			trans.commit();
		} catch (Exception ex) {
			log.debug("The list add operation failed.  Rolling back transaction...");
			trans.rollback();
			log.debug("Transaction rolled back.");

			throw new PersistentListException("An exception occurred while attempting to add the item to the list.", ex);
		} finally {
			session.close();
		}
	}

	/**
	 * Removes the specified item from the list.
	 * @param item The item to remove.
	 * @return {@code true} if the item was removed; otherwise, {@code false}.
	 */
	@Override
	public boolean remove(final PersistentListItemModel item) {
		Session session = sessionFactory.getCurrentSession();
		Transaction trans = null;

		try
		{
			// Start transaction
			trans = session.beginTransaction();

			// Delete item with index
			session.delete(item);
			session.flush();

			// Update all items >= index to be their current index - 1
			// TODO: Can this type of query be handled by Hibernate without needing to use hardcoded SQL?
			session.doWork(new Work() {
				public void execute(Connection connection) {
					try {
						PreparedStatement cmd = connection.prepareStatement(REMOVE_SQL);
						cmd.setInt(1, item.getListId());
						cmd.setInt(2, item.getItemOrder());

						cmd.executeUpdate();
					} catch (SQLException sex) {
						throw new PersistentListException(sex);
					}
				}
			});

			// Commit transaction
			trans.commit();
		} catch (Exception ex) {
			log.error("The list item delete operation failed.  Rolling back transaction...", ex);
			trans.rollback();
			log.debug("Transaction rolled back.");

			throw new PersistentListException("An exception occurred while attempting to delete the item from the list.", ex);
		} finally {
			session.close();
		}

		return true;
	}

	/**
	 * Removes all items from the specified list.
	 * @param list The list to clear.
	 */
	@Override
	public void clear(final PersistentList list) {
		Session session = sessionFactory.getCurrentSession();

		try {
			// Delete all items with list key
			session.doWork(new Work() {
				public void execute(Connection connection) {
					try {
						PreparedStatement cmd = connection.prepareStatement(CLEAR_SQL);
						cmd.setInt(1, list.getId());

						cmd.executeUpdate();
					} catch (SQLException sex) {
						throw new PersistentListException(sex);
					}
				}
			});
		} catch (Exception ex) {
			throw new PersistentListException("An exception occurred while attempting to get the list items.", ex);
		} finally {
			session.close();
		}
	}

	/**
	 * Gets all the items from the list in order.
	 * @param list The @see PersistentList to get.
	 * @return The items in the list.
	 */
	@Override
	public PersistentListItemModel[] getItems(PersistentList list) {
		List<PersistentListItemModel> result = null;

		Session session = sessionFactory.getCurrentSession();
		try {
			// Return the items in the specified list ordered by the primary order
			Criteria search = session.createCriteria(PersistentListItemModel.class)
					.add(Restrictions.eq("list_id", list.getId()))
					.addOrder(Order.asc("primary_order"))
					.addOrder(Order.asc("secondary_order"))
					.addOrder(Order.asc("tertiary_order"));

			result = new ArrayList<PersistentListItemModel>(search.list());
		} catch (Exception ex) {
			throw new PersistentListException("An exception occurred while attempting to get the list items.", ex);
		} finally {
			session.close();
		}

		if (result == null) {
			return new PersistentListItemModel[0];
		} else {
			return result.toArray(new PersistentListItemModel[0]);
		}
	}
}

