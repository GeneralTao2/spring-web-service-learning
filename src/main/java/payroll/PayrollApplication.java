package payroll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

// is a meta-annotation that pulls in component scanning, autoconfiguration, and property support.
// We won’t dive into the details of Spring Boot in this tutorial, but in essence,
// it will fire up a servlet container and serve up our service.
@SpringBootApplication
public class PayrollApplication {
    <T> List<T> hello() {
        return new ArrayList<T>();
    }
    public static void main(String... args) {
        SpringApplication.run(PayrollApplication.class, args);
    }
}