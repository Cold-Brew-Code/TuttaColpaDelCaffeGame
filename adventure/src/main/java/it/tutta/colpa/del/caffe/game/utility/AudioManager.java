package it.tutta.colpa.del.caffe.game.utility;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class AudioManager {

    private static AudioManager instance;
    private final Map<String, Clip> audioClips;
    private float volume = 0.4f; // default
    private String currentTrack;

    private AudioManager() {
        audioClips = new HashMap<>();
    }

    /**
     * Restituisce l'istanza unica di {@code AudioManager}.
     *
     * @return l'istanza singleton
     */
    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    /**
     * Carica un file audio nella mappa dei clip gestiti.
     *
     * @param name nome identificativo della traccia
     * @param path percorso del file audio relativo alla cartella
     * {@code /sounds/}
     */
    public void loadAudio(String name, String path) {
        try {
            URL audioUrl = getClass().getResource("/sounds/" + path);
            if (audioUrl == null) {
                System.err.println("File audio non trovato: /sounds/" + path);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioUrl);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            audioClips.put(name, clip);
            System.out.println("[SUCCESS] Audio caricato: " + name);
        } catch (Exception e) {
            System.err.println("[ERROR] Caricamento fallito per '" + name + "':");
            e.printStackTrace();
        }
    }

    /**
     * Riproduce una traccia audio caricata.
     * <p>
     * Se un'altra traccia è in riproduzione, viene prima fermata.
     * </p>
     *
     * @param name nome della traccia
     * @param loop {@code true} se deve essere riprodotta in loop, {@code false}
     * altrimenti
     */
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

    /**
     * Riproduce una traccia applicando un effetto di fade-in sul volume.
     *
     * @param name nome della traccia
     * @param loop {@code true} per ripetizione continua
     * @param durationMillis durata del fade-in in millisecondi
     */
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
        float min = gainControl.getMinimum();
        float max = gainControl.getMaximum();

        gainControl.setValue(min); // volume minimo

        clip.setFramePosition(0);
        if (loop) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            clip.start();
        }
        currentTrack = name;

        new Thread(() -> {
            try {
                float targetVolume;
                if (volume <= 0f) {
                    targetVolume = min;
                } else {
                    targetVolume = (float) (Math.log10(volume) * 20);
                    targetVolume = Math.min(max, Math.max(min, targetVolume));
                }

                float increment = (targetVolume - min) / (durationMillis / 30f);

                for (float vol = min; vol < targetVolume; vol += increment) {
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

    /**
     * Ferma la riproduzione di una traccia audio.
     *
     * @param name nome della traccia da fermare
     */
    public void stop(String name) {
        Clip clip = audioClips.get(name);
        if (clip != null) {
            clip.stop();
            if (name.equals(currentTrack)) {
                currentTrack = null;
            }
        }
    }

    /**
     * Imposta il volume globale per tutte le tracce.
     *
     * @param volume livello del volume tra {@code 0.1f} e {@code 1.0f}
     */
    public void setVolume(float volume) {
        this.volume = Math.max(0.1f, Math.min(1.0f, volume));
        for (Clip clip : audioClips.values()) {
            if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float min = gainControl.getMinimum();
                float max = gainControl.getMaximum();

                float dB;
                if (this.volume <= 0f) {
                    dB = min;
                } else {
                    dB = (float) (Math.log10(this.volume) * 20);
                    dB = Math.min(max, Math.max(min, dB));
                }

                gainControl.setValue(dB);
            }
        }
    }

    /**
     * Restituisce il volume attualmente impostato.
     *
     * @return il livello del volume (0.1 - 1.0)
     */
    public float getVolume() {
        return volume;
    }

    /**
     * Applica un effetto di fade-out e interrompe la riproduzione di una
     * traccia.
     *
     * @param name nome della traccia
     * @param durationMillis durata del fade-out in millisecondi
     */
    public void fadeOut(String name, int durationMillis) {
        Clip clip = audioClips.get(name);
        if (clip != null && clip.isRunning() && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float initialVolume = gainControl.getValue();
            float min = gainControl.getMinimum();
            float max = gainControl.getMaximum();

            // assicurati che delta sia positivo
            float delta = (initialVolume - min) / (durationMillis / 50f);

            new Thread(() -> {
                float v = initialVolume;
                while (v > min) {
                    gainControl.setValue(v);
                    v -= delta;
                    if (v < min) {
                        v = min;
                    }
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

    /**
     * Mette in pausa la traccia specificata salvando la posizione corrente.
     *
     * @param name nome della traccia da mettere in pausa
     */
    public void pause(String name) {
        Clip clip = audioClips.get(name);
        if (clip != null && clip.isRunning()) {
            pausePositions.put(name, clip.getMicrosecondPosition());
            clip.stop();
        }
    }

    /**
     * Riprende la riproduzione di una traccia precedentemente messa in pausa.
     *
     * @param name nome della traccia
     */
    public void resume(String name) {
        if (!pausePositions.containsKey(name)) {
            return;
        }

        Clip clip = audioClips.get(name);
        if (clip != null && !clip.isRunning()) {
            clip.setMicrosecondPosition(pausePositions.get(name));
            clip.start();
            pausePositions.remove(name);
        }
    }

    /**
     * Verifica se una traccia è attualmente in pausa.
     *
     * @param name nome della traccia
     * @return {@code true} se la traccia è in pausa, {@code false} altrimenti
     */
    public boolean isPaused(String name) {
        return pausePositions.containsKey(name);
    }
}
