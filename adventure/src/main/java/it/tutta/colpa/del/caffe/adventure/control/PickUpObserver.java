/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.control;

/**
 *
 * @author giova
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.tutta.colpa.del.caffe.game.control.ServerInterface;
import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.entity.GameObserver;
import it.tutta.colpa.del.caffe.game.entity.GeneralItem;
import it.tutta.colpa.del.caffe.game.entity.Inventory;
import it.tutta.colpa.del.caffe.game.entity.ItemContainer;
import it.tutta.colpa.del.caffe.game.exception.InventoryException;
import it.tutta.colpa.del.caffe.game.exception.ServerCommunicationException;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.GameUtils;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;
import static it.tutta.colpa.del.caffe.game.utility.RequestType.UPDATED_LOOK;

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
    public String update(GameDescription description, ParserOutput parserOutput) throws ServerCommunicationException{
        //System.out.println("sono in predni ");
        StringBuilder msg = new StringBuilder();
        if (parserOutput.getCommand().getType() == CommandType.PICK_UP) {
            ServerInterface server;
            try {
                server = new ServerInterface("localhost",49152 );
            } catch (ServerCommunicationException ex) {
                server= null;
            }

            Object obj = parserOutput.getObject();
            //GeneralItem obj1= (GeneralItem) obj; oggetto che voglio trovare
            boolean conteiner = false;
            if (obj == null) {
                msg.append("Non hai specificato l'oggetto da raccogliere. (scrivi 'raccogli nome oggetto')");
                return msg.toString();

            } else if (!description.getCurrentRoom().getObjects().isEmpty()) {
                System.out.println("fmklmf");
                GeneralItem isobjRoom = description.getCurrentRoom()
                        .getObjects()
                        .keySet()
                        .stream()
                        .filter(o -> o.getName().equals(parserOutput.getObject().getName()))
                        .findFirst()
                        .orElse(null);
                        //System.out.println("sono in predni 2\n"+isobjRoom.getName()+isobjRoom.getClass());

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
                    // xkè per controllare all'interno dell'inventario deve prima raccoglierlo o aperto ( per l'armadietto) 
                    if (isobjRoom instanceof ItemContainer isobjRoomC) {
                        if (conteiner && isobjRoom.getId() != 15) {
                            msg.append("l'oggetto non trovato. C'è l'oggetto: ").append(isobjRoomC.getName())
                                    .append("chissà se al suo intero c'è qualcosa. (Usa il comando prendi nome oggetto");
                            // mentre se l'oggetto non può essere raccolto ma può essere aperto  
                        } else {
                            System.out.println(isobjRoomC.getName()+isobjRoomC.isOpen());
                            System.out.println("PICK_UP: armadietto - isOpen: " + isobjRoomC.isOpen() + " - hash: " + System.identityHashCode(isobjRoomC)+isobjRoom.getName());
                            System.out.println("bastardo sei diverso\t"+isobjRoomC.equals(isobjRoom)+isobjRoom.getId()+isobjRoom.getName());
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
                                        // ho raccolto la candeggina quindi cambio la descrizione della stanza 9
                                        if (server != null) {
                                            description.getCurrentRoom().setLook((String)server.requestToServer(UPDATED_LOOK, 2));
                                        } else {
                                            throw new ServerCommunicationException ("connessione al server fallita");
                                        }
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
                } else if(parserOutput.getObject().isPickupable()){ // oggetto trovato direttamente nella stanza
                    Map<GeneralItem, Integer> objRoom = description.getCurrentRoom().getObjects();
                    System.out.println(isobjRoom.getName()+ isobjRoom.isVisibile()+"visibile?");
                    if(isobjRoom.isVisibile()){
                        int quantity = objRoom.get(isobjRoom);
                        try {

                            description.getInventory().add(isobjRoom, quantity);
                            msg.append(" ").append(quantity).append(" x ").append(isobjRoom.getName());
                            objRoom.remove(isobjRoom, quantity);

                            // oggetto trovato nella stanza quindi è stato raccolto e aggiorno la descrizione della stanza
                            try {
                                int roomId = description.getCurrentRoom().getId();
                                if(server!= null){
                                    switch (roomId) {
                                        case 16 -> // ha raccolto il bigliettino evento 3
                                            description.getCurrentRoom().setLook(server.requestToServer(UPDATED_LOOK, 3));
                                        case 30 -> // ha raccolto scheda madre evento 5
                                            description.getCurrentRoom().setLook(server.requestToServer(UPDATED_LOOK, 5));
                                        case 13 -> // ha raccolto il borsellino evento 7
                                            description.getCurrentRoom().setLook(server.requestToServer(UPDATED_LOOK, 7));
                                        case 7 -> // ha raccolto libro evento 1
                                            description.getCurrentRoom().setLook(server.requestToServer(UPDATED_LOOK, 1));
                                        case 19 -> // ha raccolto la scatola evento 9
                                            description.getCurrentRoom().setLook(server.requestToServer(UPDATED_LOOK, 9));
                                        default -> {
                                        }
                                    }
                                }else{
                                    throw new ServerCommunicationException ("connessione al server fallita");
                                }
                            } catch (ServerCommunicationException | NullPointerException e) {
                                msg.append(" Errore nella comunicazione col server: ").append(e.getMessage());
                            }

                        } catch (InventoryException e) {
                            msg.append(" Non puoi aggiungere ").append(isobjRoom.getName()).append(" all'inventario: ").append(e.getMessage());
                        } catch (IllegalArgumentException e) {
                            msg.append(e.getMessage());
                        }
                    }else{
                        msg.append("non c'è: ").append(isobjRoom.getName()).append(" nella stanza\n L'urgenza da alla testa.");
                    }
                }else{
                    msg.append("l'oggetto ").append(parserOutput.getObject().getName()).append(" non può essere raccolto");
                }

                // caso inventario     
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
                                    if (pippo != null && !pippo.isEmpty()) {// se il container ha l'oggetto indicato
                                        GeneralItem objFinde = pippo.keySet().iterator().next();
                                        int quantity = pippo.get(objFinde);
                                        description.getInventory().add(objFinde, quantity);
                                        msg.append(" ").append(quantity).append(" x ").append(objFinde.getName());
                                        container.remove(objFinde, quantity);
                                        trovato = true;
                                    }
                                } catch (InventoryException e) {
                                    msg.append(" Non puoi aggiungere l'oggetto all'inventario. ").append(e.getMessage());
                                } catch (IllegalArgumentException e) {
                                    System.out.println("manaccia");
                                    System.err.println(e);
                                    msg.append(e.getMessage());
                                }
                            } else {
                                // contenitore chiuso
                                msg.append("Ops l'oggetto : ").append(container.getName())
                                        .append(" è chiuso. Dovresti prima aprirlo. (Usa il comando APRI nome oggetto");
                            }
                        }
                    }
                    if (!listContainer.isEmpty()) {
                        if(!trovato){
                            msg.append("L'oggetto ").append(invObj.getName()).append(" non è nell'inventario");
                        }
                    }else{
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
