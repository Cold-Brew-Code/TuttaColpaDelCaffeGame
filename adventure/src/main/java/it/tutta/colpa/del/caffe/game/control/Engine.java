package it.tutta.colpa.del.caffe.game.control;

import it.tutta.colpa.del.caffe.game.entity.GameDescription;
import it.tutta.colpa.del.caffe.game.exception.GameMapException;
import it.tutta.colpa.del.caffe.game.utility.Direzione;

import javax.swing.*;
import java.io.Serializable;

public class Engine implements Serializable {
    private JFrame frame;
    private GameDescription description;

    public Engine(){

    }

    private void initGame(){

    }

    private void initGame(String savePath){

    }

    private boolean moveFromHere(Direzione direction){
        try {
            description.getGameMap().getStanzaArrivo(direction);
        } catch (GameMapException e) {
            return false;
        }
        return true;
    }

    private boolean moveWithElevator(int floor){
        try {
            description.getGameMap().prendiAscensore(floor);
        } catch (GameMapException e) {
            return false;
        }
        return true;
    }
}
