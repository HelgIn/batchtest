package batch.config;

import batch.model.User;
import org.springframework.batch.core.*;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;

/**
 * @author okoybaev
 */
@Configuration
public class BatchConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").<User, User>chunk(1)
                .reader(reader("defaultFilename"))
                .processor(processor())
                .writer(writer())
                .listener(chunkListener())
                .build();
    }

    @Bean
    public ItemWriter<User> writer() {
        return System.out::println;
    }

    @Bean
    public ItemProcessor<User, User> processor() {
        return p -> p;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<User> reader(@Value("#{jobParameters['filename']}") String filename) {
        FlatFileItemReader<User> reader = new FlatFileItemReader<>();

        reader.setResource(new ClassPathResource(filename));

        DefaultLineMapper<User> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        lineTokenizer.setNames(new String[]{"name", "age"});

        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);

        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        reader.setLineMapper(defaultLineMapper);
        return reader;
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    ChunkListener chunkListener() {
        return new ChunkListener() {
            @Override
            public void beforeChunk(ChunkContext context) {

            }

            @Override
            public void afterChunk(ChunkContext context) {
                System.out.println(context.getStepContext().getJobName() + " " + (context.isComplete() ? "completed" : "not completed"));
            }

            @Override
            public void afterChunkError(ChunkContext context) {

            }
        };
    }
}
