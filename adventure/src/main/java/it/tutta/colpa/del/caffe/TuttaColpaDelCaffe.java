package it.tutta.colpa.del.caffe;

import it.tutta.colpa.del.caffe.rete.Server;
import it.tutta.colpa.del.caffe.start.StartHandler;

/**
 * @author giovav
 * @since 18/07/25
 */
public class TuttaColpaDelCaffe {
    public static void main(String[] args){
        StartHandler.main(args);
        Server.main(args);
    }
}
