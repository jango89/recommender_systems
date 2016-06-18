package com.system.core.crawler.support;

import java.util.Set;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import com.system.core.entity.nosql.DataIndexer;

public class SearchUtil {

	public SearchQuery generateSearchQuery(Set<String> keywords) {
		NativeSearchQueryBuilder sqBuilder = new NativeSearchQueryBuilder();
		sqBuilder.withIndices(DataIndexer.class.getAnnotation(Document.class).indexName());
		sqBuilder.withTypes(DataIndexer.class.getAnnotation(Document.class).type());
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.should(generateQueryForKeywords(keywords));
		sqBuilder.withQuery(boolQueryBuilder);
		return sqBuilder.build();
	}

	private QueryBuilder generateQueryForKeywords(Set<String> keywords) {
		return QueryBuilders.boolQuery().should(QueryBuilders.termsQuery("courseKeywords", keywords));
	}
}
