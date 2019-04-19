package rsb;

import org.springframework.stereotype.Component;

@Component
class PrepressPdfProducer extends AbstractPdfProducer {

	PrepressPdfProducer(PublicationProperties properties) {
		super(properties);
	}

	@Override
	String getMedia() {
		return PublicationProperties.Pdf.PdfMedia.PREPRESS.name().toLowerCase();
	}

}
