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
		AttributesBuilder attributesBuilder = this
				.buildCommonAttributes(bookName, getIndexAdoc(this.properties.getCode()))
				.attribute("pdfmarks").attribute("idseparator", "-").imagesDir("images")
				.attribute("pdf-stylesdir",
						this.properties.getPdf().getStyles().getAbsolutePath())
				.attribute("pdf-fontsdir",
						this.properties.getPdf().getFonts().getAbsolutePath())
				.attribute("media", getMedia())
				.attribute("code", this.properties.getCode().getAbsolutePath())
				.attribute("idprefix").attribute("icons", "font").attribute("idprefix")
				.attribute("project-version", "2.0.0-SNAPSHOT")
				.attribute("subject", bookName).attribute("project-name", bookName)
				.attribute("pdfmarks").title(bookName);

		OptionsBuilder optionsBuilder = this.buildCommonOptions("pdf", attributesBuilder)
				.docType("book");

		asciidoctor.convertFile(this.getIndexAdoc(this.properties.getRoot()),
				optionsBuilder);

		return new File[] { new File(this.properties.getRoot(), "index.pdf") };
	}

}
