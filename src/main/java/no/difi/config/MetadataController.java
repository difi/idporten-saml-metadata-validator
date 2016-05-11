package no.difi.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller
public class MetadataController {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MetadataController.class, args);
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String hello() {
        return "<html>" +
                "<head>" +
                "    <title>Hello World</title>" +
                "</head>" +
                "<body>" +
                "    Biiig <strong>hello</strong> world" +
                "</body>" +
                "</html>";
    }
}
