package service;

import entity.Expanse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ExpanseService {

    public static String FILE_PATH = System.getProperty("user.home") +
            File.separator + "expanse-tracker" + File.separator + "expanses.json";
    public static final Map<Integer, Expanse> expanses = new HashMap<>();

    {
        initializeFile();
        loadFromJsonFile();
    }

    private String convertExpansesToJson() {
        return expanses.values().stream()
                .map(this::toJson)
                .collect(Collectors.joining(",", "[", "]"));
    }

    private void writeJsonToFile(String json) {
        try(FileWriter fileWriter = new FileWriter(FILE_PATH)) {
            fileWriter.write(json);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void saveExpanses() {
        String json = convertExpansesToJson();
        writeJsonToFile(json);
    }

    public void updateExpanse(int id, String description) {
        Expanse expanse = expanses.get(id);
        if (expanse != null) {
            expanse.setDescription(description);
            saveExpanses();
        } else {
            System.out.println("Expanse with ID: " + id + " not found!");
        }
    }

    public void delete(int id) {
        if (expanses.remove(id) != null) {
            saveExpanses();
        } else {
            System.out.println("Expanse with id: " + id + " not found!");
        }
    }

    public void loadFromJsonFile() {
        try {
            String json = new String(Files.readAllBytes(Paths.get(FILE_PATH)));

            if (!json.trim().equals("[]") || !json.isEmpty()) {
                String content = json.substring(1, json.length() - 1);
                String[] expanseObjects = content.split("\\},\\s*\\{");

                for (String expanseJson : expanseObjects) {
                    String formattedJson = "{" + expanseJson + "}";

                    Expanse expanse = fromJson(formattedJson);
                    expanses.put(expanse.getId(), expanse);
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error parsing the JSON: " + e.getMessage());
        }
    }

    public void initializeFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                File parentDir = file.getParentFile();
                if (parentDir != null) {
                    parentDir.mkdirs();
                }
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating file: " + e.getMessage());
            }
        }
    }

    public void newExpanse(String description, int amount) {
        Expanse expanse = new Expanse(description, amount);
        expanses.put(expanse.getId(), expanse);
        saveExpanses();
    }

    public void printUsage() {
        System.out.println("""
                expanse-cli add \\"<expanse_description>\\"  - Add a new expanse
                expanse-cli list                       - List all expanses
                expanse-cli update <id> <new description>         - Updates the expanses where id = ?
                """);
    }

    public Optional<Integer> checkIfNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Integer.parseInt(str));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public String toJson(Expanse expanse) {
        return "{" +
                "\"id\": \"" + expanse.getId() + "\"," +
                "\"description\": \"" + expanse.getDescription() + "\"," +
                "\"amount\": \"" + expanse.getAmount() + "\"," +
                "\"createdAt\": \"" + expanse.getCreatedAt() + "\"" +
                "}";
    }

    public static Expanse fromJson(String json) {
        String[] parts = json.replace("{", "")
                .replace("}", "")
                .split(",");
        int id = Integer.parseInt(parts[0].split(":")[1].replace("\"", "").trim());
        String description = parts[1].split(":")[1].replace("\"", "").trim();
        int amount = Integer.parseInt(parts[2].split(":")[1].replace("\"", "").trim());
        LocalDateTime createdAt = parseDateTime(parts[3].split(":")[1].replace("\"", "").trim());
        return new Expanse(id, description, amount, createdAt);
    }

    private static LocalDateTime parseDateTime(String dateTime) {
        try {
            return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (Exception e) {
            if (dateTime.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}")) {
                dateTime += ":00:00";
                return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } else {
                throw new IllegalArgumentException("Invalid date-time format: " + dateTime);
            }
        }
    }

}
