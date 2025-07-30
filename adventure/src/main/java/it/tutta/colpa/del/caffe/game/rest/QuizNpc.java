package it.tutta.colpa.del.caffe.game.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import it.tutta.colpa.del.caffe.game.entity.DialogoQuiz;
import it.tutta.colpa.del.caffe.game.exception.ErrorConnection;

public class QuizNpc {

    /**
     * Classe che rappresenta una singola domanda ottenuta dall'API
     */
    public class DomandaApi {
        private String difficulty;
        private String type;
        private String question;
        private List<String> incorrect_answers;
        private String correct_answer;
        private String category;

        public String getDifficulty() {
            return difficulty;
        }

        public String getType() {
            return type;
        }

        public String getQuestion() {
            return question;
        }

        public List<String> getIncorrect_answers() {
            return incorrect_answers;
        }

        public String getCorrect_answer() {
            return correct_answer;
        }

        public String getCategory() {
            return category;
        }
    }

    /**
     * Classe che rappresenta la risposta JSON dell'API contenente le domande
     */
    private class ResponseRiquest {
        private int response_code;
        private List<DomandaApi> results;

        public int getResponse_code() {
            return response_code;
        }

        public List<DomandaApi> getResults() {
            return results;
        }
    }

    /**
     * Metodo che effettua la richiesta all'API e restituisce la risposta deserializzata
     */
    public static ResponseRiquest methodRest() {
        try{ 
            Client client = ClientBuilder.newClient();
            WebTarget target = client
                    .target("https://opentdb.com/api.php")
                    .queryParam("amount", 1)
                    .queryParam("category", 18)
                    .queryParam("type", "multiple");

            Response resp = target.request(MediaType.APPLICATION_JSON).get();
            Gson g = new Gson();
            return g.fromJson(resp.readEntity(String.class), ResponseRiquest.class);
        }catch (ProcessingException e) {
            throw new ErrorConnection("Connessione a Internet non disponibile.", e);
        }
    }

    /**
     * Metodo che converte la domanda in un oggetto DialogoQuiz con traduzioni e risposte mischiate
     * @return 
     */
    public static DialogoQuiz getQuiz() {
         // esegue la chiamata
        ResponseRiquest r = methodRest();
        if (r.getResults() == null || r.getResults().isEmpty()) {

            throw new IllegalStateException("Nessuna domanda trovata.");
        }
        DomandaApi curr = r.getResults().get(0);

        // Traduce la domanda
        String domandaTradotta = TraduttoreApi.traduci(curr.getQuestion(), "en", "it");

        List<String> risposteTradotte = new ArrayList<>();

        // Traduce la risposta corretta
        String rispostaCorrettaTradotta = TraduttoreApi.traduci(curr.getCorrect_answer(), "en", "it");
        risposteTradotte.add(rispostaCorrettaTradotta);
        //risposteTradotte.add(curr.getCorrect_answer());

        // Traduce e aggiunge le risposte sbagliate
        for (String rispErrata : curr.getIncorrect_answers()) {
            risposteTradotte.add(TraduttoreApi.traduci(rispErrata, "en", "it"));
            //risposteTradotte.add(rispErrata);
        }

        // Mischia tutte le risposte
        Collections.shuffle(risposteTradotte);

        // Crea e restituisce il DialogoQuiz
        DialogoQuiz d = new DialogoQuiz(domandaTradotta, "sei una bomba", "bocciato", risposteTradotte);
        return d;
    }
}
