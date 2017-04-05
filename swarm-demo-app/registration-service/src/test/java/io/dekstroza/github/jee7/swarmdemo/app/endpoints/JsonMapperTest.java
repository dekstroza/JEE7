package io.dekstroza.github.jee7.swarmdemo.app.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dekstroza.github.jee7.swarmdemo.app.domain.RegistrationInfo;
import io.dekstroza.github.jee7.swarmdemo.app.domain.User;
import org.junit.Test;

public class JsonMapperTest {

    @Test
    public void convertToJsonTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        RegistrationInfo info = new RegistrationInfo();
        info.setEmail("dejan.kitic@ericsson.com");
        info.setPassword("kolokvijum");
        System.out.println("info = " + mapper.writeValueAsString(info));
    }
}
