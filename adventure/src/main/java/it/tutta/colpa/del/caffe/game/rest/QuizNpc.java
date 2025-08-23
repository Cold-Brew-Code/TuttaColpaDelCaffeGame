package it.tutta.colpa.del.caffe.game.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import it.tutta.colpa.del.caffe.game.entity.DialogoQuiz;
import it.tutta.colpa.del.caffe.game.exception.ConnectionError;
import it.tutta.colpa.del.caffe.game.exception.TraduzioneException;

public class QuizNpc {
    public static final List<DialogoQuiz> defaultQuizzes = List.of(
            new DialogoQuiz(
                    "Quale viene considerato il primo computer elettronico della storia?",
                    List.of("ENIAC", "Colossus", "Zuse Z3", "UNIVAC"),
                    2
            ),
            new DialogoQuiz(
                    "In che anno è stato inventato il World Wide Web da Tim Berners-Lee?",
                    List.of("1983", "1989", "1991", "1995"),
                    1
            ),
            new DialogoQuiz(
                    "Chi è considerato il 'padre dell’informatica teorica'?",
                    List.of("John von Neumann", "Alan Turing", "Charles Babbage", "Konrad Zuse"),
                    1
            ),
            new DialogoQuiz(
                    "Quale fu il primo linguaggio di programmazione ad alto livello di ampia diffusione?",
                    List.of("BASIC", "COBOL", "FORTRAN", "Pascal"),
                    2
            ),
            new DialogoQuiz(
                    "Quale azienda ha introdotto il primo microprocessore commerciale, l’Intel 4004, nel 1971?",
                    List.of("IBM", "Intel", "Texas Instruments", "Motorola"),
                    1
            )
    );
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
    public static ResponseRiquest methodRest() throws ConnectionError {
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
            throw new ConnectionError("Connessione a Internet non disponibile.", e);
        }
    }

    /**
     * Metodo che converte la domanda in un oggetto DialogoQuiz con traduzioni e risposte mischiate
     * @return 
     */
    public static DialogoQuiz getQuiz() throws ConnectionError {
         // esegue la chiamata
        ResponseRiquest r;
        do {
             r = methodRest();
             try {
                 Thread.sleep(2000);
             } catch (InterruptedException ignored) {}
        }while (r.getResults() == null || r.getResults().isEmpty());

        DomandaApi curr = r.getResults().get(0);

        // Traduce la domanda
        String domandaTradotta= " ";
        try {
             domandaTradotta = TraduttoreApi.traduci(curr.getQuestion()
                                            .replaceAll("&quot;", "\"")
                                            .replaceAll("&#039;", "'")
                                            .replaceAll("&#233;", "é")
                                            .replaceAll("&#225;", "á")
                                                        , "en", "it");
        } catch (TraduzioneException e) {
            System.err.println("Errore nella traduzione della domanda: " + e.getMessage());
        } catch (ConnectionError c){
            throw c;
        }

        Map<String, Boolean> risposteConFlag = new HashMap<>();// dizioanrio con chiave risposta e valore se e corretta o meno
        // Traduce la risposta corretta
        try {
            String rispostaCorrettaTradotta = TraduttoreApi.traduci(curr.getCorrect_answer()
                                                            .replaceAll("&quot;", "\"")
                                                            .replaceAll( "&#039;", "'"), 
                                                                "en", "it");
            risposteConFlag.put(rispostaCorrettaTradotta, true);
        } catch (TraduzioneException e) {
            System.err.println("Errore nella traduzione della domanda: " + e.getMessage());
        }

        // Traduce e aggiunge le risposte sbagliate
        try{
            for (String rispErrata : curr.getIncorrect_answers()) {
                risposteConFlag.put(TraduttoreApi.traduci(rispErrata
                                                .replaceAll("&quot;", "\"")
                                                .replaceAll( "&#039;", "'"), 
                                                    "en", "it"),false);
            }
        }catch(TraduzioneException e){
            System.err.println("Errore nella traduzione della domanda: " + e.getMessage());
        }
        List<String> risposteTradotte = new ArrayList<>(risposteConFlag.keySet());// lsita con le rispsote 
        // Mischia tutte le risposte

        int indiceCorretta = -1;
        Collections.shuffle(risposteTradotte);
        for (int i = 0; i < risposteTradotte.size(); i++) {
            if (risposteConFlag.get(risposteTradotte.get(i))) {
                indiceCorretta = i;
                break;
            }
        }
        // Crea e restituisce il DialogoQuiz
        DialogoQuiz d = new DialogoQuiz(domandaTradotta, risposteTradotte, indiceCorretta);
        return d;
    }
}
