/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.control;

import it.tutta.colpa.del.caffe.game.control.ServerInterface;
import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.entity.GameObserver;
import it.tutta.colpa.del.caffe.game.exception.ServerCommunicationException;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;
import static it.tutta.colpa.del.caffe.game.utility.RequestType.UPDATED_LOOK;

/**
 *
 * @author giovanni
 */
public class LookAtObserver implements GameObserver {

    /**
     *
     * @param description
     * @param parserOutput
     * @return
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
