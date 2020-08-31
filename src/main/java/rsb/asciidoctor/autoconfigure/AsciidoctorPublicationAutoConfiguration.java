package rsb.asciidoctor.autoconfigure;

import lombok.extern.log4j.Log4j2;
import org.asciidoctor.Asciidoctor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
	EpubProducer epubProducer(PublicationProperties pp) {
		return new EpubProducer(pp);
	}

	@Bean
	HtmlProducer htmlProducer(PublicationProperties pp) {
		return new HtmlProducer(pp);
	}

	@Bean
	MobiProducer mobiProducer(PublicationProperties pp,
			@Value("classpath:/kindlegen.zip") Resource kindlegen) throws Exception {
		return new MobiProducer(pp, kindlegen);
	}

	@Bean
	ScreenPdfProducer screenPdfProducer(PublicationProperties pp) {
		return new ScreenPdfProducer(pp);
	}

	@Bean
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
