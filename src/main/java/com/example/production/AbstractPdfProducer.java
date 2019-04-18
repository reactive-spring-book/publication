package com.example.production;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;

import java.io.File;

@Log4j2
@RequiredArgsConstructor
abstract class AbstractPdfProducer implements DocumentProducer {

	private final PublicationProperties properties;

	abstract String getMedia();

	@Override
	public File[] produce(Asciidoctor asciidoctor) throws Exception {
		String bookName = this.properties.getBookName();
		File indexAdoc = getIndexAdoc(this.properties.getCode());
		PublicationProperties.Pdf pdf = this.properties.getPdf();
		AttributesBuilder attributesBuilder = this
				.buildCommonAttributes(bookName, pdf.getIsbn(), indexAdoc)
				.attribute("pdfmarks") //
				.title(this.properties.getBookName()).attribute("idseparator", "-") //
				.imagesDir("images") //
				.attribute("pdf-stylesdir", pdf.getStyles().getAbsolutePath()) //
				.attribute("pdf-fontsdir", pdf.getFonts().getAbsolutePath()) //
				.attribute("media", getMedia()) //
				.attribute("code", this.properties.getCode().getAbsolutePath()) //
				.attribute("idprefix") //
				.attribute("icons", "font") //
				.attribute("pdf-style", getMedia()) //
				.attribute("idprefix") //
				.attribute("project-version", "2.0.0-SNAPSHOT") //
				.attribute("subject", bookName) //
				.attribute("project-name", bookName) //
				.attribute("pdfmarks");

		OptionsBuilder optionsBuilder = this.buildCommonOptions("pdf", attributesBuilder)
				.docType("book");

		asciidoctor.convertFile(this.getIndexAdoc(this.properties.getRoot()),
				optionsBuilder);

		return new File[] { new File(this.properties.getRoot(), "index.pdf") };
	}

}
