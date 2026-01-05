package finalproject.gamestate;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import finalproject.ui.components.CyberDialog;

class BackdoorSnapshot {
    int x, y;
    int steps;
    int score;
    int coins;
    int exp;
}

class PlayerData {
    public String name = "";
    public int level = 1;
    public int coins = 0;
    public int exp = 0;
    public boolean soundEnabled = false;
    public Map<String, Integer> highScores = new HashMap<>();
    public List<String> purchasedItemIds = new ArrayList<>();
   
    public List<String> unlockedAchievements = new ArrayList<>();
   
    public Map<String, Integer> achievementProgress = new HashMap<>();
    public Map<String, Integer> inventory = new HashMap<>();
    
    public List<String> permanentUpgrades = new ArrayList<>();  
    public String equippedArtifactId = "";
}

public class GameManager {
    public enum Difficulty { EASY, MEDIUM, HARD }
    

    private static final String SAVE_FILE = "playerdata.json";
    private final List<Runnable> economyListeners = new ArrayList<>();
    private final List<Consumer<String>> achievementListeners = new ArrayList<>(); 
    private final List<Runnable> difficultyListeners = new ArrayList<>();
    private final Gson gson;
    
    private PlayerData data;
    private int currentSteps;
    private int currentScore;
    private int maxSteps;
    private Difficulty activeDifficulty;
    
 
    private boolean steppedOnNormalThisLevel = false;
    private int consecutiveCorrectAnswers = 0;
    

    public GameManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.data = loadPlayerData();
        this.currentScore = 0;
        this.activeDifficulty = Difficulty.EASY; 
       
    }

    public boolean isDifficultyUnlocked(Difficulty isUnlocked) {
        return switch (isUnlocked) {
            case EASY -> true;
            case MEDIUM -> getCurrentLevel() >= 10;
            case HARD -> getCurrentLevel() >= 15;
        };
    }
    
    public void applyDifficulty(Difficulty difficulty) {
        this.maxSteps = switch (difficulty) {
            case EASY -> 50;
            case MEDIUM -> 40;
            case HARD -> 30;
        };
        this.currentSteps = this.maxSteps;
        this.activeDifficulty = difficulty;
        this.steppedOnNormalThisLevel = false;
        
        notifyDifficultyListeners(); 
    }
    
    public Difficulty getActiveDifficulty() { 
        return activeDifficulty;
    }
   
    public int getMaxSteps() {
        return maxSteps;
    }
    
    public void decrementSteps() { 
        currentSteps = Math.max(0, currentSteps - 1);
    }
    
    public void incrementSteps(int bonus) {
        currentSteps += bonus; 
    }
    
    public int getCurrentSteps() { 
        return currentSteps;
    }
        
    public void recordScore(int points) {
        currentScore += points;
        if (activeDifficulty != null) {
            int high = getHighScore(activeDifficulty);
            if (currentScore > high) {
                data.highScores.put(activeDifficulty.name(), currentScore);
                savePlayerData();
            }
        }
    }
    
    public int getCurrentScore() { 
        return currentScore; 
    }
    
    public int getHighScore(Difficulty diff) {
        return data.highScores.getOrDefault(String.valueOf(diff), 0);
    }
    
    public void completeLevel() {
        data.level++;
        savePlayerData();
    }
    
    public int getCurrentLevel() { 
        return data.level; 
    }
    
    public void addRewards(int coins, int exp) {
        data.coins += coins;
        data.exp += exp;
        savePlayerData();
    }
    
    public void spendGems(int amount) {
        if (data.exp >= amount) {
            data.exp -= amount;
           
            savePlayerData(); 
        } else {
            System.out.println("Not enough Gems (EXP)!");
        }
    }
    
    public int getTotalCoins() { 
        return data.coins;
    }
    
    public int getTotalGems() {
        return data.exp;
    }
    
    public int getTotalExp() { 
        return data.exp; 
    }
    
    public void recordPurchase(String itemName) {
        int currentCount = data.inventory.getOrDefault(itemName, 0);
        data.inventory.put(itemName, currentCount + 1);
        savePlayerData(); 
    }
    
    public void consumeItem(String itemName) {
        int count = getInventoryCount(itemName);
        if (count > 1) {
            data.inventory.put(itemName, count - 1);
        } else {
            data.inventory.remove(itemName);
        }
        savePlayerData();
    }
    
    public void buyItem(String itemId, int cost) {
        int finalCost = cost;
        if (hasTrojanEffect()) {
            finalCost = (int)(cost * 0.8); 
        }
        
        if (data.coins >= finalCost && !data.purchasedItemIds.contains(itemId)) {
            data.coins -= finalCost;
            data.purchasedItemIds.add(itemId);
            savePlayerData();
        }
    }
    
    public boolean hasItem(String itemId) {
        return data.purchasedItemIds.contains(itemId);
    }
    
    public int getInventoryCount(String itemName) {
        return data.inventory.getOrDefault(itemName, 0);
    }
    
    public void equipArtifact(String id) {
        data.equippedArtifactId = id;
        savePlayerData(); 
    }
    
    public void consumeArtifact() {
        data.equippedArtifactId = ""; 
        savePlayerData();
    }
    
    public boolean isArtifactEquipped(String id) {
        return id.equals(data.equippedArtifactId);
    }
    
    public boolean hasSatelliteEffect() {
        return isArtifactEquipped("FULL_DISCLOSURE");
    }

    public boolean hasFirewallEffect() {
        return isArtifactEquipped("CLEAR_PATH");
    }

    public boolean hasAIBonus() {
        return isArtifactEquipped("DEEP_LEARNING");
    }

    public boolean hasTrojanEffect() {
        return isArtifactEquipped("LONG_STRIDE");
    }

    public boolean hasWorms() {
        return getInventoryCount("WORMS") > 0;
    }

    public void useWorms() {
        if (hasWorms()) {
            consumeItem("WORMS");
            notifyEconomyListeners();
        }
    }
    
    public boolean hasBackdoor() {
        return getInventoryCount("BACKDOOR") > 0;
    }
    
    public boolean hasRNgesusEffect() {
        return getInventoryCount("RNGESUS") > 0;
    }
    
    public void useRNgesusEffect() {
    	if (hasRNgesusEffect()) {
    		consumeItem("RNGESUS");
            notifyEconomyListeners();
    	}
    }
    
    public boolean isDroneActive() {
        return getInventoryCount("DRONE") > 0;
    }
    
    public void useDrone() {
        if (isDroneActive()) {
            consumeItem("DRONE");
            
            notifyEconomyListeners(); 
         }
    }
    
    public boolean hasGoogleHint() {
        return getInventoryCount("GOOGLE") > 0;
    }

    public void useGoogleHint() {
        int count = getInventoryCount("GOOGLE"); 
        if (count > 0) {
            consumeItem("GOOGLE");
            notifyEconomyListeners();
        }
    }
    
    public boolean hasReboot() {
        return getInventoryCount("REBOOT") > 0;
    }

    public void useReboot() {
        if (getInventoryCount("REBOOT") > 0) {
            consumeItem("REBOOT"); 
            notifyEconomyListeners();
        }
    }
    
    public void incrementCorrectStreak() {
        consecutiveCorrectAnswers++;
        if (consecutiveCorrectAnswers >= 5) {
            unlockAchievement("DEEP_LEARNING");
        }
    }
    
    public void resetCorrectStreak() {
        consecutiveCorrectAnswers = 0;
    }
    
    public void unlockAchievement(String id) {
        if (data.unlockedAchievements.contains(id)) return;

        if (id.equals("DEEP_LEARNING")) {
            data.unlockedAchievements.add(id);
            data.permanentUpgrades.add("AI_BONUS");
        } else if (id.equals("FULL_DISCLOSURE")) {
            data.unlockedAchievements.add(id);
            data.permanentUpgrades.add("SATELLITE");
        } else if (id.equals("CLEAR_PATH")) {
            data.unlockedAchievements.add(id);
            data.permanentUpgrades.add("FIREWALL");
        } else if (id.equals("LONG_STRIDE")) {
            data.unlockedAchievements.add(id);
            data.permanentUpgrades.add("TROJAN_HORSE");
        }
        
        savePlayerData();
        for (Consumer<String> listener : achievementListeners) {
            listener.accept(id);
        }
    }
    
    public boolean isAchievementUnlocked(String id) {
        return data.unlockedAchievements.contains(id);
    }
    
    public void checkNormalTileStep() {
        this.steppedOnNormalThisLevel = true;
    }

    public boolean getSteppedOnNormalThisLevel() {
        return steppedOnNormalThisLevel;
    }

    public void resetNormalTileFlag() {
        this.steppedOnNormalThisLevel = false;
    }
    
    public void addEconomyListener(Runnable listener) {
        if (listener != null) economyListeners.add(listener);
    }
    
    public void addAchievementListener(Consumer<String> listener) {
        if (listener != null) achievementListeners.add(listener);
    }
    
    public void addDifficultyListener(Runnable listener) {
        if (listener != null) difficultyListeners.add(listener);
    }
    
    public void setSoundEnabled(boolean enabled) {
        data.soundEnabled = enabled;
        savePlayerData();
    }
    
    public boolean isSoundEnabled() {
        return data.soundEnabled; 
    }
    public void saveNewName(String newName, JFrame frame) {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(frame);
        if (data.name.equals(newName.trim())) {
            CyberDialog.showMessage(parent, String.format("%30s", "Existing name..."), "OKAY", 550, 280);
            return;
        }
        if (newName != null && !newName.trim().isEmpty()) {
            data.name = newName.trim();
            CyberDialog.showMessage(parent, "Name updated successfully.", "OKAY", 550, 280);
            savePlayerData();
        }
    }
    
    public String getName() { 
        return data.name; 
    }
    
    public void resetGame() {
        data.level = 1;
        data.coins = 0;
        data.exp = 0;
        data.name = ""; 

        data.highScores.put(String.valueOf(Difficulty.EASY), 0);
        data.highScores.put(String.valueOf(Difficulty.MEDIUM), 0);
        data.highScores.put(String.valueOf(Difficulty.HARD), 0);

        data.inventory.clear();
        data.purchasedItemIds.clear();


        data.unlockedAchievements.clear();
        data.achievementProgress.clear();
        data.permanentUpgrades.clear();

        data.equippedArtifactId = "";

        data.soundEnabled = false;

        currentScore = 0;
        currentSteps = 0;
        maxSteps = 0;
        activeDifficulty = Difficulty.EASY;
        steppedOnNormalThisLevel = false;
        consecutiveCorrectAnswers = 0;

        savePlayerData();
    }
    
    private void notifyDifficultyListeners() {
        SwingUtilities.invokeLater(() -> {
            for (Runnable listener : difficultyListeners) {
                if (listener != null) listener.run();
            }
        });
    }
    
    private void notifyEconomyListeners() {
        SwingUtilities.invokeLater(() -> {
            for (Runnable listener : economyListeners) {
                if (listener != null) {
                    listener.run();
                }
            }
        });
    }
    
    private PlayerData loadPlayerData() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            return new PlayerData();
        }

        try (FileReader reader = new FileReader(file)) {
            PlayerData loadedData = gson.fromJson(reader, PlayerData.class);
            System.out.println("Save file location: " + file.getAbsolutePath());
            return (loadedData != null) ? loadedData : new PlayerData();
        } catch (IOException e) {
            e.printStackTrace();
            return new PlayerData();
        }
    }

    private void savePlayerData() {
        try (FileWriter writer = new FileWriter(SAVE_FILE)) {
            gson.toJson(data, writer);
            //notifyEconomyChanged();
            notifyEconomyListeners();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}