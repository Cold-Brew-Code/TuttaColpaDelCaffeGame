/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tutta.colpa.del.caffe.adventure.utility;

import it.tutta.colpa.del.caffe.game.entity.AdvObject;
import it.tutta.colpa.del.caffe.game.entity.Command;

/**
 *
 * @author pierpaolo
 */
public class ParserOutput {

    private Command command;

    private AdvObject object;
    
    private AdvObject invObject;

    /**
     *
     * @param command
     * @param object
     */
    public ParserOutput(Command command, AdvObject object) {
        this.command = command;
        this.object = object;
    }

    /**
     *
     * @param command
     * @param object
     * @param invObejct
     */
    public ParserOutput(Command command, AdvObject object, AdvObject invObejct) {
        this.command = command;
        this.object = object;
        this.invObject = invObejct;
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
    public AdvObject getObject() {
        return object;
    }

    /**
     *
     * @param object
     */
    public void setObject(AdvObject object) {
        this.object = object;
    }

    /**
     *
     * @return
     */
    public AdvObject getInvObject() {
        return invObject;
    }

    /**
     *
     * @param invObject
     */
    public void setInvObject(AdvObject invObject) {
        this.invObject = invObject;
    }

}
