/**
 * @File: EntityReadStatus.java
 */
package com.txn.datasource;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shahinkonadath
 */
public class EntityReadStatus<T, ID extends Serializable> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2582815510490538364L;

	private Class<?> entityClass;
	private ID lastUpdatedId;
	private Date rLastUpdated;
	private Date nLastUpdated;

	/**
	 * @param entityClass
	 * @param lastUpdatedId
	 * @param rLastUpdated
	 * @param nLastUpdated
	 */
	public EntityReadStatus(Class<?> entityClass, ID lastUpdatedId, Date rLastUpdated, Date nLastUpdated) {
		this.entityClass = entityClass;
		this.lastUpdatedId = lastUpdatedId;
		this.rLastUpdated = rLastUpdated;
		this.nLastUpdated = nLastUpdated;
	}

	/**
	 * @return the entityClass
	 */
	public Class<?> getEntityClass() {
		return entityClass;
	}

	/**
	 * @param entityClass
	 *            the entityClass to set
	 */
	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * @return the lastUpdatedId
	 */
	public ID getLastUpdatedId() {
		return lastUpdatedId;
	}

	/**
	 * @param lastUpdatedId
	 *            the lastUpdatedId to set
	 */
	public void setLastUpdatedId(ID lastUpdatedId) {
		this.lastUpdatedId = lastUpdatedId;
	}

	/**
	 * @return the rLastUpdated
	 */
	public Date getrLastUpdated() {
		return rLastUpdated;
	}

	/**
	 * @param rLastUpdated
	 *            the rLastUpdated to set
	 */
	public void setrLastUpdated(Date rLastUpdated) {
		this.rLastUpdated = rLastUpdated;
	}

	/**
	 * @return the nLastUpdated
	 */
	public Date getnLastUpdated() {
		return nLastUpdated;
	}

	/**
	 * @param nLastUpdated
	 *            the nLastUpdated to set
	 */
	public void setnLastUpdated(Date nLastUpdated) {
		this.nLastUpdated = nLastUpdated;
	}

}// End of the class