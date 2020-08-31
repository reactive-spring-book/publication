package rsb.asciidoctor.autoconfigure;

import lombok.extern.log4j.Log4j2;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.internal.AsciidoctorCoreException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

/**
 * This is _super_ fragile. In order for this to work you need to provide an environment
 * variable - ${code KINDLEGEN} - where the ${code kindlegen} binary can be downloaded.
 * The environment variable should point to the location of `kindlegen` itself. You
 * specify that and this code will download the requisite binary from Amazon and make sure
 * it's there for macOS or Unix variants.
 * <p>
 * TODO as of 31/08/2020, the Kindlegen utility no longer seems to be available for
 * download in the usual places.
 */
@Log4j2
class MobiProducer implements DocumentProducer {

	private final PublicationProperties properties;

	private final Resource kindlegenZipArchive;

	MobiProducer(PublicationProperties properties, Resource kindlegenZipArchive)
			throws Exception {
		this.properties = properties;
		this.kindlegenZipArchive = kindlegenZipArchive;
		var os = System.getProperty("os.name").toLowerCase();
		/*
		 * this.downloadKindlegen(os.contains("mac"), (os.contains("nix") ||
		 * os.contains("nux") || os.indexOf("aix") > 0) );
		 */

		this.installKindlegen(
				(os.contains("nix") || os.contains("nux") || os.indexOf("aix") > 0));

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
		return new File[] { new File(this.properties.getRoot(), "index-kf8.epub"),
				new File(this.properties.getRoot(), "index.mobi") };
	}

	private void installKindlegen(boolean unix) throws Exception {

		if (unix) {
			var kindlegen = properties.getMobi().getKindlegen();
			var kindlegenLocation = kindlegen.getBinaryLocation();
			var ext = "zip";
			var dir = kindlegenLocation.getParentFile();
			var out = new File(dir, "dl." + ext);
			// if the directory doesn't exist, lets make it exist and install the archive
			if (!dir.exists() || !out.exists()) {
				Assert.isTrue(dir.exists() || dir.mkdirs(),
						"couldn't create the directory for the archive, "
								+ dir.getAbsolutePath());
				try (var is = this.kindlegenZipArchive.getInputStream();
						var os = new FileOutputStream(out)) {
					FileCopyUtils.copy(is, os);
				}
				log.info("downloaded the file to " + out.getAbsolutePath() + " to "
						+ kindlegenLocation.getAbsolutePath());
			}

			if (!kindlegenLocation.exists()) {
				this.unpack(out, kindlegenLocation);
			}
		}
		else {
			log.info("this won't work on the Mac or Windows. Try Linux.");
		}
	}

	/*
	 * the archive for Kindlegen is no longer accessible from Amazon. Thanks Ammazon! So,
	 * for now, we bundle our own until we can figure out what the upgrade path is.
	 */
	@Deprecated
	private void downloadKindlegen(boolean mac, boolean nix) throws Exception {

		var kindlegen = properties.getMobi().getKindlegen();
		var kindlegenLocation = kindlegen.getBinaryLocation();

		Assert.isTrue(mac || nix, "This program was only tested on Mac and Linux.");

		var toDownload = kindlegen.getUnixDownloadURI();
		var ext = "tgz";
		if (mac) {
			toDownload = kindlegen.getOsxDownloadURI();
			ext = "zip";
		}

		var uri = new URL(toDownload);
		var dir = kindlegenLocation.getParentFile();
		var out = new File(dir, "dl." + ext);
		if (!dir.exists() || !out.exists()) {
			Assert.isTrue(dir.exists() || dir.mkdirs(),
					"couldn't create the directory for the archive, "
							+ dir.getAbsolutePath());
			try (var is = uri.openStream(); var os = new FileOutputStream(out)) {
				FileCopyUtils.copy(is, os);
			}
			log.info("downloaded the file to " + out.getAbsolutePath() + " to "
					+ kindlegenLocation.getAbsolutePath());
		}

		if (!kindlegenLocation.exists()) {
			this.unpack(out, kindlegenLocation);
		}
	}

	private void unpack(File dl, File kindlegen) throws Exception {
		var in = dl.getAbsolutePath();
		var out = kindlegen.getParentFile().getAbsolutePath();
		var cmd = (dl.getName().endsWith(".zip")) ? "unzip " + in + " -d " + out
				: "tar xvzf " + in + " -C " + out;
		var returnValue = Runtime.getRuntime().exec(cmd).waitFor();
		log.info("extracted " + in + " to " + kindlegen.getAbsolutePath()
				+ " having return value " + returnValue);
	}

}
