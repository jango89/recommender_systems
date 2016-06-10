package com.system.core.craweler.support;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import com.google.common.collect.Lists;

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
	private static final String CRAWLER_PROGRAM = "crawler.program";
	private static final String CRAWLER_COURSE = "crawler.course";
	private Environment environment;

	public NlpParserUtil(Environment environment) {
		try {
			InputStream is = getClass().getResourceAsStream(PARSE_BIN);
			parserModel = new ParserModel(is);
			this.environment = environment;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void generateParsedTokens(List<String> sentences, String message) {
		LOGGER.info(message);
		Parser parser = ParserFactory.create(parserModel);
		sentences.stream().map(sentence -> {
			Arrays.asList(ParserTool.parseLine(sentence, parser, 1)).stream().map(this::parseChildrenNodes).count();
			return sentence;
		}).count();
	}

	private Parse parseChildrenNodes(Parse parse) {
		if (parse.getType().equals(TOP_NODE)) {
			Arrays.asList(parse.getHead().getChildren()).stream()
					.filter(parseval -> parseval.getParent().getType().startsWith(NOUN_ABR))
					.filter(parseval -> !BLACKLISTED.contains(parseval.toString())).map(this::parseNounChildrenNodes)
					.count();
		}
		return parse;
	}

	private Parse parseNounChildrenNodes(Parse parse) {
		LOGGER.info(parse.toString());
		return parse;
	}

}
