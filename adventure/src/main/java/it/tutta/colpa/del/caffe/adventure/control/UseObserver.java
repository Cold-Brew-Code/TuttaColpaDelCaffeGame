/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.control;

import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.entity.GameObserver;
import it.tutta.colpa.del.caffe.game.entity.GeneralItem;
import it.tutta.colpa.del.caffe.game.entity.Inventory;
import it.tutta.colpa.del.caffe.game.entity.ItemContainer;
import it.tutta.colpa.del.caffe.game.exception.ServerCommunicationException;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.GameUtils;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;

/**
 *
 * @author giova
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
    public String update(GameDescription description, ParserOutput parserOutput) throws ServerCommunicationException {
        StringBuilder msg = new StringBuilder();
        Object obj = parserOutput.getObject();
        if (parserOutput.getCommand().getType() == CommandType.USE) {
            GeneralItem objInv;
            GeneralItem objInv1;
            if (obj == null) {
                msg.append("Non hai specificato l'oggetto da usare. (scrivi 'usa nome oggetto')");
                return msg.toString();
            } else {
                switch (parserOutput.getObject().getId()) {
                    case 14 -> {
                        // sto usando la carta magica per aprire le porta
                        boolean magicCard = GameUtils.getObjectFromInventory(description.getInventory(), 14) != null;
                        if (!magicCard) {
                            boolean hasChip = GameUtils.getObjectFromInventory(description.getInventory(), 12) != null; // chip
                            boolean hasDemagnetizedCard = GameUtils.getObjectFromInventory(description.getInventory(), 6) != null; // tessera samgnetizzata
                            
                            if (hasChip && hasDemagnetizedCard) {
                                msg.append("Devi creare la carta prima di usarla! (usa il comando 'crea' per creare la carta magica)");
                                
                            } else if (!hasChip && hasDemagnetizedCard) {
                                objInv = GameUtils.getObjectFromInventory(description.getInventory(), 12); // chip?
                                msg.append("Non hai l'oggetto ").append(objInv.getName()).append(" nell'inventario. Quindi non hai la carta magica.");
                                
                            } else if (!hasDemagnetizedCard && hasChip) {
                                objInv1 = GameUtils.getObjectFromInventory(description.getInventory(), 6); // tessera smagnetizzata
                                ItemContainer scatola = (ItemContainer) GameUtils.getObjectFromInventory(description.getInventory(), 11); // controllo se ha la scatola nell'inventario
                                
                                
                                if (scatola != null && scatola.isOpen()) {
                                    msg.append("Non hai l'oggetto ").append(objInv1.getName())
                                            .append(" nell'inventario. Quindi non hai la carta magica. ")
                                            .append("Potrebbe essere all'interno dell'oggetto: ").append(scatola.getName());
                                } else {
                                    msg.append("Non hai l'oggetto ").append(objInv1.getName())
                                            .append(" nell'inventario. Quindi non hai la carta magica.");
                                }
                            }
                            
                        }
                        /*
                        else if (magicCard && parserOutput.getObject().getUtilizzi() > 0) {
                            //se voglio utilizzare la carta su una qualsiasi porta (compreso quelle che erno chiuse ma che sono già aperte) aperta
                            if (description.getCurrentRoom().isDeniedEntry() == false) {
                                msg.append("La porta è già aperta");
                                
                            } else {
                                description.getCurrentRoom().setDeniedEntry(false);
                                parserOutput.getObject().setUtilizzi(parserOutput.getObject().getUtilizzi() - 1);
                                msg.append("Hai usato la carta magica per aprire la porta. Ora puoi entrare nella stanza.");
                                
                            }
                        } else if (magicCard && parserOutput.getObject().getUtilizzi() <= 0 && description.getCurrentRoom().isDeniedEntry()) {
                            msg.append("sorry but you can not use the magic card becasue you have finished the uses");
                            
                        }
                        */
                    }
                    case 16 -> {
                        // il caffè è l'oggetto 16
                        boolean isInRoom = description.getCurrentRoom().getObject(16) != null;
                        if (isInRoom && GameUtils.getObjectFromInventory(description.getInventory(), 8) != null) {
                            objInv = GameUtils.getObjectFromInventory(description.getInventory(), 8);
                            msg.append("You can take a caffè");
                            description.getInventory().remove(objInv); // c'è l'oggetto e tolgo una moneta.
                            
                        } else if (isInRoom && GameUtils.getObjectFromInventory(description.getInventory(), 8) == null) {
                            msg.append("you can not take a caffè , because you have not a maney into inventory  you are poor");
                           
                        } else if (isInRoom == false && GameUtils.getObjectFromInventory(description.getInventory(), 8) != null) {
                            msg.append("non fare lo spendaccione!"); // la stanza è errata ma ha i soldi
                            
                        }
                    }   //mappa 
                    case 1 -> {
                        boolean isVisibleMap = description.getCurrentRoom().getObject(1) != null;
                        if (isVisibleMap && parserOutput.getObject().isVisibile() == false) {
                            msg.append("there is not a map in this room"); // perche la mappa e visibile solo se passa l'indovinello altrimenti non è nota la sua esistenza.
                        } else if (isVisibleMap && GameUtils.getObjectFromInventory(description.getInventory(), 1) == null) {
                            msg.append("You must take the map before!");
                        } else if (isVisibleMap == false && GameUtils.getObjectFromInventory(description.getInventory(), 1) != null) {
                            //la mappa può essere aperta ovunque
                            // stampa il contenuto 
                            description.getGameMap().stampaDirezioniPerStanza();
                        }
                    }

                    // chiave
                    case 9 -> {
                        int id = description.getCurrentRoom().getId();
                        boolean takeKey = (id == 3 || id == 4 || id == 5 || id == 6 || id == 14 || id == 17 || id == 20 || id == 25);
                        boolean keyVisible=parserOutput.getObject().isVisibile();
                        if(!takeKey && GameUtils.getObjectFromInventory(description.getInventory(), 9) != null){
                            msg.append("non c'è l'ascensore qui. Puoi usare questa chiave solo per sbloccare l'asscensore");
                            
                        }else if (takeKey && GameUtils.getObjectFromInventory(description.getInventory(), 9) != null) {
                            msg.append("puoi usare la chiave per sbloccare l'ascensore");
                            description.getInventory().remove(parserOutput.getObject());// sblocco l'ascensore quindi la chiave è stata utilizzata 
                        } else if (keyVisible== false){
                            msg.append("non esiste nessuna chiave l'urgenza ti sta dando alla testa");// questo perchè la chiave è visible se passi l'indovinello
                        }
                        // se la chiave è visibile ma non ha 
                        else if (GameUtils.getObjectFromInventory(description.getInventory(), 9) == null && keyVisible && takeKey) {
                             msg.append("non puoi sbloccare l'ascensore, devi prendere la chiave");
                        }
                    }
                    default -> {
                        Inventory InInventory = description.getInventory();
                        if (InInventory.getQuantity(parserOutput.getObject()) == -1) { // poichè gli oggetti che non possono essere utilizzati hanno una quantità == -1
                            msg.append("non puoi utilizzare l'oggetto");
                           
                        }
                    }
                }
            }
        }
        return msg.toString();
    }
}
