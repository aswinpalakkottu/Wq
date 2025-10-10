// src/com/vehiclerental/util/DateUtil.java
package com.vehiclerental.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public static LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd format.");
        }
    }
    
    public static String formatDate(LocalDate date) {
        return date.format(formatter);
    }
    
    public static boolean isValidDate(String dateString) {
        try {
            parseDate(dateString);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    public static boolean isFutureDate(LocalDate date) {
        return !date.isBefore(LocalDate.now());
    }
    
    public static boolean isValidDateRange(LocalDate startDate, LocalDate endDate) {
        return !startDate.isAfter(endDate) && !startDate.isBefore(LocalDate.now());
    }
    
    public static long calculateDaysBetween(LocalDate startDate, LocalDate endDate) {
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
    }
}