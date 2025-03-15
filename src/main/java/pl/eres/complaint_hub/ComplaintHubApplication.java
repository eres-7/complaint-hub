package pl.eres.complaint_hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("pl.eres")
public class ComplaintHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComplaintHubApplication.class, args);
    }

}
