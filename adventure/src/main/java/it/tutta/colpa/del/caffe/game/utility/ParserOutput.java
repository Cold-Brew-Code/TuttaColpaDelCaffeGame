/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tutta.colpa.del.caffe.game.utility;

import it.tutta.colpa.del.caffe.game.entity.Command;
import it.tutta.colpa.del.caffe.game.entity.GeneralItem;
import it.tutta.colpa.del.caffe.game.entity.NPC;

/**
 *
 * @author giova
 * 
 * Rappresenta il risultato dell'analisi di un comando inserito dal giocatore.
 * Contiene il comando interpretato e, opzionalmente, informazioni su oggetti,
 * NPC o numero di piano associati all'input.
 * 
 * 
 */
 
public class ParserOutput {

    private Command command;

    private GeneralItem object;
    private NPC npc;
    private int piano=-1;

    /**
     * Costruisce un ParserOutput contenente un comando e un oggetto.
     *
     * @param command comando interpretato
     * @param object  oggetto generico associato
     */
    public ParserOutput(Command command, GeneralItem object) {
        this.command = command;
        this.object = object;
    }

    /**
     * Costruisce un ParserOutput contenente solo un comando.
     *
     * @param command comando interpretato
     */
    public ParserOutput(Command command) {
        this.command = command;
    }

    /**
     * Costruisce un ParserOutput contenente un comando e un NPC.
     *
     * @param command comando interpretato
     * @param npc     NPC associato
     */
    public ParserOutput(Command command, NPC npc) {
        this.command = command;
        this.npc = npc;
    }


    /**
     * Costruisce un ParserOutput contenente un comando e un numero di piano.
     *
     * @param command comando interpretato
     * @param piano   numero del piano
     */
    public ParserOutput(Command command, int piano) {
        this.command = command;
        this.piano = piano;
    }

    /**
     * Restituisce il numero di piano associato.
     *
     * @return numero del piano o -1 se non impostato
     */
    public int getPiano() {
        return this.piano;
    }

    /**
     * Imposta il numero di piano associato.
     *
     * @param piano numero del piano
     */
    public void setPiano(int piano) {
        this.piano = piano;
    }

    /**
     * Restituisce il comando interpretato.
     *
     * @return comando
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Imposta il comando interpretato.
     *
     * @param command comando da impostare
     */
    public void setCommand(Command command) {
        this.command = command;
    }

    /**
     * Restituisce l'oggetto generico associato.
     *
     * @return oggetto generico o null se non presente
     */
    public GeneralItem getObject() {
        return object;
    }

    /**
     * Imposta l'oggetto generico associato.
     *
     * @param object oggetto generico da impostare
     */
    public void setObject(GeneralItem object) {
        this.object = object;
    }

    /**
     * Restituisce l'NPC associato.
     *
     * @return NPC o null se non presente
     */
    public NPC getNpc() {
        return npc;
    }

    /**
     * Imposta l'NPC associato.
     *
     * @param npc NPC da impostare
     */
    public void setObject(NPC npc) {
        this.npc = npc;
    }

}
