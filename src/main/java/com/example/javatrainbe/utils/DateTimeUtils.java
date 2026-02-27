package com.example.javatrainbe.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Utility class cho xử lý DateTime
 */
public class DateTimeUtils {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Tạo timestamp dạng yyyyMMdd_HHmmss
     */
    public static String generateTimestamp() {
        return LocalDateTime.now().format(TIMESTAMP_FORMATTER);
    }

    /**
     * Lấy epoch millis hiện tại
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * Format epoch millis theo mẫu mặc định yyyy-MM-dd HH:mm:ss
     */
    public static String formatEpochMillis(long epochMillis) {
        return Instant.ofEpochMilli(epochMillis)
                .atZone(ZoneId.systemDefault())
                .format(DISPLAY_FORMATTER);
    }

    /**
     * Format epoch millis theo mẫu tùy chỉnh
     */
    public static String formatEpochMillis(long epochMillis, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return Instant.ofEpochMilli(epochMillis)
                .atZone(ZoneId.systemDefault())
                .format(formatter);
    }

    /**
     * Format LocalDateTime theo mẫu mặc định yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DISPLAY_FORMATTER);
    }
}
