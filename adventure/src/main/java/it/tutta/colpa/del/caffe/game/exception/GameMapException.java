/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.exception;

/**
 * Eccezione personalizzata che viene lanciata quando si verifica un errore
 * relativo alla mappa di gioco.
 * <p>
 * Estende {@link RuntimeException}, quindi è un'eccezione non controllata.
 * </p>
 *
 * @author giova
 */
public class GameMapException extends RuntimeException {

    /**
     * Costruisce una nuova GameMapException con un messaggio descrittivo.
     *
     * @param message il messaggio che descrive l'errore della mappa di gioco
     */
    public GameMapException(String message) {
        super(message);
    }

}
