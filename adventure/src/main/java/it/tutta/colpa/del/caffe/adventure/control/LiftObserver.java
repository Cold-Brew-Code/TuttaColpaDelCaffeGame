/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.control;

import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.entity.GameObserver;
import it.tutta.colpa.del.caffe.game.entity.Room;
import it.tutta.colpa.del.caffe.game.exception.GameMapException;
import it.tutta.colpa.del.caffe.game.exception.ServerCommunicationException;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.GameUtils;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;

/**
 * Osservatore che gestisce il comando {@code LIFT} nel gioco.
 * <p>
 * Implementa l'interfaccia {@link GameObserver} e consente al giocatore di
 * usare l'ascensore per spostarsi a un determinato piano dell'edificio, a
 * condizione che possieda la chiave necessaria nell'inventario.
 * </p>
 *
 * <p>
 * Esempio di comportamento:</p>
 * <ul>
 * <li>Se il giocatore scrive <i>"sali a 3"</i>, e possiede la chiave (oggetto
 * con ID 9), viene teletrasportato al piano 3 e la stanza corrente viene
 * aggiornata.</li>
 * <li>Se non specifica un piano valido, viene mostrato un messaggio di
 * errore.</li>
 * <li>Se non possiede la chiave nell'inventario, non può usare
 * l'ascensore.</li>
 * </ul>
 *
 * @author giovanni
 */
public class LiftObserver implements GameObserver {

    /**
     *
     * @param description
     * @param parserOutput
     * @return
     */
    @Override
    public String update(GameDescription description, ParserOutput parserOutput) throws ServerCommunicationException {
        /**
         * Gestisce il comando {@code LIFT}, verificando se il giocatore
         * possiede la chiave necessaria e aggiornando la stanza corrente con
         * quella corrispondente al piano richiesto.
         *
         * @param description lo stato corrente del gioco (inventario, mappa,
         * stanza attuale)
         * @param parserOutput il risultato del parsing del comando dell'utente
         * @return un messaggio testuale che descrive l'esito dell'operazione
         * @throws ServerCommunicationException se si verifica un errore nella
         * comunicazione con il server
         */
        StringBuilder msg = new StringBuilder();
        System.out.println(parserOutput.getCommand().getType() == CommandType.LIFT);
        int p = parserOutput.getPiano();
        if (parserOutput.getCommand().getType() == CommandType.LIFT) {
            if (GameUtils.getObjectFromInventory(description.getInventory(), 9) != null) {
                if (p < 0) {
                    msg.append("Non hai specificato il numero del piano in cui vuoi teletrasportarti\n").append("Scrivi sali a numero piano (1,2,...7)");
                } else {
                    try {
                        Room room = description.getGameMap().prendiAscensore(p);
                        description.getGameMap().setCurrentRoom(room);
                        msg.append("siamo arrivati al piano ").append(p).append("\n stanza: ").append(room.getName());
                    } catch (GameMapException ex) {
                        msg.append(ex.getMessage());
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        msg.append(ex.getMessage());
                    }
                }
            } else {
                msg.append("non hai l'achiave nell'inventario");
            }
        }
        return msg.toString();
    }
}
