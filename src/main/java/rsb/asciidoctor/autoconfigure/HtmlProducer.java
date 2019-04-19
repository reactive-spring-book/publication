package rsb.asciidoctor.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.asciidoctor.Asciidoctor;
import org.springframework.stereotype.Component;

import java.io.File;

@Log4j2
@RequiredArgsConstructor
class HtmlProducer implements DocumentProducer {

	private final PublicationProperties properties;

	@Override
	public File[] produce(Asciidoctor asciidoctor) throws Exception {
		var builder = this.buildCommonAttributes(this.properties.getBookName(),
				"(No ISBN required)", this.properties.getCode());
		var html = this.buildCommonOptions("html", builder);
		var index = this.getIndexAdoc(this.properties.getRoot());
		asciidoctor.convertFile(index, html);
		var images = new File(index.getParentFile(), "images");
		var indexHtml = new File(index.getParentFile(), "index.html");
		return new File[] { indexHtml, images };
	}

}
