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
 *shift + alt + f 
 * @author pierpaolo
 */
public class UseObserver implements GameObserver {

    /**
     * SE l'oggetto è componibile O se può essere USATO
     *
     * @param description
     * @param parserOutput
     * @return
     */
    @Override
    public String update(GameDescription description, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        Object obj = parserOutput.getObject();
        if (parserOutput.getCommand().getType() == CommandType.USE) {
            boolean interact = false;
            String nameObj = "";
            AdvObject objInv;
            AdvObject objInv1;
            if (obj == null) {
                msg.append("Non hai specificato l'oggetto da leggere. (scrivi 'leggi nome oggetto')");
                return msg.toString();
            } else {
                if (parserOutput.getObject().getId() == 14) {// sto usando la carta magica per aprire le porta 
                    boolean magicCard= GameUtils.getObjectFromInventory(description.getInventory(), 14)!= null;
                    if(magicCard == false){
                        boolean isNotbuild= GameUtils.getObjectFromInventory(description.getInventory(), 13)!= null;
                        boolean isNotbuild1= GameUtils.getObjectFromInventory(description.getInventory(),6)!= null;
                        if(isNotbuild && isNotbuild1){
                            msg.append("Devi creare la carta prima di usarala! ( usa il comando crea per crare la carta magiaca)");
                            interact= true;
                        }else if (isNotbuild == false && isNotbuild1)
                        {   objInv =GameUtils.getObjectFromInventory(description.getInventory(), 13);
                            msg.append("Non hai l'oggetto ").append(objInv.getName()).append(" non è nell'inventario. Quinidi non hai la carta magica.");
                            interact= true;

                        }else if (isNotbuild1 == false && isNotbuild)
                        {   objInv1 =GameUtils.getObjectFromInventory(description.getInventory(), 6);
                            msg.append("Non hai l'oggetto ").append(objInv1.getName()).append(" non è nell'inventario. Quinidi non hai la carta magica.");
                            interact= true;
                        }
                    }else if (magicCard && parserOutput.getObject().getUtilizzi() > 0) {
                        //se voglio utilizzare la carta su una qualsiasi porta (compreso quelle che erno chiuse ma che sono già aperte) aperta
                        if (description.getCurrentRoom().isOpen()) {
                            msg.append("La porta è già aperta");
                            interact= true; 
                        } else {
                            description.getCurrentRoom().setOpen(true);
                            parserOutput.getObject().setUtilizzi(parserOutput.getObject().getUtilizzi() - 1);
                            msg.append("Hai usato la carta magica per aprire la porta. Ora puoi entrare nella stanza.");
                            interact = true;
                        }
                    } else if (magicCard && parserOutput.getObject().getUtilizzi() <= 0 && description.getCurrentRoom().isOpen() == false) {
                        msg.append("sorry but you can not use the magic card becasue you have finished the uses");
                        interact= true; 
                    }

                } else if (parserOutput.getObject().getId() == 16) { // il caffè è l'oggetto 16
                    boolean isInRoom = description.getCurrentRoom().getObject(16) != null;
                    if (isInRoom && GameUtils.getObjectFromInventory(description.getInventory(), 8) != null) {
                        objInv=GameUtils.getObjectFromInventory(description.getInventory(), 8);
                        if(objInv.getQuantity(objInv)== 0){
                            msg.append("you can not take a caffè , because you are poor");//stanza correta soldi 0
                            interact= true; 
                        }else{
                            msg.append("You can take a caffè");
                            objInv.setQuantity(getQuantity(objInv)-1); 
                            interact= true; 
                        }
                    }else if(isInRoom && GameUtils.getObjectFromInventory(description.getInventory(), 8) == null){
                        msg.append("you can not take a caffè , because you have not a maney into inventory");
                        interact= true; 
                    }
                    else if(isInRoom== false && GameUtils.getObjectFromInventory(description.getInventory(), 8) != null){
                        msg.append("non fare lo spendaccione!"); // la stanza è errata ma ha i soldi
                        interact= true; 
                    }


                } else if (parserOutput.getObject().getId() == 1) {
                    boolean isVisibleMap = description.getCurrentRoom().getObject(1) != null;
                    if (isVisibleMap && parserOutput.getObject().isVisibile() == false) {
                        msg.append("there is not a map in this room"); // perche la mappa e visibile solo se passa l'indovinello altrimenti non è nota la sua esistenza.
                    } else if (isVisibleMap && GameUtils.getObjectFromInventory(description.getInventory(), 1) == null) {
                        msg.append("You must take the map before!");
                    } else if (isVisibleMap == false && GameUtils.getObjectFromInventory(description.getInventory(), 1) != null) {
                        interact = true;//la mappa può essere aperta ovunque
                    }
                } else if (parserOutput.getObject().getId() == 9) {
                    int id = description.getCurrentRoom().getId();
                    boolean takeKey = (id == 3 || id == 4 || id == 5 || id == 6 || id == 14 || id == 17 || id == 20 || id == 25);
                    if (takeKey && GameUtils.getObjectFromInventory(description.getInventory(), 9) != null && parserOutput.getObject().isVisibile()) {
                        msg.append("puoi usare la chiave per sbloccare l'ascensore");
                        interact = true;
                    } else {
                        msg.append("non esiste nessuna chiave l'urgenza ti sta dando alla testa");// questo perchè la chiave è visible se passi l'indovinello
                    }
                }  else{
                    boolean isInInventory= GameUtils.getObjectFromInventory(description.getInventory(), parserOutput.getObject().getId()) != null;
                    if(isInInventory && parserOutput.getObject().isUses()==false){
                        msg.append("non puoi utilizzare l'oggetto");
                        interact= true;
                     }
                }
            }
            if (interact==false) {
                msg.append("l'oggetto indicato non è nell'inventario");
            }
        }
        return msg.toString();
    }
}
