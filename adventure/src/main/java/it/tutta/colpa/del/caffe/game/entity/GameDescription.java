/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tutta.colpa.del.caffe.game.entity;

import it.tutta.colpa.del.caffe.game.utility.GameStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pierpaolo
 */
public class GameDescription implements Serializable {
    private static final long serialVersionUID = 1L;

    private final GameMap gameMap;
    private final List<Command> commands;
    private final Inventory inventory;
    private GameStatus status;
    private final List<String> messages;

    public GameDescription(GameMap gameMap, List<Command> commands) {
        this.gameMap = gameMap;
        this.commands = commands;
        this.inventory = new Inventory();
        this.status = GameStatus.IN_CORSO;
        this.messages = new ArrayList<>();
    }

    // <editor-fold defaultstate="collapsed" desc="< Get & Set >">
    public String getWelcomeMsg() {
        return "\t[ Tutta colpa del caffè! ]";
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public List<String> getMessages() {
        return messages;
    }

    public Room getCurrentRoom() {
        return this.gameMap.getCurrentRoom();
    }

    // </editor-fold>>
}
