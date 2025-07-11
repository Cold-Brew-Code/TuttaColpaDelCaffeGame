/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.tutta.colpa.del.caffe.rete;


import it.tutta.colpa.del.caffe.game.utility.Direzione;
import it.tutta.colpa.del.caffe.game.entity.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


/**
 * @author giova
 */
public class GestioneDB {

    private Connection con;
    private Properties credenziali;

    public GestioneDB() throws SQLException{
        connessioneDb();
    }

    // chiusura della connessione al database
    public void close() throws SQLException {
        con.close();
    }


    public void connessioneDb() throws SQLException {
        credenziali = new Properties();
        credenziali.setProperty("user", "tcdf");
        credenziali.setProperty("pw", "1234");
        con = DriverManager.getConnection("jdbc:h2:./database;INIT=RUNSCRIPT FROM 'classpath:inizioDB.sql'", credenziali);

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


    public GameMap creaMappa() throws SQLException {
        GameMap mappa = new GameMap();
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery("SELECT * FROM stanza");
        Map<Integer, Room> map = new HashMap();
        while (rs.next()) {
            Room stanza = creaStanza(rs);
            map.put(stanza.getId(), stanza);
            mappa.aggiungiStanza(stanza);
        }
        rs.close();
        stm.close();
        return creaCollegamentoS(mappa, map);
    }

    private GameMap creaCollegamentoS(GameMap mappa, Map<Integer,Room> map) throws SQLException {
        Room stanza1;
        Room stanza2;
        Direzione direzione;
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery("SELECT * FROM CollecgamentoStanze");
        while (rs.next()) {
            stanza1 = map.get(rs.getInt("idStanzaIniziale"));
            stanza2 = map.get(rs.getInt("idStanzaFinale"));
            if (rs.getString("direzione").equals("n"))
                direzione = Direzione.NORD;
            else if (rs.getString("direzione").equals("s"))
                direzione = Direzione.SUD;
            else if (rs.getString("direzione").equals("e"))
                direzione = Direzione.EST;
            else if (rs.getString("direzione").equals("o"))
                direzione = Direzione.OVEST;
            else if (rs.getString("direzione").equals("sopra"))
                direzione = Direzione.SOPRA;
            else if (rs.getString("direzione").equals("sotto"))
                direzione = Direzione.SOTTO;
            else
                direzione = null;
            mappa.collegaStanze(stanza1, stanza2, direzione);
        }
        rs.close();
        stm.close();
        return mappa;
    }


    //dizionario per l'alias dei comandi e oggetti


    private Set<String> getAliasC(int id) throws SQLException {
        return executeWithRetry(() -> {
            PreparedStatement pstm = con.prepareStatement("SELECT * FROM AliasComandi WHERE id = ?");
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
        Room stanza = new Room(rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("descrizioneIniziale"),
                rs.getString("immagine")
        );
        stanza.setLook(rs.getString("descrizioneAggiuntiva"));
        stanza.setDeniedEntry(!rs.getBoolean("aperta"));
        stanza.setVisible(rs.getBoolean("visibile"));
        stanza.setObjects(getOggettiStanza(rs.getInt("id")));
        stanza.setNPCs(getNpcStanza(rs.getInt("id")));
        return stanza;
    }

    //lista di oggetti di per ogni stanza
    private Map<AdvObject, Integer> getOggettiStanza(int id) throws SQLException {
        return executeWithRetry(() -> {
            Map<AdvObject, Integer> oggetti = new HashMap<>();
            PreparedStatement pstm = con.prepareStatement("SELECT " +
                    "    so.idStanza      AS so_idStanza, " +
                    "    so.idOggetto     AS so_idOggetto, " +
                    "    so.quantita      AS so_quantita, " +
                    "    o.id             AS o_id, " +
                    "    o.nome           AS o_nome, " +
                    "    o.descrizione    AS o_descrizione, " +
                    "    o.contenitore    AS o_contenitore, " +
                    "    o.apribile       AS o_apribile, " +
                    "    o.leggibile      AS o_leggibile, " +
                    "    o.cliccabile     AS o_cliccabile, " +
                    "    o.visibile       AS o_visibile, " +
                    "    o.componibile    AS o_componibile, " +
                    "    o.aperto         AS o_aperto, " +
                    "    o.raccoglibile   AS o_raccoglibile, " +
                    "    o.utilizzi       AS o_utilizzi, " +
                    "    o.immagine       AS o_immagine " +
                    "FROM stanza_oggetto  AS so " +
                    "INNER JOIN Oggetto   AS o ON so.idOggetto = o.id\n" +
                    "WHERE so.idStanza = ?;");
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                if (rs.getBoolean("o_contenitore")) {
                    AdvObjectContainer o = creaOggettoContenitore(rs);
                    oggettoContenitore(oggetti, rs.getInt("so_idOggetto"), o);
                } else {
                    oggetti.put(creaOggetto(rs), 1);
                }
            }
            rs.close();
            pstm.close();
            return oggetti;
        });
    }

    private List<NPC> getNpcStanza(int id) throws SQLException {
        List<NPC> listaNpc = new ArrayList<>();
        PreparedStatement pstm = con.prepareStatement("SELECT * FROM Npc WHERE idStanza=?");
        pstm.setInt(1, id);
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            NPC npc = creaNpc(rs);
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

    private NPC aggiungiDialoghiNPC(NPC n) throws SQLException {
        PreparedStatement pstm = con.prepareStatement(
                "SELECT d.id AS id_dialogo, g.id AS id_domanda, g.domanda AS domanda " +
                        "FROM Dialoghi AS d " +
                        "INNER JOIN DomandeDialoghi AS g " +
                        "ON d.id=g.dialogo  " +
                        "WHERE NPC=? ORDER BY d.id ASC;");
        pstm.setInt(1, n.getId());
        ResultSet rsDialoghi = pstm.executeQuery();
        Map<Integer,String> nodi = new HashMap<>();
        int dialogoID = -1;
        ResultSet rsRisposte = null;
        Dialogo d = new Dialogo();
        while (rsDialoghi.next()) {
            if ((dialogoID != rsDialoghi.getInt("id_dialogo")) || (dialogoID == -1)) {
                if (dialogoID != -1) {
                    while (rsRisposte.next()) {
                        d.addRisposta(nodi.get(rsRisposte.getInt("domanda_partenza")),
                                nodi.get(rsRisposte.getInt("domanda_arrivo")),
                                rsRisposte.getString("risposta")
                        );
                    }
                }
                dialogoID = rsDialoghi.getInt("id_dialogo");
                pstm = con.prepareStatement("SELECT * FROM RisposteDomande WHERE dialogo=?;");
                pstm.setInt(1, dialogoID);

                rsRisposte = pstm.executeQuery();
                n.addDialogo(d);
                d = new Dialogo();
            }
            nodi.put(rsDialoghi.getInt("id_domanda"), rsDialoghi.getString("domanda"));
            d.addDialogo(nodi.get(rsDialoghi.getInt("id_domanda")));
        }
        while (rsRisposte.next()) {
            d.addRisposta(nodi.get(rsRisposte.getInt("domanda_partenza")),
                    nodi.get(rsRisposte.getInt("domanda_arrivo")),
                    rsRisposte.getString("risposta")
            );
        }
        n.addDialogo(d);
        pstm.close();
        try {
            rsRisposte.close();
        } catch (Exception ignored) {
        }
        rsDialoghi.close();
        return n;
    }

    private void oggettoContenitore(Map oggetti, int idOggetto, AdvObjectContainer o) throws SQLException {
        Statement stm = con.createStatement();
        PreparedStatement pstm = con.prepareStatement("SELECT " +
                "    c.idOggetto1     AS so_idStanza, " +
                "    c.idOggetto1     AS so_idOggetto, " +
                "    c.quantita       AS so_quantita, " +
                "    o.id             AS o_id, " +
                "    o.nome           AS o_nome, " +
                "    o.descrizione    AS o_descrizione, " +
                "    o.contenitore    AS o_contenitore, " +
                "    o.apribile       AS o_apribile, " +
                "    o.leggibile      AS o_leggibile, " +
                "    o.cliccabile     AS o_cliccabile, " +
                "    o.visibile       AS o_visibile, " +
                "    o.componibile    AS o_componibile, " +
                "    o.utilizzi       AS o_utilizzi, " +
                "    o.aperto         AS o_aperto, " +
                "    o.raccoglibile   AS o_raccoglibile, " +
                "    o.immagine       AS o_immagine " +
                "FROM Contiene AS c " +
                "INNER JOIN Oggetto AS o ON c.idOggetto2 = o.id " +
                "WHERE c.idOggetto1 =?;");
        pstm.setInt(1, idOggetto);
        ResultSet rsContenitore = pstm.executeQuery();

        while (rsContenitore.next()) {
            if (rsContenitore.getBoolean("o_contenitore")) {
                o.add(creaOggettoContenitore(rsContenitore), rsContenitore.getInt("quantita"));
            } else {
                o.add(creaOggetto(rsContenitore), rsContenitore.getInt("quantita"));
            }
        }
        oggetti.put(o, 1);
        rsContenitore.close();
        stm.close();
    }

    public AdvObject getOggetto(int id) throws SQLException {
        return executeWithRetry(() -> {
            Map<AdvObject, Integer> oggetti = new HashMap<>();
            PreparedStatement pstm = con.prepareStatement("SELECT " +
                    "    o.id             AS o_id, " +
                    "    o.nome           AS o_nome, " +
                    "    o.descrizione    AS o_descrizione, " +
                    "    o.contenitore    AS o_contenitore, " +
                    "    o.apribile       AS o_apribile, " +
                    "    o.leggibile      AS o_leggibile, " +
                    "    o.cliccabile     AS o_cliccabile, " +
                    "    o.visibile       AS o_visibile, " +
                    "    o.componibile    AS o_componibile, " +
                    "    o.aperto         AS o_aperto, " +
                    "    o.raccoglibile   AS o_raccoglibile, " +
                    "    o.utilizzi       AS o_utilizzi, " +
                    "    o.immagine       AS o_immagine " +
                    "FROM Oggetto " +
                    "WHERE o.id = ?;");
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            AdvObject obj=null;
            if(rs.next()){
                if(rs.getBoolean("o_contenitore")){
                    obj = creaOggettoContenitore(rs);
                }else{
                    obj = creaOggetto(rs);
                }
            }
            rs.close();
            pstm.close();
            return obj;
        });
    }
    //creazione oggetto dal resultset
    private AdvObject creaOggetto(ResultSet rs) throws SQLException {
        AdvObject oggetto = new AdvObject(rs.getInt("o_id"),
                rs.getString("o_nome"),
                rs.getString("o_descrizione"),
                rs.getString("o_immagine"));
        oggetto.setOpenable(rs.getBoolean("o_apribile"));
        oggetto.setPickupable(rs.getBoolean("o_raccoglibile"));
        oggetto.setOpen(rs.getBoolean("o_aperto"));
        oggetto.setLeggibile(rs.getBoolean("o_leggibile"));
        oggetto.setCliccabile(rs.getBoolean("o_cliccabile"));
        oggetto.setVisibile(rs.getBoolean("o_visibile"));
        oggetto.setComponibile(rs.getBoolean("o_componibile"));
        oggetto.setUtilizzi(rs.getInt("o_utilizzi"));
        oggetto.setAlias(getAliasOgg(rs.getInt("o_id")));
        return oggetto;
    }

    private AdvObjectContainer creaOggettoContenitore(ResultSet rs) throws SQLException {
        AdvObjectContainer oggetto = new AdvObjectContainer(rs.getInt("o_id"),
                rs.getString("o_nome"),
                rs.getString("o_descrizione"),
                rs.getString("o_immagine"));
        oggetto.setOpenable(rs.getBoolean("o_apribile"));
        oggetto.setPickupable(rs.getBoolean("o_raccoglibile"));
        oggetto.setOpen(rs.getBoolean("o_aperto"));
        oggetto.setLeggibile(rs.getBoolean("o_leggibile"));
        oggetto.setCliccabile(rs.getBoolean("o_cliccabile"));
        oggetto.setVisibile(rs.getBoolean("o_visibile"));
        oggetto.setComponibile(rs.getBoolean("o_componibile"));
        oggetto.setUtilizzi(rs.getInt("o_utilizzi"));
        oggetto.setAlias(getAliasOgg(rs.getInt("o_id")));
        return oggetto;
    }


    //lista comandi
    public List<Command> getComandi() throws SQLException {
        return executeWithRetry(() -> {
            List<Command> comandi = new ArrayList<>();
            PreparedStatement pstm = con.prepareStatement("SELECT * FROM Comandi");
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Command c = new Command(rs.getString("nome"));
                c.setAlias(getAliasC(rs.getInt("id")).toArray(new String[0]));
                comandi.add(c);
            }
            rs.close();
            pstm.close();
            return comandi;
        });
    }


    // aggiornamento dialoghi

    public String aggiornaDialoghi(int id) throws SQLException {
        return executeWithRetry(() -> {
            PreparedStatement pstm = con.prepareStatement("SELECT * FROM Evento where id=?");
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
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