/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.adventure.other;



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
    
    
    
    public Connection getConnection() throws SQLException {
    if (con != null) {
        return con;
    } else {
        throw new SQLException("Connessione non inizializzata: chiama prima connessioneDb().");
    }
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

    //dizionario per l'alias dei comandi e oggetti
   
    
    private Set<String> getaliasC(int id) throws SQLException{
        Statement stm= con.createStatement();
        ResultSet rs= stm.executeQuery("SELECT * FROM Alias WHERE id = " + id + ";");
        Set<String> aliasC= new HashSet<>();
        while(rs.next()){
            aliasC.add(rs.getString("alias"));
        }
        rs.close();
        stm.close();
        return aliasC;
    }

    private Set<String> getaliasOgg(int id) throws SQLException{
        Statement stm= con.createStatement();
        ResultSet rs= stm.executeQuery("SELECT * FROM Alias WHERE id = " + id + ";");
        Set<String> aliasOg= new HashSet<>();
        while(rs.next()){
            aliasOg.add(rs.getString("alias"));
        }
        rs.close();
        stm.close();
        return aliasOg;
    }

    private Stanza creaStanza(int id) throws SQLException{
        Statement stm= con.createStatement();
        PreparedStatement pstm= con.prepareStatement("SELECT * FROM Stanza WHERE id = ?");
        pstm.setInt(1, id);
        ResultSet rs= pstm.executeQuery();
        while(rs.next()){
            stanza = new Stanza(rs.getInt("id"), rs.getString("nome"),rs.getString("descrizioneIniziale"),rs.getString("descrizioneFinale"), rs.getBoolean("aperta"), rs.getBoolean("visibile"));
        }
        rs.close();
        stm.close();
        return stanza;
    }
    
    
    
    
    private Stanza creaStanza(ResultSet rs) throws SQLException {
        Stanza stanza = new Stanza(rs.getInt("id"));
        stanza.setNome(rs.getString("nome"));
        stanza.setDescrizioneIniziale(rs.getString("descrizioneIniziale"));
        stanza.setDescrizioneAggiuntiva(rs.getString("descrizioneAggiuntiva"));
        stanza.setAperta(rs.getBoolean("aperto"));
        stanza.setVisibile(rs.getBoolean("visibile"));
        return stanza;
    }
    
//lista di oggetti di per ogni stanza
    private List<AdvObject> getOgettoStanza(int id) throws SQLException{
        List<AdvObject> oggettList = new ArrayList<>();
        Statement stm= con.createStatement();
        PreparedStatement pstm= con.prepareStatement("SELECT * FROM stanza_oggetto AS so JOIN oggetto AS o ON so.idOggetto= o.id where so.idStanza= ?");
        pstm.setInt(1,id);
        ResultSet rs= pstm.executeQuery();
        while(rs.next()){
            AdvObject oggetto= creaOggetto(rs);
            Integer quantità = rs.getInt("quantita");
            for (int i = 0; i < quantità; i++)
                oggettList.add(oggetto);
        }
        pstm.close();
        rs.close();
        stm.close();
        return oggettList;
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
        return oggetto;
    }
    
}
