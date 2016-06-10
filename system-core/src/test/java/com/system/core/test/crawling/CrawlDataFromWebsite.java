package com.system.core.test.crawling;

import java.io.IOException;
import java.net.URL;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.elasticsearch.common.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.system.core.config.SystemCoreCrawlerConfig;
import com.system.core.craweler.support.NlpSentenceUtil;

import opennlp.tools.util.InvalidFormatException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SystemCoreCrawlerConfig.class })
public class CrawlDataFromWebsite {

	private static final String CRAWLER_SITE = "crawler.site";

	private static String crawlerSiteDetail = StringUtils.EMPTY;

	@Autowired
	private Environment environment;
	private NlpSentenceUtil nlpSentenceUtil;

	@Before
	public void before() {
		crawlerSiteDetail = environment.getProperty(CRAWLER_SITE);
		nlpSentenceUtil = new NlpSentenceUtil(environment);
	}

	@Test
	public void getDataFromWebsite() throws InvalidFormatException, IOException {
		String courseDetails = getCourseDetails();
		nlpSentenceUtil.generateNlpTokensForCourse(courseDetails);
	}

	private String getCourseDetails() {
		try {
			PDDocument pddoc = PDDocument.load(new URL(crawlerSiteDetail));
			COSDocument cosDoc = pddoc.getDocument();
			PDFTextStripper pdfStripper = new PDFTextStripper();
			PDDocument pdDoc = new PDDocument(cosDoc);
			pdfStripper.setStartPage(3);
			pdfStripper.setEndPage(3);
			String parsedText = pdfStripper.getText(pdDoc);
			return parsedText;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}