package no.difi.filevalidator;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;


public class Validator {

    public boolean validate(InputStream stream, RedirectAttributes redirectAttributes){
        if(!validateXML(stream, redirectAttributes)){
            redirectAttributes.addFlashAttribute("message", "Filen er ikke gyldig xml");
            return false;
        }
        redirectAttributes.addFlashAttribute("message", "Filen er gyldig xml");
        return true;
    }

    private boolean validateXML(InputStream stream, RedirectAttributes redirectAttributes) {

        if(stream==null){
            return false;
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(true);

        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            //TODO: Log stacktrace to file
            e.printStackTrace();
            return false;
        }
        try {
            Document document = builder.parse(stream);
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
