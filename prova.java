import java.io.Serializable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class prova implements Serializable {
    //private static final long serialVersionUID=0;

    private final int initialTimeInSeconds;
    private int remainingTimeInSeconds;
    private boolean isRunning;

    private transient ScheduledExecutorService scheduler;

    /**
     * Costruttore: inizializza il timer con il tempo iniziale desiderato.
     * @param minutes tempo iniziale in minuti
     */
    public prova(int minutes) {
        this.initialTimeInSeconds = minutes * 60;
        this.remainingTimeInSeconds = initialTimeInSeconds;
        this.isRunning = false;
    }
/**
     * Avvia il timer se non è già in esecuzione e c'è tempo residuo.
     * Inizializza uno scheduler che decrementa il tempo residuo ogni secondo. questo fatto n volte 
     * Quando il tempo residuo arriva a zero, il timer si ferma automaticamente.
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
                } else {
                    stop();
                }
            }, 1, 1, TimeUnit.SECONDS);
        }
    }

    /**
     * stoppa il timer.
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
     * Resetta il timer al tempo iniziale.
     */
    public void reset() {
        stop();
        remainingTimeInSeconds = initialTimeInSeconds;
    }

    /**
     * permette di cambiare il tempo durante l'esecuzione.
     * @param seconds tempo residuo
     */
    public void setRemainingTime(int seconds) {
        this.remainingTimeInSeconds = seconds;
    }

    /**
     * Restituisce il tempo residuo in secondi.
     * @return tempo residuo
     */
    public int getRemainingTimeInSeconds() {
        return remainingTimeInSeconds;
    }

    /**
     * Restituisce true se il timer è in esecuzione.
     * @return true se in esecuzione
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Restituisce il tempo formattato come mm:ss o hh:mm:ss se supera un'ora.
     * @return tempo formattato
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

    /**
     * Metodo main di esempio: avvia un timer di 20 minuti e stampa il tempo ogni secondo.
     */
    public static void main(String[] args) {
        prova timer = new prova(20); // timer di 20 minuti

        timer.start();

        // Avvia un altro scheduler per stampare il tempo formattato ogni secondo
        ScheduledExecutorService printer = Executors.newSingleThreadScheduledExecutor();
        printer.scheduleAtFixedRate(() -> {
            System.out.println("Tempo residuo: " + timer.getTimeFormatted());
            if (timer.getRemainingTimeInSeconds()==1140){
                System.out.println("cambio tempo");
                timer.setRemainingTime(684);
            }
            if (timer.getRemainingTimeInSeconds() <= 0) {
                System.out.println("Timer scaduto!");
                printer.shutdown();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
}
