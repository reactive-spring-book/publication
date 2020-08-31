package rsb.asciidoctor.autoconfigure;

import lombok.extern.log4j.Log4j2;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.internal.AsciidoctorCoreException;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This is _super_ fragile. In order for this to work you need to provide an environment
 * variable - ${code KINDLEGEN} - where the ${code kindlegen} binary can be found. The
 * environment variable should point to the location of the `kindlegen` binary itself.
 */
@Log4j2
class MobiProducer implements DocumentProducer {

	private final PublicationProperties properties;

	private final Resource kindlegenBinary;

	MobiProducer(PublicationProperties properties, Resource kindlegenBinary)
			throws Exception {
		this.properties = properties;
		this.kindlegenBinary = kindlegenBinary;
		this.installKindlegen();
	}

	@Override
	public File[] produce(Asciidoctor asciidoctor) throws Exception {

		var indexAdoc = getIndexAdoc(this.properties.getRoot());
		var bookName = this.properties.getBookName();
		var attributesBuilder = this.buildCommonAttributes(bookName,
				this.properties.getMobi().getIsbn(), this.properties.getCode())
				.attribute("ebook-format", "kf8");
		var optionsBuilder = this.buildCommonOptions("epub3", attributesBuilder);
		try {
			asciidoctor.convertFile(indexAdoc, optionsBuilder);
		}
		catch (AsciidoctorCoreException ace) {
			log.warn(
					"Exception when producing the .mobi. The cause is " + ace.getMessage()
							+ ". If the error says 'No child processes' but you "
							+ "see a resulting .mobi in "
							+ this.properties.getRoot().getParentFile().getAbsolutePath()
							+ "then don't worry about the error.");
		}
		catch (Throwable t) {
			log.warn("something went wrong! ", t);
		}
		return new File[] { new File(this.properties.getRoot(), "index-kf8.epub"),
				new File(this.properties.getRoot(), "index.mobi") };
	}

	private void installKindlegen() throws Exception {

		File binaryLocation = this.properties.getMobi().getKindlegen()
				.getBinaryLocation();
		try (InputStream inputStream = this.kindlegenBinary.getInputStream();
				OutputStream outputStream = new FileOutputStream(binaryLocation)) {
			FileCopyUtils.copy(inputStream, outputStream);
		}

		Assert.state(
				0 == Runtime.getRuntime()
						.exec("chmod a+x " + binaryLocation.getAbsolutePath()).waitFor(),
				"couldn't make the kindlegen binary executable");
	}

}
