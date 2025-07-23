package it.tutta.colpa.del.caffe.game.boundary;

import it.tutta.colpa.del.caffe.game.control.Controller;
import it.tutta.colpa.del.caffe.game.exception.ImageNotFoundException;

/**
 * Interfaccia che definisce i metodi per la comunicazione in uscita
 * verso l'attore esterno (es. utente o interfaccia grafica).
 * I metodi possono essere implementati da UI diverse (testuale, grafica, ecc.)
 * in modo indipendente dalla logica del gioco.
 *
 * @author giovav
 * @since 15/07/25
 */
public interface GameGUI extends GUI{

    /**
     * Mostra un messaggio all'utente.
     *
     * @param message Il messaggio da visualizzare.
     */
    void out(String message);

    /**
     * Notifica un evento interattivo all'utente, mostrando un messaggio
     * con intestazione e ottenendo una risposta numerica.
     * <p>
     * I possibili valori restituiti indicano la risposta dell'utente:
     * <ul>
     *   <li><b>0</b>: Sì / Conferma</li>
     *   <li><b>1</b>: No / Rifiuto</li>
     *   <li><b>2</b>: Annulla / Chiudi</li>
     * </ul>
     * Questi valori possono essere interpretati liberamente
     * dal chiamante secondo il contesto applicativo.
     *
     * @param header  L'intestazione o titolo del messaggio.
     * @param message Il messaggio da visualizzare.
     * @return Un intero che rappresenta la scelta dell'utente:
     *         0 = sì, 1 = no, 2 = annulla.
     */
    int notifySomething(String header, String message);


    void notifyWarning(String header, String message);
    void notifyError(String header, String message);
    /**
     * Cambia l'immagine mostrata all'utente, utilizzando il percorso
     * (path) fornito come riferimento all'immagine.
     * <p>
     * L'implementazione dovrà occuparsi di caricare e visualizzare
     * l'immagine corrispondente al percorso specificato.
     *
     * @param path Il percorso (ad esempio, un file system path o URL)
     *             dell'immagine da mostrare.
     */
    void setImage(String path) throws ImageNotFoundException;

    void open();

    void close();

    void setDisplayedClock(String time);

    void increaseProgressBar();
}