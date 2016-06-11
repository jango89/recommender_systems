package com.system.core.crawler.support;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

public class NlpParserUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(NlpParserUtil.class);
	private static final String PARSE_BIN = "/en-parser-chunking.bin";
	private static final String TOP_NODE = "TOP";
	private static final String NOUN_ABR = "N";
	private final ParserModel parserModel;
	private static final List<String> BLACKLISTED = Lists.newArrayList("•");

	public NlpParserUtil() {
		try {
			InputStream is = getClass().getResourceAsStream(PARSE_BIN);
			parserModel = new ParserModel(is);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Set<String> generateParsedTokens(List<String> sentences, String message) {
		LOGGER.info(message);
		Parser parser = ParserFactory.create(parserModel);
		Set<String> courseKeywords = Sets.newHashSet();
		sentences.stream().map(sentence -> {
			Arrays.asList(ParserTool.parseLine(sentence, parser, 1)).stream()
					.map(parse -> parseChildrenNodes(parse, courseKeywords)).count();
			return sentence;
		}).count();
		return courseKeywords;
	}

	private Set<String> parseChildrenNodes(Parse parse, Set<String> courseKeywords) {
		if (parse.getType().equals(TOP_NODE)) {
			courseKeywords.addAll(Arrays.asList(parse.getHead().getChildren()).stream()
					.filter(parseval -> parseval.getParent().getType().startsWith(NOUN_ABR))
					.filter(parseval -> !BLACKLISTED.contains(parseval.toString())).map(this::parseNounChildrenNodes)
					.collect(Collectors.toSet()));
		}
		return courseKeywords;
	}

	private String parseNounChildrenNodes(Parse parse) {
		return parse.toString();
	}

}
