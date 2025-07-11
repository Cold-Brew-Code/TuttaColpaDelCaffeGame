/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.utility;
import org.jgrapht.graph.DefaultEdge;
/**
 *
 * @author giova
 */
public class StringArcoGrafo extends DefaultEdge {
    private String etichetta;

    public StringArcoGrafo(String etichetta) {
        this.etichetta = etichetta;
    }

    public String getEtichetta() {
        return etichetta;
    }

    @Override
    public String toString() {
        return etichetta;
    }
    
    
}
