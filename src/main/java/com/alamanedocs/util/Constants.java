package com.alamanedocs.util;

public class Constants {
    
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    public static final String[] ALLOWED_FILE_TYPES = {"application/pdf", "image/jpeg", "image/png"};
    public static final String UPLOAD_DIR = "uploads";
    
    private Constants() {
        // Utility class
    }
}
