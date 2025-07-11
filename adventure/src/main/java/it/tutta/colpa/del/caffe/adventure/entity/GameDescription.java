/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tutta.colpa.del.caffe.adventure.entity;

import it.tutta.colpa.del.caffe.adventure.utility.ParserOutput;
import it.tutta.colpa.del.caffe.adventure.entity.AdvObject;
import it.tutta.colpa.del.caffe.adventure.entity.Command;
import it.tutta.colpa.del.caffe.adventure.entity.Room;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pierpaolo
 */
public abstract class GameDescription {

    private final List<Room> rooms = new ArrayList<>();
    private final List<Command> commands = new ArrayList<>();

    private final Inventory inventory = new Inventory();

    private Room currentRoom;

    /**
     * Restituisce la lista delle stanze del gioco.
     */
    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * Restituisce la lista dei comandi disponibili.
     */
    public List<Command> getCommands() {
        return commands;
    }

    /**
     * Restituisce la stanza attualmente in cui si trova il giocatore.
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Imposta la stanza corrente.
     */
    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    /**
     * Restituisce l'inventario del giocatore.
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Metodo astratto per inizializzare il gioco.
     */
    public abstract void init() throws Exception;

    /**
     * Metodo astratto per gestire la prossima mossa del giocatore.
     */
    public abstract void nextMove(ParserOutput p, PrintStream out);

    /**
     * Messaggio di benvenuto del gioco.
     */
    public abstract String getWelcomeMsg();
}

