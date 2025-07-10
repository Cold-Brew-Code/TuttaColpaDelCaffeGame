/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.control;

import it.tutta.colpa.del.caffe.adventure.entity.GameDescription;
import it.tutta.colpa.del.caffe.adventure.utility.CommandType;
import it.tutta.colpa.del.caffe.adventure.utility.GameUtils;
import it.tutta.colpa.del.caffe.adventure.utility.ParserOutput;

/**
 *
 * @author pierpaolo
 */
public class UseObserver implements GameObserver {

    /**
     * SE l'oggetto è componibile O se può essere USATO 
     * @param description
     * @param parserOutput
     * @return
     */
    @Override
    public String update(GameDescription description, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        if (parserOutput.getCommand().getType() == CommandType.USE) {
            boolean interact = false;
            String nameObj= "";
            if(parserOutput.getInvObject() != null && parserOutput.getInvObject().getId() == 14){// sto usando la carta magica per aprire le port 
                boolean magicCard =parserOutput.getObject() != null && parserOutput.getObject().getId() == 14;
                if(magicCard && parserOutput.getObject().getUtilizzi()>0) {
                    if (description.getCurrentRoom().isOpen()) {
                        msg.append("La porta è già aperta");
                    }else{
                        description.getCurrentRoom().setOpen(true);
                        parserOutput.getObject().setUtilizzi(parserOutput.getObject().getUtilizzi()-1);
                        msg.append("Hai usato la carta magica per aprire la porta. Ora puoi entrare nella stanza.");
                        interact = true;
                    } 
                }
                else if(magicCard && parserOutput.getObject().getUtilizzi()<=0 && description.getCurrentRoom().isOpen()== false){
                    msg.append("sorry but you can not use the magic card becasue you have finished the uses");
                }
                else if(magicCard== false && description.getCurrentRoom().isOpen()== false){
                    nameObj= parserOutput.getObject().getName();
                    msg.append("You can not use " + nameObj+ " to open the door");
                }
            }else if(parserOutput.getObject() != null && parserOutput.getObject().getId() == 16){ // il caffè è l'oggetto 16
                boolean isInRoom = description.getCurrentRoom().getObject(16) != null;
                if(isInRoom && GameUtils.getObjectFromInventory(description.getInventory(),8)!= null){
                    msg.append("You can take a caffè");
                    parserOutput.getObject().setUtilizzi(parserOutput.getObject().getUtilizzi()-1);
                    interact= true;
                } 
                else if (isInRoom && GameUtils.getObjectFromInventory(description.getInventory(),8)== null ){
                    msg.append("you don't have a money to buy a caffè. You are poor");
                }
                else{
                    msg.append("you don't find a correct room");

                }
            }else if(parserOutput.getObject() != null && parserOutput.getObject().getId() == 1){
                boolean isVisibleMap= description.getCurrentRoom().getObject(1) != null;
                if(isVisibleMap && parserOutput.getObject().isVisibile()== false){
                    msg.append("there is not a map in this room"); // perche la mappa e visibile solo se passa l'indovinello altrimenti non è nota la sua esistenza.
                }
                else if(isVisibleMap && GameUtils.getObjectFromInventory(description.getInventory(),1)== null){
                    msg.append("You must take the map before!");
                }else{
                    interact= true;
                }
            }else if (parserOutput.getObject() != null && parserOutput.getObject().getId() == 9){
                int id= description.getCurrentRoom().getId();
                boolean takeKey = (id == 3 || id == 4 || id == 5 || id == 6 || id == 14 ||id==17 ||id==20|| id==25);
                if( takeKey && GameUtils.getObjectFromInventory(description.getInventory(),9)!= null && parserOutput.getObject().isVisibile()){
                    msg.append("puoi usare la chiave per sbloccare l'ascensore");
                    interact=true;
            }else{
                    msg.append("non esiste nessuna chiave l'urgenza ti sta dando alla testa");// questo perchè la chiave è visible se passi l'indovinello
                    }

            }
            if (!interact) {
                msg.append("Non ci sono oggetti utilizzabili qui.");
            }
        }
        return msg.toString();
    }
}

