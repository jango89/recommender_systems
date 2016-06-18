package com.system.core.crawler.support;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;

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

	public String getPdfStringData(MultipartFile multipartFile) throws IOException {
		File convFile = multipartToFile(multipartFile);
		try {
			multipartFile.transferTo(convFile);
			PDDocument pddoc = PDDocument.load(convFile);
			COSDocument cosDoc = pddoc.getDocument();
			PDFTextStripper pdfStripper = new PDFTextStripper();
			PDDocument pdDoc = new PDDocument(cosDoc);
			String parsedText = pdfStripper.getText(pdDoc);
			cosDoc.close();
			pddoc.close();
			return parsedText;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			convFile.delete();
		}
	}

	private File multipartToFile(MultipartFile multipartFile) throws IOException {

		File uploadFile = null;

		if (multipartFile != null && multipartFile.getSize() > 0) {
			uploadFile = new File("/tmp/" + multipartFile.getOriginalFilename());
			FileOutputStream fos = null;
			try {
				uploadFile.createNewFile();
				fos = new FileOutputStream(uploadFile);
				IOUtils.copy(multipartFile.getInputStream(), fos);
			} catch (FileNotFoundException e) {
				System.out.println("File conversion error");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("File conversion error");
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						System.out.println("File conversion error");
						e.printStackTrace();
					}
				}
			}
		} else {
			System.out.println("null MultipartFile");
		}

		return uploadFile;
	}
}
