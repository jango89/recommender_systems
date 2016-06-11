package com.system.core.crawler.support;

import java.net.URL;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class PdfReaderUtil {

	public String getPdfStringData(String url) {
		try {
			PDDocument pddoc = PDDocument.load(new URL(url));
			COSDocument cosDoc = pddoc.getDocument();
			PDFTextStripper pdfStripper = new PDFTextStripper();
			PDDocument pdDoc = new PDDocument(cosDoc);
			pdfStripper.setStartPage(3);
			pdfStripper.setEndPage(3);
			String parsedText = pdfStripper.getText(pdDoc);
			cosDoc.close();
			pddoc.close();
			return parsedText;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
