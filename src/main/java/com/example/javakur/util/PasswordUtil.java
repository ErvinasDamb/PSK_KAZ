package com.example.javakur.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for password hashing and verification using BCrypt
 */
public class PasswordUtil {

    // BCrypt work factor (log rounds) - 12 is a good balance between security and performance
    private static final int WORK_FACTOR = 12;

    // Private constructor to prevent instantiation
    private PasswordUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Hashes a plain text password using BCrypt
     * @param plainPassword Plain text password
     * @return Hashed password
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(WORK_FACTOR));
    }

    /**
     * Verifies a plain text password against a hashed password
     * @param plainPassword Plain text password to verify
     * @param hashedPassword Hashed password from database
     * @return true if passwords match, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            // Invalid hash format
            return false;
        }
    }

    /**
     * Checks if a password needs to be rehashed (if work factor has changed)
     * @param hashedPassword Hashed password to check
     * @return true if password should be rehashed
     */
    public static boolean needsRehash(String hashedPassword) {
        if (hashedPassword == null || hashedPassword.isEmpty()) {
            return true;
        }
        // Extract the work factor from the hash
        // BCrypt hash format: $2a$[work_factor]$[salt][hash]
        try {
            String[] parts = hashedPassword.split("\\$");
            if (parts.length >= 3) {
                int currentWorkFactor = Integer.parseInt(parts[2]);
                return currentWorkFactor < WORK_FACTOR;
            }
        } catch (Exception e) {
            return true;
        }
        return true;
    }
}