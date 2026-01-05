package finalproject.gamestate;

import finalproject.gamestate.GameManager;
import finalproject.gamestate.Question;

public class ArtifactEffectHandler {

    private final GameManager gameManager;

    public ArtifactEffectHandler(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    // ----------------------------------
    // Core check (single source of truth)
    // ----------------------------------
    private boolean isActive(String artifactId) {
        return gameManager.isArtifactEquipped(artifactId);
    }

    // ----------------------------------
    // BOARD EFFECTS
    // ----------------------------------
    public void onBoardCreated(Runnable revealAllTiles) {
        if (isActive("FULL_DISCLOSURE")) {
            revealAllTiles.run();
        }
    }

    // ----------------------------------
    // MOVEMENT / COST EFFECTS
    // ----------------------------------
    public int modifyMoveCost(int baseCost) {
        if (isActive("LONG_STRIDE")) {
            return (int) (baseCost * 0.8);
        }
        return baseCost;
    }

    // ----------------------------------
    // QUESTION EFFECTS
    // ----------------------------------
    public void applyQuestionModifiers(Question question) {
        if (isActive("DEEP_LEARNING")) {
            question.enableHint();
        }
    }

    // ----------------------------------
    // HAZARD / TILE EFFECTS
    // ----------------------------------
    public boolean canBypassHazard(String hazardId) {
        if (hazardId.equals("FIREWALL")) {
            return isActive("CLEAR_PATH");
        }
        return false;
    }
}
