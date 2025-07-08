/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.entity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author giova
 */
public class Dialogo  {
    int passoC;
    int risposta;
    List<Passo> passi;

    public Dialogo() {
        this.passoC = 1;
        this.risposta = 0;
        this.passi = new ArrayList<>();
    }
    
    public void setPasso(String domanda, String risposta1, String risposta2){
        this.passi.add(new Passo(domanda, risposta1,risposta2));
    }
    
    
    
private class Passo{
    String domanda;
    String risposta1;
    String risposta2;

        public Passo(String domanda, String risposta1, String risposta2) {
            this.domanda = domanda;
            this.risposta1 = risposta1;
            this.risposta2 = risposta2;
        }
    
    
    




}

}
