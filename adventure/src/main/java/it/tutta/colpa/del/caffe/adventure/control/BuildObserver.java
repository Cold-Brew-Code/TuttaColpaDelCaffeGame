/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.control;

import it.tutta.colpa.del.caffe.game.control.ServerInterface;
import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.entity.GameObserver;
import it.tutta.colpa.del.caffe.game.entity.GeneralItem;
import it.tutta.colpa.del.caffe.game.entity.IteamCombinable;
import it.tutta.colpa.del.caffe.game.entity.Item;
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
        System.out.println("sono in build");
        StringBuilder msg = new StringBuilder();
        if (parserOutput.getCommand().getType() == CommandType.MERGE) {
            ServerInterface server;
            System.out.println("sono in build2");
            try {
                server = new ServerInterface("localhost", 49152);
            } catch (ServerCommunicationException ex) {
                server = null;
            }
            // controllo se l'oggetto è stato specificato
            Object obj = parserOutput.getObject();
            boolean isRoomCurrent1 = false;
            boolean isRoomCurrent2 = false;
            if (obj == null) {
                msg.append("Non hai specificato gli oggetto da combinare. (scrivi 'combina nome oggetto <nome oggetto>')");
                return msg.toString();
            } else if (obj instanceof IteamCombinable pippo) {
                System.out.println(" debug\n" + (pippo.getName().equalsIgnoreCase("tessera magica")));
                if (pippo.getName().equalsIgnoreCase("tessera magica")) {
                    System.out.println("pippoo ");
                    if (GameUtils.getObjectFromInventory(description.getInventory(), 14) != null) {
                        msg.append("non puoi avere 200 mila tessere magiche, la vita sarebbe troppo bella. Hai già la tessera magica nell'inventario");
                    } else {
                        boolean objInv1 = GameUtils.getObjectFromInventory(description.getInventory(), 12) != null; // carta
                        boolean objInv2 = GameUtils.getObjectFromInventory(description.getInventory(), 6) != null; //scheda madre
                        //Sint IdRoomCurrent = description.getCurrentRoom().getId();
                        if (objInv1 && objInv2) {
                            try {
                                if (server != null) {
                                    Item cartaM = server.requestToServer(RequestType.ITEM, 14);
                                    if (cartaM instanceof IteamCombinable cartaMagica) {

                                        System.out.println("ho fatto richiesta");
                                        GeneralItem obj1 = GameUtils.getObjectFromInventory(description.getInventory(), 12);
                                        GeneralItem obj2 = GameUtils.getObjectFromInventory(description.getInventory(), 6);
                                        //System.out.println("Tipo di oggetto inatteso: " + obj1.getClass()+ obj2.getClass());
                                        Item objInvR1 = (Item) obj1;
                                        Item objInvR2 = (Item) obj2;

                                        description.getInventory().remove(objInvR1);
                                        description.getInventory().remove(objInvR2);

                                        description.getInventory().add(cartaMagica, 1);
                                    } else {
                                        msg.append("Non puoi fare tutte le magie che vuoi.");//L'oggetto ricevuto non è combinabile
                                    }

                                } else {
                                    throw new ServerCommunicationException("connessione al server fallita");
                                }
                            } catch (ServerCommunicationException ex) {
                                msg.append(ex.getMessage());

                            }

                            msg.append("Fabbricatum Objectum!");
                        } else if (objInv1 == false && objInv2 == false) {
                            msg.append("non hai tutti gli oggetti a disposizione");
                        } else if (objInv1 == false && objInv2) {
                            isRoomCurrent1 = description.getCurrentRoom().getId() == 19;
                            if (isRoomCurrent1) {
                                msg.append("devi prima raccogliere l'oggetto ")
                                        .append(description.getCurrentRoom()
                                                .getObject(12).getName()).append("prima di combinare i due oggetti");
                            } else {
                                msg.append("non hai tutti gli oggetti a disposizione");
                            }
                        } else if (!objInv2 && objInv1) {
                            isRoomCurrent2 = description.getCurrentRoom().getId() == 30;
                            if (isRoomCurrent2) {
                                msg.append("devi prima raccogliere l'oggetto ").append(description.getCurrentRoom().getObject(6).getName()).append("prima di combinare i due oggetti");
                            } else {
                                msg.append("non hai tutti gli oggetti a disposizione");
                            }
                        }
                    }
                }
            } else {// se non è un oggetto allora da errore ( devo considerare il caso in cui abbia indicato i due oggetti che possono essere combinati che danno la magia
                msg.append("non puoi fare ste magie");
            }
        }
        return msg.toString();
    }
}
