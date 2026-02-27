package com.example.javatrainbe.service;

import com.example.javatrainbe.utils.DateTimeUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service xử lý Batch Processing
 */
@Service
public class BatchService {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job exportStudentCsvJob;

    /**
     * Chạy batch job export student data sang CSV
     */
    public JobExecution runExportStudentCsvJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("timestamp", DateTimeUtils.getCurrentTimestamp())
                    .addString("jobName", "exportStudentCsvJob")
                    .toJobParameters();
            return jobLauncher.run(exportStudentCsvJob, params);
        } catch (Exception e) {
            throw new RuntimeException("Batch job thất bại: " + e.getMessage(), e);
        }
    }

    /**
     * Lấy trạng thái job
     */
    public String getJobStatus(JobExecution jobExecution) {
        if (jobExecution == null) {
            return "Job not found";
        }
        return String.format("Job ID: %d, Status: %s, Start Time: %s, End Time: %s",
                jobExecution.getId(),
                jobExecution.getStatus(),
                jobExecution.getStartTime(),
                jobExecution.getEndTime());
    }
}
