/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.control;

import it.tutta.colpa.del.caffe.adventure.entity.GameDescription;
import it.tutta.colpa.del.caffe.adventure.utility.ParserOutput;
import it.tutta.colpa.del.caffe.adventure.entity.AdvObject;
import it.tutta.colpa.del.caffe.adventure.entity.AdvObjectContainer;
import it.tutta.colpa.del.caffe.adventure.utility.CommandType;
import it.tutta.colpa.del.caffe.adventure.utility.GameUtils;

/**
 *
 * @author pierpaolo
 */
public class OpenObserver implements GameObserver {

    /**
     *
     * @param description
     * @param parserOutput
     * @return
     */
    @Override
    public String update(GameDescription description, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        Object obj= parserOutput.getObject();
        if (parserOutput.getCommand().getType() == CommandType.OPEN) {
            if (obj == null && parserOutput.getInvObject() == null) {
                msg.append("Non hai specificato l'oggetto da aprire. (scrivi 'apri nome oggetto').");
                return msg.toString();
            }
            //oggetto nella stanza da aprire
            else if(obj!= null && parserOutput.getInvObject() == null ){
                if( obj instanceof AdvObjectContainer){
                    AdvObjectContainer c= (AdvObjectContainer) obj;
                    boolean isCurrentRoom= false;
                    if(c.getId()== 11){
                        isCurrentRoom= description.getCurrentRoom().getId()== 19;
                        if(isCurrentRoom && c.isOpen()== false){
                            //aprimao l'oggetto
                            c.setOpen(true);
                            msg.append("Hai aperto: ").append(c.getName());
                            if (!c.getObjects().isEmpty()) {
                                msg.append(". ").append(c.getName()).append(" contiene:");
                                c.getObjects().forEach((next, quantity) -> msg.append(" ").append(quantity).
                                        append(" x ").append(next.getName())
                                );// mi stampo ogni oggetto con la sua quantità presente nel contenitore: 
                            }     
                        }else if(isCurrentRoom && c.isOpen()){
                            msg.append("L'oggetto ").append(c.getName()).append(" è già aperto");
                        }else if(isCurrentRoom== false){
                           msg.append("Che fai ti immagini gli oggetti???\n L'oggetto ").append(c.getName()).append(" non è in questa stanza.");
                        }
                    } else if(c.getId()== 7){
                       isCurrentRoom= description.getCurrentRoom().getId()== 13;
                       if(isCurrentRoom && c.isOpen()== false){
                            //aprimao l'oggetto
                            c.setOpen(true);
                            msg.append("Hai aperto: ").append(c.getName());
                            if (!c.getObjects().isEmpty()) {
                                msg.append(". ").append(c.getName()).append(" contiene:");
                                c.getObjects().forEach((next, quantity) -> msg.append(" ").append(quantity).
                                        append(" x ").append(next.getName())
                                );// mi stampo ogni oggetto con la sua quantità presente nel contenitore:
                            }    
                        }else if(isCurrentRoom && c.isOpen()){
                            msg.append("L'oggetto ").append(c.getName()).append(" è già aperto");
                        }else if(isCurrentRoom== false){
                           msg.append("Che fai ti immagini gli oggetti???\n L'oggetto ").append(c.getName()).append(" non è in questa stanza.");
                        }   
                    }
                }else{
                     AdvObject curr= (AdvObject) obj;
                     if(description.getCurrentRoom().getObject(curr.getId())!= null){
                         msg.append("L'oggetto ").append(curr.getName()).append(" non può essere aperto");
                        }else{
                         msg.append("Il bisogno ti sta dando alla testa forse... L'oggetto ").append(curr.getName()).append(" non è qui");
                     }
                    }
                }
            }
            
            // se ho l'oggetto nell'inventario 
            if(obj== null & parserOutput.getInvObject() != null ){
                AdvObject invObj= GameUtils.getObjectFromInventory(description.getInventory(), parserOutput.getObject().getId());
                if(invObj.getId()==11){
                 AdvObjectContainer c = (AdvObjectContainer) invObj;
                 if(c.isOpen()== false){
                     c.setOpen(true);
                     msg.append("Hai aperto: ").append(c.getName());
                            if (!c.getObjects().isEmpty()) {
                                msg.append(". ").append(c.getName()).append(" contiene:");
                                c.getObjects().forEach((next, quantity) -> msg.append(" ").append(quantity).
                                        append(" x ").append(next.getName())
                                    );
                            }   
                    }else if(c.isOpen()){
                         msg.append("L'oggetto ").append(c.getName()).append(" è già aperto");    
                    }   
                }else if(invObj.getId()==7){
                    AdvObjectContainer c = (AdvObjectContainer) invObj;
                    if(c.isOpen()== false){
                     c.setOpen(true);
                     msg.append("Hai aperto: ").append(c.getName());
                            if (!c.getObjects().isEmpty()) {
                                msg.append(". ").append(c.getName()).append(" contiene:");
                                c.getObjects().forEach((next, quantity) -> msg.append(" ").append(quantity).
                                        append(" x ").append(next.getName())
                                    );
                            }   
                    }   
                    //caso in cui l'oggetto è nell'inventario ma non è uno di quelli apribili
                }else if(invObj != null){
                    msg.append("L'oggetto indicato non è apribili"); 
                }else{
                    msg.append("L'oggetto indicato non è nell'inventario"); 
                }
            }
            return msg.toString();    
        }
    }





