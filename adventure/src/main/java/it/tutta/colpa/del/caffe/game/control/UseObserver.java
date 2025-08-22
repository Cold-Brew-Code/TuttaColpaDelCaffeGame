/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.control;

import java.util.List;

import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.entity.GameObserver;
import it.tutta.colpa.del.caffe.game.entity.GeneralItem;
import it.tutta.colpa.del.caffe.game.entity.Inventory;
import it.tutta.colpa.del.caffe.game.entity.Item;
import it.tutta.colpa.del.caffe.game.entity.ItemContainer;
import it.tutta.colpa.del.caffe.game.entity.Room;
import it.tutta.colpa.del.caffe.game.exception.ServerCommunicationException;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.GameUtils;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;
import it.tutta.colpa.del.caffe.game.utility.RequestType;

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
        System.out.println("sono in use cazp");
        if (parserOutput.getCommand().getType() == CommandType.USE) {
            ServerInterface server;
            try {
                server = new ServerInterface("localhost",49152 );
            } catch (ServerCommunicationException ex) {
                server= null;
            }
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
                        else if (magicCard) {
                            Item card= (Item) parserOutput.getObject();
                            if(card.getUses()>0){
                                //se voglio utilizzare la carta su una qualsiasi porta (compreso quelle che erno chiuse ma che sono già aperte) aperta
                                List<Room> listR= description.getGameMap().getStanzeRaggiungibiliDallaStanzaCorrente();
                                Room room= listR.stream().filter(r-> r.isDeniedEntry()==false).findFirst().orElse(null);
                                if (room== null) {
                                    msg.append("La porta è già aperta");
                                }else{
                                    room.setDeniedEntry(true);
                                    card.setUses(card.getUses() - 1);
                                    if(room.isVisible()==false){
                                        room.setVisible(true);
                                        msg.append("OMG!!! Hai trovato la leggenda ").append(room.getName()).append(" ora si che puoi soddisfare il tuo bisogno");
                                    }else{
                                        msg.append("Hai usato la carta magica per aprire la porta. Ora puoi entrare nella stanza: ").append(room.getName());
                                    }
                                }
                            } else if (magicCard && card.getUses() <= 0 && description.getCurrentRoom().isDeniedEntry()) {
                                msg.append("Mi dipsice, ma non puoi più usare la carta magina. Hai finito gli utilizzi!");
                                
                            }      
                        }
                    }
                    case 16 -> {
                        // il caffè è l'oggetto 16
                        boolean isInRoom = description.getCurrentRoom().getObject(16) != null;
                        System.out.println("isInRoom: " + isInRoom);
                        if (isInRoom && GameUtils.getObjectFromInventory(description.getInventory(), 8) != null) {
                            objInv = GameUtils.getObjectFromInventory(description.getInventory(), 8);
                            msg.append(" caffè");
                            description.getInventory().remove(objInv); // c'è l'oggetto e tolgo una moneta.
                            try {
                                if(server!= null){
                                    GeneralItem c= server.requestToServer(RequestType.ITEM, 2);
                                    if(c instanceof Item i){
                                        description.getInventory().add(i, 1);
                                        msg.append(" Hai preso l'oggetto: ").append(i.getName());
                                        System.out.println(i.getName());
                                    }else{
                                        msg.append("errore");
                                    }
                                    
                                }else{
                                    throw new ServerCommunicationException ("connessione al server fallita");
                                }
                            } catch (ServerCommunicationException | NullPointerException e) {
                                msg.append(" Errore nella comunicazione col server: ").append(e.getMessage());
                            }
                            
                            
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
                            msg.append(description.getGameMap().stampaDirezioniPerStanza());
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
