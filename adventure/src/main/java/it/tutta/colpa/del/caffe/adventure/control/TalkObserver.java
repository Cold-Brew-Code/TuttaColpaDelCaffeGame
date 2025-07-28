/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.control;

import it.tutta.colpa.del.caffe.game.entity.Dialogo;
import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.entity.GameObserver;
import it.tutta.colpa.del.caffe.game.entity.NPC;
import it.tutta.colpa.del.caffe.game.utility.CommandType;
import it.tutta.colpa.del.caffe.game.utility.ParserOutput;
import java.util.List;

/**
 *
 * @author giova
 */
public class TalkObserver implements GameObserver {
    
    @Override
    public String update(GameDescription description, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        // lista npc 1 e ha specificato con chi vuole paralre  ma non è necessari 
        if (parserOutput.getCommand().getType() == CommandType.TALK_TO
                && description.getCurrentRoom().getNPCs().size()==1 && parserOutput.getObject()!= null) {
            msg.append("Per parlare con qualcuno scrivere 'Parla'! ");
        }// lista npc 1 e non ha specificato con chi vuole paralre 
        else if(parserOutput.getCommand().getType() == CommandType.TALK_TO
                && description.getCurrentRoom().getNPCs().size()==1 && parserOutput.getObject()== null){
            
            NPC npcRoom= description.getCurrentRoom().getNPCs().get(0);
            msg.append("Stai parlando con ").append(npcRoom.getNome()).append(". ");
            Dialogo dialogo= npcRoom.getDialoghi().get(0);
            msg.append(dialogo.getCurrentNode());
            String currentNode = null;
            dialogo.setCurrentNode(currentNode);
            
        }
        // lista npc 2 e ha specificato con chi vuole paralre 
        else if(parserOutput.getCommand().getType() == CommandType.TALK_TO
                && description.getCurrentRoom().getNPCs().size()>1 && parserOutput.getObject()!= null ){
            
        }//lista npc 2 e  non ha specificato con chi vuole paralre
        else if(parserOutput.getCommand().getType() == CommandType.TALK_TO
                && description.getCurrentRoom().getNPCs().size()>1 && parserOutput.getObject()== null ){
            
        } // nessun npc
        else{
            msg.append("la cacca ti da alla testa, non c'è nessuno qui");
            
        }
            
       
     return msg.toString();
    }
}
