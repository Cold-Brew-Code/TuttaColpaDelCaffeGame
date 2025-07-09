/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.entity;

import it.tutta.colpa.del.caffe.adventure.utility.StringArcoGrafo;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.Graph;

/**
 *
 * @author giova
 */
public class Dialogo  {
    
    private final Graph <String,StringArcoGrafo> dialogo;
    
    public Dialogo(){
        
        dialogo= new DefaultDirectedGraph<>(StringArcoGrafo.class);
    }
    
    public void setDialogo(String dialogo){
        this.dialogo.addVertex(dialogo);
    }
    
    public void setRisposta(String domandaP, String domandaA,String risposta){
        this.dialogo.addEdge(domandaP, domandaA, new StringArcoGrafo(risposta));
    }
   // get da fare 
    
    

}
