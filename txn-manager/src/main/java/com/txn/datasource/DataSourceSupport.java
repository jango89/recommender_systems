package com.txn.datasource;

/**
 * @author shahinkonadath
 */
public class DataSourceSupport {
    
    public enum DataSourceType {
        MYSQL, MONGO, ELASTIC_SEARCH, RABBIT_MQ, REDIS, HBASE, KAFKA, REDISENTITYSTATUS
    }
    
    public enum DataOperation {
        INSERT, UPDATE, DELETE, FETCH
    }
    
    public final static String DS_COMMON = "common";
	public static final String REDIS_COMMON = "redisTemplateCommon";

}
