package com.example.production;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;

import java.io.File;

import static org.asciidoctor.AttributesBuilder.attributes;
import static org.asciidoctor.OptionsBuilder.options;

public interface DocumentProducer {

	default OptionsBuilder buildCommonOptions(String backend) {
		return options().safe(SafeMode.UNSAFE).backend(backend).inPlace(false);
	}

	default OptionsBuilder buildCommonOptions(String backend,
																																											AttributesBuilder attributesBuilder) {
		return this.buildCommonOptions(backend).attributes(attributesBuilder);
	}

	default File getIndexAdoc(File root) {
		return new File(root, "index.adoc");
	}

	default AttributesBuilder buildCommonAttributes(
		String bookName,
		String isbn,
		File source) {

		return attributes()//
			.tableOfContents(true) //

			.attribute("isbn", isbn) //
//				.attribute("title", bookName) //
//				.attribute("subject", bookName) //
			.attribute("book-name", bookName) //
			.sectionNumbers(true) //
			.attribute("code", source.getAbsolutePath()) //
			.tableOfContents(true) //
			.sectionNumbers(true) //
			// .showTitle(true) //
			.imagesDir("images") //
			.sourceHighlighter("coderay");
	}

	File[] produce(Asciidoctor asciidoctor) throws Exception;

}
