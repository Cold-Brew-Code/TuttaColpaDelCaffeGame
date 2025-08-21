/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.control;

import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.entity.GameObserver;
import it.tutta.colpa.del.caffe.game.entity.Inventory;
import it.tutta.colpa.del.caffe.game.entity.ItemRead;
import it.tutta.colpa.del.caffe.game.entity.Room;
import it.tutta.colpa.del.caffe.game.exception.ItemException;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;

/**
 * Observer che gestisce il comando {@code READ} nel gioco.
 * <p>
 * Questa classe implementa la logica necessaria per consentire al giocatore di
 * leggere un oggetto di tipo {@link ItemRead}, se presente nell'inventario o
 * nella stanza corretta.
 * <br>
 * Gestisce i seguenti casi:
 * <ul>
 * <li>Oggetto non specificato dal giocatore → messaggio di errore.</li>
 * <li>Oggetto leggibile presente nell'inventario → viene letto (consumando un
 * uso).</li>
 * <li>Oggetto leggibile presente nella stanza ma non ancora raccolto →
 * suggerisce di raccoglierlo prima.</li>
 * <li>Oggetto leggibile letto in una stanza sbagliata → mostra un avviso e
 * consuma comunque un uso.</li>
 * <li>Oggetto non leggibile → messaggio di errore.</li>
 * </ul>
 * Alcuni oggetti leggibili hanno effetti speciali solo se letti nella stanza
 * corretta.
 *
 * @author giova
 */
public class ReadObserver implements GameObserver {

    /**
     * Aggiorna lo stato del gioco in base al comando {@code READ}.
     * <p>
     * Se l'oggetto è leggibile e si trova nell'inventario del giocatore, viene
     * letto. Se l'oggetto è nella stanza corretta ma non ancora raccolto, il
     * giocatore viene avvisato che deve prima raccoglierlo. Se il giocatore
     * prova a leggere in una stanza sbagliata, l'oggetto viene comunque
     * consumato parzialmente ma viene mostrato un messaggio di avvertimento.
     *
     * @param description stato corrente del gioco (stanze, inventario, ecc.)
     * @param parserOutput output del parser, contenente comando e oggetto
     * indicato dal giocatore
     * @return un messaggio testuale per il giocatore che descrive l'esito del
     * comando
     */
    @Override
    public String update(GameDescription description, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        // controllo se l'oggetto è stato specificato
        Object obj = parserOutput.getObject();

        if (parserOutput.getCommand().getType() == CommandType.READ) {
            if (obj == null) {
                msg.append("Non hai specificato gli oggetto da combinare. (scrivi 'combina nome oggetto nome oggetto')");
                return msg.toString();
            } else if (obj instanceof ItemRead pippo) {
                Room currRoom = description.getCurrentRoom();
                Inventory inventory = description.getInventory();
                boolean objInRoom = false;
                if (pippo.getId() == 3 && inventory.contains(pippo)) {// libro cc
                    if (currRoom.getId() == 10) {
                        try {// sto leggendo l'ooggetto nella stanza corretta
                            pippo.decreaseUses();
                            msg.append(pippo.getContent());
                        } catch (ItemException ex) {
                            msg.append(ex.getMessage());

                        }
                    } else {
                        // sta leggendo nella stanza sbagliata 
                        try {
                            pippo.decreaseUses();
                            msg.append("Ti sembra il momento di leggere ora?? Ricordati che gli oggetti più vengono usati più si consumano nel tempo...");
                            msg.append("Il contenuto è: ").append(pippo.getContent());
                        } catch (ItemException ex) {
                            msg.append(ex.getMessage());

                        }
                    }

                } else if (pippo.getId() == 3 && !inventory.contains(pippo) && currRoom.getId() == 7) {
                    objInRoom = true;
                    /* deve prima raccoglire l'oggetto e poi può leggerlo
                    altrimenti messaggio generale : oggetto non presente nell'inventario 
                    poiche non è nell'inventario ma non è nemmeno nella stanzto in cui può
                    raccoglire il bigliettino
                     */
                } else if (pippo.getId() == 5 && inventory.contains(pippo)) {
                    if (currRoom.getId() == 15) {
                        try {// sto leggendo l'ooggetto nella stanza corretta
                            pippo.decreaseUses();
                            msg.append(pippo.getContent());
                        } catch (ItemException ex) {
                            msg.append(ex.getMessage());

                        }
                    } else {
                        // sta leggendo nella stanza sbagliata 
                        try {
                            pippo.decreaseUses();
                            msg.append("Ti sembra il momento di leggere ora?? Ricordati che gli oggetti più vengono usati più si consumano nel tempo...");
                            msg.append("Il contenuto è: ").append(pippo.getContent());
                        } catch (ItemException ex) {
                            msg.append(ex.getMessage());

                        }
                    }

                } else if (pippo.getId() == 5 && !inventory.contains(pippo) && currRoom.getId() == 16) {
                    objInRoom = true;
                    /* devi prima raccoglire il bigliettino e poi puoi leggerlo, 
                    altrimenti messaggio generale : oggetto non presente nell'inventario  poiche non è nell'inventario ma non è nemmeno nella stanzto in cui può
                    raccoglire il bigliettino
                     */

                } else if (objInRoom == false) {// no oggetto nell'inventario  ne nella stanza nella quale può raccoglire
                    msg.append("cosa vuoi leggere il vuoto?? L'oggetto ").append(pippo.getName()).append(" non è nell'inventario");
                } else {
                    //si trova nella stanza in cui può raccoglire l'oggetto
                    msg.append("Devi prima ragliere il ").append(pippo.getName()).append(" e poi forse ( se riuscirai ) potrai leggerlo.").append("Usa il comando prendi nome oggetto");
                }
            } else {
                msg.append("vuoli leggere cosa?? L'oggetto indicato non può essere letto");
            }

        }
        return msg.toString();
    }
}
