package com.example.production;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;
import org.springframework.stereotype.Component;

import java.io.File;

@Log4j2
@Component
@RequiredArgsConstructor
class HtmlProducer implements DocumentProducer {

	private final PublicationProperties properties;

	@Override
	public File[] produce(Asciidoctor asciidoctor) throws Exception {
		AttributesBuilder builder = this.buildCommonAttributes(
				this.properties.getBookName(), this.properties.getCode());
		OptionsBuilder html = this.buildCommonOptions("html", builder);
		File index = getIndexAdoc(this.properties.getRoot());
		asciidoctor.convertFile(index, html);
		File images = new File(index.getParentFile(), "images");
		File indexHtml = new File(index.getParentFile(), "index.html");
		return new File[] { indexHtml, images };
	}

}
