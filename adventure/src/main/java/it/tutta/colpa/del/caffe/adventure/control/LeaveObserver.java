/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.control;

import java.util.Map;

import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.entity.GameObserver;
import it.tutta.colpa.del.caffe.game.entity.GeneralItem;
import it.tutta.colpa.del.caffe.game.entity.Room;
import it.tutta.colpa.del.caffe.game.exception.ServerCommunicationException;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.GameUtils;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;

/**
 *
 * @author giovanni
 */
public class LeaveObserver implements GameObserver {

    /**
     *
     * @param description
     * @param parserOutput
     * @return
     */
    @Override
    public String update(GameDescription description, ParserOutput parserOutput) throws ServerCommunicationException {

        StringBuilder msg = new StringBuilder();
        if (parserOutput.getCommand().getType() == CommandType.LEAVE) {
            if (parserOutput.getObject() == null) {
                msg.append("Non hai specificato il nome dell'oggetto da lasciare\n").append("Scrivi lascia nome oggetto");
            } else if (parserOutput.getObject() instanceof GeneralItem obj) {
                if (description.getInventory().contains(obj)) {
                    GeneralItem objLeave1 = GameUtils.getObjectFromInventory(description.getInventory(), obj.getId());
                    int quantity = description.getInventory().getQuantity(objLeave1);
                    description.getInventory().remove(objLeave1, quantity);
                    Room roomCurr = description.getCurrentRoom();
                    Map<GeneralItem, Integer> map = roomCurr.getObjects();
                    map.put(objLeave1, quantity);
                    roomCurr.setObjects(map);

                    msg.append("Hai laciato ").append(objLeave1.getName()).append(" nella stanza");
                } else {
                    msg.append("l'oggetto ").append(obj.getName()).append(" non è nell'inventario");
                }
            } else {
                msg.append(" hai indicato qualcosa che non è un oggetto.");
            }

        }
        return msg.toString();
    }

}
