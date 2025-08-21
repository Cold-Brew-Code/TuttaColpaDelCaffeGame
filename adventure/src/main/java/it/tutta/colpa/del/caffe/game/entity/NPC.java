
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import it.tutta.colpa.del.caffe.game.exception.DialogueException;

/**
 * Rappresenta un NPC (Non-Player Character) nel gioco, con un identificatore
 * univoco, un nome, una lista di dialoghi e alias alternativi.
 * <p>
 * Ogni NPC può avere più dialoghi, che vengono consumati nell'ordine in cui
 * sono stati aggiunti. L'identità dell'NPC è basata sull'id, quindi due NPC con
 * lo stesso id sono considerati uguali.
 * </p>
 *
 * @author giovanni
 */
public class NPC implements Serializable {

    private final int id;
    private String nome;
    private int dialogoCor;
    private final List<Dialogo> dialoghi;
    private Set<String> alias;

    /**
     * Costruisce un NPC con id, nome e alias, senza dialoghi iniziali.
     *
     * @param id identificatore univoco dell'NPC
     * @param nome nome dell'NPC
     * @param alias set di alias alternativi
     */
    public NPC(int id, String nome, Set<String> alias) {
        this.id = id;
        this.nome = nome;
        this.dialogoCor = 0;
        this.dialoghi = new ArrayList<>();
        this.alias = alias;
    }

    /**
     * Costruisce un NPC con id, nome, lista di dialoghi e alias.
     *
     * @param id identificatore univoco dell'NPC
     * @param nome nome dell'NPC
     * @param dialoghi lista dei dialoghi iniziali
     * @param alias set di alias alternativi
     */
    public NPC(int id, String nome, List<Dialogo> dialoghi, Set<String> alias) {
        this.id = id;
        this.nome = nome;
        this.dialogoCor = 0;
        this.dialoghi = dialoghi;
        this.alias = alias;
    }

    /**
     * Restituisce il set di alias dell'NPC.
     *
     * @return set di alias
     */
    public Set<String> getAlias() {
        return alias;
    }

    /**
     * @param alias
     */
    public void setAlias(Set<String> alias) {
        this.alias = alias;
    }

    /**
     * Imposta il nome dell'NPC.
     *
     * @param nome nuovo nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce l'identificatore dell'NPC.
     *
     * @return id dell'NPC
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta il nome dell'NPC.
     *
     * @param nome nuovo nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Aggiunge un dialogo alla lista dei dialoghi dell'NPC.
     *
     * @param dialogo dialogo da aggiungere
     */
    public void addDialogo(Dialogo dialogo) {
        this.dialoghi.add(dialogo);
    }

    /**
     * Restituisce il dialogo corrente dell'NPC.
     *
     * @return dialogo corrente
     * @throws DialogueException se non ci sono dialoghi disponibili
     */
    public Dialogo getDialogoCorr() throws DialogueException {
        if (this.dialoghi.size() - 1 < this.dialogoCor) {
            throw new DialogueException("Non puoi parlare con " + this.nome);
        }
        return this.dialoghi.get(this.dialogoCor);
    }

    /**
     * Segna il dialogo corrente come consumato e passa al dialogo successivo.
     */
    public void consumedDialogue() {
        dialogoCor++;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NPC castedObj) {
            return castedObj.id == this.id;
        }
        return false;
    }

    /**
     * metodo il quale mi restituisce la lista dei dialoghi dell'NPC
     *
     * @return
     */
    public List<Dialogo> getDialoghi() {
        return dialoghi;
    }

}
