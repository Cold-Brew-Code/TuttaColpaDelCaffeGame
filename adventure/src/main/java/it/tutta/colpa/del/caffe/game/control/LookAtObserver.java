/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.control;

import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.entity.GameObserver;
import it.tutta.colpa.del.caffe.game.exception.ServerCommunicationException;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;
import static it.tutta.colpa.del.caffe.game.utility.RequestType.UPDATED_LOOK;

/**
 * Osservatore che gestisce il comando {@code LOOK_AT} nel gioco.
 * <p>
 * Implementa l'interfaccia {@link GameObserver} e permette al giocatore di
 * osservare la stanza corrente, recuperandone la descrizione. In alcuni casi
 * particolari, effettua anche una richiesta al server per aggiornare il testo
 * visibile della stanza in base agli eventi di gioco.
 * </p>
 *
 * <p>
 * Esempio di comportamento:</p>
 * <ul>
 * <li>Se la stanza ha una descrizione predefinita, questa viene mostrata.</li>
 * <li>Se la stanza ha un evento collegato (es. indizi o cambiamenti), viene
 * inviata una richiesta al server per aggiornare la descrizione.</li>
 * <li>Se non è disponibile nessuna descrizione, viene restituito un messaggio
 * che segnala l'assenza di dettagli interessanti.</li>
 * </ul>
 *
 * <p>
 * Nota: se non è possibile comunicare con il server, viene sollevata
 * un'eccezione {@link ServerCommunicationException} oppure mostrato un
 * messaggio di errore all'utente.</p>
 *
 * @author giovanni
 */
public class LookAtObserver implements GameObserver {

    /**
     * Gestisce il comando {@code LOOK_AT}, restituendo la descrizione della
     * stanza corrente o aggiornandola tramite il server in base alle condizioni
     * del gioco.
     *
     * @param description lo stato corrente del gioco, incluse le stanze e i
     * dettagli visivi
     * @param parserOutput il risultato del parsing del comando dell'utente
     * @return una stringa che rappresenta il messaggio da mostrare al giocatore
     * @throws ServerCommunicationException se si verifica un errore nella
     * comunicazione con il server
     */
    @Override
    public String update(GameDescription description, ParserOutput parserOutput) throws ServerCommunicationException {

        StringBuilder msg = new StringBuilder();
        if (parserOutput.getCommand().getType() == CommandType.LOOK_AT) {
            ServerInterface server;
            try {
                server = new ServerInterface("localhost", 49152);
            } catch (ServerCommunicationException ex) {
                server = null;
            }
            if (description.getCurrentRoom().getLook() != null) {
                msg.append(description.getCurrentRoom().getLook());
                try {
                    int roomId = description.getCurrentRoom().getId();
                    if (server != null) {
                        switch (roomId) {
                            case 18 -> // ho fatto osserva nella stanza che ha l'indizzio della cartigenica in possesso dell'inservientre
                                description.getCurrentRoom().setLook(server.requestToServer(UPDATED_LOOK, 8));
                            case 11 -> // ho fatto osserva nel bagno 4 piano 
                                description.getCurrentRoom().setLook(server.requestToServer(UPDATED_LOOK, 6));
                            default -> {
                            }
                        }
                    } else {
                        throw new ServerCommunicationException("connessione al server fallita");
                    }
                } catch (ServerCommunicationException | NullPointerException e) {
                    msg.append(" Errore nella comunicazione col server: ").append(e.getMessage());
                }

            } else {
                msg.append("Non c'è niente di interessante qui.");
            }
        }
        return msg.toString();
    }

}
