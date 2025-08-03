/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.entity;

import java.util.List;

/**
 *
 * @author giova
 */
public class DialogoQuiz {
    private String domanda;
    private String messaggioCorret;
    private String messaggioErrato;
    List<String> risposte;
    private int id_rispostaCorretta;

    public DialogoQuiz(String domanda, String messaggioCorret, String messaggioErrato, List<String> risposte,int id_rispostaCorretta) {
        this.domanda = domanda;
        this.messaggioCorret = messaggioCorret;
        this.messaggioErrato = messaggioErrato;
        this.risposte = risposte;
        this.id_rispostaCorretta= id_rispostaCorretta;

    }

    public String getDomanda() {
        return domanda;
    }

    public void setDomanda(String domanda) {
        this.domanda = domanda;
    }

    public String getMessaggioCorret() {
        return messaggioCorret;
    }

    public void setMessaggioCorret(String messaggioCorret) {
        this.messaggioCorret = messaggioCorret;
    }

    public String getMessaggioErrato() {
        return messaggioErrato;
    }

    public void setMessaggioErrato(String messaggioErrato) {
        this.messaggioErrato = messaggioErrato;
    }

    public List<String> getRisposte() {
        return risposte;
    }

    public void setRisposte(List<String> risposte) {
        this.risposte = risposte;
    }
    
    public int getIdCorretta() {
        return id_rispostaCorretta;
    }

    public void setIdCorretta(int id_rispostaCorretta) {
        this.id_rispostaCorretta = id_rispostaCorretta;
    }
}
