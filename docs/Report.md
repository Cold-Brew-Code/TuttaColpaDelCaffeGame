<div align="center">
    <h1>Tutta colpa del caffè</h1>
    <img src="img/icon.png" alt="Icona Gioco" width="25%"/>
    <h3>Esame di Metodi Avanzati di Programmazione</h3>
    <h3>(track M-Z)</h3>
    <h3>A.A. 2024-2025</h3>

</div>

## Caso di studio a cura di
- Patruno Mirko, [@]()
- Vendola Giovanni, [@Giovanni0910](http://github.com/Giovanni0910)
- Vittore Giovanni, [@giovav](http://github.com/giovav)

---
## Indice
- ### [Descrizione dell'avventura](#descrizione-dellavventura-1)
- ### [Progettazione](#progettazione-1)
  - #### [Competenze](#competenze-1)
  - #### [Organizzazione in packages](#organizzazione-in-packages-1)
- ### [Diagramma delle classi](#diagramma-delle-classi-1)
- ### [Specifica algebrica](#specifica-algebrica-1)
- ### [Dettagli implementativi](#dettagli-implementativi-1)
- ### [Modo più veloce per vincere](#modo-più-veloce-per-vincere-il-gioco)
---

# Descrizione dell’avventura
Tratto (quasi) da una storia vera.
È una calda 🥵 mattina di luglio. Uno studente di Informatica si sta dirigendo al Dipartimento per sostenere uno degli esami più temuti del corso di laurea: Metodi Avanzati di Programmazione.

Tutto sembra andare secondo i piani... finché, non appena varcato l'ingresso del campus, viene colto da un’improvvisa, impellente esigenza fisiologica 😰.

Inizia così un'odissea tragicomica tra i corridoi dell’università. Nessun bagno sembra essere facilmente accessibile, ogni porta è chiusa, ogni indicazione fuorviante. Lo studente dovrà esplorare a fondo il campus, raccogliere indizi, affrontare dialoghi surreali e cercare aiuto da personaggi secondari come studenti fuori corso, baristi svogliati, inservienti criptici e persino macchinette del caffè apparentemente senzienti.

Riuscirà a trovare un bagno funzionante prima che sia troppo tardi? E soprattutto, ce la farà ad arrivare in tempo all’esame senza compromettere il proprio futuro accademico?

Un’avventura testuale tra il grottesco e il quotidiano, dove ogni scelta può fare la differenza.

Preparati a ridere, riflettere... e correre💨.


# Progettazione

*< Fornire dettagli sulla progettazione. Come sono state individuate le classi, quali sono le competenze di ogni classe, come sono state organizzate le classi in package. >*
### Individuazione delle classi

### Competenze
La suddivisione delle competenze o **responsabilità** delle classi viene effettuata secondo il principio di presentazione separata **Entity, Control, Boundary** (**ECB**), dove ogni classe ha una propria responsabilità.
Più precisamente, le responsabilità costituiscono il ciò che un’istanza di una classe è destinato a fare.
Andando ad assegnare responsabilità precise per ogni classe si va a rendere le classi invarianti ai cambiamenti tra loro.

In questo progetto le classi sono state suddivise per competenze all'interno di relativi packages (entity, control, boundary) e, a livello superiore, è stata effettuata un'ulteriore raggruppamento per "aree" dell'applicativo: `game`, `start`, `loadAndSave` e `rete`; in questo modo abbiamo ottenuto una suddivisione per competenze corretta e, inoltre, la facilità di **individuare** le classi corrette per ogni area dell'applicativo.

### Organizzazione in packages
Per praticità e per coerenza con la suddivisione ECB delle competenze, come già specificato in precedenza, questo progetto è stato scomposto in più package.
Inizialmente il progetto è stato scomposto in **4 packages** principali, che hanno dato vita ai seguenti **4 namespaces** principali:
- `it.tutta.colpa.del.caffe.game`: contiene tutte le classi che permettono il funzionamento e la rappresentazione della partita vera e propria. Il package contiene anche tutte le interfacce grafiche (GUI) che permettono all'utente di interfacciarsi con la partita.
- `it.tutta.colpa.del.caffe.loadsave`: contiene tutte le classi che permettono il funzionamento del caricamento e del salvataggio di una partita in memoria (su file come chiariremo in seguito) e le classi che permettono la visualizzazione della GUI che permette all'utente di poter selezionare e caricare un salvataggio dal disco rigido.
- `it.tutta.colpa.del.caffe.rete`: contiene tutti gli strumenti necessari al funzionamento di un piccolo server che gestisce un database per rispondere alle richieste di uno (o volendo anche più di uno) client(s), i quali richiedono le informazioni iniziali per poter istanziare correttamente una partita in memoria e/o aggiornare i dati già scaricati ad inizio partita qualora fosse necessario.
- `it.tutta.colpa.del.caffe.start`: contiene tutte le classi per poter rappresentare e gestire il launcher del gioco.
All'interno del namespace principale `it.tutta.colpa.del.caffe`, il package che contiene tutti gli altri packages, è presente anche la classe `TuttaColpaDelCaffe`, classe **static** che rappresenta l'access point per l'intero gioco. All'interno della classe, infatti, viene avviato la classe `StartHandler`, che collega il controller alla GUI iniziale, e la classe `Server`, anch'esse entrambe statiche.
I packages `game`, `loadsave` e `start` vengono suddivisi in ulteriori sotto-packages, principalmente `entity`, `boundary` e `control` (fatta eccezione per le aree che non necessitano la memorizzazione dei dati su disco ove, ovviamente, non viene inserito il package `entity`).
Il package `game`, a differenza degli altri package, sviluppa una struttura di sotto-packages più complessa, infatti include, oltre ai packages citati in precedenza, anche:
- Il package `exception` che contiene tutti gli **errori** ed **eccezioni** che si possono verificare a runtime durante la partita. Le eccezioni sono importanti al fine di poter gestire uno specifico errore sempre durante il corso della partita;
- Il package `rest` che contiene tutte le classi necessarie all'implementazione del quiz finale del gioco che, se passato, porta alla vittoria. Il package prevede le classi che permettono di effettuare una richiesta rest ad un'API di *trivia games* e successivamente, presa la risposta della prima API, effettuare una seconda richiesta ad un'API traduttore che traduce la risposta della prima API in italiano;
- Il package `utility` che contiene tutte le classi di supporto all'implementazione della partita, come il `Parser`(che non necessita di presentazioni), tipi `Enum` vari, il `Clock` per implementare il timer del gioco, `TypeWriterEffect` che implementa l'effetto "macchina da scrivere" nella GUI e la classe `Audio manager` che gestisce l'audio della partita.

Il package `rete` non viene suddiviso ulteriormente in packages perché tutte le classi che contiene sono di tipo `control` (non c'è nessuna necessità - se non per info di debug già provviste - di comunicare con l'esterno per quest'area dell'applicativo).

Ogni package `game`, `loadsave` e `start` ha la propria classe `Handler` che si occupa di gestire il **binding** tra la GUI e il proprio controller. Questo può essere fatto molto facilmente, pur mantenendo un **basso accoppiamento**, come le buone norme dell'ingegneria del software prevedono, mediante l'uso di **Interfacce**.
In java le interfacce costituiscono la specifica sintattica di metodi che una classe andrà a implementare e sono un meccanismo molto potente. Applicando il polimorfismo ad oggetti che implementano le interfacce è possibile utilizzare gli oggetti senza conoscerne il reale tipo, per questo se in futuro verranno effettuate modifiche di implementazione alle GUI o ai Controller, nessuna controparte necessiterà di ulteriori modifiche (**codice invariante rispetto ai cambiamenti**).

Segue un breve riassunto delle competenze di ciascuna classe, raggruppate per packages:
- **`it.tutta.colpa.del.caffe.game`**:
  - `GameHandler`: Classe che fa il binding di GUI e controller della partita. Starta effettivamente la partita.
  - **`it.tutta.colpa.del.caffe.game.boundary`**:
    - `DialogueGUI`: Interfaccia che specifica i metodi necessari, privi di implementazione, che la GUI che mostra i dialoghi dovrà implementare;
    - `DialoguePage`: GUI che mostra un dialogo e permette al player di interfacciarsi con lo stesso (implementazione di DialogueGUI);
    - `GameEndedPage`: GUI che mostra lo scenario di fine partita (diviso per vittoria e sconfitta);
    - `GameGUI`: Interfaccia che specifica i metodi necessari, privi di implementazione, che la GUI che mostra la partita dovrà implementare;
    - `GamePage`: GUI che permette all'utente di interfacciarsi con la partita;
    - `GUI`: Interfaccia dalla quale ereditano tutte le altre interfacce-GUI. Contiene i metodi essenziali di un'interfaccia grafica comune. Necessaria per il principio di sostituibilità;
    - `InventoryPage`: GUI che mostra il contenuto dell'inventario e la descrizione degli oggetti che il player ha inserito nello stesso;
    - `MapPage`: GUI che mostra la mappa del gioco a seguito del prompt `mappa`, da parte dell'utente nella console di gioco (in `GamePage`).
  - **`it.tutta.colpa.del.caffe.game.control`**:
    - `Controller`: Interfaccia che contiene i metodi essenziali di un controller di una GUI;
    - `GameController`: Estensione dell'interfaccia controller, include le specifiche sintattiche di metodi necessari a comunicare adeguatamente con l'interfaccia di gioco;
    - `Engine`: Classe che gestisce l'intera logica della partita, caricandola o istanziandola adeguatamente;
    - `DialogueController`: Estensione dell'interfaccia controller, include le specifiche sintattiche di metodi necessari a comunicare adeguatamente con l'interfaccia che mostra i dialoghi;
    - `BuildObserver`: Classe che gestisce la logica del comando `costruisci`;
    - `LeaveObserver`: Classe che gestisce la logica del comando `lascia`;
    - `LiftObserver`: Classe che gestisce la logica del comando `ascensore` (o `sali/scendi` più comunemente);
    - `LookAtObserver`: Classe che gestisce la logica del comando `osserva`;
    - `MoveObserver`: Classe che gestisce la logica di movimento all'interno della mapppa;
    - `OpenObserver`: Classe che gestisce l'apertura di oggetti contenitore (comando `apri`);
    - `PickUpObserver`: Classe che gestisce la raccolta di oggetti mediante il comando `prendi`;
    - `ReadObserver`: Classe che gestisce la logica del comando `leggi`, potendo così mostrare il contenuto degli oggetti leggibili;
    - `ServerInterface`: Classe che permette di interfacciarsi correttamente con il server per effettuare correttamente tutte le richieste necessarie ad istanziare una partita o ad aggiornare lo stato del gioco (se necessario);
    - `TalkObserver`: Classe che gestisce i dialoghi e le interazioni con gli Non Player Characters nel gioco;
    - `UseObserver`: Classe che gestisce la logica del comando `usa`, permettendo al player di usare gli oggetti;
  - **`it.tutta.colpa.del.caffe.game.entity`**:
    - `CombinableItem`: Classe che rappresenta gli oggetti combinabili;
    - `Command`: Classe che rappresenta i comandi del gioco;
    - `ContainerItem`: Classe che rappresenta gli oggetti contenitore;
    - `DialogoQuiz`: Classe che rappresenta i dialoghi-quiz presi dal web tramite richiesta RESTful;
    - `Dialogue`: Classe che rappresenta gli oggetti combinabili;
    - `GameDescription`: Classe che rappresenta la descrizione del gioco. È un'implementazione di GameObservable;
    - `GameMap`: Classe che rappresenta la mappa di gioco;
    - `GameObservable`: Interfaccia che rappresenta i metodi necessari ad una classe per poter essere osservata dal gioco mediante gli Observers;
    - `GameObserver`: Interfaccia che rappresenta il generico observer;
    - `GeneralItem`: Classe che rappresenta un oggetto di tipo generico, dal quale ereditano classi specifiche: `ContainerItem` e `Item`;
    - `Inventory`: Classe che rappresenta l'inventario;
    - `Item`: Classe che rappresenta un oggetto nel gioco;
    - `NPC`: Classe che rappresenta un NPC nel gioco;
    - `ReadableItem`: Classe che rappresenta un oggetto leggibile, estende `Item`;
    - `Room`: Classe che rappresenta una stanza della mappa di gioco;
  - **`it.tutta.colpa.del.caffe.game.exception`**:
    - `ConnectionError`: Eccezione sollevata in caso di errori di connessione nella programmazione di rete;
    - `DialogueException`: Eccezione sollevata in caso di errori runtime con i dialoghi;
    - `GameMapException`: Eccezione sollevata in caso di errori con il reperimento e la gestione delle stanze all'interno della mappa di gioco;
    - `ImageNotFoundException`: Eccezione sollevata in caso di immagini non trovate all'interno della cartella `.../resources/images`;
    - `InventoryException`: Eccezione sollevata nel caso in casi in cui la gestione dell'inventario fallisce (inventario pieno, oggetto non presente, ...);
    - `ItemException`: Eccezione sollevata nel caso di errori con la gestione di oggetti nel gioco;
    - `ParserException`: Eccezione sollevata nel caso in cui il parser non dovesse riconoscere un comando;
    - `ServerComunicationException`: Eccezione sollevata nel caso di problemi con la comunicazione con il server di gioco;
    - `TraduzioneException`: Eccezione specifica sollevata nel caso di problemi con l'API REST di traduzione;
  - **`it.tutta.colpa.del.caffe.game.rest`**:
    - `QuizNpc`: Classe che si interfaccia con l'API RESTful `opentdb.com` per ottenere i quiz da passare come esame finale;
    - `TraduttoreApi`: Classe che si interfaccia con l'API RESTful `api.mymemory.translated.net` per la traduzione del quiz in italiano;
  - **`it.tutta.colpa.del.caffe.game.utility`**:
    - `ArcoGrafo`: Classe che estende `DefaultEdge` permettendo di usare, all'interno del grafo utilizzato per implementare  `GameMap`, il tipo `Direzione` come etichetta;
    - `AudioManager`: Classe che gestisce la musica di gioco all'internod delle interfacce;
    - `Clock`: Classe che permette l'esecuzione di un Thread parallelo per la gestione del Timer di gioco;
    - `CommandType`: Enumerativo che rappresenta i tipi di comando che un utente può inserire;
    - `Direzione`: Enumerativo che rappresenta la direzione le direzioni all'interno della mappa di gioco;
    - `GameStatus`: Enumerativo che rappresenta lo stato di gioco;
    - `GameUtils`: Classe che contiene metodi di utility per la gestione della partita;
    - `Parser`: Classe che provvede al parsing dei comandi e all'istanziazione di un oggetto `ParserOutput` processabile dagli Observers;
    - `ParserOutput`: Classe che rappresenta l'output del parser;
    - `RequestType`: Enumerativo che rappresenta il tipo di richiesta che il client può fare al server;
    - `StringArcoGrafo`: Classe che estende `DefaultEdge` permettendo di usare `String` come etichetta di un grafo (usato nei dialoghi);
    - `TimeObserver`: Interfaccia che fornisce la specifica sintattica dei metodi che servono al controller per comunicare direttamente con il thread del timer di gioco;
    - `TypeWriterEffect`: Classe che permette l'implementazione dell'effetto TypeWriter all'interno delle interfacce grafiche;
    - `Utils`: Classe che contiene metodi di utility per la gestione della partita;
- **`it.tutta.colpa.del.caffe.loadsave`**:
  - `ChoseSaveHandler`:
  - **`it.tutta.colpa.del.caffe.loadsave.boundary`**
  - **`it.tutta.colpa.del.caffe.loadsave.control`**
- **`it.tutta.colpa.del.caffe.rete`**:
  - `ClientHandler`: Classe che gestisce, con l'ausilio di threads, i client che si connettono al server. Ogni thread, istanza di questa classe, viene lanciato dal `Server` e si occupa di gestire la comunicazione con il client e provvedere ad una risposta;
  - `DataBaseManager`: Classe che utilizza JDBC per interfacciarsi con il DataBase del gioco gestendo connessione e query di inizializzazione degli altri oggetti del gioco;
  - `Server`: Classe che resta in attesa di client;
- **`it.tutta.colpa.del.caffe.start`**:
- `StartHandler`: Classe che fa il binding tra la GUI della schermata iniziale e il suo Controller;
  - **`it.tutta.colpa.del.caffe.start.boundary`**:
    - `MainPage`: GUI della schermata iniziale del gioco, il launcher;
  - **`it.tutta.colpa.del.caffe.start.control`**:
    - `Engine`: Controller della GUI del gioco, gestisce le operazioni conseguenti alle scelte effettuate dall'utente;
    - `MainPageController`: Estensione dell'interfaccia controller, fornisce una specifica sintattica dei metodi che deve avere il `MainPageController` per interfacciarsi con la GUI;

> Per maggiori informazioni riguardanti le classi e le proprie competenze specifiche si rimanda alla [**javadoc**](javadoc/index.html) di questo progetto.

## Diagramma delle classi
*<-- Inserire una diagramma delle classi di una porzione significativa del progetto e commentare il diagramma fornendo dettagli sui principi della programmazione ad oggetti che sono stati utilizzati (ereditarietà, interfacce, classi astratte, composizione, …) -->*

## Specifica algebrica
Di seguito riportiamo la specifica algebrica della struttura dati **Dizionario** (in java chiamata `Map`), utilizzato all'interno del progetto in alcuni contesti importanti, come per la gestione dell'inventario e anche come struttura dati ausiliaria in molti contesti (in particolare in `DataBaseManager`).

### Sort Necessari
- Chiave
- Valore
- Dizionario - Dato astratto che stiamo definendo
- boolean - Sort ausiliario

### Specifica Sintattica
- $$CreaDizionario()\rightarrow Dizionario$$
- $$DizionarioVuoto(Dizionario)\rightarrow boolean$$
- $$Appartiene(Chiave, Dizionario)\rightarrow boolean$$
- $$Inserisci(<Chiave, Valore>, Dizionario)\rightarrow Dizionario$$
- $$Cancella(Chiave, Dizionario)\rightarrow Dizionario$$
- $$Recupera(Chiave, Dizionario)\rightarrow Valore$$

### Specifica Semantica
Individuiamo come osservatori le funzioni: `DizionarioVuoto`, `Appartiene`, `Recupera`, `Cancella`;

Individuiamo come costruttori le funzioni: `CreaDizionario`, `Inserisci`.

Ne consegue la seguente tabella:


---

## Dettagli implementativi
Nella seguente sezione viene mostrato come gli argomenti trattati nel corso sono stati utilizzati all'interno di questo progetto.
- ### Programmazione generica
  A livello teorico la programmazione generica è una forma di **polimorfismo universale**, più precisamente di **polimorfismo parametrico**.
  Questo tipo di polimorfismo è molto potente perché permette di rendere **metodi polimorfi** e di poter, più precisamente, applicare l'**operazione che il metodo implementa a insiemi di tipi di dato**.
  In Java il polimorfismo parametrico viene implementato in più modi e particolarmente con le **Generics**.
  I metodi generici consentono di effettuare un'operazione su un tipo di dato `<T>` generico per un determinato insieme di dati (o per restrizione di un insieme di dati per mezzo delle *wildcards*).

  La programmazione generica, all'interno di questo progetto è stata utilizzata per poter fornire un'interfaccia unica con la classe `ServerInterface` all'esterno.
  La classe `ServerInterface`, infatti, presenta un unico metodo pubblico (oltre che al suo costruttore), che serve per effettuare una richiesta generica al server. In questo modo le classi che la usano (come `Engine`, ad esempio) possono effettuare diverse richieste mediante un unico metodo,
  semplicemente specificando tra i parametri del metodo il tipo di richiesta.

  Il metodo generico è `T<T> requestToServer(...)` e si presenta in più forme. È un metodo polimorfico anche per altri motivi (implementa polimorfismo ad Hoc con overloading), ma focalizzandoci sulla programmazione generica, questa è la sua implementazione:
  ```java
    /**
     * Invia una richiesta senza parametri al server, gestendo una logica di tentativi.
     *
     * @param rt  Il tipo di richiesta da inviare, definito in {@link RequestType}.
     * @param <T> Il tipo di dato atteso come risposta dal server.
     * @return L'oggetto ricevuto dal server, castato al tipo T.
     * @throws ServerCommunicationException se la comunicazione fallisce definitivamente.
     */
    @SuppressWarnings("unchecked")
    public <T> T requestToServer(RequestType rt) throws ServerCommunicationException {
        return executeWithRetry(() -> (T) getRequestAction(rt).call());
    }

    /**
     * Invia una richiesta con un parametro ID al server, gestendo una logica di tentativi.
     *
     * @param rt  Il tipo di richiesta da inviare, definito in {@link RequestType}.
     * @param id  L'identificatore numerico da inviare con la richiesta.
     * @param <T> Il tipo di dato atteso come risposta dal server.
     * @return L'oggetto ricevuto dal server, castato al tipo T.
     * @throws ServerCommunicationException se la comunicazione fallisce definitivamente.
     */
    @SuppressWarnings("unchecked")
    public <T> T requestToServer(RequestType rt, int id) throws ServerCommunicationException {
        return executeWithRetry(() -> (T) getRequestAction(rt, id).call());
    }
  ```
  Il metodo, in base al parametro `RequestType` ricevuto in input, sceglie il metodo da chiamare mediante `getRequestAction(...).call()`.
  Quest'ultimo metodo chiama effettivamente uno dei metodi privati della classe che si occupa di effettuare una richiesta specifica al server. Il valore di ritorno di questa funzione non è mai lo stesso, ma è un tipo `<T>` generico.
  L'uso delle generics, dunque, consente di usare il metodo qualsiasi sia il suo valore di ritorno, cioè qualsiasi sia il tipo della richiesta da gestire.
  
  Sempre all'interno della stessa classe, la programmazione generica viene sfruttata all'interno della seguente **interfaccia funzionale**, utile al funzionamento delle lambda expressions (che in seguito tratteremo).
  ```java
   @FunctionalInterface
    private interface RetryAction<T> {
        T execute() throws Exception;
    }
  ```
  L'interfaccia viene utilizzata per astrarre sul tipo di ritorno dell'azione da compiere all'interno del metodo lambda (anch'esso generico) che segue:
  ```java
    /**
     * Esegue un'azione di richiesta al server con un meccanismo di retry.
     * Tenta di eseguire l'operazione fino a 5 volte. Se tutti i tentativi falliscono,
     * lancia una {@link ServerCommunicationException}.
     *
     * @param action La Callable che rappresenta l'azione di richiesta.
     * @param <T> Il tipo di dato atteso come risposta.
     * @return Il risultato dell'azione.
     * @throws ServerCommunicationException se l'azione fallisce dopo 5 tentativi.
     */
    private <T> T executeWithRetry(RetryAction<T> action) throws ServerCommunicationException {
        int attempts = 0;
        final int maxAttempts = 5;
        while (attempts < maxAttempts) {
            try {
                return action.execute();
            } catch (ServerCommunicationException e) {
                throw e; // Rilancia subito se l'eccezione è di comunicazione, poiché non è temporanea
            } catch (Exception e) {
                attempts++;
                System.err.println("[Retry] Tentativo " + attempts + " fallito. Riprovo... " + e.getMessage());
                if (attempts >= maxAttempts) {
                    throw new ServerCommunicationException("Impossibile completare l'operazione dopo " + maxAttempts + " tentativi.");
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new ServerCommunicationException("Thread interrotto durante il retry.");
                }
            }
        }
        return null;
    }
  ```
  Grazie all'interfaccia funzionale il metodo `executeWithRetry(...)` può funzionare indipendentemente dal valore di ritorno che l'azione da ripetere ha. In questo modo il metodo può agire su qualsiasi tipo di richiesta al server e non è necessario sovraccaricare il codice con inutili metodi specifici in più.
- ### File


- ### Database (JDBC)
  Java, grazie a JDBC, mette a disposizione una serie di librerie, contenute nel namespaces `java.sql`, che permettono di sfruttare l'utilizzo di basi di dati all'interno del codice.
  Il modo in cui le basi di dati vengono utilizzate non è altro che SQLEmbedded, dove ogni richiesta (query) al database, effettuata mediante un metodo della libreria java, viene interpretato come un oggetto di tipo `ResultSet` che deve essere ispezionato una tupla alla volta (questo perché i linguaggi di programmazione non sono set-oriented).
  
  Le librerie mettono a disposizione diversi metodi per interfacciarsi con la base di dati. Per poter effettuare la connessione, infatti, viene utilizzata la classe `DriverManager`, che conosce molti tipi di DBMS e permette di generare un oggetto di tipo `Connection` che il codice può usare per effettuare delle query qualsiasi sia il DBMS adottato.
  Ottenuto l'oggetto connection è possibile effettuare due tipi di richiesta:
  - con Statement
  - con Statement Preparati
  
  Le richieste `Statement` consentono di effettuare, attraverso il metodo `executeQuery("...");` sull'oggetto istanziato, delle query direttamente eseguibili, che non necessitano di parametri ottenibili solo a runtime, come ad esempio una `SELECT` senza clausola `WHERE`.
  Le richieste `PreparedStatement` consentono di inizializzare lo statement da comunicare al DBMS, mediante il metodo `prepareStatement("...");` sull'oggetto connessione e di lasciarlo incompleto. Questo consente di completare la query quando possibile con il dato mancante, attraverso il metodo `set[Type](index, value);` richiamato sullo statement, e di effettuare la query quando è completa.
  Per i `PreparedStatement`s è possibile lasciare la query incompleta mediante l'uso del placeholder `?`.
  
  Una volta invocato il metodo `executeQuery()` sullo statement, e nel caso la query vada a buon fine senza sollevare una `SQLException`, l'istruzione ritornerà un oggetto di tipo `ResultSet` che contiene non altro che l'insieme di tuple (o la singola tupla nel caso di query scalary) che soddisfa la query.
  Il `ResultSet` ottenuto è accessibile mediante il metodo `next()`, comunemente usato in un `while`, il quale consentirà di accedere alla successiva tupla ogni volta. Sull'oggetto ResultStatement è possibile invocare il metodo `get[Type](columnName)` per ottenere il valore di una specifica colonna della tupla che si sta ispezionando al momento.
  
  Di seguito sono riportate alcuni dei metodi presenti nella classe `DataBaseManager` che mostrano come le precedenti librerie siano state utilizzate all'interno di questo progetto:

  ```java
    /**
     * Stabilisce la connessione con il database utilizzando le credenziali fornite.
     *
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    private void establishConnection() throws SQLException {
        Properties dbProperties = new Properties();
        String username = "cacca";
        dbProperties.setProperty("user", username);
        String password = "12345";
        dbProperties.setProperty("pw", password);
        String dataBasePath = "jdbc:h2:./database;INIT=RUNSCRIPT FROM 'classpath:inizioDB.sql'";
        connection = DriverManager.getConnection(dataBasePath, dbProperties);
    }
  ```
  Il precedente metodo mostra la generazione dell'oggetto di tipo connessione, ottenuto per mezzo della classe `DriverManager`.
  Il DBMS con il quale ci si sta cercando di interfacciare è **H2**, un database utilizzabile direttamente in Java che non necessita di essere adoperato necessariamente per mezzo di una console (anche se ne fornisce una).
  H2 è un'ottima scelta come DBMS per il precedente motivo e per la sua facilità d'uso, infatti basta includere la dipendenza all'interno del progetto *Maven* e funzionerà perfettamente.



  ```java
  /**
     * Costruisce e restituisce la mappa di gioco completa, con stanze e collegamenti.
     *
     * @return l'oggetto GameMap inizializzato.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    public GameMap askForGameMap() throws SQLException {
        GameMap gameMap = new GameMap();
        executeWithRetry(() -> {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Rooms ORDER BY id ASC;");
            Map<Integer, Room> nodes = new HashMap<>();
            if (rs.next()) {
                Room room = generateRoom(rs);
                nodes.put(room.getId(), room);
                gameMap.aggiungiStanza(room, true);
            }
            while (rs.next()) {
                Room room = generateRoom(rs);
                nodes.put(room.getId(), room);
                gameMap.aggiungiStanza(room);
            }
            rs.close();
            stm.close();
            getLinkedMap(gameMap, nodes);
        });
        return gameMap;
    }
  ```
  Il precedente metodo, invece, mostra come viene utilizzato l'oggetto `Statement` e come le tuple del `ResultSet` possono essere scorse per mezzo di un costrutto `while` mediante il metodo `next()`.

  ```java
    /**
     * Recupera gli oggetti presenti in una stanza specifica, con le loro quantità.
     *
     * @param roomID l'ID della stanza.
     * @return una mappa di GeneralItem e le loro quantità.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    private Map<GeneralItem, Integer> askForInRoomItems(int roomID) throws SQLException {
      Map<GeneralItem, Integer> items = new HashMap<>();
      executeWithRetry(() -> {
        PreparedStatement pstm = connection.prepareStatement("SELECT " +
                "    iro.room_id        AS iro_room_id, " +
                "    iro.object_id      AS iro_object_id, " +
                "    iro.quantity       AS iro_quantity, " +
                "    i.id               AS i_id, " +
                "    i.name             AS i_name, " +
                "    i.description      AS i_description, " +
                "    i.is_container     AS i_is_container, " +
                "    i.is_readable      AS i_is_readable, " +
                "    i.is_visible       AS i_is_visible, " +
                "    i.is_composable    AS i_is_composable, " +
                "    i.is_pickable      AS i_is_pickable, " +
                "    i.uses             AS i_uses, " +
                "    i.image_path       AS i_image_path " +
                "FROM InRoomObjects     AS iro " +
                "INNER JOIN Items AS i ON i.id = iro.object_id " +
                "WHERE iro.room_id = ?;");
        pstm.setInt(1, roomID);
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
          if (rs.getBoolean("i_is_container")) {
            items.put(generateContainerItem(rs), rs.getInt("iro_quantity"));
          } else {
            items.put(generateItem(rs), rs.getInt("iro_quantity"));
          }
        }
        rs.close();
        pstm.close();
      });
      return items;
    }
  ```
  Il precedente metodo, invece, mostra molto similmente come viene utilizzato l'oggetto `PreparedStatement`.


  ```java
    /**
       * Genera un oggetto Room a partire dai dati di un ResultSet.
       *
       * @param room il ResultSet contenente i dati della stanza.
       * @return un nuovo oggetto Room.
       * @throws SQLException se si verifica un errore di accesso al database.
       */
      private Room generateRoom(ResultSet room) throws SQLException {
          return new Room(
                  room.getInt("id"),
                  room.getString("name"),
                  room.getString("description"),
                  room.getString("look"),
                  room.getBoolean("is_visible"),
                  room.getBoolean("allowed_entry"),
                  room.getString("image_path"),
                  askForInRoomItems(room.getInt("id")),
                  askForNPCs(room.getInt("id"))
          );
      }
  ```
  Infine, il metodo qui sopra mostra come ottenere i valori dei campi di una specifica tupla del `ResultSet` per mezzo dei nomi delle colonne della relazione.

- ### Lamba Expression


- ### SWING


- ### Thread e programmazione concorrente


- ### Socket e API RESTful


---

## Modo più veloce per vincere il gioco

1. `nord`
2. `est`
3. `nord`
4. `sali`
5. `sali`
6. `sali`
7. `sali`
8. `parla con Ivano` - dialogo futile
9. `parla con Ivano` - alla fine del dialogo verrà droppata la carta igienica
10. `parla con Javanna` - Risposta all'indovinello "Non si sà"
11. `ovest` - si vince la prima parte del gioco
12. `est` 
13. `scendi`
14. `scendi`
15. `scendi`
16. `scendi`
17. `nord`
18. `parla` - risolvere il quiz per essere promosso. Le risposte corrette appariranno nella console.

