package com.example.production;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.asciidoctor.Asciidoctor;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;

import static com.example.production.FileCopyUtils.copy;

@Log4j2
@Component
@RequiredArgsConstructor
class DocumentProducerProcessor implements SmartInitializingSingleton {

	private final Asciidoctor asciidoctor;

	private final DocumentProducer[] producers;

	private final PublicationProperties properties;

	@Override
	public void afterSingletonsInstantiated() {
		log.info("there are " + this.producers.length + " " + DocumentProducer.class.getName() + " instances");
		for (var producer : this.producers) {
			try {


				/*
				// todo don't let this get checked in
				if (!(producer instanceof PrepressPdfProducer)) {
					System.err.println("REMOVE THIS!");
					continue;
				}
				*/
				log.info("... running " + producer.getClass().getName());
				File[] files = producer.produce(this.asciidoctor);
				for (File f : files) {
					Assert.isTrue(f.exists(), "the output file " + f.getAbsolutePath()
						+ " does not exist, but should");
				}
				this.collectOutputFiles(producer, files);
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void collectOutputFiles(DocumentProducer producer, File[] files)
		throws Exception {
		var name = producer.getClass().getSimpleName().toLowerCase().replace("producer", "");
		var target = new File(this.properties.getTarget(), name);
		Assert.isTrue(target.exists() || target.mkdirs(), "the target directory " + target.getAbsolutePath() + " does not exist");
		for (var inputFile : files) {
			copy(inputFile, new File(target, inputFile.getName()));
		}
	}

}
