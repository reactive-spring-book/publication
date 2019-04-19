package rsb;

import lombok.extern.log4j.Log4j2;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.ast.Document;
import org.asciidoctor.extension.Treeprocessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * Thank you <a href="https://github.com/asciidoctor/asciidoctorj">Asciidoctorj</a>!
 */
@Log4j2
@Configuration
class AsciidocConfiguration {

	private final PublicationProperties properties;

	private final File indexAdocFile;

	AsciidocConfiguration(PublicationProperties properties) {
		this.properties = properties;
		this.indexAdocFile = new File(this.properties.getRoot(), "index.adoc");
	}

	@Bean
	Asciidoctor asciidoctor() {
		Asciidoctor asciidoctor = Asciidoctor.Factory.create();
		asciidoctor.javaExtensionRegistry().treeprocessor(new Treeprocessor() {
			@Override
			public Document process(Document document) {
				log.debug("processing: '" + document.doctitle() + "'");
				return document;
			}
		});
		return asciidoctor;
	}

}
