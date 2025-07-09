/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.other;



import it.tutta.colpa.del.caffe.adventure.entity.*;
import it.tutta.colpa.del.caffe.adventure.utility.Direzione;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


/**
 *
 * @author giova
 */
public class GestioneDB {
    
    private static Connection con;
    private Properties credenziali;
    
    
    // restituisce la connessione al database
    public Connection getConnection() throws SQLException {
    if (con != null) {
        return con;
    } else {
        throw new SQLException("Connessione non inizializzata: chiama prima connessioneDb().");
    }
}

// chiusura della connessione al database
    public void close() throws SQLException{
        con.close();
    }


    
    public void connessioneDb()throws SQLException
    {
        try{
            credenziali= new Properties();
            credenziali.setProperty("user", "tcdf");
            credenziali.setProperty("pw", "1234");
            con =DriverManager.getConnection("jdbc:h2:./database", credenziali);
        }catch(SQLException ex){
            throw ex;
        }
    }
    
    
    
    private <T> T executeWithRetry(SqlFunction<T> function) throws SQLException {
    int retryCount = 0, max_retries = 5;
    while (retryCount < max_retries) {
        try {
            return function.apply();
        } catch (SQLException e) {
            if (e.getSQLState().equals("08003")) { // connessione chiusa o non valida
                connessioneDb(); // tentativo di ristabilire la connessione
            } else {
                throw e; // altro errore → rilancio subito
            }
            retryCount++;
        }
    }
    throw new SQLException("Operazione fallita dopo " + max_retries + " tentativi.");
    }
    
    
    
    public GameMap creaMappa()throws SQLException{
        GameMap mappa= new GameMap(); 
        Statement stm= con.createStatement();
        ResultSet rs= stm.executeQuery("SELECT * FROM stanza");
        Room array[]= new Room[30];
        while(rs.next()){
            Room stanza= creaStanza(rs);
            array[stanza.getId()]= stanza;
            mappa.aggiungiStanza(stanza);
        }
        rs.close();
        stm.close();
        return creaCollegamentoS(mappa,array);
    }
    
    private GameMap creaCollegamentoS(GameMap mappa, Room array[]) throws SQLException{
        Room stanza1;
        Room stanza2;
        Direzione direzione;
        Statement stm= con.createStatement();
        ResultSet rs= stm.executeQuery("SELECT * FROM CollecgamentoStanze");
        while(rs.next()){
            stanza1=array[rs.getInt("idStanzaIniziale")];
            stanza2=array[rs.getInt("idStanzaFinale")];
            if (rs.getString("direzione")== "n")
                direzione= Direzione.NORD;
            else if(rs.getString("direzione").equals("s"))
                    direzione= Direzione.SUD;
            else if(rs.getString("direzione").equals("e"))
                    direzione= Direzione.EST;
            else if(rs.getString("direzione").equals("o"))
                    direzione= Direzione.OVEST;
            else if(rs.getString("direzione").equals("sopra"))
                    direzione= Direzione.SOPRA;
            else if(rs.getString("direzione").equals("sotto"))
                    direzione= Direzione.SOTTO;
            else 
                direzione=null;
            mappa.collegaStanze(stanza1, stanza2, direzione);

        }
        rs.close();
        stm.close();
        return mappa;
    }

    

    //dizionario per l'alias dei comandi e oggetti
   
    
    private Set<String> getAliasC(int id) throws SQLException {
    return executeWithRetry(() -> {
        PreparedStatement pstm = con.prepareStatement("SELECT * FROM Alias WHERE id = ?");
        pstm.setInt(1, id);
        ResultSet rs = pstm.executeQuery();
        
        Set<String> aliasC = new HashSet<>();
        while (rs.next()) {
            aliasC.add(rs.getString("alias"));
        }
        
        rs.close();
        pstm.close();
        return aliasC;
    });
}


    private Set<String> getAliasOgg(int id) throws SQLException {
    return executeWithRetry(() -> {
        PreparedStatement pstm = con.prepareStatement("SELECT * FROM Alias WHERE id = ?");
        pstm.setInt(1, id);
        ResultSet rs = pstm.executeQuery();
        
        Set<String> aliasOg = new HashSet<>();
        while (rs.next()) {
            aliasOg.add(rs.getString("alias"));
        }
        
        rs.close();
        pstm.close();
        return aliasOg;
    });
}

              

    
    
    private Room creaStanza(ResultSet rs) throws SQLException {
        Room stanza = new Room(rs.getInt("id"));
        stanza.setName(rs.getString("nome"));
        stanza.setDescription(rs.getString("descrizioneIniziale"));
        stanza.setLook(rs.getString("descrizioneAggiuntiva"));
        if(rs.getBoolean("aperto")){
            stanza.setDeniedEntry(false);
        }else{
            stanza.setDeniedEntry(true);
        }

        stanza.setVisible(rs.getBoolean("visibile"));
        return stanza;
    }
    
//lista di oggetti di per ogni stanza
    private List<AdvObject> getOggettoStanza(int id) throws SQLException {
        return executeWithRetry(() -> {
            List<AdvObject> oggettList = new ArrayList<>();
            PreparedStatement pstm = con.prepareStatement("SELECT * FROM stanza_oggetto AS so JOIN oggetto AS o ON so.idOggetto = o.id WHERE so.idStanza = ?");
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                if(rs.getBoolean("contenitore")){
                    oggettoContenitore(oggettList,rs.getInt("idOggetto"));
                }
                AdvObject oggetto = creaOggetto(rs);
                int quantita = rs.getInt("quantita");
                for (int i = 0; i < quantita; i++) {
                    oggettList.add(oggetto);
                }
            }
            rs.close();
            pstm.close();
            return oggettList;
        });
    }
    
    public List<NPC> getNpcStanza(int id) throws SQLException{
        List<NPC> listaNpc= new ArrayList<>();
        PreparedStatement pstm = con.prepareStatement("SELECT * FROM Npc WHERE idStanza=?");
        pstm.setInt(1,id);
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            NPC npc= creaNpc(rs);
            listaNpc.add(npc);
        }
        rs.close();
        pstm.close();
        return listaNpc;

    }

    private NPC creaNpc(ResultSet rs) throws SQLException {
        NPC npc = new NPC(rs.getInt("id"), rs.getString("nome"));
        return aggiungiDialoghiNPC(npc);
    }

    private NPC aggiungiDialoghiNPC(NPC n) throws SQLException{
        PreparedStatement pstm = con.prepareStatement("SELECT d.id AS id_dialogo, g.id AS id_domanda, g.domanda AS domanda" +
                                                          "FROM Dialoghi AS d " +
                                                          "INNER JOIN DomandeDialoghi AS g " +
                                                          "ON d.id=g.dialogo  " +
                                                          "WHERE NPC=? ORDER BY d.id ASC;");
        pstm.setInt(1, n.getId());
        ResultSet rsDialoghi = pstm.executeQuery();
        pstm.close();
        List<String> nodi=new ArrayList<>();
        int dialogoID=-1;
        ResultSet rsRisposte=null;
        Dialogo d=new Dialogo();
        while(rsDialoghi.next()){
            if((dialogoID!=rsDialoghi.getInt("id_dialogo")) || (dialogoID==-1)){
                if(dialogoID!=-1){
                    while(rsRisposte.next()){
                        d.addRisposta(nodi.get(rsRisposte.getInt("domanda_partenza")),
                                      nodi.get(rsRisposte.getInt("domanda_arrivo")),
                                      rsDialoghi.getString("risposta")
                                    );
                    }
                }
                dialogoID=rsDialoghi.getInt("id_dialogo");
                pstm = con.prepareStatement("SELECT * FROM RisposteDomande WHERE dialogo=?;");
                pstm.setInt(1, dialogoID);

                rsRisposte = pstm.executeQuery();
                n.addDialogo(d);
                d=new Dialogo();
            }
            nodi.add(rsDialoghi.getInt("id_domanda"),rsDialoghi.getString("domanda"));
            d.addDialogo(nodi.get(rsDialoghi.getInt("id_domanda")));
        }
        pstm.close();
        rsRisposte.close();
        rsDialoghi.close();
        return n;
    }

    private void oggettoContenitore(List<AdvObject> oggettList, int idOggetto) throws SQLException {
        Statement stm = con.createStatement();
        ResultSet rsContenitore = stm.executeQuery("SELECT * FROM Contiene WHERE idOggetto1="+ idOggetto+";");

        while (rsContenitore.next()) {
            AdvObject oggettoContenitore = creaOggetto(rsContenitore);
            oggettList.add(oggettoContenitore);
        }
        rsContenitore.close();
        stm.close();
    }


//creazione oggetto dal resulset 
    private AdvObject creaOggetto(ResultSet rs) throws SQLException {
        AdvObject oggetto = new AdvObject(rs.getInt("id"), rs.getString("nome"), rs.getString("descrizione"));
        oggetto.setOpenable(rs.getBoolean("aperto"));
        oggetto.setPickupable(rs.getBoolean("raccoglibile"));
        oggetto.setOpen(rs.getBoolean("aperibile"));
        oggetto.setLeggibile(rs.getBoolean("leggibile"));
        oggetto.setCliccabile(rs.getBoolean("cliccabile"));
        oggetto.setVisibile(rs.getBoolean("visibile"));
        oggetto.setComponibile(rs.getBoolean("componibile"));
        oggetto.setUtilizzi(rs.getInt("utilizzi"));
        oggetto.setContenitore(rs.getBoolean("componibile"));
        return oggetto;
    }


    //lista comandi 
    public List<String> getComandi(int id) throws SQLException {
    return executeWithRetry(() -> {
        List<String> comandi = new ArrayList<>();
        PreparedStatement pstm = con.prepareStatement("SELECT * FROM Comandi WHERE id = ?");
        pstm.setInt(1, id);
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            comandi.add(rs.getString("nome"));
        }
        rs.close();
        pstm.close();
        return comandi;
    });
}


    // aggiornamento dialoghi
    
    public String aggiornaDialoghi(int id) throws SQLException {
    return executeWithRetry(() -> {
        PreparedStatement pstm= con.prepareStatement("SELECT * FROM Evento where id=?");
        pstm.setInt(1,id);
        ResultSet rs=pstm.executeQuery();
        if(rs.next()){
            return rs.getString("descrizioneAggiornata");
        }
        rs.close();
        pstm.close();
        return null;
    });
}
    

    
    
    @FunctionalInterface
    /**
     * Interfaccia funzionale per operazioni SQL.
     * 
     */
    private interface SqlFunction<T> {
        /**
         * Esegue l'operazione SQL e restituisce un risultato.
         *
         * @return il risultato dell'operazione SQL
         * @throws SQLException se si verifica un errore durante l'esecuzione
         */
        T apply() throws SQLException;
    }
    
}