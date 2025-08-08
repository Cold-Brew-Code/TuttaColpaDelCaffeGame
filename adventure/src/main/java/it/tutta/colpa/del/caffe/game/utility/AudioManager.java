package it.tutta.colpa.del.caffe.game.utility;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AudioManager {
    private static AudioManager instance;
    private final Map<String, Clip> audioClips;
    private float volume = 0.5f; // default
    private String currentTrack;

    private AudioManager() {
        audioClips = new HashMap<>();
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void loadAudio(String name, String path) {
        try {
            URL url = getClass().getResource("/sounds/" + path);
            if (url == null) {
                System.err.println("File audio non trovato: " + path);
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            audioClips.put(name, clip);
            setVolume(volume);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Errore di caricamento dell'audio: " + e.getMessage());
        }
    }

    public void play(String name, boolean loop) {
        if (currentTrack != null && !currentTrack.equals(name)) {
            stop(currentTrack);
        }

        Clip clip = audioClips.get(name);
        if (clip != null) {
            clip.setFramePosition(0);
            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start();
            }
            currentTrack = name;
        }
    }

    public void fadeIn(String name, boolean loop, int durationMillis) {
        Clip clip = audioClips.get(name);
        if (clip == null) {
            System.err.println("Traccia '" + name + "' non trovata per fadeIn");
            return;
        }

        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            play(name, loop);
            return;
        }

        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-80.0f); // imposta volume minimo

        clip.setFramePosition(0);
        if (loop) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            clip.start();
        }
        currentTrack = name;

        new Thread(() -> {
            try {
                float targetVolume = (float) (Math.log10(volume) * 20);
                float increment = (targetVolume + 80f) / (durationMillis / 30f);

                for (float vol = -80f; vol < targetVolume; vol += increment) {
                    gainControl.setValue(vol);
                    Thread.sleep(30);
                }
                gainControl.setValue(targetVolume);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("[AudioManager] FadeIn interrotto per '" + name + "': " + e.getMessage());
            }
        }).start();
    }

    public void stop(String name) {
        Clip clip = audioClips.get(name);
        if (clip != null) {
            clip.stop();
            if (name.equals(currentTrack)) {
                currentTrack = null;
            }
        }
    }

    public void setVolume(float volume) {
        this.volume = Math.max(0.0f, Math.min(1.0f, volume));
        for (Clip clip : audioClips.values()) {
            if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log10(volume) * 20);
                gainControl.setValue(dB);
            }
        }
    }

    public float getVolume() {
        return volume;
    }

    public void fadeOut(String name, int durationMillis) {
        Clip clip = audioClips.get(name);
        if (clip != null && clip.isRunning()) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float initialVolume = gainControl.getValue();
            float delta = initialVolume / (durationMillis / 50f);

            new Thread(() -> {
                for (float v = initialVolume; v > -80f; v -= delta) {
                    gainControl.setValue(Math.max(-80f, v));
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                clip.stop();
            }).start();
        }
    }

    private Map<String, Long> pausePositions = new HashMap<>();

    public void pause(String name) {
        Clip clip = audioClips.get(name);
        if (clip != null && clip.isRunning()) {
            pausePositions.put(name, clip.getMicrosecondPosition());
            clip.stop();
        }
    }

    public void resume(String name) {
        if (!pausePositions.containsKey(name))
            return;

        Clip clip = audioClips.get(name);
        if (clip != null && !clip.isRunning()) {
            clip.setMicrosecondPosition(pausePositions.get(name));
            clip.start();
            pausePositions.remove(name);
        }
    }

    public boolean isPaused(String name) {
        return pausePositions.containsKey(name);
    }
}