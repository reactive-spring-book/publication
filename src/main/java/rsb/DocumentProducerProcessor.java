package rsb;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.asciidoctor.Asciidoctor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.util.stream.Stream;

import static rsb.FileCopyUtils.copy;

@Log4j2
@Component
@RequiredArgsConstructor
class DocumentProducerProcessor {

	private final Asciidoctor asciidoctor;

	private final DocumentProducer[] producers;

	private final PublicationProperties properties;

	@EventListener(ApplicationReadyEvent.class)
	public void produceDocuments() throws Exception {
		log.info("there are " + this.producers.length + " "
				+ DocumentProducer.class.getName() + " instances");
		for (var producer : this.producers) {
			var name = producer.getClass().getName();
			var filesArray = Stream.of(producer.produce(this.asciidoctor));
			Assert.isTrue(filesArray.count() > 0,
					"The " + name + " didn't produce any artifacts!");
			filesArray.forEach(file -> Assert.isTrue(file.exists(), "the output file "
					+ file.getAbsolutePath() + " does not exist, but should"));

			this.collectOutputFiles(producer, filesArray);
		}
	}

	private void collectOutputFiles(DocumentProducer producer, Stream<File> files) {
		var name = producer.getClass().getSimpleName() //
				.toLowerCase().replace("producer", "");
		var target = new File(this.properties.getTarget(), name);
		Assert.isTrue(target.exists() || target.mkdirs(), "the target directory "
				+ target.getAbsolutePath() + " does not exist and couldn't be created");
		files.forEach(
				inputFile -> copy(inputFile, new File(target, inputFile.getName())));

	}

}
