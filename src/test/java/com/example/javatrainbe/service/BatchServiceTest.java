package com.example.javatrainbe.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BatchServiceTest {

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private Job exportStudentCsvJob;

    @InjectMocks
    private BatchService batchService;

    @Nested
    @DisplayName("runExportStudentCsvJob")
    class RunExportStudentCsvJob {

        @Test
        @DisplayName("Chạy batch job thành công")
        void shouldRunJobSuccessfully() throws Exception {
            JobExecution jobExecution = mock(JobExecution.class);
            when(jobExecution.getStatus()).thenReturn(BatchStatus.COMPLETED);
            when(jobLauncher.run(eq(exportStudentCsvJob), any(JobParameters.class))).thenReturn(jobExecution);

            JobExecution result = batchService.runExportStudentCsvJob();

            assertNotNull(result);
            assertEquals(BatchStatus.COMPLETED, result.getStatus());
            verify(jobLauncher).run(eq(exportStudentCsvJob), any(JobParameters.class));
        }

        @Test
        @DisplayName("Ném RuntimeException khi job thất bại")
        void shouldThrowExceptionWhenJobFails() throws Exception {
            when(jobLauncher.run(eq(exportStudentCsvJob), any(JobParameters.class)))
                    .thenThrow(new JobInstanceAlreadyCompleteException("Job failed"));

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> batchService.runExportStudentCsvJob());
            assertTrue(exception.getMessage().contains("Batch job thất bại"));
        }
    }

    @Nested
    @DisplayName("getJobStatus")
    class GetJobStatus {

        @Test
        @DisplayName("Trả về thông tin trạng thái job")
        void shouldReturnJobStatus() {
            JobExecution jobExecution = mock(JobExecution.class);
            when(jobExecution.getId()).thenReturn(1L);
            when(jobExecution.getStatus()).thenReturn(BatchStatus.COMPLETED);
            when(jobExecution.getStartTime()).thenReturn(LocalDateTime.of(2026, 3, 10, 10, 0, 0));
            when(jobExecution.getEndTime()).thenReturn(LocalDateTime.of(2026, 3, 10, 10, 0, 5));

            String status = batchService.getJobStatus(jobExecution);

            assertTrue(status.contains("Job ID: 1"));
            assertTrue(status.contains("COMPLETED"));
        }

        @Test
        @DisplayName("Trả về 'Job not found' khi jobExecution null")
        void shouldReturnNotFoundWhenNull() {
            String status = batchService.getJobStatus(null);

            assertEquals("Job not found", status);
        }
    }
}
