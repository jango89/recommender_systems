package com.system.core.service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
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
import org.springframework.util.CollectionUtils;

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
		jsonObject = getAggregatedStringValues(jsonObject);
		jsonObject.put("courseStatus", dataIndexerRepository.count());
		jsonObject.put("recommendations", userInfoRepository.count());
		jsonObject.put("userCount", getUserCountToday());
		jsonObject = generateLastVisitorStatistics(jsonObject);
		return jsonObject;
	}

	private JSONObject getAggregatedStringValues(JSONObject jsonObject) {
		NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
		searchQueryBuilder.withIndices(UserInfo.class.getAnnotation(Document.class).indexName());
		searchQueryBuilder.withTypes(UserInfo.class.getAnnotation(Document.class).type());
		searchQueryBuilder.addAggregation(AggregationBuilders.terms("ipAddress").field("ipAddress")
				.order(org.elasticsearch.search.aggregations.bucket.terms.Terms.Order.count(false)));
		searchQueryBuilder.addAggregation(AggregationBuilders.terms("courseName").field("courseName")
				.order(org.elasticsearch.search.aggregations.bucket.terms.Terms.Order.count(false)));
		searchQueryBuilder.addAggregation(AggregationBuilders.terms("searchVia").field("searchVia")
				.order(org.elasticsearch.search.aggregations.bucket.terms.Terms.Order.count(false)));
		searchQueryBuilder.addAggregation(AggregationBuilders.avg("avg").field("timeInSeconds"));
		Aggregations aggs = esTemplate.query(searchQueryBuilder.build(), new ResultsExtractor<Aggregations>() {
			@Override
			public Aggregations extract(SearchResponse response) {
				return response.getAggregations();
			}
		});
		if (!CollectionUtils.isEmpty(((StringTerms) aggs.get("ipAddress")).getBuckets())) {
			Bucket buck = ((StringTerms) aggs.get("ipAddress")).getBuckets().get(0);
			jsonObject.put("maxHitIp", buck.getKeyAsString().toUpperCase());
		}
		if (!CollectionUtils.isEmpty(((StringTerms) aggs.get("courseName")).getBuckets())) {
			Bucket buck = ((StringTerms) aggs.get("courseName")).getBuckets().get(0);
			jsonObject.put("maxHitCourse", buck.getKeyAsString().toUpperCase());
		}
		if (!CollectionUtils.isEmpty(((StringTerms) aggs.get("searchVia")).getBuckets())) {
			for (Bucket buck : ((StringTerms) aggs.get("searchVia")).getBuckets()) {
				if (buck.getKeyAsString().equals("pdf")) {
					jsonObject.put("recommendPdf", buck.getDocCount());
				} else {
					jsonObject.put("recommendSearch", buck.getDocCount());
				}
			}

		}
		NumberFormat df = DecimalFormat.getInstance();
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(3);
		df.setRoundingMode(RoundingMode.DOWN);
		jsonObject.put("recommendTimeAvg", df.format(((InternalAvg) aggs.get("avg")).getValue()));

		Pageable pageable = new PageRequest(0, 1, Direction.DESC, "currentTime");
		UserInfo userInfo = userInfoRepository
				.search(QueryBuilders.boolQuery()
						.must(QueryBuilders.termQuery("ipAddress", jsonObject.get("maxHitIp"))), pageable)
				.getContent().get(0);
		jsonObject.put("maxHitValue", userInfo.getCurrentTime());

		return jsonObject;
	}

	private Long getUserCountToday() {
		NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
		searchQueryBuilder.withIndices(UserInfo.class.getAnnotation(Document.class).indexName());
		searchQueryBuilder.withTypes(UserInfo.class.getAnnotation(Document.class).type());
		LocalDate localDate = LocalDate.now();
		searchQueryBuilder.withQuery(QueryBuilders.boolQuery()
				.must(QueryBuilders.termQuery("month", localDate.getMonth().toString().toLowerCase()))
				.must(QueryBuilders.termQuery("year", localDate.getYear()))
				.must(QueryBuilders.termQuery("day", localDate.getDayOfMonth())));
		searchQueryBuilder.addAggregation(AggregationBuilders.terms("count").field("ipAddress")
				.order(org.elasticsearch.search.aggregations.bucket.terms.Terms.Order.count(false)));
		Aggregations aggs = esTemplate.query(searchQueryBuilder.build(), new ResultsExtractor<Aggregations>() {
			@Override
			public Aggregations extract(SearchResponse response) {
				return response.getAggregations();
			}
		});
		if (!CollectionUtils.isEmpty(((StringTerms) aggs.get("count")).getBuckets())) {
			Bucket buck = ((StringTerms) aggs.get("count")).getBuckets().get(0);
			return buck.getDocCount();
		} else {
			return 0L;
		}
	}

	private JSONObject generateLastVisitorStatistics(JSONObject jsonObject) {
		Pageable pageable = new PageRequest(0, 1, Direction.DESC, "currentTime");
		UserInfo userInfo = userInfoRepository.search(QueryBuilders.boolQuery(), pageable).getContent().get(0);
		jsonObject.put("lastDateVisited", userInfo.getDay() + "-" + userInfo.getMonth() + "-" + userInfo.getYear());
		jsonObject.put("lastCourseSelected", userInfo.getCourseName().toUpperCase());
		NumberFormat df = DecimalFormat.getInstance();
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(3);
		df.setRoundingMode(RoundingMode.DOWN);
		jsonObject.put("matchScore", df.format(userInfo.getScore() * 1000));
		return jsonObject;
	}

}
