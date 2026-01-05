package finalproject.util;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SpriteAnimator {
    private List<BufferedImage> frames;
    private int currentFrame = 0;
    private long lastFrameTime;
    private int frameDelay = 80;
    private boolean facingLeft = false;

    public SpriteAnimator(BufferedImage spriteSheet, int frameCount) {
        frames = new ArrayList<>();
        if (spriteSheet == null || frameCount <= 0) {
            System.err.println("Invalid spriteSheet or frameCount provided.");
            return;
        }
        int width = spriteSheet.getWidth() / frameCount;
        int height = spriteSheet.getHeight();
        for (int i = 0; i < frameCount; i++) {
            frames.add(spriteSheet.getSubimage(i * width, 0, width, height));
        }
    }

    public BufferedImage getStaticFrame() {
        if (!frames.isEmpty()) {
            return applyFlip(frames.get(0));
        }
        return null;
    }

    public BufferedImage getNextFrame() {
        if (frames.isEmpty()) {
            return null; 
        }
        long now = System.currentTimeMillis();
        if (now - lastFrameTime > frameDelay) {
            currentFrame = (currentFrame + 1) % frames.size();
            lastFrameTime = now;
        }
        return applyFlip(frames.get(currentFrame));
    }

    public void setFacingLeft(boolean left) {
        this.facingLeft = left;
    }
    
    public void dispose() {
        if (frames != null) {
            frames.clear();
            frames = null; 
            System.out.println("SpriteAnimator resources disposed.");
        }
    }

    private BufferedImage applyFlip(BufferedImage img) {
        if (!facingLeft && img != null) return img;
        if (img == null) return null;

        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-img.getWidth(), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(img, null);
    } 
}

