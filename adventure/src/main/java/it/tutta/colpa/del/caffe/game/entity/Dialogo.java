
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.entity;

import java.io.Serializable;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import it.tutta.colpa.del.caffe.game.utility.StringArcoGrafo;

/**
 *
 * @author giova
 */
public class Dialogo  implements Serializable {
    private final int id;
    private final Graph <String,StringArcoGrafo> dialogo;
    private String currentNode;
    
    public Dialogo(int id){
        this.id=id;
        dialogo = new DefaultDirectedGraph<>(StringArcoGrafo.class);
    }

    public void addDialogo(String dialogo, boolean corrente){
        this.dialogo.addVertex(dialogo);
        this.currentNode = dialogo;
    }

    public void addDialogo(String dialogo){
        this.dialogo.addVertex(dialogo);
    }
    
    public void addRisposta(String domandaP, String domandaA,String risposta){
        this.dialogo.addEdge(domandaP, domandaA, new StringArcoGrafo(risposta));
    }

    /**
     *
     * @return id del dialogo
     */
    public int getId() {
        return id;
    }

    public String getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(String currentNode) {
        this.currentNode = currentNode;
    }

    public void stampaDialogo() {
        for (String nodo : dialogo.vertexSet()) {
            System.out.println("Nodo: " + nodo);
            Set<StringArcoGrafo> archiUscenti = dialogo.outgoingEdgesOf(nodo);
            if (archiUscenti.isEmpty()) {
                System.out.println("  Nessuna risposta disponibile.");
            } else {
                for (StringArcoGrafo arco : archiUscenti) {
                    String nodoDestinazione = dialogo.getEdgeTarget(arco);
                    System.out.println(arco.getEtichetta() + "\" porta a: " + "  Risposta: \"" + nodoDestinazione);
                }
            }
        }
    }


}
