package finalproject.gamestate;

import java.util.List;

public class AchievementRegistry {
	public enum AchievementEffect {
	    SATELLITE,
	    AI_BONUS,
	    EXTENDED_RANGE
	}
	
    public static final Achievement FULL_DISCLOSURE =
        new Achievement(
            "FULL_DISCLOSURE",
            "SATELLITE",
            "Win the game without ever stepping on a Normal tile."
        );

    public static final Achievement DEEP_LEARNING =
        new Achievement(
            "DEEP_LEARNING",
            "AI",
            "Answer 5 consecutive questions correctly without failing."
        );

    public static final Achievement LONG_STRIDE =
        new Achievement(
            "LONG_STRIDE",
            "TROJAN HORSE",
            "Reach the END tile with more than 50% of your steps left."
        );

    public static List<Achievement> all() {
        return List.of(FULL_DISCLOSURE, DEEP_LEARNING, LONG_STRIDE);
    }
    
    public static List<Achievement> getAllAchievements() {
        return List.of(
            new Achievement(
                "FULL_DISCLOSURE",
                "Satellite",
                "Win the game without ever stepping on a Normal tile."
            ),
            new Achievement(
                "DEEP_LEARNING",
                "AI",
                "Answer 5 consecutive questions correctly without failing."
            ),
            new Achievement(
                "CLEAR_PATH",
                "Firewall",
                "Activate the Firewall on a Hard difficulty board with at least 8 obstacles."
            ),
            new Achievement(
                "LONG_STRIDE",
                "Trojan Horse",
                "Reach the END tile with more than 50% steps remaining."
            )
        );
    }

}
