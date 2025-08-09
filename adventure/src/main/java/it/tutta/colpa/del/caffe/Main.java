package it.tutta.colpa.del.caffe;
import java.util.List;

import it.tutta.colpa.del.caffe.game.entity.Dialogo;
import it.tutta.colpa.del.caffe.game.entity.NPC;
import it.tutta.colpa.del.caffe.rete.DataBaseManager;

public class Main {
    public static void main(String[] args) {
        try {
            DataBaseManager dbm = new DataBaseManager();

            List<NPC> items = dbm.askForNonPlayerCharacters();


            System.out.println("📦 Tutti gli oggetti nel database:");
            System.out.println("======================================");

            for (NPC item : items) {
                System.out.println(" Nome: " + item.getNome());
               // System.out.println(" Descrizione: " + item.getDescription());
                System.out.println("alias " + item.getAlias());
                for (Dialogo dialogo : item.getDialoghi()) {
                    System.out.println(" Dialogo ID: " + dialogo.getId());
                    //wdialogo.stampaDialogo();
                }

/*
                if (item instanceof ItemContainer container) {
                    System.out.println("📁 Tipo: Contenitore");
                    System.out.println("📦 Contenuto:");
                    for (Map.Entry<GeneralItem, Integer> entry : container.getList().entrySet()) {
                        System.out.println("    🔹 " + entry.getKey().getName() + " x" + entry.getValue());
                    }
                    
                } else if (item instanceof IteamCombinable combinable) {
                    System.out.println("🔧 Tipo: Componibile");
                    System.out.println("🧩 Componenti:");
                    for (Item comp : combinable.getComposedOf()) {
                        System.out.println("    🔸 " + comp.getName());
                    }
                } else if (item instanceof ItemRead readable) {
                    System.out.println("📖 Tipo: Leggibile");
                    System.out.println("📚 Contenuto: " + readable.getContent());
                    System.out.println("merda  "+ readable.isVisibile());
                } else {
                    System.out.println("📦 Tipo: Oggetto semplice");
                }
*/
                System.out.println("--------------------------------------");
            }

            dbm.closeConnection();

        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
    }
}
