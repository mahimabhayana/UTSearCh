package UTSearCh;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfFileParser implements DocumentParser{

	/*
	 * Parses text from pdf file
	 * @param file: A pdf file to be parsed.
	 * @return the string of the parsed contents of the pdf file
	 */
	public String parseDoc(File file) {
		// pdfbox stuff, instantiating
		PDFTextStripper pdfStripper = null;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;
		try {
            // open file for reading
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            // set the parser, parse
            PDFParser parser = new PDFParser(randomAccessFile);
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            pdfStripper.setStartPage(1);
            // parse all the pages
            pdfStripper.setEndPage(pdDoc.getNumberOfPages());
            String parsedText = pdfStripper.getText(pdDoc);
            // close everything
            randomAccessFile.close();
            cosDoc.close();
            pdDoc.close();
            return parsedText;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } 
	}

}
