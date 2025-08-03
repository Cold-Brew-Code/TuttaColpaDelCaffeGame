package it.tutta.colpa.del.caffe;

import it.tutta.colpa.del.caffe.game.entity.DialogoQuiz;
import it.tutta.colpa.del.caffe.game.exception.ErrorConnection;
import it.tutta.colpa.del.caffe.game.rest.QuizNpc;

public class MainQuizTest {
    public static void main(String[] args) {
        try {
            DialogoQuiz quiz = QuizNpc.getQuiz();

            System.out.println("Domanda: " + quiz.getDomanda());
            System.out.println("Risposte possibili:");
            for (int i = 0; i < quiz.getRisposte().size(); i++) {
                System.out.println((i + 1) + ". " + quiz.getRisposte().get(i));
            }
             System.out.println("id corretto: " + quiz.getIdCorretta());
            System.out.println("Messaggio in caso di successo: " + quiz.getMessaggioCorret());
            System.out.println("Messaggio in caso di errore: " + quiz.getMessaggioErrato());
        }catch(ErrorConnection e){
            System.out.println("Sei offline! Riprova quando sei connesso.");

        }
        catch (Exception e) {
            System.err.println("Errore durante il recupero del quiz: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
