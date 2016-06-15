package no.difi.filevalidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

@Component
public class Validator {
    private final Environment environment;

    @Autowired
    public Validator(final Environment environment){
        this.environment = environment;
    }

    public boolean validate(final InputStream stream, final RedirectAttributes redirectAttributes){
        if(!validateXML(stream, redirectAttributes)){
            redirectAttributes.addFlashAttribute("message", environment.getProperty("validation.error.xml"));
            return false;
        }
        redirectAttributes.addFlashAttribute("message", environment.getProperty("validation.ok"));
        return true;
    }

    private boolean validateXML(final InputStream stream, final RedirectAttributes redirectAttributes) {

        if(stream == null){
            return false;
        }
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(true);

        final DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            //TODO: Log stacktrace to file
            e.printStackTrace();
            return false;
        }
        try {
            builder.parse(stream);
            stream.close();
        } catch (SAXException e) {
            //TODO: Log stacktrace to file
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return false;
        } catch (IOException e) {
            //TODO: Log stacktrace to file
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return false;
        }

        return true;
    }
}
