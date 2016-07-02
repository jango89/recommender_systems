package com.system.core.crawler.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;

public class NlpSentenceUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(NlpSentenceUtil.class);
	private static final String SEMESTER = "SEMESTER";
	private static final String SENT_BIN_LIB = "/en-sent.bin";
	private final NlpParserUtil nlpParserUtil;

	public NlpSentenceUtil() {
		nlpParserUtil = new NlpParserUtil();
	}

	public Set<String> generateNlpTokens(String parsedText) throws InvalidFormatException, IOException {
		String[] sentences = retrieveSentences(parsedText);
		return generateSentencesWithSemesterTokens(sentences);
	}

	public Set<String> generateNlpTokensForProfilePdf(String parsedText) throws InvalidFormatException, IOException {
		String[] sentences = retrieveSentences(parsedText);
		return generateSentences(sentences);
	}

	public Set<String> generateNlpTokensForNormalSearch(String parsedText) throws InvalidFormatException, IOException {
		String[] sentences = retrieveSentences(parsedText);
		return generateSentencesForNormalSearch(sentences);
	}

	private String[] retrieveSentences(String parsedText) throws InvalidFormatException, IOException {
		InputStream modelIn = getClass().getResourceAsStream(SENT_BIN_LIB);
		SentenceModel sentenceModel = new SentenceModel(modelIn);
		SentenceDetectorME sentenceDetectorME = new SentenceDetectorME(sentenceModel);
		String[] sentences = sentenceDetectorME.sentDetect(parsedText);
		return sentences;
	}

	private Set<String> generateSentencesWithSemesterTokens(String[] sentences) {
		List<String> sentenceList = Arrays.asList(sentences);
		List<Integer> indexesOfSemester = sentenceList.stream().filter(sentence -> sentence.contains(SEMESTER))
				.map(sentence -> sentenceList.indexOf(sentence)).collect(Collectors.toList());
		List<String> firstSemList = sentenceList.subList(indexesOfSemester.get(0), indexesOfSemester.get(1));
		List<String> secondSemList = sentenceList.subList(indexesOfSemester.get(1), sentenceList.size() - 1);
		LOGGER.info("Successfully generated first and second semester course details");
		Set<String> courseKeywords = nlpParserUtil.generateParsedTokens(firstSemList, "first semester");
		courseKeywords.addAll(nlpParserUtil.generateParsedTokens(secondSemList, "second semester"));
		return courseKeywords;
	}

	private Set<String> generateSentences(String[] sentences) {
		List<String> sentenceList = Arrays.asList(sentences);
		Set<String> keywords = nlpParserUtil.generateParsedTokens(sentenceList, "Profile pdf");
		return keywords;
	}

	private Set<String> generateSentencesForNormalSearch(String[] sentences) {
		List<String> sentenceList = Arrays.asList(sentences);
		Set<String> keywords = nlpParserUtil.generateParsedTokensForNormalSearches(sentenceList, "Normal searches");
		return keywords;
	}
}
