package no.difi.config;

import no.difi.filevalidator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MetadataController {

    private static final String VALIDATION_OK_RESULT = "validation.ok.result";
    private static final String VALIDATION_GENERAL_ERROR = "validation.general.error";
    private final Validator validator;

    @Autowired
    private Environment environment;

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

        return "index";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (!file.isEmpty()) {
            try {
                validator.validate(file);
            } catch (IOException e) {
                //TODO: Log stacktrace to file
                validator.setMessage(environment.getProperty(VALIDATION_GENERAL_ERROR));
            }
            redirectAttributes
                    .addFlashAttribute("showpanel", true)
                    .addFlashAttribute("filename", file.getOriginalFilename())
                    .addFlashAttribute("message", validator.getMessage())
                    .addFlashAttribute("result", validator.getResult());
        }
        return "redirect:/";
    }
}
