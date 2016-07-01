package no.difi.config;

import no.difi.filevalidator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;
import java.util.stream.Collectors;

@Controller
public class MetadataController {

    private final Validator validator;
    private InputStream stream;

    @Autowired
    public MetadataController(Validator validator){
        this.validator = validator;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String provideUploadInfo(Model model) {
        File rootFolder = new File(Application.ROOT);

        model.addAttribute("files",
                Arrays.stream(rootFolder.listFiles())
                        .sorted(Comparator.comparingLong(f -> -1 * f.lastModified()))
                        .map(File::getName)
                        .collect(Collectors.toList())
        );
        model.addAttribute("appVersion", applicationVersion());

        return "index";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (!file.isEmpty()) {
            try {
                stream = file.getInputStream();
            } catch (IOException e) {
                //TODO: Log stacktrace to file
                redirectAttributes.addFlashAttribute("message", "Feil under streaming av fil.");
            }

            redirectAttributes
                    .addFlashAttribute("message", "Filen er lastet opp")
                    .addFlashAttribute("showpanel", true)
                    .addFlashAttribute("filename", file.getOriginalFilename());
        }

        validator.validate(stream, redirectAttributes);
        redirectAttributes.addFlashAttribute("file", file.getOriginalFilename() + " er validert");

        return "redirect:/";
    }

    private String applicationVersion() {
        InputStream pomPropertiesResource = getClass().getResourceAsStream(
                "/META-INF/maven/no.difi.kontaktinfo/idporten-saml-metadata-validator/pom.properties"
        );
        if (pomPropertiesResource != null) {
            Properties pomProperties = new Properties();
            try {
                pomProperties.load(pomPropertiesResource);
            } catch (IOException e) {
                return "Could not find version";
            }
            return pomProperties.getProperty("version");
        }
        return "Could not find version";
    }
}
