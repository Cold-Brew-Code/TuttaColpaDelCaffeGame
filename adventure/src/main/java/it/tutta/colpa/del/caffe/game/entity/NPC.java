
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.entity;

import it.tutta.colpa.del.caffe.game.exception.DialogueException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author giovanni
 */
public class NPC implements Serializable {

    private final int id;
    private String nome;
    private int dialogoCor;
    private final List<Dialogo> dialoghi;
    private Set<String> alias;



    public NPC(int id, String nome, Set<String> alias) {
        this.id = id;
        this.nome = nome;
        this.dialogoCor=0;
        this.dialoghi= new ArrayList<>();
        this.alias = alias;
    }

    public NPC(int id, String nome, List<Dialogo> dialoghi, Set<String> alias) {
        this.id = id;
        this.nome = nome;
        this.dialogoCor = 0;
        this.dialoghi = dialoghi;
        this.alias = alias;
    }


    /**
     * @return
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

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void addDialogo(Dialogo dialogo){
        this.dialoghi.add(dialogo);
    }

    public Dialogo getDialogoCorr() throws DialogueException {
        if (this.dialoghi.size() - 1 < this.dialogoCor)
            throw new DialogueException("Non puoi parlare con " + this.nome);
        return this.dialoghi.get(this.dialogoCor++);
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
     * @return
     */

    public List<Dialogo> getDialoghi() {
        return dialoghi;
    }



}
