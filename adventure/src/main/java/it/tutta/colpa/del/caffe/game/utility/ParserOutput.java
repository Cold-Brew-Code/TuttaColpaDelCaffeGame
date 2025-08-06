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
 * @author pierpaolo
 */
public class ParserOutput {

    private Command command;

    private GeneralItem object;
    
    private GeneralItem object1;
    private NPC npc;
    private int piano;

    /**
     *
     * @param command
     * @param object
     */
    public ParserOutput(Command command, GeneralItem object) {
        this.command = command;
        this.object = object;
    }

    public ParserOutput(Command command) {
        this.command = command;
    }

    /**
     *
     * @param command
     * @param object
     * @param invObejct
     */
    public ParserOutput(Command command, GeneralItem object, GeneralItem object1) {
        this.command = command;
        this.object = object;
        this.object1 = object1;
    }


    public ParserOutput(Command command, NPC npc) {
        this.command = command;
        this.npc = npc;
    }

    public ParserOutput(Command command, int piano) {
        this.command = command;
        this.piano = piano;
    }

    public int getPiano() {
        return this.piano;
    }

    /**
     *
     * @param piano
     */
    public void setPiano(int piano) {
        this.piano = piano;
    }


    /**
     *
     * @return
     */
    public Command getCommand() {
        return command;
    }

    /**
     *
     * @param command
     */
    public void setCommand(Command command) {
        this.command = command;
    }

    /**
     *
     * @return
     */
    public GeneralItem getObject() {
        return object;
    }

    /**
     *
     * @param object
     */
    public void setObject(GeneralItem object) {
        this.object = object;
    }

    /**
     *
     * @return
     */
    public NPC getNpc() {
        return npc;
    }

    /**
     *
     * @param n
     */
    public void setObject(NPC npc) {
        this.npc = npc;
    }

    public GeneralItem getObject1() {
        return object1;
    }

    public void setObject1(GeneralItem object1) {
        this.object1 = object1;
    }

}
