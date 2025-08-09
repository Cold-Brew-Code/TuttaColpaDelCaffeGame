
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.entity;

import it.tutta.colpa.del.caffe.game.exception.DialogueException;
import it.tutta.colpa.del.caffe.game.utility.StringArcoGrafo;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.Graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author giova
 */
public class Dialogo implements Serializable {
    private final int id;
    private final Graph<String, StringArcoGrafo> dialogo;
    private String currentNode;
    private boolean activeDialogue = true;

    public Dialogo(int id) {
        this.id = id;
        dialogo = new DefaultDirectedGraph<>(StringArcoGrafo.class);
    }

    public void addDialogo(String dialogo, boolean corrente) {
        this.dialogo.addVertex(dialogo);
        if (corrente) this.currentNode = dialogo;
    }

    public void addRisposta(String domandaP, String domandaA, String risposta) {
        this.dialogo.addEdge(domandaP, domandaA, new StringArcoGrafo(risposta));
    }

    public String getCurrentNode() {
        return this.currentNode;
    }


    public List<String> getCurrentAssociatedPossibleAnswers() {
        return getCurrentLabels().stream()
                .map(answer->answer.toString())
                .collect(Collectors.toList());
    }

    private List<StringArcoGrafo> getCurrentLabels() {
        List<StringArcoGrafo> possibleAnswers = new ArrayList<>();
        for (StringArcoGrafo statement : this.dialogo.outgoingEdgesOf(this.currentNode)) {
            possibleAnswers.add(statement);
        }
        return possibleAnswers;
    }

    public void setNextStatementFromAnswer(String answerChosen) throws DialogueException {
        this.setCurrentNode(this.dialogo.getEdgeTarget(getCurrentLabels().stream()
                .filter(answer->answer.getEtichetta()
                        .equals(answerChosen))
                .findFirst()
                .orElseThrow(() -> new DialogueException("Risposta non valida!")))) ;
    }

    /**
     * @return id del dialogo
     */
    public int getId() {
        return id;
    }

    public void setCurrentNode(String currentNode) {
        this.currentNode = currentNode;
    }



    public boolean isActive() {
        return activeDialogue;
    }

    public void setActivity(boolean activeDialogue) {
        this.activeDialogue = activeDialogue;
    }
}