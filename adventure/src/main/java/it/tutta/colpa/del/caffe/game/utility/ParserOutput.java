/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tutta.colpa.del.caffe.game.utility;

import it.tutta.colpa.del.caffe.game.entity.GeneralItem;
import it.tutta.colpa.del.caffe.game.entity.Command;

/**
 *
 * @author pierpaolo
 */
public class ParserOutput {

    private Command command;

    private GeneralItem object;
    
    private GeneralItem invObject;

    /**
     *
     * @param command
     * @param object
     */
    public ParserOutput(Command command, GeneralItem object) {
        this.command = command;
        this.object = object;
    }

    /**
     *
     * @param command
     * @param object
     * @param invObejct
     */
    public ParserOutput(Command command, GeneralItem object, GeneralItem invObejct) {
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
    public GeneralItem getInvObject() {
        return invObject;
    }

    /**
     *
     * @param invObject
     */
    public void setInvObject(GeneralItem invObject) {
        this.invObject = invObject;
    }

}
