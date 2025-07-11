/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tutta.colpa.del.caffe.game.entity;

import it.tutta.colpa.del.caffe.adventure.utility.GameStatus;
import it.tutta.colpa.del.caffe.adventure.utility.ParserOutput;

import java.util.List;

/**
 * @author pierpaolo
 */
public class GameDescription implements GameObservable{

    private final GameMap gameMap;
    private final List<Command> commands;
    private final Inventory inventory;
    private GameStatus status;
    private ParserOutput parserOutput;
    private List<GameObserver>  observers;
    private List<String> messages;


    public GameDescription(GameMap gameMap, List<Command> commands, Inventory inventory) {
        this.gameMap = gameMap;
        this.commands = commands;
        this.inventory = inventory;
        this.status = GameStatus.IN_CORSO;
    }

    @Override
    public void attach(GameObserver o) {
        if (!this.observers.contains(o)) {
            this.observers.add(o);
        }
    }

    @Override
    public void detach(GameObserver o) {
        this.observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for(GameObserver o : this.observers){
            messages.add(o.update(this, parserOutput));
        }
    }

    // <editor-fold defaultstate="collapsed" desc="< Get & Set >">
    public String getWelcomeMsg() {
        return "[ Tutta colpa del caffè! ]";
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

    public ParserOutput getParserOutput() {
        return parserOutput;
    }

    public void setParserOutput(ParserOutput parserOutput) {
        this.parserOutput = parserOutput;
    }

    public List<GameObserver> getObservers() {
        return observers;
    }

    public void setObservers(List<GameObserver> observers) {
        this.observers = observers;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
    public Room getCurrentRoom(){
        return this.gameMap.getCurrentRoom();
    }
    // </editor-fold>>
}
