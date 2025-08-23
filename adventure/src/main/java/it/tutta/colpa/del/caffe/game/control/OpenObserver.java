
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.control;

import it.tutta.colpa.del.caffe.game.entity.ContainerItem;
import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.entity.GameObserver;
import it.tutta.colpa.del.caffe.game.entity.GeneralItem;
import it.tutta.colpa.del.caffe.game.exception.ServerCommunicationException;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.GameUtils;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;

/**
 * Gestisce il comando {@code OPEN} nel gioco.
 * <p>
 * L'osservatore controlla se l'oggetto da aprire si trova:
 * <ul>
 * <li>nella stanza corrente;</li>
 * <li>nell'inventario del giocatore;</li>
 * <li>oppure se non è presente nei paraggi.</li>
 * </ul>
 * Se l'oggetto è un {@link ContainerItem}, può essere aperto per rivelarne il
 * contenuto, altrimenti viene segnalato come non apribile.
 * </p>
 *
 * @author giovanni
 */
public class OpenObserver implements GameObserver {

    /**
     * Elabora il comando {@code OPEN} in base all'oggetto specificato dal
     * giocatore.
     * <p>
     * Sono gestiti i seguenti casi:
     * <ul>
     * <li>Se non viene specificato un oggetto, viene restituito un messaggio di
     * errore.</li>
     * <li>Se l'oggetto si trova nella stanza e può essere aperto, viene aperto
     * e ne viene mostrato il contenuto.</li>
     * <li>Se l'oggetto si trova nell'inventario ed è apribile, viene aperto e
     * ne viene mostrato il contenuto.</li>
     * <li>Se l'oggetto non è apribile o non è presente né nella stanza né
     * nell'inventario, viene segnalato con un messaggio.</li>
     * </ul>
     * </p>
     *
     * @param description lo stato corrente del gioco, che include mappa, stanze
     * e inventario
     * @param parserOutput il risultato dell'analisi del comando inserito dal
     * giocatore
     * @return un messaggio testuale che descrive l'esito del comando
     * @throws ServerCommunicationException se si verificano problemi nella
     * comunicazione con il server
     */
    @Override
    public String update(GameDescription description, ParserOutput parserOutput) throws ServerCommunicationException {
        StringBuilder msg = new StringBuilder();
        Object obj = parserOutput.getObject();

        if (parserOutput.getCommand().getType() != CommandType.OPEN) {
            return msg.toString();
        }

        if (obj == null) {
            return "Non hai specificato l'oggetto da aprire. (scrivi 'apri nome oggetto').";
        }

        // Caso 1: oggetto nella stanza
        if (description.getCurrentRoom().hasObject(((GeneralItem) obj).getId())) {
            GeneralItem item = description.getCurrentRoom().getObject(((GeneralItem) obj).getId());

            if (item instanceof ContainerItem c) {
                if (!c.isOpen()) {
                    if (c.getId() == 11 || c.getId() == 7) {
                        return "Come pretendi di aprirlo se non lo prendi?? L'oggetto " + c.getName() + " è nella stanza, fai 'raccogli' per prenderlo.";
                    } else {
                        c.setOpen(true);
                        msg.append("Hai aperto: ").append(c.getName());
                        if (!c.getList().isEmpty()) {
                            msg.append(". ").append(c.getName()).append(" contiene:");
                            c.getList().forEach((next, quantity) -> msg.append(" ").append(quantity).append(" x ").append(next.getName()));
                        }
                    }
                } else {
                    msg.append("L'oggetto ").append(c.getName()).append(" è già aperto.");
                }
            } else {
                if (item != null) {
                    msg.append("L'oggetto ").append(item.getName()).append(" non può essere aperto.");
                } else {
                    msg.append("L'oggetto specificato non è presente nella stanza.");
                }
            }

            // Caso 2: oggetto nell’inventario
        } else if (GameUtils.getObjectFromInventory(description.getInventory(), parserOutput.getObject().getId()) != null) {
            GeneralItem invObj = GameUtils.getObjectFromInventory(description.getInventory(), parserOutput.getObject().getId());

            if (invObj instanceof ContainerItem c) {
                if (!c.isOpen()) {
                    c.setOpen(true);
                    msg.append("Hai aperto: ").append(c.getName());
                    if (!c.getList().isEmpty()) {
                        msg.append(". ").append(c.getName()).append(" contiene:");
                        c.getList().forEach((next, quantity) -> msg.append(" ").append(quantity).append(" x ").append(next.getName()));
                    }
                } else {
                    msg.append("L'oggetto ").append(c.getName()).append(" è già aperto.");
                }
            } else {
                if (invObj != null) {
                    msg.append("L'oggetto ").append(invObj.getName()).append(" non è apribile.");
                } else {
                    msg.append("L'oggetto specificato non è presente nell'inventario.");
                }
            }

            // Caso 3: oggetto non trovato
        } else {
            msg.append("L'oggetto indicato non è nei paraggi.");
        }

        return msg.toString();
    }
}
