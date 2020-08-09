package rsb.asciidoctor.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.OptionsBuilder;
import org.springframework.util.Assert;

import java.io.File;

@Log4j2
@RequiredArgsConstructor
abstract class AbstractPdfProducer implements DocumentProducer {

	private final PublicationProperties properties;

	protected abstract String getMedia();

	@Override
	public File[] produce(Asciidoctor asciidoctor) throws Exception {
		var bookName = this.properties.getBookName();
		var indexAdoc = getIndexAdoc(this.properties.getCode());
		var pdf = this.properties.getPdf();
		var attributesBuilder = this
				.buildCommonAttributes(bookName, pdf.getIsbn(), indexAdoc)
				.attribute("idseparator", "-") //
				.imagesDir("images") //
				.attribute("media", getMedia()) //
				.attribute("code", this.properties.getCode().getAbsolutePath()) //
				.attribute("icons", "font") //
				.attribute("pdf-style", getMedia()) //
				.attribute("idprefix") //
				.attribute("project-version", "2.0.0-SNAPSHOT") //
				.attribute("subject", bookName) //
				.attribute("project-name", bookName) //
				.attribute("pdfmarks")
				.attribute("pdf-stylesdir", pdf.getStyles().getAbsolutePath()) //
				.attribute("pdf-fontsdir", pdf.getFonts().getAbsolutePath());

		if (false) {

			var fonts = pdf.getFonts();
			if (null != fonts && fonts.exists()) {
				var root = fonts.getAbsolutePath();
				attributesBuilder = attributesBuilder.attribute("pdf-fontsdir", root);
			}

			var styles = pdf.getStyles();
			if (null != styles && styles.exists()) {
				var directoryForThemes = styles.getParentFile();
				var themeFileName = styles.getName();
				var file = new File(directoryForThemes, themeFileName);
				Assert.isTrue(file.exists(),
						"the two must equal a valid style for the PDF theme");
				Assert.isTrue(file.getName().toLowerCase().endsWith(".yml"),
						"the file must end with .yml");
				var themesDirectoryPath = directoryForThemes.getAbsolutePath();
				for (var k : "pdf-stylesdir,pdf-themesdir".split(",")) {
					attributesBuilder = attributesBuilder.attribute(k,
							themesDirectoryPath);
				}
				for (var k : "pdf-style,pdf-theme".split(",")) {
					attributesBuilder = attributesBuilder.attribute(k, themeFileName);
				}
				attributesBuilder = attributesBuilder.stylesDir(themesDirectoryPath)
						.styleSheetName(themeFileName);
			}
		}

		OptionsBuilder optionsBuilder = this.buildCommonOptions("pdf", attributesBuilder)
				.docType("book");
		asciidoctor.convertFile(this.getIndexAdoc(this.properties.getRoot()),
				optionsBuilder);

		return new File[] { new File(this.properties.getRoot(), "index.pdf") };
	}

}
