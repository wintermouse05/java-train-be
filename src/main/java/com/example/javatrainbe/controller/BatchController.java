package com.example.javatrainbe.controller;

import com.example.javatrainbe.service.BatchService;
import com.example.javatrainbe.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller xử lý Batch Processing
 */
@RestController
@RequestMapping("/api/batch")
@Tag(name = "Batch Processing", description = "API batch export dữ liệu")
@CrossOrigin(origins = "*")
public class BatchController {

    @Autowired
    private BatchService batchService;

    /**
     * API trigger batch export student data sang CSV
     */
    @PostMapping("/export")
    @Operation(summary = "Export CSV", description = "Chạy batch job đọc thông tin student từ DB và ghi vào file CSV")
    public ResponseEntity<ApiResponse<String>> exportStudentCsv() {
        JobExecution execution = batchService.runExportStudentCsvJob();
        return ResponseEntity.ok(ApiResponse.success(
                "Batch job đã chạy thành công",
                batchService.getJobStatus(execution)));
    }

    /**
     * API kiểm tra trạng thái batch service
     */
    @GetMapping("/status")
    @Operation(summary = "Batch Status", description = "Kiểm tra trạng thái batch service")
    public ResponseEntity<ApiResponse<String>> getBatchStatus() {
        return ResponseEntity.ok(ApiResponse.success(
                "Batch service đang hoạt động",
                "Batch processing service is running"));
    }

    /**
     * API lấy thông tin về batch processing
     */
    @GetMapping("/info")
    @Operation(summary = "Batch Info", description = "Lấy thông tin về batch processing features")
    public ResponseEntity<ApiResponse<String>> getBatchInfo() {
        return ResponseEntity.ok(ApiResponse.success(
                "Thông tin Batch Processing",
                """
                Batch Processing Features:
                - Export Student data to CSV
                - Chunk-based processing (10 records per chunk)
                - Automatic directory creation
                - Timestamped file names
                - Transaction management
                - Error handling and logging
                """));
    }
}
