/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.entity;

import it.tutta.colpa.del.caffe.game.exception.ServerCommunicationException;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;

/**
 * Interfaccia che rappresenta un osservatore del gioco. Ogni classe che
 * implementa questa interfaccia può ricevere aggiornamenti sullo stato del
 * gioco e reagire di conseguenza.
 *
 * @author giova
 */
public interface GameObserver {

    /**
     * Aggiorna l'osservatore in base alla descrizione corrente del gioco e
     * all'output del parser dei comandi.
     *
     * @param description la descrizione corrente dello stato del gioco
     * @param parserOutput l'output generato dal parser dopo l'elaborazione di
     * un comando
     * @return una stringa che rappresenta il risultato dell'aggiornamento per
     * l'osservatore
     * @throws ServerCommunicationException se si verifica un errore durante la
     * comunicazione con il server
     */
    public String update(GameDescription description, ParserOutput parserOutput) throws ServerCommunicationException;

}
