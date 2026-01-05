package finalproject.util.audio;

import javax.sound.sampled.*;

import finalproject.util.ResourceLoader;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.SwingUtilities;

public final class SoundManager implements Runnable {
    private static final Map<String, Clip> soundMap = new ConcurrentHashMap<>();
    private static volatile boolean isReady = false;
    private static boolean isMuted = false;

    public static void initialize() {
        Thread loaderThread = new Thread(new SoundManager(), "Sound-Loader");
        loaderThread.setDaemon(true);
        loaderThread.start();
    }

    @Override
    public void run() {
        load("click", "/assets/audios/clickonmouse.wav");
        load("teleport", "/assets/audios/teleport_audio.wav");
        load("gamble", "/assets/audios/gamble_audio.wav");
        load("bonus", "/assets/audios/bonus_audio.wav");
        load("question", "/assets/audios/question.wav");
        load("coin", "/assets/audios/coin_audio.wav");
        load("gem", "/assets/audios/gem_audio.wav");
        SwingUtilities.invokeLater(() -> {
            isReady = true;
            System.out.println("UI Thread: Sound system is ready for interaction.");
        });
    }

    private void load(String name, String path) {
        try {
            InputStream is = ResourceLoader.getStream(path);
            if (is == null) return;

            InputStream bufferedIn = new BufferedInputStream(is);
            AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedIn);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            
            soundMap.put(name, clip);
        } catch (Exception e) {
            System.err.println("Load failed: " + name);
        }
    }

    public static void play(String name) {
        if (!isReady || isMuted) return;

        Clip clip = soundMap.get(name);
        if (clip != null) { 
            if (clip.isRunning()) {
                return; 
            }
            
            clip.setFramePosition(0);
            clip.start();
        }
    }
    
    public static void setMuted(boolean muted) {
        isMuted = muted;
        if (isMuted) {
        	stopAll();
        }
    }
    
    public static void stop(String name) {
        Clip clip = soundMap.get(name);
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.setFramePosition(0); 
        }
    }

    public static void stopAll() {
        soundMap.forEach((name, clip) -> {
            if (clip.isRunning()) {
                clip.stop();
            }
        });
        System.out.println("All sounds stopped.");
    }
    
    public static void dispose() {
   
        soundMap.forEach((name, clip) -> {
            if (clip != null) {
                if (clip.isRunning()) clip.stop();
                clip.close();
            }
        });
      
        soundMap.clear();
        isReady = false;
        System.out.println("SoundManager resources disposed.");
    }
}