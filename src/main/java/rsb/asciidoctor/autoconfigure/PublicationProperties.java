package rsb.asciidoctor.autoconfigure;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import java.io.File;

@Data
@ConfigurationProperties("publication")
class PublicationProperties {

	private File root, target, code;

	private String bookName = "";

	private Epub epub = new Epub();

	private Pdf pdf = new Pdf();

	private Mobi mobi = new Mobi();

	@Data
	public static class Pdf {

		private String isbn;

		private Pdf.PdfMedia media = Pdf.PdfMedia.SCREEN;

		private File fonts, styles;

		public enum PdfMedia {

			PREPRESS, SCREEN

		}

	}

	@Data
	public static class Epub {

		private String isbn;

	}

	@Data
	public static class Mobi {

		private String isbn;

		private Kindlegen kindlegen = new Kindlegen();

		@Data
		@Log4j2
		public static class Kindlegen {

			private File binaryLocation;

			private String osxDownloadURI = "https://kindlegen.s3.amazonaws.com/KindleGen_Mac_i386_v2_9.zip";

			private String unixDownloadURI = "http://kindlegen.s3.amazonaws.com/kindlegen_linux_2.6_i386_v2_9.tar.gz";

			Kindlegen() {
				String kindleGenEnvVariableName = "KINDLEGEN";
				String kindleGenEnvVariableValue = System
						.getenv(kindleGenEnvVariableName);
				Assert.hasText(kindleGenEnvVariableValue,
						"$" + kindleGenEnvVariableName + " must not be null");
				this.binaryLocation = new File(kindleGenEnvVariableValue);
			}

		}

	}

}
