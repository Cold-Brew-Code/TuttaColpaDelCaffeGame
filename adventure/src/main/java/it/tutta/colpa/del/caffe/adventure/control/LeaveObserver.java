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
            if(parserOutput.getObject()==null){
                msg.append("Non hai specificato il nome dell'oggetto da lasciare\n").append("Scrivi lascia nome oggetto");
            }
            else if(parserOutput.getObject() instanceof GeneralItem obj){
                if(description.getInventory().contains(obj)){
                    GeneralItem objLeave = description.getInventory().getItem(obj);
                    int quantity= description.getInventory().getQuantity(objLeave);
                    description.getInventory().remove(objLeave, quantity);
                    Room roomCurr= description.getCurrentRoom();
                    Map<GeneralItem, Integer> map=  roomCurr.getObjects();
                    map.put(objLeave, quantity);
                    roomCurr.setObjects(map);
                    msg.append("Hai laciato ").append(objLeave.getName()).append(" nella stanza");
                }else{
                    msg.append("l'oggetto ").append(obj.getName()).append(" non è nell'inventario");
                }
            }else{
                msg.append(" hai indicato qualcosa che non è un oggetto.");
            }
          
        }
        return msg.toString();
    }

}
