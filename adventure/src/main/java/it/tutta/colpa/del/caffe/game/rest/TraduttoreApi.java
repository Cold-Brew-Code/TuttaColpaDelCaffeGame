package it.tutta.colpa.del.caffe.game.rest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import it.tutta.colpa.del.caffe.game.exception.TraduzioneException;
/**
 *
 * @author giova
 */
public class TraduttoreApi {

     //Classe interna che rappresenta  primo campo contenuto della risposta API
    private static class ResponseData {
        String translatedText; //il campo all'interno di ResponseData che contiene a sua volta un altro json 
    }

    // Classe interna che rappresenta la risposta data da MyMemory API (nel nostro caso ci interessa solo il primo)
    private static class MyMemoryResponse {
        ResponseData responseData; // il campo interessato 
        /* quotaFinished
           codeError ci dice il codice di errore spec
           responderId ecc*/
           int responseStatus; // 200== sei una bomba
           String responseDetails; //ci dice il tipo di errore del server 
    }

    /**
     * metodo statico che effettua una richiesta HTTP a MyMemory API per tradurre il testo
     *
     * @param testo il testo da tradurre
     * @param daLingua codice lingua iniziale
     * @param aLingua codice lingua destinazione 
     * @return testo tradotto
     */
    public static String traduci(String testo, String daLingua, String aLingua) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client
                .target("https://api.mymemory.translated.net/get")
                .queryParam("q", testo)
                .queryParam("langpair", daLingua + "|" + aLingua);

        Response resp = target.request(MediaType.APPLICATION_JSON).get();// risposta del server

        // controllo per la risposta  http 
        if (resp.getStatus() != 200) {
            return "errore risposta API: " + resp.getStatus();
        }

        Gson g = new Gson();
        // mi predno il contenuto della risposta api e la converto da json a oggetto java
        MyMemoryResponse rispostaApi= g.fromJson(resp.readEntity(String.class),MyMemoryResponse.class ); 
        // se la risposa http è ok ma c'è stato un problema con il server MyMemory API
            if(rispostaApi.responseStatus!=200){
                throw new TraduzioneException("Errore dalla MyMemory API: " + rispostaApi.responseDetails);
            }
            
        return rispostaApi.responseData.translatedText; // mi prendo il campo interessto 
    }
}
