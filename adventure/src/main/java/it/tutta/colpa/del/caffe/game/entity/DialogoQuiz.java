/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.entity;

import java.util.List;
import java.util.Set;

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
    List<String> risposte;

    /**
     * Indice della risposta corretta nella lista {@link #risposte}.
     */
    private int id_rispostaCorretta;
    private static final List<String> rispostePositive = List.of(
            "Bravo! Finalmente qualcuno che sembra sveglio prima di un caffè doppio.",
            "Perfetto! E pensare che pensavo stessi ancora cercando il bagno…",
            "Ottimo lavoro! La tua mente è più agile del tuo stomaco, complimenti.",
            "Esatto! Hai passato la prova… e senza bisogno di rotoli di carta questa volta.",
            "Grande! Dicono che chi sa rispondere bene anche in situazioni critiche ha un futuro brillante.",
            "Ehi, lo sapevo che avresti cavato qualcosa di buono da quella testa… e non era solo la fame di toilette.",
            "Bingo! Quasi come trovare il bagno al primo colpo, ma molto più impressionante.",
            "Corretto! Mi chiedo come fai a concentrarti così bene dopo le tue avventure igieniche…",
            "Perfetto! Sei come un supereroe: salva la situazione e passa l’esame.",
            "Fantastico! Non solo hai trovato la risposta giusta, ma hai anche guadagnato punti karma per la tua sopravvivenza intestinale."
    );

    private static final List<String> risposteNegative = List.of(
            "Oh no… sembra che il tuo cervello stia ancora cercando il bagno.",
            "Mmh… credo che la tua mente abbia preso una pausa dopo l’epopea della toilette.",
            "Sbagliato! Ma ti do punti per lo sforzo… e per la sopravvivenza intestinale.",
            "Ah, quasi! Ma non preoccuparti, tutti abbiamo giornate no… soprattutto dopo certe corse.",
            "Non esatto… forse il bagno ti ha distratto un po’, eh?",
            "Purtroppo no… ma almeno sei riuscito a sederti e affrontare l’esame, coraggio!",
            "Sbagliato! Ma hey, almeno hai mantenuto il sorriso… e quello conta.",
            "No, ma la tua audacia dopo mille peripezie è encomiabile!",
            "Manca! Ma ti ammiro per averci provato senza fuggire di nuovo alla toilette.",
            "Errato… però apprezzo lo spirito di sopravvivenza, e non molti possono dire lo stesso."
    );

    public DialogoQuiz(String domanda, List<String> risposte,int id_rispostaCorretta) {
        this.domanda = domanda;
        this.risposte = risposte;
        this.id_rispostaCorretta= id_rispostaCorretta;

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
        return DialogoQuiz.rispostePositive.get((int) (Math.random() * 10));
    }

    /**
     * Restituisce il messaggio da mostrare in caso di risposta errata.
     *
     * @return Il messaggio errato.
     */
    public String getMessaggioErrato() {
        return (DialogoQuiz.risposteNegative.get((int) (Math.random() * 10)));
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
