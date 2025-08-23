/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.control;

/**
 *
 * @author giova
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.tutta.colpa.del.caffe.game.entity.*;
import it.tutta.colpa.del.caffe.game.entity.ContainerItem;
import it.tutta.colpa.del.caffe.game.exception.InventoryException;
import it.tutta.colpa.del.caffe.game.exception.ServerCommunicationException;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.GameUtils;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;
import static it.tutta.colpa.del.caffe.game.utility.RequestType.ITEMS;
import static it.tutta.colpa.del.caffe.game.utility.RequestType.UPDATED_LOOK;

/**
 * Observer che gestisce il comando {@code PICK_UP} nel gioco.
 * <p>
 * Questa classe implementa la logica necessaria per consentire al giocatore di
 * raccogliere oggetti da una stanza o da contenitori, gestendo anche i casi in
 * cui:
 * <ul>
 * <li>l'oggetto non è specificato</li>
 * <li>l'oggetto non è presente nella stanza o nell'inventario</li>
 * <li>l'oggetto si trova dentro un contenitore (aperto o chiuso)</li>
 * <li>l'oggetto non è raccoglibile</li>
 * <li>il server deve aggiornare la descrizione della stanza a seguito della
 * raccolta</li>
 * </ul>
 *
 * In caso di problemi, possono essere sollevate eccezioni come
 * {@link ServerCommunicationException} o {@link InventoryException}.
 *
 * @author giova
 */
public class PickUpObserver implements GameObserver {

    /**
     * Aggiorna lo stato del gioco in base al comando {@code PICK_UP}.
     * <p>
     * Questo metodo tenta di raccogliere l'oggetto specificato dal giocatore:
     * <ul>
     * <li>se l'oggetto è nella stanza e raccoglibile, lo aggiunge
     * all'inventario</li>
     * <li>se l'oggetto è contenuto in un contenitore, ne gestisce l'apertura e
     * il trasferimento</li>
     * <li>se l'oggetto è già nell'inventario, viene notificato al
     * giocatore</li>
     * <li>se necessario, aggiorna la descrizione della stanza contattando il
     * server</li>
     * </ul>
     *
     * @param description stato corrente del gioco, incluse stanze, inventario e
     * oggetti
     * @param parserOutput output del parser contenente comando, oggetto e
     * parametri
     * @return un messaggio testuale per il giocatore che descrive l'esito
     * dell'azione
     * @throws ServerCommunicationException se la comunicazione col server
     * fallisce
     */
    @Override
    public String update(GameDescription description, ParserOutput parserOutput) throws ServerCommunicationException {
        StringBuilder msg = new StringBuilder();
        if (parserOutput.getCommand().getType() == CommandType.PICK_UP) {
            ServerInterface server;
            try {
                server = new ServerInterface("localhost", 49152);
            } catch (ServerCommunicationException ex) {
                server = null;
            }

            GeneralItem obj = parserOutput.getObject();
            boolean conteiner = false;
            if (obj == null) {
                msg.append("Non hai specificato l'oggetto da raccogliere. (scrivi 'raccogli nome oggetto')");
                return msg.toString();

            }
            boolean c = findObjectInventory(description, server, msg, obj);
            if (!description.getCurrentRoom().getObjects().isEmpty() || c) {
                //controllo se è nella stazna

                GeneralItem isobjRoom = description.getCurrentRoom()
                        .getObjects()
                        .keySet()
                        .stream()
                        .filter(o -> o.getName().equals(parserOutput.getObject().getName()))
                        .findFirst()
                        .orElse(null);
                //controllo se è nell'inventario 
                if (isobjRoom == null && c == false) {
                    // msg.append("oggetto contenit\n");
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
                        if (isobjRoom instanceof ContainerItem isobjRoomC) {
                            if (conteiner && isobjRoom.getId() != 15) {
                                msg.append("l'oggetto non trovato. C'è l'oggetto: ").append(isobjRoomC.getName())
                                        .append("chissà se al suo intero c'è qualcosa. (Usa il comando prendi nome oggetto");
                                // mentre se l'oggetto non può essere raccolto ma può essere aperto  
                            } else {
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
                                                description.getCurrentRoom().setLook((String) server.requestToServer(UPDATED_LOOK, 2));
                                            } else {
                                                throw new ServerCommunicationException("connessione al server fallita");
                                            }
                                        } catch (InventoryException e) {
                                            msg.append(" Non puoi aggiungere ")
                                                    .append(objCont.getName())
                                                    .append(" all'inventario: ").append(e.getMessage());
                                        } catch (IllegalArgumentException e) {
                                            msg.append(e.getMessage());
                                        }
                                    } else {
                                        msg.append(isobjRoomC.getName()).append("non ha l'oggetto cercato");
                                    }
                                } else { // contenitore chiuso
                                    msg.append("Ops l'oggetto : ").append(isobjRoomC.getName())
                                            .append(" è chiuso. Dovresti prima aprirlo. (Usa il comando APRI nome oggetto");

                                }
                            }
                        }
                    } else {
                        msg.append("L'oggetto ").append(obj.getName()).append(" non è nella stanza o nell'inventario");
                    }
                } else if (isobjRoom == null && c != false) {
                    // msg.append("invent");
                    List<GeneralItem> listContainer = new ArrayList<>();
                    if (!description.getInventory().getInventory().containsKey(obj)) {
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
                            ContainerItem container = null;
                            boolean isOpen = false;

                            for (GeneralItem objCont : listContainer) {
                                container = (ContainerItem) objCont;
                                if (container.isOpen()) {

                                    if (container.containsObject(obj)) {
                                        isOpen = true;
                                        break; // esco dal ciclo se ho trovato l'oggetto\  
                                    }
                                }
                            }
                            if (!isOpen) {
                                for (GeneralItem co : listContainer) {
                                    ContainerItem con = (ContainerItem) co;
                                    if (!con.isOpen()) {
                                        msg.append("\nOps l'oggetto : ").append(co.getName())
                                                .append(" è chiuso.");
                                    }
                                }
                                msg.append("Prova ad aprire un oggetto, potrebbero contentere").append(obj.getName()).
                                        append("Usa il comando APRI nome oggetto");
                            }
                            if (container == null) {
                                msg.append(" non c'è l'oggetto nell'inventario");
                            } else if (container.isOpen()) {
                                try {
                                    Map<GeneralItem, Integer> pippo = container.getObject(obj);
                                    if (pippo != null && !pippo.isEmpty()) {// se il container ha l'oggetto indicato
                                        GeneralItem objFinde = pippo.keySet().iterator().next();
                                        int quantity = pippo.get(objFinde);
                                        description.getInventory().add(objFinde, quantity);
                                        msg.append(" ").append(quantity).append(" x ").append(objFinde.getName());
                                        container.remove(objFinde, quantity);
                                    }

                                } catch (InventoryException e) {
                                    msg.append(" Non puoi aggiungere l'oggetto all'inventario. ").append(e.getMessage());
                                } catch (IllegalArgumentException e) {
                                    msg.append(e.getMessage());
                                }
                            }
                        } else {
                            msg.append("L'oggetto ").append(obj.getName()).append(" non è nell'inventario");
                        }
                    } else {
                        msg.append("L'oggetto inidcato è già nell'inventario");
                    }
                } else if (isobjRoom != null && c == false) {// oggetto nella stanza
                    //msg.append("nella stanza");
                    if (parserOutput.getObject().isPickupable()) {
                        Map<GeneralItem, Integer> objRoom = description.getCurrentRoom().getObjects();
                        if (isobjRoom.isVisibile()) {
                            int quantity = objRoom.get(isobjRoom);
                            try {

                                description.getInventory().add(isobjRoom, quantity);
                                msg.append(" ").append(quantity).append(" x ").append(isobjRoom.getName());
                                objRoom.remove(isobjRoom, quantity);
                                description.getCurrentRoom().setObjects(objRoom);
                                // oggetto trovato nella stanza quindi è stato raccolto e aggiorno la descrizione della stanza
                                try {
                                    int roomId = description.getCurrentRoom().getId();
                                    if (server != null) {
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
                                    } else {
                                        throw new ServerCommunicationException("connessione al server fallita");
                                    }
                                } catch (ServerCommunicationException | NullPointerException e) {
                                    msg.append(" Errore nella comunicazione col server: ").append(e.getMessage());
                                }
                            } catch (InventoryException e) {
                                msg.append(" Non puoi aggiungere ").append(isobjRoom.getName()).append(" all'inventario: ").append(e.getMessage());
                            } catch (IllegalArgumentException e) {
                                msg.append(e.getMessage());
                            }
                        } else {
                            msg.append(" non c'è: ").append(isobjRoom.getName()).append(" nella stanza L'urgenza da alla testa.");
                        }
                    } else {
                        msg.append("l'oggetto ").append(parserOutput.getObject().getName()).append(" non può essere raccolto");
                    }

                }

            } else {
                msg.append("Lurgenza da alla testa, non c'è nessun l'oggetto").append(obj.getName());
            }
        }
        return msg.toString();
    }

    private boolean findObjectInventory(GameDescription description, ServerInterface server, StringBuilder msg, GeneralItem obj) {
        boolean c = false;
        try {
            if (server == null) {
                throw new ServerCommunicationException("connessione al server fallita");
            }
            List<GeneralItem> list = server.requestToServer(ITEMS);
            for (GeneralItem item : list) {
                if (item.getId() == obj.getId() && description.getInventory().getInventory().containsKey(item)) {
                    c = true;
                    break;
                } else {
                    if (item instanceof ContainerItem container) {
                        System.out.println(container.getName() + container.containsObject(obj));

                        if (description.getInventory().contains(container)) {
                            ContainerItem cont = (ContainerItem) GameUtils.getObjectFromInventory(description.getInventory(), container.getId());
                            if (cont.containsObject(obj)) {
                                c = true;
                                break;

                            }

                        }
                    }
                }
            }
        } catch (ServerCommunicationException | NullPointerException e) {
            msg.append(" Errore nella comunicazione col server: ").append(e.getMessage());
        }
        return c;
    }

}
