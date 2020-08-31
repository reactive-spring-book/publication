package rsb.asciidoctor.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.asciidoctor.Asciidoctor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.util.Assert;

import java.io.File;
import java.util.stream.Stream;

import static rsb.asciidoctor.autoconfigure.FileCopyUtils.copy;

@Log4j2
@RequiredArgsConstructor
class DocumentProducerProcessor {

	private final Asciidoctor asciidoctor;

	private final DocumentProducer[] producers;

	private final PublicationProperties properties;

	@EventListener(ApplicationReadyEvent.class)
	public void produceDocuments() throws Exception {
		log.info("there are " + this.producers.length + " "
				+ DocumentProducer.class.getName() + " instances");
		Stream.of(this.producers).forEach(producer -> {
			try {
				var name = producer.getClass().getName();
				log.info("Running " + name + ".");
				var filesArray = producer.produce(this.asciidoctor);
				var fileStream = Stream.of(filesArray);
				Assert.isTrue(filesArray.length > 0,
						"The " + name + " didn't produce any artifacts!");
				this.collectOutputFiles(producer, fileStream);
			}
			catch (Exception e) {
				log.error("had trouble running " + producer.getClass().getName()
						+ " and received the following exception: ", e);
			}
		});
	}

	private void collectOutputFiles(DocumentProducer producer, Stream<File> files) {
		var name = producer.getClass().getSimpleName().toLowerCase().replace("producer",
				"");
		var target = new File(this.properties.getTarget(), name);
		Assert.isTrue(target.exists() || target.mkdirs(), "the target directory "
				+ target.getAbsolutePath() + " does not exist and couldn't be created");
		files.forEach(
				inputFile -> doCopy(inputFile, new File(target, inputFile.getName())));
	}

	private void doCopy(File in, File out) {
		Assert.isTrue(in.exists(),
				"The input file " + in.getAbsolutePath() + " does not exist");
		copy(in, out);
		var outAbsolutePath = out.getAbsolutePath();
		Assert.isTrue(out.exists(),
				"The output file, " + outAbsolutePath + ", does not exist; copy failed.");
	}

}
