/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.control;

import it.tutta.colpa.del.caffe.adventure.entity.GameDescription;
import it.tutta.colpa.del.caffe.adventure.utility.ParserOutput;
import it.tutta.colpa.del.caffe.adventure.utility.CommandType;

/**
 *
 * @author pierpaolo
 */
public class PickUpObserver implements GameObserver {

    /**
     *
     * @param description
     * @param parserOutput
     * @return
     */
    @Override
    public String update(GameDescription description, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        if (parserOutput.getCommand().getType() == CommandType.PICK_UP) {
            if (parserOutput.getObject() != null) {
                if (parserOutput.getObject().isPickupable()) {
                    description.getInventory().add(parserOutput.getObject());
                    description.getCurrentRoom().getObjects().remove(parserOutput.getObject());
                    msg.append("Hai raccolto: ").append(parserOutput.getObject().getDescription());
                    if (description.getCurrentRoom().getId() == 2) {
                        description.getCurrentRoom().setLook("La solita cucina...");
                    } else if (description.getCurrentRoom().getId() == 3) {
                        description.getCurrentRoom().setLook("Non c'è nulla di interessante qui.");
                    }
                } else {
                    msg.append("Non puoi raccogliere questo oggetto.");
                }
            } else {
                msg.append("Non c'è niente da raccogliere qui.");
            }
        }
        return msg.toString();
    }

}
