/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.utility;
import org.jgrapht.graph.DefaultEdge;
/**
 *
 * @author giova
 */
public class ArcoGrafo extends DefaultEdge{
    private Direzione etichetta;

    public ArcoGrafo(Direzione etichetta) {
        this.etichetta = etichetta;
    }

    public Direzione getEtichetta() {
        return etichetta;
    }

    @Override
    public String toString() {
        return etichetta.toString();
    }
    
}
