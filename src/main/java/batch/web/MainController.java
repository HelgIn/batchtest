package batch.web;

import batch.model.User;
import batch.service.BatchService;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * @author okoybaev
 */
@Controller
public class MainController {

    @Autowired
    private BatchService batchService;

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException, ClassNotFoundException, JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        Files.copy(file.getInputStream(), Paths.get(file.getOriginalFilename()));
//        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
//        String line;
//        while ((line = reader.readLine()) != null) {
//            User user = createUser(line);
//            itemCsvReader.getUsers().add(user);
//            System.out.println(user);
//        }
//        System.out.println(file.getOriginalFilename());
//

        JobParameters jobParameters = new JobParametersBuilder().addString("filename", file.getOriginalFilename()).toJobParameters();
        batchService.start(jobParameters);

        return "redirect:/static/index.html";
    }

    private User createUser(String line) {
        String[] split = line.split(",");
        return new User(split[0], Integer.parseInt(split[1].trim()));
    }

}
