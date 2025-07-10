/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.control;

import it.tutta.colpa.del.caffe.adventure.entity.AdvObject;
import it.tutta.colpa.del.caffe.adventure.entity.GameDescription;
import it.tutta.colpa.del.caffe.adventure.utility.CommandType;
import it.tutta.colpa.del.caffe.adventure.utility.GameUtils;
import it.tutta.colpa.del.caffe.adventure.utility.ParserOutput;

/**
 *
 * @author giova
 */
public class BuildObserver implements GameObserver {

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
        if (parserOutput.getCommand().getType() == CommandType.CREATE) {
                String nameObj= "";
                boolean isRoomCurrent1=false;
                boolean isRoomCurrent2=false;
                 if (obj == null) {
                    msg.append("Non hai specificato gli oggetto da combinare. (scrivi 'combina nome oggetto nome oggetto')");
                    return msg.toString();
                }
                else if(obj instanceof String[]){
                    String [] item= (String []) obj;
                    if(item.length != 2){
                        msg.append("Non è il momento di giocare con gli oggetti! ( puoi combinare solo due oggetti)");
                        return msg.toString();
                    }
                    else{
                        boolean objInv1= GameUtils.getObjectFromInventory(description.getInventory(), 12)!= null; // carta 
                        boolean objInv2= GameUtils.getObjectFromInventory(description.getInventory(), 6)!= null; //scheda madre
                        int IdRoomCurrent= description.getCurrentRoom().getId();
                        if (objInv1 && objInv2){
                            //AdvObject cartaMagica= new AdvObject()
                            AdvObject objInvR1= GameUtils.getObjectFromInventory(description.getInventory(), 12);
                            AdvObject objInvR2= GameUtils.getObjectFromInventory(description.getInventory(), 6);
                            description.getInventory().remove(objInvR1);
                            description.getInventory().remove(objInvR2);
                            msg.append("Magia");
                        }else if(objInv1==false && objInv2){
                            isRoomCurrent1= description.getCurrentRoom().getId()==19;
                            if(isRoomCurrent1){
                                msg.append("devi prima raccogliere l'oggetto ").append(description.getCurrentRoom().getObject(12).getName()).append("prima di combinare i due oggetti");;
                            }// non puoi costrurie
                        }else if (objInv2==false && objInv1){
                            isRoomCurrent2= description.getCurrentRoom().getId()==30;
                            if(isRoomCurrent2){
                                msg.append("devi prima raccogliere l'oggetto ").append(description.getCurrentRoom().getObject(6).getName()).append("prima di combinare i due oggetti");
                                // else non puoi costrurie   
                        }else if(IdRoomCurrent!=19 ||IdRoomCurrent!=30){
                            msg.append("Non hai gli oggetti necessari per scostruire la carta magica");
                        }                            
                    }
                }
            }
        }
        return msg.toString();
    }
}