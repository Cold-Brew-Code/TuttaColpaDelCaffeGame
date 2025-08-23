
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;

import it.tutta.colpa.del.caffe.game.exception.DialogueException;
import it.tutta.colpa.del.caffe.game.utility.StringArcoGrafo;

/**
 * Rappresenta un dialogo strutturato come un grafo orientato, dove i nodi sono
 * affermazioni o domande e gli archi rappresentano le possibili risposte.
 * Questa classe permette di navigare nel dialogo scegliendo risposte, ottenere
 * le possibili risposte correnti, e tenere traccia del percorso del dialogo.
 *
 * <p>
 * Ogni dialogo ha un nodo principale da cui inizia la conversazione e un nodo
 * corrente che rappresenta lo stato attuale della conversazione.</p>
 *
 * <p>
 * La classe utilizza una rappresentazione con {@link DefaultDirectedGraph} di
 * JGraphT e archi di tipo {@link StringArcoGrafo}.</p>
 *
 * @author giova
 */
public class Dialogue implements Serializable {

    /**
     * L'identificativo univoco del dialogo.
     */
    private final int id;

    /**
     * Il grafo che rappresenta il dialogo, con nodi come stringhe e archi come
     * {@link StringArcoGrafo}.
     */
    private final Graph<String, StringArcoGrafo> dialogo;

    /**
     * Il nodo corrente del dialogo, rappresenta l'affermazione/question
     * attuale.
     */
    private String currentNode;

    /**
     * Il nodo principale del dialogo, da cui parte la conversazione.
     */
    private String main;

    /**
     * Indica se il dialogo è attivo o meno.
     */
    private boolean activeDialogue = true;

    /**
     * Costruisce un nuovo dialogo con un identificativo specifico.
     *
     * @param id L'identificativo univoco del dialogo.
     */
    public Dialogue(int id) {
        this.id = id;
        dialogo = new DefaultDirectedGraph<>(StringArcoGrafo.class);
    }

    /**
     * Aggiunge un'affermazione al dialogo.
     *
     * @param dialogo La stringa dell'affermazione da aggiungere.
     * @param main {@code true} se questa affermazione è il nodo principale del
     * dialogo.
     */
    public void addStatement(String dialogo, boolean main) {
        this.dialogo.addVertex(dialogo);
        if (main) {
            this.main = dialogo;
            this.currentNode = dialogo;
        }
    }

    /**
     * Aggiunge una risposta al dialogo tra due affermazioni esistenti.
     *
     * @param domandaP Nodo di partenza della risposta.
     * @param domandaA Nodo di arrivo della risposta.
     * @param risposta La stringa associata all'arco che rappresenta la
     * risposta.
     */
    public void addRisposta(String domandaP, String domandaA, String risposta) {
        this.dialogo.addEdge(domandaP, domandaA, new StringArcoGrafo(risposta));
    }

    /**
     * Restituisce il nodo corrente del dialogo.
     *
     * @return Il nodo corrente.
     */
    public String getCurrentNode() {
        return this.currentNode;
    }

    /**
     * Restituisce le possibili risposte associate al nodo corrente.
     *
     * @return Lista di stringhe con le risposte possibili.
     */
    public List<String> getCurrentAssociatedPossibleAnswers() {
        return getCurrentLabels().stream()
                .map(answer -> answer.toString())
                .collect(Collectors.toList());
    }

    /**
     * Ottiene gli archi (risposte) uscenti dal nodo corrente.
     *
     * @return Lista di {@link StringArcoGrafo} rappresentanti le possibili
     * risposte.
     */
    private List<StringArcoGrafo> getCurrentLabels() {
        List<StringArcoGrafo> possibleAnswers = new ArrayList<>();
        for (StringArcoGrafo statement : this.dialogo.outgoingEdgesOf(this.currentNode)) {
            possibleAnswers.add(statement);
        }
        return possibleAnswers;
    }

    /**
     * Aggiorna il nodo corrente del dialogo scegliendo una risposta tra quelle
     * possibili. Lancia {@link DialogueException} se la risposta scelta non è
     * valida.
     *
     * @param answerChosen La risposta selezionata.
     * @throws DialogueException Se la risposta non è valida per il nodo
     * corrente.
     */
    public void setNextStatementFromAnswer(String answerChosen) throws DialogueException {
        this.setCurrentNode(this.dialogo.getEdgeTarget(getCurrentLabels().stream()
                .filter(answer -> answer.getEtichetta()
                .equals(answerChosen))
                .findFirst()
                .orElseThrow(() -> new DialogueException("Risposta non valida!"))));
    }

    /**
     * Restituisce l'identificativo del dialogo.
     *
     * @return L'id del dialogo.
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta il nodo corrente del dialogo.
     *
     * @param currentNode Il nuovo nodo corrente.
     */
    public void setCurrentNode(String currentNode) {
        this.currentNode = currentNode;
    }

    /**
     * Restituisce se il dialogo è attivo.
     *
     * @return {@code true} se il dialogo è attivo, {@code false} altrimenti.
     */
    public boolean isActive() {
        return activeDialogue;
    }

    /**
     * Imposta lo stato di attività del dialogo.
     *
     * @param activeDialogue {@code true} per attivare il dialogo, {@code false}
     * per disattivarlo.
     */
    public void setActivity(boolean activeDialogue) {
        this.activeDialogue = activeDialogue;
    }

    /**
     * Restituisce il nodo principale del dialogo.
     *
     * @return Il nodo principale.
     */
    public String getMainNode() {
        return main;
    }

    /**
     * Restituisce la sequenza dei nodi e delle risposte percorse fino al nodo
     * corrente.
     *
     * @return Lista alternata di nodi e risposte che rappresenta il percorso
     * dal nodo principale al nodo corrente.
     */
    public List<String> getPreviewsStatement() {
        DijkstraShortestPath<String, StringArcoGrafo> dsp = new DijkstraShortestPath<>(this.dialogo);
        GraphPath<String, StringArcoGrafo> path = dsp.getPath(main, currentNode);
        List<String> ps = new ArrayList<>();
        for (int i = 0; i < path.getVertexList().size() - 1; i++) {
            ps.add(path.getVertexList().get(i));
            ps.add(path.getEdgeList().get(i).getEtichetta());
        }
        return ps;
    }
}
