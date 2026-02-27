package com.example.javatrainbe.batch;

import com.example.javatrainbe.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Batch Writer: Ghi dữ liệu student vào file CSV
 * Giữ FileWriter mở suốt job, flush mỗi chunk
 */
@Slf4j
@Component
public class StudentCsvWriter implements ItemWriter<String[]> {

    private static final String[] HEADER = {
            "StudentID", "StudentName", "StudentCode", "Address", "AverageScore", "DateOfBirth"
    };

    @Value("${batch.export.path:./exports/}")
    private String exportPath;

    private FileWriter fileWriter;
    private boolean headerWritten = false;

    @Override
    public void write(Chunk<? extends String[]> chunk) throws Exception {
        // Tạo file nếu chưa có
        if (fileWriter == null) {
            String fileName = exportPath + "students_" + DateTimeUtils.generateTimestamp() + ".csv";
            fileWriter = new FileWriter(fileName);
            log.info("Created CSV file: {}", fileName);
        }

        // Ghi header nếu chưa có
        if (!headerWritten) {
            fileWriter.write(String.join(",", HEADER) + "\n");
            headerWritten = true;
        }

        // Ghi từng dòng dữ liệu
        for (String[] row : chunk.getItems()) {
            String[] escapedRow = new String[row.length];
            for (int i = 0; i < row.length; i++) {
                escapedRow[i] = escapeCsvValue(row[i]);
            }
            fileWriter.write(String.join(",", escapedRow) + "\n");
        }

        // Flush để đảm bảo dữ liệu được ghi
        fileWriter.flush();
        log.info("Written {} records to CSV", chunk.getItems().size());
    }

    /**
     * Escape giá trị CSV (handle comma, quote, newline)
     */
    private String escapeCsvValue(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
