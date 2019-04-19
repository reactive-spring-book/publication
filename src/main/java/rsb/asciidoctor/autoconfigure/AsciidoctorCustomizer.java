package rsb.asciidoctor.autoconfigure;

import org.asciidoctor.Asciidoctor;

@FunctionalInterface
public interface AsciidoctorCustomizer {

	void customize(Asciidoctor a);

}
