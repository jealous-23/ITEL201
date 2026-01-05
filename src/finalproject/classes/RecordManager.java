package finalproject.classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RecordManager {
	public enum Difficulty {
    	EASY, MEDIUM, HARD
    }
	
    private static final String NAME_AND_HIGH_SCORE_FILE = "tilepacerrecord.txt";
    private static String name;
    private static Map<Difficulty, Integer> highScores;

    static {
    	name = loadSavedName();
        highScores = loadHighScores();
    } 
    
    public void saveNewName(String newName) {
    	name = newName;
    	saveNewNameToFile();
    	System.out.println("New name saved!");
    }
    
    public String getName() {
    	return name;
    }
    
    public void saveHighScore(Difficulty difficulty, int score) {
        int currentHighScore = highScores.getOrDefault(difficulty, 0);
        if (score > currentHighScore) {
            highScores.put(difficulty, score);
            saveHighScoresToFile();
            System.out.println("New high score saved for " + difficulty + ": " + score);
        }
    }

    public static int getHighScore(String difficulty) {
    	difficulty = difficulty.toUpperCase().trim(); 
        return highScores.getOrDefault(difficulty, 0);
    }
    
    private static String loadSavedName() {
        String foundName = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(NAME_AND_HIGH_SCORE_FILE))) {
            String line = reader.readLine();
            
            if (line != null && !line.trim().isEmpty()) {
                foundName = line.trim();
            }
        } catch (IOException e) {
            System.err.println("Name file not found or could not be read. Starting with no saved name.");
        }
        return foundName;
    }
    
    private static Map<Difficulty, Integer> loadHighScores() {
        Map<Difficulty, Integer> loadedScores = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(NAME_AND_HIGH_SCORE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    try {
                    	Difficulty difficulty = Difficulty.valueOf(parts[0].trim());
                        int score = Integer.parseInt(parts[1].trim());
                        loadedScores.put(difficulty, score);
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping malformed score record: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("High score file not found or could not be read. Starting with empty records.");
        }
        return loadedScores;
    }
    
    private static void saveNewNameToFile() {
    	try (var writer = new FileWriter(NAME_AND_HIGH_SCORE_FILE)) {
            String newName = name;
            writer.write(newName + "\n");
            
        } catch (IOException e) {
            System.err.println("Error saving a new name to file: " + e.getMessage());
        }
    }
    private static void saveHighScoresToFile() {
        try (var writer = new FileWriter(NAME_AND_HIGH_SCORE_FILE)) {
            for (Map.Entry<Difficulty, Integer> entry : highScores.entrySet()) {
                String recordLine = entry.getKey() + " : " + entry.getValue();
                writer.write(recordLine + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving high scores to file: " + e.getMessage());
        }
    }
}