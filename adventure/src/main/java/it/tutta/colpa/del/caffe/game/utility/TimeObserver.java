package it.tutta.colpa.del.caffe.game.utility;

/**
 * Interfaccia per oggetti che vogliono essere notificati
 * quando il tempo del timer scade.
 */
public interface TimeObserver {
    void onTimeExpired();
    void onTimeUpdate(String timeFormatted);

}
