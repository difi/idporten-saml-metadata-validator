package no.difi.config;

import no.difi.filevalidator.Validator;
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
class MetadataController {

    private final Validator validator;
    private InputStream stream;

    public MetadataController(){
        validator = new Validator();
    }
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String provideUploadInfo(Model model) {
        File rootFolder = new File(Application.ROOT);
        List<String> fileNames = Arrays.stream(rootFolder.listFiles())
                .map(f -> f.getName())
                .collect(Collectors.toList());

        model.addAttribute("files",
                Arrays.stream(rootFolder.listFiles())
                        .sorted(Comparator.comparingLong(f -> -1 * f.lastModified()))
                        .map(f -> f.getName())
                        .collect(Collectors.toList())
        );

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
        }

        validator.validate(stream, redirectAttributes);
        redirectAttributes.addFlashAttribute("file", file.getOriginalFilename() + " er validert");

        return "redirect:/";
    }
}
