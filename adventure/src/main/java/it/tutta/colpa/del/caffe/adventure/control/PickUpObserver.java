/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.control;

/**
 *
 * @author giova
 */


import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.entity.GameObserver;
import it.tutta.colpa.del.caffe.game.entity.Inventory;
import it.tutta.colpa.del.caffe.game.exception.InventoryException;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.GameUtils;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;
import java.util.Map;
import it.tutta.colpa.del.caffe.game.entity.ItemContainer;
import it.tutta.colpa.del.caffe.game.entity.GeneralItem;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author giova
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
    Object obj = parserOutput.getObject();
    boolean conteiner = false;

    if (parserOutput.getCommand().getType() == CommandType.PICK_UP) {
        if (obj == null) {
            msg.append("Non hai specificato l'oggetto da raccogliere. (scrivi 'raccogli nome oggetto')");
            return msg.toString();
            
        } else if (!description.getCurrentRoom().getObjects().isEmpty()) {
            GeneralItem isobjRoom = description.getCurrentRoom().getObjects().keySet().stream()
                .filter(o -> o.getName().equals(parserOutput.getObject().getName())
                        || o.getAlias().contains(parserOutput.getObject().getName()))
                .findFirst()
                .orElse(null);

            if (isobjRoom == null) {
                if (description.getCurrentRoom().getObject(7) != null) {
                    isobjRoom = description.getCurrentRoom().getObject(7);
                    
                    conteiner = true;
                } else if (description.getCurrentRoom().getObject(11) != null) {
                    isobjRoom = description.getCurrentRoom().getObject(11);
                    conteiner = true;
                } else if (description.getCurrentRoom().getObject(15) != null) {
                    isobjRoom = description.getCurrentRoom().getObject(15);
                    conteiner = true;
                }

                if (conteiner) {
                    if (isobjRoom instanceof ItemContainer) {
                        ItemContainer isobjRoomC = (ItemContainer) isobjRoom;
                        if (isobjRoomC.isOpen()) {
                            Map.Entry<GeneralItem, Integer> contenuto = isobjRoomC.getList().entrySet().stream()
                                .filter(entry -> entry.getKey().getName().equals(parserOutput.getObject().getName())
                                        || entry.getKey().getAlias().contains(parserOutput.getObject().getName()))
                                .findFirst()
                                .orElse(null);
                            if (contenuto != null) {
                                GeneralItem objCont = contenuto.getKey();
                                int quantity = contenuto.getValue();
                                try {
                                    // Aggiungiamo all'inventario con la stessa quantità
                                    description.getInventory().add(objCont, quantity);
                                    msg.append(" ").append(quantity).append(" x ").append(objCont.getName());
                                    isobjRoomC.remove(objCont, quantity);
                                } catch (InventoryException e) {
                                    msg.append(" Non puoi aggiungere ")
                                       .append(objCont.getName())
                                       .append(" all'inventario: ").append(e.getMessage());
                                } catch (IllegalArgumentException e) {
                                    msg.append(e.getMessage());
                                }
                            }
                        } else { // contenitore chiuso
                            msg.append("Ops l'oggetto : ").append(isobjRoomC.getName())
                               .append(" è chiuso. Dovresti prima aprirlo. (Usa il comando APRI nome oggetto");
                        }
                    }
                }
            } else { // oggetto trovato direttamente nella stanza
                Map<GeneralItem, Integer> objRoom = description.getCurrentRoom().getObjects();
                int quantity = objRoom.get(isobjRoom);
                try {
                    description.getInventory().add(isobjRoom, quantity);
                    msg.append(" ").append(quantity).append(" x ").append(isobjRoom.getName());
                    objRoom.remove(isobjRoom, quantity);
                } catch (InventoryException e) {
                    msg.append(" Non puoi aggiungere ").append(isobjRoom.getName()).append(" all'inventario: ").append(e.getMessage());
                } catch (IllegalArgumentException e) {
                    msg.append(e.getMessage());
                }
            }
        } else if (!description.getInventory().getInventory().isEmpty()) {
            GeneralItem invObj = (GeneralItem) obj;
            List<GeneralItem> listContainer = new ArrayList<>();
            boolean trovato = false;
            if (!description.getInventory().getInventory().containsKey(invObj)) {
                Inventory inventario = description.getInventory();
                if (GameUtils.getObjectFromInventory(inventario, 7) != null) {
                    listContainer.add(GameUtils.getObjectFromInventory(inventario, 7));
                }
                if (GameUtils.getObjectFromInventory(inventario, 11) != null) {
                    listContainer.add(GameUtils.getObjectFromInventory(inventario, 11));
                }
                if (GameUtils.getObjectFromInventory(inventario, 15) != null) {
                    listContainer.add(GameUtils.getObjectFromInventory(inventario, 15));
                }

                if (!listContainer.isEmpty()) {
                    for (GeneralItem objCont : listContainer) {
                        ItemContainer container = (ItemContainer) objCont;
                        if (container.isOpen()) {
                            try {
                                Map<GeneralItem, Integer> pippo = container.getObject(invObj);
                                GeneralItem objFinde = pippo.keySet().iterator().next();
                                int quantity = pippo.get(objFinde);
                                description.getInventory().add(objFinde, quantity);
                                msg.append(" ").append(quantity).append(" x ").append(objFinde.getName());
                                container.remove(objFinde, quantity);
                                trovato = true;
                                break;
                            } catch (InventoryException e) {
                                msg.append(" Non puoi aggiungere l'oggetto all'inventario. ").append(e.getMessage());
                            } catch (IllegalArgumentException e) {
                                msg.append(e.getMessage());
                            }
                        }
                    }
                }
                if (!trovato || listContainer.isEmpty()) {
                    msg.append("L'oggetto ").append(invObj.getName()).append(" non è nell'inventario");
                }
            } else {
                msg.append("Hai già l'oggetto nell'inventario, non prendi nulla.");
            }
        } else if (description.getInventory().getInventory().isEmpty()) {
            
            msg.append("Impossibile, non hai oggetti nell'inventario.");
            } else {
            
            msg.append("Nella stanza: ").append(description.getCurrentRoom().getName())
               .append(" non è presente l'oggetto ").append(parserOutput.getObject().getName());
            }
        }
        return msg.toString();
    }
}