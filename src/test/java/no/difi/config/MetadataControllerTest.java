package no.difi.config;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MetadataControllerTest {
    public static final String ROOT_TEMPLATE = "/";
    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new MetadataController()).build();
    }

    @Test
    public void should_get_ok_with_mediatype_text_html_when_requesting_root() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ROOT_TEMPLATE).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }
}