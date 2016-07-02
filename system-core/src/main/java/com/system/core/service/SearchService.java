package com.system.core.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.system.core.crawler.support.NlpSentenceUtil;
import com.system.core.crawler.support.PdfReaderUtil;
import com.system.core.crawler.support.SearchUtil;
import com.system.core.entity.nosql.DataIndexer;
import com.system.core.response.GraphResponse;

@Service
public class SearchService implements SearchInterface {

	private final NlpSentenceUtil nlpSentenceUtil = new NlpSentenceUtil();
	private final PdfReaderUtil pdfReaderUtil = new PdfReaderUtil();
	private final SearchUtil searchUtil = new SearchUtil();
	private final ElasticsearchTemplate esTemplate;

	@Autowired
	public SearchService(ElasticsearchTemplate esTemplate) {
		this.esTemplate = esTemplate;
	}

	@Override
	public List<GraphResponse> searchByUploadedPdf(MultipartFile multiPartFile) {
		Set<String> pdfKeywords;
		try {
			String contents = pdfReaderUtil.getPdfStringData(multiPartFile);
			pdfKeywords = nlpSentenceUtil.generateNlpTokensForProfilePdf(contents);
		} catch (IOException e) {
			throw new RuntimeException("Error happened while reading the pdf");
		}
		pdfKeywords = pdfKeywords.stream().map(keyword -> keyword.toLowerCase()).collect(Collectors.toSet());
		SearchQuery searchQuery = searchUtil.generateSearchQuery(pdfKeywords);
		SearchHits hits = esTemplate.query(searchQuery, new ResultsExtractor<SearchHits>() {
			public SearchHits extract(SearchResponse searchResponse) {
				return searchResponse.getHits();
			}
		});
		List<GraphResponse> graphResponseList = generateGraphResponseList(hits);
		return graphResponseList;
	}

	private List<GraphResponse> generateGraphResponseList(SearchHits hits) {
		List<GraphResponse> graphResponseList = Lists.newLinkedList();
		for (SearchHit hit : hits.getHits()) {
			GraphResponse graphResponse = new GraphResponse();
			graphResponse.setScore(hit.getScore());
			try {
				DataIndexer dataIndexer = new com.fasterxml.jackson.databind.ObjectMapper()
						.readValue(hit.getSourceAsString(), DataIndexer.class);
				graphResponse.setCourseName(dataIndexer.getCourseName());
				graphResponse.setLabel(dataIndexer.getProgrameName().concat(" - ").concat(dataIndexer.getCourseName()));
				graphResponseList.add(graphResponse);
			} catch (Exception e) {
				throw new RuntimeException("Cannot cast value from search hit", e);
			}
		}
		return graphResponseList;
	}

	@Override
	public List<GraphResponse> searchByUploadedText(String stringValue) {
		Set<String> pdfKeywords;
		try {
			pdfKeywords = nlpSentenceUtil.generateNlpTokensForNormalSearch(stringValue);
		} catch (IOException e) {
			throw new RuntimeException("Error happened while reading the pdf");
		}
		pdfKeywords = pdfKeywords.stream().map(keyword -> keyword.toLowerCase()).collect(Collectors.toSet());
		SearchQuery searchQuery = searchUtil.generateSearchQuery(pdfKeywords);
		SearchHits hits = esTemplate.query(searchQuery, new ResultsExtractor<SearchHits>() {
			public SearchHits extract(SearchResponse searchResponse) {
				return searchResponse.getHits();
			}
		});
		List<GraphResponse> graphResponseList = generateGraphResponseList(hits);
		return graphResponseList;
	}

}
