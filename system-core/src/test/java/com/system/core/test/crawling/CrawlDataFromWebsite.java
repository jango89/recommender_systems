package com.system.core.test.crawling;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.system.core.config.SystemCoreCrawlerConfig;
import com.system.core.crawler.support.DataIndexerService;
import com.system.core.crawler.support.NlpSentenceUtil;
import com.system.core.crawler.support.PdfReaderUtil;
import com.system.core.entity.nosql.DataIndexer;

import opennlp.tools.util.InvalidFormatException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SystemCoreCrawlerConfig.class })
public class CrawlDataFromWebsite {

	private static final String CRAWLER_SITE = "crawler.site";
	private static final String CRAWLER_PROGRAM = "crawler.program";
	private static final String CRAWLER_COURSE = "crawler.course";

	private static String crawlerSiteDetail = StringUtils.EMPTY;

	@Autowired
	private Environment environment;

	@Autowired
	private DataIndexerService dataIndexerService;
	private NlpSentenceUtil nlpSentenceUtil;
	private PdfReaderUtil pdfReaderUtil;

	@Before
	public void before() {
		crawlerSiteDetail = environment.getProperty(CRAWLER_SITE);
		nlpSentenceUtil = new NlpSentenceUtil();
		pdfReaderUtil = new PdfReaderUtil();
	}

	@Test
	// @Ignore
	public void getDataFromWebsite() throws InvalidFormatException, IOException {
		String courseDetails = pdfReaderUtil.getPdfStringData(crawlerSiteDetail);
		Set<String> courseKeywords = nlpSentenceUtil.generateNlpTokens(courseDetails);
		courseKeywords.addAll(Arrays.asList(environment.getProperty(CRAWLER_COURSE).split(" ")));
		courseKeywords.addAll(Arrays.asList(environment.getProperty(CRAWLER_PROGRAM).split(" ")));
		DataIndexer dataIndexer = new DataIndexer(courseKeywords, environment.getProperty(CRAWLER_COURSE),
				environment.getProperty(CRAWLER_PROGRAM));
		dataIndexer = dataIndexerService.saveData(dataIndexer);
	}

	@Ignore
	// @Test
	public void deleteAllCrawledCourse() {
		dataIndexerService.resetAllData();
	}

}