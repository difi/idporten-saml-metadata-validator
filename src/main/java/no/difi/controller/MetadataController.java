package no.difi.controller;

import no.difi.application.Application;
import no.difi.domain.ValidationResult;
import no.difi.service.ValidatorService;
import no.difi.service.Message;
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
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
public class MetadataController {

    private final ValidatorService validatorService;

    @Autowired
    private Environment environment;

    @Autowired
    public MetadataController(final ValidatorService validatorService){
        this.validatorService = validatorService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String provideUploadInfo(final Model model) {
        final File rootFolder = new File(Application.ROOT);

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
                                   final RedirectAttributes redirectAttributes) {

        ValidationResult validationResult = ValidationResult.builder().valid(false).message(Message.VALIDATION_GENERAL_ERROR.key()).result("").build();
            try {
                if(!file.isEmpty()) {
                    validationResult = validatorService.validate(file);
                }
            } catch (IOException e) {
                //TODO: Log stacktrace to file
                validationResult = ValidationResult.builder().valid(false).message(Message.VALIDATION_GENERAL_ERROR.key()).result(e.getMessage()).build();
            }
            redirectAttributes
                    .addFlashAttribute("showpanel", validationResult.getValid())
                    .addFlashAttribute("message", validationResult.getMessage())
                    .addFlashAttribute("result", validationResult.getResult());
        if(!file.isEmpty()) {
            redirectAttributes.addFlashAttribute("filename", file.getOriginalFilename());
        }

        return "redirect:/";
    }
}
