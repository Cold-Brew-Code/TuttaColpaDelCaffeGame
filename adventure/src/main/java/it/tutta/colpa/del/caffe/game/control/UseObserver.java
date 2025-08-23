/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.control;

import java.util.List;

import it.tutta.colpa.del.caffe.game.entity.*;
import it.tutta.colpa.del.caffe.game.entity.ContainerItem;
import it.tutta.colpa.del.caffe.game.exception.ServerCommunicationException;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.GameUtils;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;
import it.tutta.colpa.del.caffe.game.utility.RequestType;

/**
 * Observer che gestisce il comando {@code USE} nel gioco.
 * <p>
 * Questa classe implementa la logica per l'utilizzo di oggetti specifici da
 * parte del giocatore. Gli oggetti possono avere effetti diversi (aprire porte,
 * comprare caffè, sbloccare ascensori, mostrare la mappa, ecc.) a seconda delle
 * condizioni della stanza e dell'inventario.
 * <br>
 * Casi gestiti:
 * <ul>
 * <li><b>Oggetto non specificato</b>: viene mostrato un messaggio di
 * errore.</li>
 * <li><b>Carta magica (id 14)</b>: può aprire porte chiuse, ma richiede la
 * creazione tramite chip e tessera smagnetizzata. Ha un numero limitato di
 * usi.</li>
 * <li><b>Caffè (id 16)</b>: può essere acquistato solo se il giocatore possiede
 * una moneta e si trova nella stanza corretta. Il server fornisce l’oggetto
 * dopo l'acquisto.</li>
 * <li><b>Mappa (id 1)</b>: può essere aperta ovunque se già raccolta,
 * altrimenti è visibile solo dopo aver risolto un enigma.</li>
 * <li><b>Chiave (id 9)</b>: utilizzabile solo in determinate stanze per
 * sbloccare l'ascensore. Una volta usata viene rimossa dall’inventario.</li>
 * <li><b>Altri oggetti</b>: se non utilizzabili, viene restituito un messaggio
 * di errore.</li>
 * </ul>
 *
 * @author giova
 */
public class UseObserver implements GameObserver {

    /**
     * Gestisce l'aggiornamento dello stato del gioco in base al comando
     * {@code USE}.
     * <p>
     * In base all’oggetto specificato dal giocatore e al contesto (inventario,
     * stanza corrente, stato della mappa), vengono determinati gli effetti.
     * Alcuni oggetti possono interagire con il server per recuperare nuovi
     * oggetti (es. acquisto del caffè).
     *
     * @param description  stato corrente del gioco (stanze, inventario, mappa,
     *                     ecc.)
     * @param parserOutput output del parser, contenente il comando e l’oggetto
     *                     scelto dal giocatore
     * @return messaggio testuale che descrive l’esito dell’uso dell’oggetto
     * @throws ServerCommunicationException se fallisce la comunicazione col
     *                                      server durante l'uso di un oggetto
     */
    @Override
    public String update(GameDescription description, ParserOutput parserOutput) throws ServerCommunicationException {
        StringBuilder msg = new StringBuilder();
        Object obj = parserOutput.getObject();
        if (parserOutput.getCommand().getType() == CommandType.USE) {
            ServerInterface server;
            try {
                server = new ServerInterface("localhost", 49152);
            } catch (ServerCommunicationException ex) {
                server = null;
            }
            GeneralItem objInv;
            GeneralItem objInv1;
            if (obj == null) {
                msg.append("Non hai specificato l'oggetto da usare. (scrivi 'usa nome oggetto')");
                return msg.toString();
            } else if (GameUtils.getObjectFromInventory(description.getInventory(), 14)==null) {
                msg.append("Non hai questo oggetto nell'inventario...");
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
                                ContainerItem scatola = (ContainerItem) GameUtils.getObjectFromInventory(description.getInventory(), 11); // controllo se ha la scatola nell'inventario

                                if (scatola != null && scatola.isOpen()) {
                                    msg.append("Non hai l'oggetto ").append(objInv1.getName())
                                            .append(" nell'inventario. Quindi non hai la carta magica. ")
                                            .append("Potrebbe essere all'interno dell'oggetto: ").append(scatola.getName());
                                } else {
                                    msg.append("Non hai l'oggetto ").append(objInv1.getName())
                                            .append(" nell'inventario. Quindi non hai la carta magica.");
                                }
                            }

                        } else if (magicCard) {
                            Item card = (Item) GameUtils.getObjectFromInventory(description.getInventory(), parserOutput.getObject().getId());
                            if (card.getUses() > 0) {
                                //se voglio utilizzare la carta su una qualsiasi porta (compreso quelle che erno chiuse ma che sono già aperte) aperta
                                List<Room> listR = description.getGameMap().getStanzeRaggiungibiliDallaStanzaCorrente();
                                Room room = listR.stream().filter(r -> r.isDeniedEntry() == false).findFirst().orElse(null);
                                if (room == null) {
                                    msg.append("La porta è già aperta");
                                } else {
                                    room.setDeniedEntry(true);
                                    card.setUses(card.getUses() - 1);
                                    if (room.isVisible() == false) {
                                        room.setVisible(true);
                                        msg.append("OMG!!! Hai trovato la leggenda ").append(room.getName()).append(" ora si che puoi soddisfare il tuo bisogno");
                                    } else {
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
                        if (isInRoom && GameUtils.getObjectFromInventory(description.getInventory(), 8) != null) {
                            objInv = GameUtils.getObjectFromInventory(description.getInventory(), 8);
                            msg.append(" caffè");
                            description.getInventory().remove(objInv); // c'è l'oggetto e tolgo una moneta.
                            try {
                                if (server != null) {
                                    GeneralItem c = server.requestToServer(RequestType.ITEM, 2);
                                    if (c instanceof Item i) {
                                        description.getInventory().add(i, 1);
                                        msg.append(" Hai preso l'oggetto: ").append(i.getName());
                                        System.out.println(i.getName());
                                    } else {
                                        msg.append("errore");
                                    }

                                } else {
                                    throw new ServerCommunicationException("connessione al server fallita");
                                }
                            } catch (ServerCommunicationException | NullPointerException e) {
                                msg.append(" Errore nella comunicazione col server: ").append(e.getMessage());
                            }

                        } else if (isInRoom && GameUtils.getObjectFromInventory(description.getInventory(), 8) == null) {
                            msg.append("non puoi prendere il caffe, non hai monete nell' inventario");

                        } else if (isInRoom == false && GameUtils.getObjectFromInventory(description.getInventory(), 8) != null) {
                            msg.append("non fare lo spendaccione!"); // la stanza è errata ma ha i soldi

                        }
                    }   //mappa 
                    case 1 -> {
                        boolean isVisibleMap = description.getCurrentRoom().getObject(1) != null;
                        if (isVisibleMap && parserOutput.getObject().isVisibile() == false) {
                            msg.append("Non c'è nessuna mappa qui"); // perche la mappa e visibile solo se passa l'indovinello altrimenti non è nota la sua esistenza.
                        } else if (isVisibleMap && GameUtils.getObjectFromInventory(description.getInventory(), 1) == null) {
                            msg.append("Hai già preso la mappa!");
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
                        boolean keyVisible = parserOutput.getObject().isVisibile();
                        if (!takeKey && GameUtils.getObjectFromInventory(description.getInventory(), 9) != null) {
                            msg.append("non c'è l'ascensore qui. Puoi usare questa chiave solo per sbloccare l'asscensore");

                        } else if (takeKey && GameUtils.getObjectFromInventory(description.getInventory(), 9) != null) {
                            msg.append("puoi usare la chiave per sbloccare l'ascensore");
                            description.getInventory().remove(parserOutput.getObject());// sblocco l'ascensore quindi la chiave è stata utilizzata 
                        } else if (keyVisible == false) {
                            msg.append("non esiste nessuna chiave l'urgenza ti sta dando alla testa");// questo perchè la chiave è visible se passi l'indovinello
                        } // se la chiave è visibile ma non ha 
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
