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
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;

/**
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
        System.out.println("sono in asce1");
        StringBuilder msg = new StringBuilder();
        System.out.println(parserOutput.getCommand().getType() == CommandType.LIFT);
        System.out.println("piano "+parserOutput.getPiano());
        int p=parserOutput.getPiano();
        if (parserOutput.getCommand().getType() == CommandType.LIFT) {
            //bisogna aggiungere un controllo per l'ascensore
            System.out.println("supero il primo if");
            if(p <0){
                msg.append("Non hai specificato il numero del piano in cui vuoi teletrasportarti\n").append("Scrivi sali a numero piano (1,2,...7)");
            }else{
                System.out.println("sono in asce2");
                try {
                    Room room= description.getGameMap().prendiAscensore(p);
                    description.getGameMap().setCurrentRoom(room);
                    msg.append("siamo arrivati al piano ").append(p).append("\n stanza: ").append(room.getName());    
                } catch (GameMapException ex) {
                msg.append(ex.getMessage());
            } catch (Exception ex) {
                msg.append(ex.getMessage());
            }
            }
        }
        return msg.toString();
    }
}