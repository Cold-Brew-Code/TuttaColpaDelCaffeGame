/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.game.entity;

import java.util.List;
import java.util.Set;

/**
 *
 * @author giova
 */
public class DialogoQuiz {
    private String domanda;
    List<String> risposte;
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

    public String getDomanda() {
        return domanda;
    }

    public void setDomanda(String domanda) {
        this.domanda = domanda;
    }

    public String getMessaggioCorret() {
        return DialogoQuiz.rispostePositive.get((int) (Math.random() * 10));
    }

    public String getMessaggioErrato() {
        return (DialogoQuiz.risposteNegative.get((int) (Math.random() * 10)));
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
