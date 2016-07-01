package com.system.core.service;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import com.system.core.entity.nosql.UserInfo;
import com.system.core.repository.nosql.DataIndexerRepository;
import com.system.core.repository.nosql.UserInfoRepository;

@Service
public class DashboardService {

	private final DataIndexerRepository dataIndexerRepository;
	private final UserInfoRepository userInfoRepository;
	private final ElasticsearchTemplate esTemplate;

	@Autowired
	public DashboardService(DataIndexerRepository dataIndexerRepository, UserInfoRepository userInfoRepository,
			ElasticsearchTemplate esTemplate) {
		this.dataIndexerRepository = dataIndexerRepository;
		this.userInfoRepository = userInfoRepository;
		this.esTemplate = esTemplate;
	}

	public JSONObject getDashboardDetails() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("courseStatus", dataIndexerRepository.count());
		jsonObject.put("recommendations", userInfoRepository.count());
		NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
		searchQueryBuilder.addAggregation(AggregationBuilders.terms("count").field("ipAddress")
				.order(org.elasticsearch.search.aggregations.bucket.terms.Terms.Order.count(false)));
		searchQueryBuilder.withIndices(UserInfo.class.getAnnotation(Document.class).indexName());
		searchQueryBuilder.withTypes(UserInfo.class.getAnnotation(Document.class).type());
		Aggregations aggs = esTemplate.query(searchQueryBuilder.build(), new ResultsExtractor<Aggregations>() {
			@Override
			public Aggregations extract(SearchResponse response) {
				return response.getAggregations();
			}
		});
		Bucket buck = ((StringTerms) aggs.get("count")).getBuckets().get(0);
		String value = buck.getKeyAsString();
		jsonObject.put("maxHitIp", value);
		Pageable pageable = new PageRequest(0, 1, Direction.DESC, "currentTime");
		UserInfo userInfo = userInfoRepository
				.search(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("ipAddress", value)), pageable)
				.getContent().get(0);
		jsonObject.put("maxHitValue", userInfo.getCurrentTime());
		return jsonObject;
	}

}
