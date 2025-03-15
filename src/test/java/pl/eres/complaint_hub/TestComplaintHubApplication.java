package pl.eres.complaint_hub;

import org.springframework.boot.SpringApplication;

public class TestComplaintHubApplication {

    public static void main(String[] args) {
        SpringApplication.from(ComplaintHubApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
