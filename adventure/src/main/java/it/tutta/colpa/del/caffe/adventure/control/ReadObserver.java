/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.control;

import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.entity.GameObserver;
import it.tutta.colpa.del.caffe.game.entity.Inventory;
import it.tutta.colpa.del.caffe.game.entity.ItemRead;
import it.tutta.colpa.del.caffe.game.entity.Room;
import it.tutta.colpa.del.caffe.game.exception.ItemException;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;

/**
 *
 * @author giova
 */
public class ReadObserver implements GameObserver {

    /**
     *
     * @param description
     * @param parserOutput
     * @return
     */

    @Override
    public String update(GameDescription description, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        // controllo se l'oggetto è stato specificato
        Object obj = parserOutput.getObject();

        if (parserOutput.getCommand().getType() == CommandType.READ) {
            if (obj == null) {
                msg.append("Non hai specificato gli oggetto da combinare. (scrivi 'combina nome oggetto nome oggetto')");
                return msg.toString();
            }else if(obj instanceof ItemRead pippo){
                Room currRoom= description.getCurrentRoom();
                Inventory inventory= description.getInventory();
                boolean objInRoom= false;
                if(pippo.getId()==3 && inventory.contains(pippo) ){// libro cc
                    if(currRoom.getId()==10){
                        try {// sto leggendo l'ooggetto nella stanza corretta
                            pippo.decreaseUses();
                            msg.append(pippo.getContent());
                        } catch (ItemException ex) {
                           msg.append(ex.getMessage());

                        }
                    }else{
                        // sta leggendo nella stanza sbagliata 
                        try {
                            pippo.decreaseUses();
                            msg.append("Ti sembra il momento di leggere ora?? Ricordati che gli oggetti più vengono usati più si consumano nel tempo...");
                            msg.append("Il contenuto è: ").append(pippo.getContent());
                        } catch (ItemException ex) {
                            msg.append(ex.getMessage());

                        }
                    }

                }else if(pippo.getId()==3 && !inventory.contains(pippo) && currRoom.getId()==7){
                    objInRoom= true;
                    /* deve prima raccoglire l'oggetto e poi può leggerlo
                    altrimenti messaggio generale : oggetto non presente nell'inventario 
                    poiche non è nell'inventario ma non è nemmeno nella stanzto in cui può
                    raccoglire il bigliettino
                    */
                }else if(pippo.getId()==5 && inventory.contains(pippo)){
                    if(currRoom.getId()==15){
                        try {// sto leggendo l'ooggetto nella stanza corretta
                            pippo.decreaseUses();
                            msg.append(pippo.getContent());
                        } catch (ItemException ex) {
                            msg.append(ex.getMessage());

                        }
                    }else{
                        // sta leggendo nella stanza sbagliata 
                        try {
                            pippo.decreaseUses();
                            msg.append("Ti sembra il momento di leggere ora?? Ricordati che gli oggetti più vengono usati più si consumano nel tempo...");
                            msg.append("Il contenuto è: ").append(pippo.getContent());
                        } catch (ItemException ex) {
                            msg.append(ex.getMessage());

                        }
                    }

                }else if(pippo.getId()==5 && !inventory.contains(pippo) && currRoom.getId()==16){
                    objInRoom= true;
                    /* devi prima raccoglire il bigliettino e poi puoi leggerlo, 
                    altrimenti messaggio generale : oggetto non presente nell'inventario  poiche non è nell'inventario ma non è nemmeno nella stanzto in cui può
                    raccoglire il bigliettino
                    */

                }else if(objInRoom== false){// no oggetto nell'inventario  ne nella stanza nella quale può raccoglire
                    msg.append("cosa vuoi leggere il vuoto?? L'oggetto ").append(pippo.getName()).append(" non è nell'inventario");
                }else{
                    //si trova nella stanza in cui può raccoglire l'oggetto
                    msg.append("Devi prima ragliere il ").append(pippo.getName()).append(" e poi forse ( se riuscirai ) potrai leggerlo.").append("Usa il comando prendi nome oggetto");
                }
            }else{
                msg.append("vuoli leggere cosa?? L'oggetto indicato non può essere letto");
            }

        }
        return msg.toString();
    }
}