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
import it.tutta.colpa.del.caffe.adventure.exception.InventoryException;

/**
 * shift + alt + f
 * 
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
                    boolean magicCard = description.getInventory().getQuantity(14) > 0;
                    if (!magicCard) {
                        boolean isNotbuild = description.getInventory().getQuantity(13) > 0; // carta igienica
                        boolean isNotbuild1 = description.getInventory().getQuantity(6) > 0; // scheda madre
                        if (isNotbuild && isNotbuild1) {
                            msg.append(
                                    "Devi creare la carta prima di usarla! ( usa il comando crea per crare la carta magica)");
                            interact = true;
                        } else if (!isNotbuild && isNotbuild1) {
                            msg.append(
                                    "Non hai l'oggetto carta igienica nell'inventario. Quindi non hai la carta magica.");
                            interact = true;
                        } else if (!isNotbuild1 && isNotbuild) {
                            msg.append(
                                    "Non hai l'oggetto scheda madre nell'inventario. Quindi non hai la carta magica.");
                            interact = true;
                        }
                    } else if (magicCard && parserOutput.getObject().getUtilizzi() > 0) {
                        // se voglio utilizzare la carta su una qualsiasi porta (compreso quelle che
                        // erno chiuse ma che sono già aperte) aperta
                        if (description.getCurrentRoom().isOpen()) {
                            msg.append("La porta è già aperta");
                            interact = true;
                        } else {
                            description.getCurrentRoom().setOpen(true);
                            parserOutput.getObject().setUtilizzi(parserOutput.getObject().getUtilizzi() - 1);
                            msg.append("Hai usato la carta magica per aprire la porta. Ora puoi entrare nella stanza.");
                            interact = true;
                        }
                    } else if (magicCard && parserOutput.getObject().getUtilizzi() <= 0
                            && !description.getCurrentRoom().isOpen()) {
                        msg.append("sorry but you can not use the magic card because you have finished the uses");
                        interact = true;
                    }

                } else if (parserOutput.getObject().getId() == 2) { // il caffè è l'oggetto 2
                    boolean isInRoom = description.getCurrentRoom().getObject(2) != null;
                    if (isInRoom && description.getInventory().getQuantity(8) > 0) { // la moneta è l'oggetto 8
                        if (description.getInventory().getQuantity(8) == 0) {
                            msg.append("You can not take a coffee, because you are poor"); // stanza correta soldi 0
                            interact = true;
                        } else {
                            msg.append("You can take a coffee");
                            try {
                                description.getInventory().remove(new AdvObject(8), 1);
                            } catch (InventoryException e) {
                                msg.append("Errore nell'aggiornamento dell'inventario");
                            }
                            interact = true;
                        }
                    } else if (isInRoom && description.getInventory().getQuantity(8) == 0) { // moneta
                        msg.append("you can not take a coffee, because you have not money into your inventory");
                        interact = true;
                    } else if (!isInRoom && description.getInventory().getQuantity(8) > 0) { // moneta
                        msg.append("non fare lo spendaccione!"); // la stanza è errata ma ha i soldi
                        interact = true;
                    }

                } else if (parserOutput.getObject().getId() == 1) { // mappa
                    boolean isVisibleMap = description.getCurrentRoom().getObject(1) != null;
                    if (isVisibleMap && !parserOutput.getObject().isVisibile()) {
                        msg.append("there is not a map in this room"); // perche la mappa e visibile solo se passa
                                                                       // l'indovinello altrimento non è nota la sua
                                                                       // esistenza.
                    } else if (isVisibleMap && description.getInventory().getQuantity(1) == 0) {
                        msg.append("You must take the map before!");
                    } else if (!isVisibleMap && description.getInventory().getQuantity(1) > 0) {
                        interact = true;// la mappa può essere aperta ovunque
                    }
                } else if (parserOutput.getObject().getId() == 9) { // chiave
                    int id = description.getCurrentRoom().getId();
                    boolean takeKey = (id == 3 || id == 4 || id == 5 || id == 6 || id == 14 || id == 17 || id == 20
                            || id == 25);
                    if (takeKey && description.getInventory().getQuantity(9) > 0
                            && parserOutput.getObject().isVisibile()) {
                        msg.append("Puoi usare la chiave per sbloccare l'ascensore");
                        interact = true;
                    } else {
                        msg.append("Non esiste nessuna chiave. L'urgenza ti sta dando alla testa");// questo perchè la
                                                                                                   // chiave è visible
                                                                                                   // se
                                                                                                   // passi
                                                                                                   // l'indovinello
                    }
                } else {
                    boolean isInInventory = description.getInventory()
                            .getQuantity(parserOutput.getObject().getId()) > 0;
                    if (isInInventory && !parserOutput.getObject().isUses()) {
                        msg.append("Non puoi utilizzare l'oggetto");
                        interact = true;
                    }
                }
            }
            if (!interact) {
                msg.append("L'oggetto indicato non è nell'inventario");
            }
        }
        return msg.toString();
    }
}