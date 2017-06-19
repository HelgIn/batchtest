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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;


/**
 * @author okoybaev
 */
@Controller
public class MainController {

    @Autowired
    private BatchService batchService;

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException, ClassNotFoundException, JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, InterruptedException {
        String filename = UUID.randomUUID().toString();
        Files.copy(file.getInputStream(), Paths.get(filename));
        JobParameters jobParameters = new JobParametersBuilder().addString("filename", filename).toJobParameters();
        batchService.start(jobParameters);
        return "redirect:/";
    }
}
