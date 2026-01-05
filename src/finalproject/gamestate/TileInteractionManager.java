package finalproject.gamestate;

import finalproject.ui.components.CyberDialog;
import javax.swing.JFrame;
import java.util.Random;

public class TileInteractionManager {
    private final Random rand = new Random();

    public void handleSpecialTile(GameTile tile, TilePacerBoard board, GameManager gameManager, JFrame parent) {
        if (tile.getVisited()) return;

        if (tile.getTileType() != GameTile.TileType.NORMAL) {
            board.setLastEffectTileType(tile.getTileType());
        }

        switch (tile.getTileType()) {
            case QUESTION -> handleQuestion(board, gameManager, parent);
            case TELEPORT -> handleTeleport(board, parent);
            case GAMBLE -> handleGamble(board, gameManager, parent);
            case BONUS_STEP -> handleBonusStep(board, gameManager, parent);
            case COIN -> handleCoin(board, gameManager, parent);
            case GEM -> handleGem(board, gameManager, parent);
            default -> {}
        }
        
        tile.setVisited(true);
        board.refreshUI();
        tile.stopSound(tile.getTileType());
    }

    private void handleGamble(TilePacerBoard board, GameManager gameManager, JFrame parent) {
        int gamble;
        if (gameManager.hasRNgesusEffect()) {
            boolean isGood = rand.nextInt(2) == 0;
            gamble = isGood ? rand.nextInt(3) : rand.nextInt(3) + 3;
            gameManager.useRNgesusEffect();
        } else {
            gamble = rand.nextInt(6);
        }

        switch (gamble) {
            case 0 -> {
                board.multiplyScore(2);
                CyberDialog.showSuccess(parent, "CONGRATULATIONS! SCORE DOUBLED.", "LOVE IT", 550, 280);
            }
            case 1 -> {
                int coins = rand.nextInt(51) + 50;
                board.addCoins(coins);
                gameManager.addRewards(coins, 0);
                CyberDialog.showSuccess(parent, String.format("%20s", "EARNED " + coins + " COINS!"), "LOVE IT", 500, 220);
            }
            case 2 -> {
                int exp = rand.nextInt(51) + 50;
                board.addExp(exp);
                gameManager.addRewards(0, exp);
                CyberDialog.showSuccess(parent, String.format("%20s", "EXP INCREASED BY " + exp + "!"), "LOVE IT", 500, 220);
            }
            case 3 -> {
                board.reduceSteps(10);
                CyberDialog.showFailure(parent, String.format("%20s", "LOST 10 STEPS!"), "HATE IT", 500, 220);
            }
            case 4 -> {
                board.setScores(0);
                CyberDialog.showFailure(parent, String.format("%20s", "LOST YOUR SCORE!"), "HATE IT", 500, 220);
            }
            default -> {
                gameManager.addRewards(-board.getCoinsEarned(), -board.getExpEarned());
                board.wipeAllCurrentRunStats();
                CyberDialog.showFailure(parent, String.format("%20s", "LOST EVERYTHING!"), "HATE IT", 500, 220);
            }
        }
    }

    private void handleQuestion(TilePacerBoard board, GameManager gameManager, JFrame parent) {
        Question questions = QuestionGenerator.getRandomQuestion(gameManager.getActiveDifficulty());
        String userAnswer;
        if (gameManager.hasAIBonus()) {
            userAnswer = CyberDialog.showQuestionWithAIHint(parent, "[AI] One false path eliminated.\n\n" + questions.getQuestion(), questions.getAnswer(), questions.getOptions(), 880, 480);
        } else if (gameManager.hasGoogleHint()) {
            userAnswer = CyberDialog.showQuestionWithAnswerRevealed(parent, "[SEARCH] Answer identified.\n" + questions.getQuestion(), questions.getAnswer(), questions.getOptions(), 880, 480);
            gameManager.useGoogleHint();
        } else {
            userAnswer = CyberDialog.showQuestion(parent, questions.getQuestion(), null, questions.getOptions(), 880, 480);
        }
        if (userAnswer != null && userAnswer.equalsIgnoreCase(questions.getAnswer())) {
            board.addScore(40); board.addCoins(75); gameManager.addRewards(75, 50);
            CyberDialog.showSuccess(parent, "CORRECT! Neural link stabilized.", "BRAVO", 550, 280);
        } else {
            board.reduceSteps(5);
            CyberDialog.showFailure(parent, "WRONG! Feedback loop detected. -5 ENERGY", "RETRY", 550, 280);
        }
    }

    private void handleTeleport(TilePacerBoard board, JFrame parent) {
    	CyberDialog.showMessage(parent, String.format("%26s🌀\n%31s", "PORTAL ACTIVATED", "Teleporting...")
                , "OKAY", 550, 280);
        board.executeRandomTeleport();
    }

    private void handleBonusStep(TilePacerBoard board, GameManager gameManager, JFrame parent) {
        int bonus = rand.nextInt(3);
        if (bonus == 0) { 
        	board.addScore(100); 
        	CyberDialog.showSuccess(
                    parent, String.format("%28s💎\n%28s", "BONUS ACQUIRED", "+100 SCORE")
                    , "LOVE IT", 550, 280
            );
        }
        else if (bonus == 1) { 
        	board.addCoins(100); 
        	gameManager.addRewards(100, 0); 
        	CyberDialog.showSuccess(
                    parent, String.format("%28s💎\n%28s", "BONUS ACQUIRED", "+100 COINS")
                    , "LOVE IT", 550, 280
            );
        }
        else {
        	board.addExp(50);
        	gameManager.addRewards(0, 50); 
        	CyberDialog.showSuccess(
                    parent, String.format("%28s💎\n%27s", "BONUS ACQUIRED", "+50 EXP")
                    , "LOVE IT", 550, 280
            );
        }
        
    }

    private void handleCoin(TilePacerBoard board, GameManager gameManager, JFrame parent) {
        board.addCoins(50); gameManager.addRewards(50, 0);
        CyberDialog.showSuccess(parent, String.format("%30s🪙\n", "+50 COINS"), "LOVE IT", 550, 280);
    }

    private void handleGem(TilePacerBoard board, GameManager gameManager, JFrame parent) {
        board.addExp(50); gameManager.addRewards(0, 50);
        CyberDialog.showSuccess(parent, String.format("%30s💎\n", "+50 EXP"), "LOVE IT", 550, 280);
    }
}