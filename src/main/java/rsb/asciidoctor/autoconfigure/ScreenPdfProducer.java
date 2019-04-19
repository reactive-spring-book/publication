package rsb.asciidoctor.autoconfigure;

class ScreenPdfProducer extends AbstractPdfProducer {

	ScreenPdfProducer(PublicationProperties properties) {
		super(properties);
	}

	@Override
	protected String getMedia() {
		return PublicationProperties.Pdf.PdfMedia.SCREEN.name().toLowerCase();
	}

}
