/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.other;



import it.tutta.colpa.del.caffe.adventure.entity.AdvObject;
import it.tutta.colpa.del.caffe.adventure.entity.GameMap;
import it.tutta.colpa.del.caffe.adventure.entity.Room;
import it.tutta.colpa.del.caffe.adventure.utility.Direzione;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;



/**
 *
 * @author giova
 */
public class GestioneDB {
    
    private static Connection con ;
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
            credenziali.setProperty("user", "cacca");
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
    throw new SQLException("Operation failed after " + max_retries + " attempts");
    }
    
    
    
    private GameMap creaMappa()throws SQLException{
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
            else if(rs.getString("direzione")== "s")
                    direzione= Direzione.SUD;
            else if(rs.getString("direzione")== "e")
                    direzione= Direzione.EST;
            else if(rs.getString("direzione")== "o")
                    direzione= Direzione.OVEST;
            else if(rs.getString("direzione")== "sopra")
                    direzione= Direzione.SOPRA;
            else if(rs.getString("direzione")== "sotto")
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
        stanza.setNome(rs.getString("nome"));
        stanza.setDescrizioneIniziale(rs.getString("descrizioneIniziale"));
        stanza.setDescrizioneAggiuntiva(rs.getString("descrizioneAggiuntiva"));
        stanza.setAperta(rs.getBoolean("aperto"));
        stanza.setVisibile(rs.getBoolean("visibile"));
        return stanza;
    }
    
//lista di oggetti di per ogni stanza
    private List<AdvObject> getOgettoStanza(int id) throws SQLException {
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
    private List<String> getComandi(int id) throws SQLException {
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