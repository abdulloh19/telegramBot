package com.mnemonic.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mnemonic.model.UserProfile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {
    private final Map<Long, UserProfile> userProfiles = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final File storageFile;

    public UserRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        this.storageFile = new File(dataDir, "users.json");
        loadFromDisk();
    }

    public synchronized UserProfile getOrCreate(long chatId, String firstName) {
        return userProfiles.computeIfAbsent(chatId, id -> {
            UserProfile profile = new UserProfile(chatId, firstName);
            saveToDisk();
            return profile;
        });
    }

    public Optional<UserProfile> get(long chatId) {
        return Optional.ofNullable(userProfiles.get(chatId));
    }

    public synchronized void save(UserProfile profile) {
        userProfiles.put(profile.getChatId(), profile);
        saveToDisk();
    }

    public List<UserProfile> getAllProfiles() {
        return new ArrayList<>(userProfiles.values());
    }

    private synchronized void saveToDisk() {
        try {
            objectMapper.writeValue(storageFile, userProfiles);
        } catch (IOException e) {
            System.err.println("❌ Foydalanuvchilar ma'lumotlarini saqlashda xatolik: " + e.getMessage());
        }
    }

    private synchronized void loadFromDisk() {
        if (!storageFile.exists() || storageFile.length() == 0) {
            return;
        }
        try {
            Map<Long, UserProfile> loaded = objectMapper.readValue(
                    storageFile,
                    new TypeReference<Map<Long, UserProfile>>() {}
            );
            if (loaded != null) {
                userProfiles.putAll(loaded);
                System.out.println("💾 Xotiradan " + loaded.size() + " ta foydalanuvchi ma'lumoti yuklandi.");
            }
        } catch (IOException e) {
            System.err.println("⚠️ Foydalanuvchilar ma'lumotlarini yuklashda xatolik (noldan boshlanadi): " + e.getMessage());
        }
    }
}
