package batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author okoybaev
 */

@SpringBootApplication
@EnableBatchProcessing
public class Main extends WebMvcConfigurerAdapter {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
