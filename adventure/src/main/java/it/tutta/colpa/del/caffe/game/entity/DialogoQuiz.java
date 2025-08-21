/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.entity;

import java.util.List;

/**
 * Rappresenta un singolo quiz dialogico con una domanda, un insieme di risposte
 * possibili e messaggi per indicare se la risposta scelta è corretta o errata.
 *
 * <p>
 * Ogni quiz contiene:</p>
 * <ul>
 * <li>La domanda da porre all'utente.</li>
 * <li>Una lista di risposte possibili.</li>
 * <li>Un messaggio da mostrare se la risposta scelta è corretta.</li>
 * <li>Un messaggio da mostrare se la risposta scelta è errata.</li>
 * <li>L'indice della risposta corretta nella lista delle risposte.</li>
 * </ul>
 *
 * <p>
 * Questa classe permette di gestire sia la domanda che le risposte, e di
 * verificare facilmente quale risposta è corretta.</p>
 *
 * @author giova
 */
public class DialogoQuiz {

    /**
     * La domanda del quiz.
     */
    private String domanda;

    /**
     * Messaggio da mostrare se l'utente sceglie la risposta corretta.
     */
    private String messaggioCorret;

    /**
     * Messaggio da mostrare se l'utente sceglie una risposta errata.
     */
    private String messaggioErrato;

    /**
     * Lista delle risposte possibili.
     */
    List<String> risposte;

    /**
     * Indice della risposta corretta nella lista {@link #risposte}.
     */
    private int id_rispostaCorretta;

    /**
     * Costruisce un nuovo quiz con domanda, risposte e messaggi di feedback.
     *
     * @param domanda La domanda del quiz.
     * @param messaggioCorret Messaggio per risposta corretta.
     * @param messaggioErrato Messaggio per risposta errata.
     * @param risposte Lista delle possibili risposte.
     * @param id_rispostaCorretta Indice della risposta corretta nella lista.
     */
    public DialogoQuiz(String domanda, String messaggioCorret, String messaggioErrato, List<String> risposte, int id_rispostaCorretta) {
        this.domanda = domanda;
        this.messaggioCorret = messaggioCorret;
        this.messaggioErrato = messaggioErrato;
        this.risposte = risposte;
        this.id_rispostaCorretta = id_rispostaCorretta;
    }

    /**
     * Restituisce la domanda del quiz.
     *
     * @return La domanda come stringa.
     */
    public String getDomanda() {
        return domanda;
    }

    /**
     * Imposta la domanda del quiz.
     *
     * @param domanda La nuova domanda.
     */
    public void setDomanda(String domanda) {
        this.domanda = domanda;
    }

    /**
     * Restituisce il messaggio da mostrare in caso di risposta corretta.
     *
     * @return Il messaggio corretto.
     */
    public String getMessaggioCorret() {
        return messaggioCorret;
    }

    /**
     * Imposta il messaggio da mostrare in caso di risposta corretta.
     *
     * @param messaggioCorret Il nuovo messaggio corretto.
     */
    public void setMessaggioCorret(String messaggioCorret) {
        this.messaggioCorret = messaggioCorret;
    }

    /**
     * Restituisce il messaggio da mostrare in caso di risposta errata.
     *
     * @return Il messaggio errato.
     */
    public String getMessaggioErrato() {
        return messaggioErrato;
    }

    /**
     * Imposta il messaggio da mostrare in caso di risposta errata.
     *
     * @param messaggioErrato Il nuovo messaggio errato.
     */
    public void setMessaggioErrato(String messaggioErrato) {
        this.messaggioErrato = messaggioErrato;
    }

    /**
     * Restituisce la lista delle possibili risposte.
     *
     * @return Lista di risposte.
     */
    public List<String> getRisposte() {
        return risposte;
    }

    /**
     * Imposta la lista delle possibili risposte.
     *
     * @param risposte La nuova lista di risposte.
     */
    public void setRisposte(List<String> risposte) {
        this.risposte = risposte;
    }

    /**
     * Restituisce l'indice della risposta corretta.
     *
     * @return L'indice della risposta corretta nella lista {@link #risposte}.
     */
    public int getIdCorretta() {
        return id_rispostaCorretta;
    }

    /**
     * Imposta l'indice della risposta corretta.
     *
     * @param id_rispostaCorretta Il nuovo indice della risposta corretta.
     */
    public void setIdCorretta(int id_rispostaCorretta) {
        this.id_rispostaCorretta = id_rispostaCorretta;
    }
}
