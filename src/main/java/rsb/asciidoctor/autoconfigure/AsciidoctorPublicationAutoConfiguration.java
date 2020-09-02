package rsb.asciidoctor.autoconfigure;

import lombok.extern.log4j.Log4j2;
import org.asciidoctor.Asciidoctor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Log4j2
@Configuration
@EnableConfigurationProperties(PublicationProperties.class)
@ConditionalOnClass(Asciidoctor.class)
class AsciidoctorPublicationAutoConfiguration {

	@Bean
	@ConditionalOnProperty(name = "publication.epub.enabled", havingValue = "true", matchIfMissing = true)
	EpubProducer epubProducer(PublicationProperties pp) {
		return new EpubProducer(pp);
	}

	@Bean
	@ConditionalOnProperty(name = "publication.mobi.enabled", havingValue = "true", matchIfMissing = true)
	MobiProducer mobiProducer(PublicationProperties pp,
			@Value("classpath:/kindlegen") Resource kindlegen) throws Exception {
		return new MobiProducer(pp, kindlegen);
	}

	@Bean
	@ConditionalOnProperty(name = "publication.html.enabled", havingValue = "true", matchIfMissing = true)
	HtmlProducer htmlProducer(PublicationProperties pp) {
		return new HtmlProducer(pp);
	}

	@Bean
	@ConditionalOnProperty(name = "publication.pdf.enabled", havingValue = "true", matchIfMissing = true)
	ScreenPdfProducer screenPdfProducer(PublicationProperties pp) {
		return new ScreenPdfProducer(pp);
	}

	@Bean
	@ConditionalOnProperty(name = "publication.pdf.enabled", havingValue = "true", matchIfMissing = true)
	PrepressPdfProducer prepressPdfProducer(PublicationProperties pp) {
		return new PrepressPdfProducer(pp);
	}

	@Bean
	DocumentProducerProcessor documentProducerProcessor(Asciidoctor ad,
			ObjectProvider<DocumentProducer> dps, PublicationProperties pp) {
		var array = dps.stream().toArray(DocumentProducer[]::new);
		return new DocumentProducerProcessor(ad, array, pp);
	}

	@Bean
	Asciidoctor asciidoctor(ObjectProvider<AsciidoctorCustomizer> customizers) {
		var asciidoctor = Asciidoctor.Factory.create();
		customizers.orderedStream().forEach(ac -> ac.customize(asciidoctor));
		return asciidoctor;
	}

}
