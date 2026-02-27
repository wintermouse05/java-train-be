package com.example.javatrainbe.batch;

import com.example.javatrainbe.entity.StudentInfoWithStudent;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.File;

/**
 * Cấu hình Spring Batch Job: Export Student data sang CSV
 * Chỉ lo wiring các Bean (Reader, Processor, Writer) thành Job/Step
 */
@Configuration
public class StudentCsvJobConfig {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private JdbcCursorItemReader<StudentInfoWithStudent> studentCsvItemReader;

    @Autowired
    private StudentCsvProcessor studentCsvProcessor;

    @Autowired
    private StudentCsvWriter studentCsvWriter;

    @Value("${batch.export.path:./exports/}")
    private String exportPath;

    /**
     * Job: Export Student CSV gồm 2 step
     */
    @Bean
    public Job exportStudentCsvJob() {
        return new JobBuilder("exportStudentCsvJob", jobRepository)
                .start(createExportDirectoryStep())
                .next(exportStudentCsvStep())
                .build();
    }

    /**
     * Step 1: Tạo thư mục export nếu chưa có
     */
    @Bean
    public Step createExportDirectoryStep() {
        return new StepBuilder("createExportDirectoryStep", jobRepository)
                .tasklet(createExportDirectoryTasklet(), transactionManager)
                .build();
    }

    /**
     * Step 2: Đọc dữ liệu và ghi vào CSV (chunk = 10)
     */
    @Bean
    public Step exportStudentCsvStep() {
        return new StepBuilder("exportStudentCsvStep", jobRepository)
                .<StudentInfoWithStudent, String[]>chunk(10, transactionManager)
                .reader(studentCsvItemReader)
                .processor(studentCsvProcessor)
                .writer(studentCsvWriter)
                .build();
    }

    /**
     * Tasklet tạo thư mục export
     */
    @Bean
    public Tasklet createExportDirectoryTasklet() {
        return (contribution, chunkContext) -> {
            File dir = new File(exportPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return RepeatStatus.FINISHED;
        };
    }
}
