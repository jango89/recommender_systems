/**
 * 
 */
package com.txn.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Helps to create beans based on the configurations. This is used basically on @Config classes to help 
 * in the creation of beans for kafka, elasticsearch, entitymanager, transactionmanager!!!
 * @author rohit
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface BeanCreationDef {

	/**
	 * variable to store the name for the entity manager.
	 * @return
	 * rohit
	 */
	String EMName() default "";
	/**
	 * variable to store the name for the transaction manager.
	 * @return
	 * rohit
	 */
	String TMName() default "";
	/**
	 * variable to store the name for the elastic search.
	 * @return
	 * rohit
	 */
	String ESName() default "";
	/**
	 * variable to store the name for the apache kafka.
	 * @return
	 * rohit
	 */
	String KFKName() default "";
	/**
	 * variable to store the name for the Reddis manager.
	 * @return
	 * rohit
	 */
	String RDSName() default "";
	/**
	 * variable to store the name of the mongo db template.
	 * @return
	 */
	String MDBName() default "";
	/**
	 * variable to store the elasticsearch client for the entity manager.
	 * @return
	 * rohit
	 */
	String ESCName() default "";
	/**
	 * variable to store the redis entity manager- read status name
	 * @return
	 * rohit
	 */
	String RDSEntityStatusName() default "";
}
