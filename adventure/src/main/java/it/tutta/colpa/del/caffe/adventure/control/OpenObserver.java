
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.control;

import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.entity.GameObserver;
import it.tutta.colpa.del.caffe.game.entity.GeneralItem;
import it.tutta.colpa.del.caffe.game.entity.ItemContainer;
import it.tutta.colpa.del.caffe.game.exception.ServerCommunicationException;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.GameUtils;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;

/**
 * @author giovanni
 */
public class OpenObserver implements GameObserver {

    /**
     * @param description
     * @param parserOutput
     * @return
     */
    @Override
    public String update(GameDescription description, ParserOutput parserOutput) throws ServerCommunicationException {
        StringBuilder msg = new StringBuilder();
        Object obj = parserOutput.getObject();
        if (parserOutput.getCommand().getType() == CommandType.OPEN) {
            if (obj == null) {
                msg.append("Non hai specificato l'oggetto da aprire. (scrivi 'apri nome oggetto').");
                return msg.toString();
            } else if (description.getCurrentRoom().hasObject(((GeneralItem) obj).getId())) { //oggetto nella stanza da aprire
                if (obj instanceof ItemContainer c) {
                    // id della stanza in cui mi trovo
                    int currentRoomId = description.getCurrentRoom().getId();
                    boolean isOpen = c.isOpen();
                    if (!isOpen) {
                        if ((c.getId() == 11 || c.getId() == 7)) {
                            msg.append("Come pretendi di aprirlo se non lo prendi?? L'oggetto: ")
                                    .append(c.getName())
                                    .append(" è nella stanza, fai 'raccogli' per prenderlo.");
                        } else if (c.getId() == 15) { // armadietto
                        System.out.println("sono qui in prendi");
                            c.setOpen(true);
                            System.out.println(c.isOpen());
                            System.out.println("OPEN: armadietto aperto - hash: " + System.identityHashCode(c));

                            msg.append("Hai aperto: ").append(c.getName());
                            if (!c.getList().isEmpty()) {
                                msg.append(". ").append(c.getName()).append(" contiene:");
                                c.getList().forEach((next, quantity)
                                        -> msg.append(" ").append(quantity).append(" x ").append(next.getName())
                                );// mi stampo ogni oggetto con la sua quantità presente nel contenitore
                            }
                        }
                    } else {
                        msg.append("L'oggetto ").append(c.getName()).append(" è già aperto.");
                    }
                } else {
                    GeneralItem curr = (GeneralItem) obj;
                    msg.append("L'oggetto ")
                            .append(curr.getName())
                            .append(" non può essere aperto");
                }
            } else if (GameUtils.getObjectFromInventory(description.getInventory(), parserOutput.getObject().getId()) != null) {
                // se ho l'oggetto che può essere raccolto (scatola o borsellino) è nell'inventario
                GeneralItem invObj = GameUtils.getObjectFromInventory(description.getInventory(), parserOutput.getObject().getId());
                if (invObj.getId() == 11 || invObj.getId() == 7) {
                    ItemContainer c = (ItemContainer) invObj;
                    if (c.isOpen() == false) { // se non è aperto lo apro e stampo il suo contenuto
                     System.out.println("OPEN: scatola aperto - hash: " + System.identityHashCode(c));
                        c.setOpen(true);
                        msg.append("Hai aperto: ").append(c.getName());
                        if (!c.getList().isEmpty()) {
                            msg.append(". ").append(c.getName()).append(" contiene:");
                            c.getList().forEach((next, quantity) -> msg.append(" ").append(quantity).
                                    append(" x ").append(next.getName())
                            );
                        }
                    } else if (c.isOpen()) {
                        msg.append("L'oggetto ").append(c.getName()).append(" è già aperto");
                    }
                } // caso in cui l'oggetto è nell'inventario ma non è uno di quelli apribili
                else if (invObj != null) {
                    msg.append("L'oggetto indicato non è apribili");
                }
            } else {
                msg.append("L'oggetto indicato non è nei paraggi"); // non è né nella stanza, né nell'inventario
            }
        }
        return msg.toString();
    }
}