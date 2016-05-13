/**
 * @File: PropspaceElasticSearchDataSource.java
 */
package com.txn.datasource.elasticsearch;

import java.io.Serializable;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

/**
 * @author shahinkonadath
 */
public class ElasticSearchDataSource {
    
    private static final Logger propLogger = LoggerFactory.getLogger(ElasticSearchDataSource.class);
    
    private ElasticsearchTemplate elasticsearchTemplate;
    private Client client;
    
    /**
     * @param env
     * @param dsName
     */
    public ElasticSearchDataSource(Environment env, String dsName) {
        this.elasticsearchTemplate = createElasticSearchTemplate(env, dsName);
    }
    
    /**
     * This constructor helps to set the elasticsearchtemplate and client as the one passed in arguments 
     */
    public ElasticSearchDataSource(ElasticsearchTemplate elasticsearchTemplate, Client client) throws Exception {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.client = client;
    }
    
    /**
     * @param env
     * @param dsName
     * @return
     */
    private ElasticsearchTemplate createElasticSearchTemplate(Environment env, String dsName) {
        
        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", env.getProperty(dsName + ".es.clustername")).build();
        String esIPs = env.getProperty(dsName + ".es.hosts");
        String[] allIps = esIPs.split("\\,");
        client = new TransportClient(settings);
        for (String esIP : allIps) {
            ((TransportClient) client).addTransportAddress(new InetSocketTransportAddress(esIP, Integer.valueOf(env.getProperty(dsName + ".es.port"))));
        }
        
        propLogger.info("Connected to elastic-search client: " + client);
        return new ElasticsearchTemplate(client);
    }
    
    /**
     * Method to execute the aggregation result for the query passed.
     * 
     * The filters when added with post-filter, the result will get reflected under the Hits session of aggregation response. The filters when added with aggregations, the result will get reflected under the Aggregation session of aggregation
     *       response.
     * 
     * @param query
     * @param indexName
     * @param type
     * @return
     * @throws Exception
     */
    public <E extends Serializable> SearchResponse generateElasticSearchResponse(NativeSearchQuery query, String indexName, String type) throws Exception {
        
        if (null != this.client) {
            query.addIndices(indexName);
            query.addTypes(type);
            SearchRequestBuilder searchRequest = this.client.prepareSearch(indexName);
            if (null != query.getAggregations()) {
                for (AbstractAggregationBuilder aggBuilder : query.getAggregations()) {
                    searchRequest.addAggregation(aggBuilder).setPostFilter(query.getFilter());
                }
            }
            
            propLogger.debug("Index : "+ indexName + " : ES-REQUEST: " + searchRequest.toString());
            
            SearchResponse searchResponse = searchRequest.execute().actionGet(TimeValue.timeValueMinutes(5));
            
//            propLogger.debug("ES-RESPONSE: " + searchResponse.toString());
            
            return searchResponse;
            
        } else {
            throw new Exception("Client cannot be null!!!");
        }
        
    }
    
    /**
     * Method to execute the aggregation result for the query passed.
     * 
     * The filters when added with post-filter, the result will get reflected under the Hits session of aggregation response. The filters when added with aggregations, the result will get reflected under the Aggregation session of aggregation
     *       response.
     * 
     */
    public static <E extends Serializable> SearchHits generateElasticSearchFunctionalScore(SearchQuery query, ElasticsearchTemplate elasticsearchTemplateSentOver) throws Exception {
        return elasticsearchTemplateSentOver.query(query, new ResultsExtractor<SearchHits>() {
            @Override
            public SearchHits extract(org.elasticsearch.action.search.SearchResponse response) {
                return response.getHits();
            }
        });
    }
    
    /**
     * @return the elasticsearchTemplate
     */
    public ElasticsearchTemplate getElasticsearchTemplate() {
        return elasticsearchTemplate;
    }
    
    public void destroy() {
        client.close();
    }
    
    /**
     * @return the client
     */
    public Client getClient() {
        return client;
    }
    
    /**
     * @param client
     *            the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }
    
    /**
     * @param elasticsearchTemplate
     *            the elasticsearchTemplate to set
     */
    public void setElasticsearchTemplate(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

}// End of the class