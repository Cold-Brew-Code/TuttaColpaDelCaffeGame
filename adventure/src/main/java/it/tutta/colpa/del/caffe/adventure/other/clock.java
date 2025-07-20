package it.tutta.colpa.del.caffe.adventure.other;

import java.io.Serializable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import it.tutta.colpa.del.caffe.game.boundary.BoundaryOutput;

public class Clock implements Serializable {

    private final int initialTimeInSeconds;
    private int remainingTimeInSeconds;
    private boolean isRunning;
    private transient ScheduledExecutorService scheduler;
    private final TimeObserver observer;
    private final BoundaryOutput GUI;

    /**
     * Costruttore: inizializza il timer con il tempo iniziale desiderato.
     * @param minutes tempo iniziale in minuti
     */
    public Clock(int minutes, TimeObserver observer, BoundaryOutput GUI ) {
        this.initialTimeInSeconds = minutes * 60;
        this.remainingTimeInSeconds = initialTimeInSeconds;
        this.isRunning = false;
        this.observer = observer;
        this.GUI= GUI;
    }
    /**
     * Avvia il timer se non è già in esecuzione e c'è tempo residuo.
     * Inizializza uno scheduler {@link ScheduledExecutorService}  che decrementa il tempo residuo ogni secondo. questo fatto n volte 
     * Quando il tempo residuo arriva a zero, il timer si ferma e viene notificato l'osservatore
     * tramite {@code observer.onTimeExpired()}.
     * scheduleAtFixedRate prende in input l'operazione che deve essere ripetuta , 
     * il tempo di attesa, il tempo di esecuzione tra k e k+1 e l'unità di tempo (in questo caso i secondi)
     */

    public void start() {
        if (!isRunning && remainingTimeInSeconds > 0) {
            isRunning = true;
            scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(() -> {
                if (remainingTimeInSeconds > 0) {
                    remainingTimeInSeconds--;
                    GUI.increaseProgressBar();
                } else {
                    stop();
                    observer.onTimeExpired();  // Notifica Engine che ha finito
                }
            }, 1, 1, TimeUnit.SECONDS);
        }
    }

    /**
     * Ferma il timer se è attualmente in esecuzione.
     * <p>
     * Arresta lo scheduler utilizzato per il conteggio del tempo.
     */

    public void stop() {
        if (isRunning) {
            isRunning = false;
            if (scheduler != null && !scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        }
    }

    /**
     * Reimposta il timer al valore iniziale specificato al momento della creazione.
     * <p>
     * Ferma il timer se è attualmente in esecuzione e riporta il tempo residuo al valore originale.
     */

    public void reset() {
        stop();
        remainingTimeInSeconds = initialTimeInSeconds;
    }

    /**
     * Imposta manualmente il tempo residuo del timer.
     *
     * @param seconds nuovo valore del tempo residuo, espresso in secondi
     */

    public void setRemainingTime(int seconds) {
        this.remainingTimeInSeconds = seconds;
    }

    /**
     * Restituisce il tempo residuo attuale, espresso in secondi.
     *
     * @return il tempo residuo in secondi
     */

    public int getRemainingTimeInSeconds() {
        return remainingTimeInSeconds;
    }

    /**
     * Indica se il timer è attualmente in esecuzione.
     *
     * @return {@code true} se il timer è in esecuzione, {@code false} altrimenti
     */

    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Restituisce il tempo residuo formattato come stringa leggibile.
     * <p>
     * Il formato restituito sarà "HH:mm:ss" se il tempo supera un'ora, altrimenti "mm:ss".
     *
     * @return una stringa rappresentante il tempo residuo formattato
     */

    public String getTimeFormatted() {
        int hours = remainingTimeInSeconds / 3600;
        int minutes = (remainingTimeInSeconds % 3600) / 60;
        int seconds = remainingTimeInSeconds % 60;

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }
}
