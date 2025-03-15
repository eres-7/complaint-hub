package pl.eres.complaint_hub;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Tag("testcontainers")
@Import(TestcontainersConfiguration.class)
@SpringBootTest
class ComplaintHubApplicationTests {

    @Test
    void contextLoads() {
        // loads spring boot context
    }

}
