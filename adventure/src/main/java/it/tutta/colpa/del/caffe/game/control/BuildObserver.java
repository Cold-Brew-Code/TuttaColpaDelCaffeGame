/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.control;

import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.entity.GameObserver;
import it.tutta.colpa.del.caffe.game.entity.GeneralItem;
import it.tutta.colpa.del.caffe.game.entity.CombinableItem;
import it.tutta.colpa.del.caffe.game.entity.Item;
import it.tutta.colpa.del.caffe.game.entity.ContainerItem;
import it.tutta.colpa.del.caffe.game.exception.ServerCommunicationException;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.GameUtils;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;
import it.tutta.colpa.del.caffe.game.utility.RequestType;

/**
 * @author giova
 */
public class BuildObserver implements GameObserver {

    /**
     * Gestisce il comando MERGE, consentendo al giocatore di combinare due
     * oggetti dell'inventario per ottenere un nuovo oggetto.
     *
     * @param description stato corrente del gioco, inclusa stanza attuale e
     * inventario
     * @param parserOutput risultato del parsing del comando, contenente il tipo
     * di comando e l'oggetto
     * @return messaggio di feedback da mostrare al giocatore
     * @throws ServerCommunicationException se la connessione al server fallisce
     */
    @Override
    public String update(GameDescription description, ParserOutput parserOutput) throws ServerCommunicationException {
        StringBuilder msg = new StringBuilder();

        if (parserOutput.getCommand().getType() == CommandType.MERGE) {

            ServerInterface server;
            try {
                server = new ServerInterface("localhost", 49152);
            } catch (ServerCommunicationException ex) {
                server = null;
            }

            // controllo se l'oggetto è stato specificato
            Object obj = parserOutput.getObject();
            if (obj == null) {
                msg.append("Non hai specificato gli oggetti da combinare. (scrivi 'combina nome oggetto <nome oggetto>')");
                return msg.toString();
            }

            if (obj instanceof CombinableItem item && item.getName().equalsIgnoreCase("tessera magica")) {

                // Controllo se già possiede la tessera magica
                if (GameUtils.getObjectFromInventory(description.getInventory(), 14) != null) {
                    msg.append("Non puoi avere 200 mila tessere magiche, la vita sarebbe troppo bella. Hai già la tessera magica nell'inventario");
                    return msg.toString();
                }

                boolean hasCarta = GameUtils.getObjectFromInventory(description.getInventory(), 12) != null;
                boolean hasSchedaMadre = GameUtils.getObjectFromInventory(description.getInventory(), 6) != null;

                if (hasCarta && hasSchedaMadre) {
                    try {
                        if (server != null) {
                            Item cartaM = server.requestToServer(RequestType.ITEM, 14);
                            if (cartaM instanceof CombinableItem cartaMagica) {
                                System.out.println("ho fatto richiesta");

                                GeneralItem obj1 = GameUtils.getObjectFromInventory(description.getInventory(), 12);
                                GeneralItem obj2 = GameUtils.getObjectFromInventory(description.getInventory(), 6);

                                description.getInventory().remove((Item) obj1);
                                description.getInventory().remove((Item) obj2);
                                description.getInventory().add(cartaMagica, 1);

                            } else {
                                msg.append("Non puoi fare tutte le magie che vuoi."); // oggetto ricevuto non combinabile
                                return msg.toString();
                            }
                        } else {
                            throw new ServerCommunicationException("Connessione al server fallita");
                        }
                    } catch (ServerCommunicationException ex) {
                        msg.append(ex.getMessage());
                        return msg.toString();
                    }

                    msg.append("Fabbricatum Objectum!");
                } else if (!hasCarta && !hasSchedaMadre) {
                    msg.append("Non hai tutti gli oggetti a disposizione");
                } else if (!hasCarta && hasSchedaMadre) {
                    if (description.getCurrentRoom().getId() == 19) {
                        GeneralItem curr = description.getCurrentRoom().getObject(11);
                        if (curr == null) {
                            // L'oggetto NON è nella stanza: quindi controlla se è nell'inventario
                            GeneralItem invObj = GameUtils.getObjectFromInventory(description.getInventory(), 11);
                            if (invObj != null) {
                                // L'oggetto è nell'inventario
                                ContainerItem curr1 = (ContainerItem) invObj;
                                System.out.println(invObj.getName());
                                if (!curr1.isOpen()) {
                                    msg.append("Non hai tutti gli oggetti a disposizione");
                                } else if (curr1.containsObjectById(12) != null) {
                                    msg.append("Prendi prima l'oggetto dal contenitore");
                                } else {
                                    msg.append("Non hai tutti gli oggetti a disposizione");
                                }
                            } else {
                                msg.append("Non hai l'oggetto né in stanza né in inventario");
                            }
                        } else {
                            // non ho ancora raccolto la scatola
                            msg.append("Non puoi creare l'oggetto, non hai tutti gli oggetti a disposizione S");
                        }

                    } else {
                        msg.append("Non hai tutti gli oggetti a disposizione");
                    }
                } else if (!hasSchedaMadre && hasCarta) {
                    if (description.getCurrentRoom().getId() == 30) {
                        msg.append("Devi prima raccogliere l'oggetto ")
                                .append(description.getCurrentRoom().getObject(6).getName())
                                .append(" prima di combinare i due oggetti");
                    } else {
                        msg.append("Non hai tutti gli oggetti a disposizione");
                    }
                }

            } else {
                // Non è un oggetto combinabile
                msg.append("Non puoi fare ste magie");
            }
        }

        return msg.toString();
    }
}
