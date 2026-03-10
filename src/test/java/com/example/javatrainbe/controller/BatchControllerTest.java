package com.example.javatrainbe.controller;

import com.example.javatrainbe.exception.GlobalExceptionHandler;
import com.example.javatrainbe.service.BatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BatchControllerTest {

    @Mock
    private BatchService batchService;

    @InjectMocks
    private BatchController batchController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(batchController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Nested
    @DisplayName("POST /api/batch/export")
    class ExportStudentCsv {

        @Test
        @DisplayName("Trả về 200 khi export thành công")
        void shouldExportSuccessfully() throws Exception {
            JobExecution jobExecution = mock(JobExecution.class);
            when(batchService.runExportStudentCsvJob()).thenReturn(jobExecution);
            when(batchService.getJobStatus(jobExecution)).thenReturn("Job ID: 1, Status: COMPLETED, Start Time: 2026-03-10T10:00:00, End Time: 2026-03-10T10:00:05");

            mockMvc.perform(post("/api/batch/export"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("Batch job đã chạy thành công"))
                    .andExpect(jsonPath("$.data").value("Job ID: 1, Status: COMPLETED, Start Time: 2026-03-10T10:00:00, End Time: 2026-03-10T10:00:05"));

            verify(batchService).runExportStudentCsvJob();
            verify(batchService).getJobStatus(jobExecution);
        }

        @Test
        @DisplayName("Trả về 400 khi export thất bại")
        void shouldReturn400WhenExportFails() throws Exception {
            when(batchService.runExportStudentCsvJob())
                    .thenThrow(new RuntimeException("Batch job thất bại: Job failed"));

            mockMvc.perform(post("/api/batch/export"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400));
        }
    }

    @Nested
    @DisplayName("GET /api/batch/status")
    class GetBatchStatus {

        @Test
        @DisplayName("Trả về 200 và trạng thái batch service")
        void shouldReturnBatchStatus() throws Exception {
            mockMvc.perform(get("/api/batch/status"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("Batch service đang hoạt động"))
                    .andExpect(jsonPath("$.data").value("Batch processing service is running"));
        }
    }

    @Nested
    @DisplayName("GET /api/batch/info")
    class GetBatchInfo {

        @Test
        @DisplayName("Trả về 200 và thông tin batch")
        void shouldReturnBatchInfo() throws Exception {
            mockMvc.perform(get("/api/batch/info"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("Thông tin Batch Processing"))
                    .andExpect(jsonPath("$.data").isString());
        }
    }
}
