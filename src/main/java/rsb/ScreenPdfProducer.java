package rsb;

import org.springframework.stereotype.Component;

@Component
class ScreenPdfProducer extends AbstractPdfProducer {

	ScreenPdfProducer(PublicationProperties properties) {
		super(properties);
	}

	@Override
	String getMedia() {
		return PublicationProperties.Pdf.PdfMedia.SCREEN.name().toLowerCase();
	}

}
