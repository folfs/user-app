package com.demo.user_app.util;
import java.util.Optional;
import java.util.UUID;

public class UUIDUtil {
    public static boolean isValidUUID(String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            return false;
        }
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static Optional<UUID> safeToUUID(String uuidString) {
        if (isValidUUID(uuidString)) {
            return Optional.of(UUID.fromString(uuidString));
        }
        return Optional.empty();
    }
}
