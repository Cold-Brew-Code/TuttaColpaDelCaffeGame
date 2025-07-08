/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.entity;

/**
 *
 * @author giovanni
 */
public class NPC {
    
    final int id;
    String nome;
    int idStanza; 
    int dialogoCor;
    
    
    public NPC(int id, String nome, int idStanza) {
        this.id = id;
        this.nome = nome;
        this.idStanza = idStanza;
        this.dialogoCor=1;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIdStanza(int idStanza) {
        this.idStanza = idStanza;
    }

    
    
   
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getIdStanza() {
        return idStanza;
    }

    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NPC other = (NPC) obj;
        return this.id == other.id;
    }
    
    
}
