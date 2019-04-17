package com.example.production;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@Log4j2
@SpringBootApplication
@EnableConfigurationProperties(PublicationProperties.class)
public class PublicationApplication {

	public static void main(String[] args) {
		SpringApplication.run(PublicationApplication.class, args);
	}

	@Bean
	InitializingBean logRequiredProperties(Environment environment) {

		String[] properties = { "publication.root", "publication.target",
				"publication.bookName", "publication.code", "KINDLEGEN",
				"publication.epub.mobi.kindlegen.binary-location",
				"publication.pdf.fonts", "publication.pdf.styles" };
		return () -> {
			String lineSeparator = System.lineSeparator();
			StringBuilder sb = new StringBuilder();
			sb.append(lineSeparator);
			sb.append("---------------------------------------").append(lineSeparator);
			sb.append("Required Properties:").append(lineSeparator);
			for (String p : properties) {
				sb.append(p + '=' + environment.getProperty(p)).append(lineSeparator);
			}
			sb.append("---------------------------------------").append(lineSeparator);
			log.info(sb.toString());
		};
	}

}
