create table if not exists Oggetto( 
    id int,
    nome varchar(40),
    descrizione varchar(40);
    contenitore boolean default false,
    apribile boolean default false,
    leggibile boolean dafault false,
    cliccabile boolean default false,
    visibile boolean default false,
    componibile boolean default false,
    utilizzi int,
    primary key(id)    
);

create table if not exists Comandi( 
    id int,
    nome varchar(40),
    primary key(id)
);



create table if not exists stanza(
    id int,
    nome varchar(40),
    descrizioneIniziale text,
    descrizioneAggiuntiva text,
    aperta boolean default true,
    visibile boolean default true,
    primary key (id)
);

create table if not exists CollecgamentoStanze(
    idStanzaIniziale int,
    idStanzaFinale int,
    direzione enum('n', 's', 'e', 'o'),
    primary key (idStanzaIniziale, idStanzaFinale),
    foreign key (idStanzaIniziale) references stanza(id),
    foreign key (idStanzaFinale) references stanza(id)
);

-- si occupa anche del collocamento degli npc nelle stanze 
create table if not exists Npc(
    id int,
    nome varchar(30),
    idStanza int,
    foreign key (id) references stanza(id),
    primary key (id)
);

create table if not exists DialogoNpc(
    Npc int,
    dialogo text,
    iterazione enum(1,2,3),
    risposta1 text,
    risposta2 text,
    passoDialogo int,
    rispostaPrecedente int,
    foreign key (Npc) references Npc(id),
    primary key (Npc,iterazione, passoDialogo, rispostaPrecedente)
);


create table if not exists stanza_oggetto(
    idStanza int,
    idOggetto int,
    quantità int,
    primary key (idStanza,idOggetto),
    foreign key (idStanza) references stanza(id),
    foreign key (idOggetto) references Oggetto(id)
);

CREATE TABLE IF NOT EXISTS Alias (
    id INT,
    alias varchar(30),
    FOREIGN KEY (id) REFERENCES Oggetto(id),
    PRIMARY KEY (id, alias)
);

CREATE TABLE IF NOT EXISTS AliasComandi(
    id INT,
    alias varchar(30),
    FOREIGN KEY (id) REFERENCES comandi(id),
    PRIMARY KEY (id, alias)
);

create table if not exists Evento(
    id int,
    descrizioneAggiornata text,
    idStanza int,
    foreign key (idStanza) references stanza(id),
    primary key(id)
);

create table if not exists Contiene(
    idOggetto1 int ,
    idOggetto2 int ,
    quantità int,
    foreign key (idOggetto1) references Oggetto(id),
    foreign key (idOggetto2) references Oggetto(id),
    primary key(idOggetto1,idOggetto2)
);


insert into Comandi(id,nome)
values
(1,'nord'),(2, 'sud'),(3,'est'),(4, 'ovest'),(5, 'inventario'),
(6,'fine'),(7, 'osserva'), (8, 'raccogli'),(9,'apri'),(10, 'premi'),
(11, 'combina'),(12, 'leggi'), (13, 'parla'), (14,'sali'), (15,'scendi');

insert into AliasComandi(id,alias)
values
(1, 'n'), (2,'s'), (3,'e'), (4,'o'),(5,'inv'), (5,'zaino'), (5, 'zai'),(6,'f'), (6,'esci'), (6,'e'), 
(6,'caccati'), (6, 'cacca'), (6, 'basta'), (6, 'exit'), (6,'end'), (6,'bocciato'), 
(7, 'guarda'), (7, 'vedi'), (7, 'trova'), (7,'cerca'), (7, 'descrivi'), (7, 'occhiali'), 
(8, 'prendi'), (8, 'afferra'), (8, 'colleziona'), (8,'accumula'), (8,'inserisci'),
(9, 'open'), (9,'accedere'),
(10, 'push'), (10, 'spingi'), (10,'attiva'),(10,'pressa'), (10,'schiacciare'),
(11, 'crea'), (11, 'costruisci'), (11, 'inventa'), (11, 'utilizza'), (11,'usa'),
(12, 'sfoglia'), (12, 'decifra'), (12, 'interpretra ') ,
(13, 'iteragisci'), (13, 'comunica'), (13, 'conversa'), (13, 'chiacchera');

insert into Oggetto(id,nome, descrizione, contenitore,apribile, leggibile, cliccabile, visibile, componibile, utilizzi)
values 
(9, 'CHIAVE', 'Una piccola chiave d''ottone, più grande e massiccia del normale,  consumata dal tempo e leggermente ossidata alle estremità. All''apparenza banale, ma capace di sbloccare 
l''ascensore del dipartimento, portando chi la possiede verso piani superiori a una velocità elevata', false,false,false,false,false,false,-1),

(1, 'LA MAPPA DEL FUORICORSO'. ' Questa mappa costruita da studenti che vaga da anni nel dipartimento, rileva segreti e stanze nascoste all''interno del dipartiemnto.',false,false,true,false,false,false-1)
(2, 'CAFFÈ RIGENERANTE', 'Un piccolo calice fumante colmo di liquido scuro e amaro, capace di ridare vigore anche allo studente più esausto. Si dice che chi beve il CAFFÈ RIGENERANTE 
possa restare vigile abbastanza da affrontare persino le lezioni del mattino e le infinite code in segreteria.',false, false, false,false, false, false, -1),
--PRIMO PIANO
(3, 'LIBRO DI CALCOLABILITÀ E COMPLESSITÀ','Un voluminoso manoscritto rilegato in pelle consumata, intriso di formule antiche che raccontano le meraviglie (e i tormenti) degli algoritmi. 
Custodisce segreti sulla Macchina di Turing Universale, diagrammi misteriosi e dimostrazioni che si avvolgono su loro stesse come un serpente che non trova la propria coda. Si narra che nelle sue pagine sia celata una verità tanto semplice quanto impossibile da dimostrare… e che 
chi osa cercarla finisca per perdersi in un labirinto di problemi apparentemente “facili”',false,false,true,false,true,false, -1),
-- SECONDO PIANO
(4,'CANDEGGINATOR 3000"', 'Rarissima reliquia chimica, introvabile all''interno del dipartimento. Talmente potente da non limitarsi a cancellare le macchie visibili: 
il suo effetto si propaga nel tempo, dissolvendo anche le macchie che potrebbero comparire nelle ,prossime 48 ore.talmente raro che alcuni dubitano perfino della sua reale esistenza… 
un po'' come gli esami facili al primo appello.',false, false,false, false,false, false,-1), 
(15, 'ARMADIETTO','Classico armadietto di metallo',true,true,false,false,true,false,-1),

(5, 'BIGLIETTINO MISTERIOSO', 'Un piccolo rettangolo di carta spiegazzato, ritrovato sotto una sedia. Vi sono annotate strane frasi, 
frecce e simboli arcani… tra cui spicca, in mezzo a scarabocchi incomprensibili, la scritta «gittata massima 𝜃=45°». Nessuno sa davvero a cosa serva, ma pare abbia qualcosa 
a che fare con il lanciare cose molto lontano (e con sorprendente precisione).',false,false,true,false,true,false,-1),

-- TERZO PIANO
(6, 'SCHEDA MADRE', 'Un''antica reliquia elettronica, corrosa dal tempo e da qualche improvvida fuoriuscita di caffè.Le piste appaiono screpolate, 
i condensatori gonfi come se stessero per esplodere, e i vecchi slot di memoria sembrano trattenere a fatica i ricordi di sistemi mai più avviati.
Rinvenuta per terra, impolverata e dimenticata in un angolo del museo di informatica, come se persino il tempo avesse deciso di voltarle le spalle.'
, true,false,false,false, true,true,-1),
-- QUARTO PIANO
(7, 'BORSELLINO', 'Un piccolo borsellino in stoffa, pesante come se celasse segreti o vecchie monete, ma sorprendentemente morbido al tatto. 
I colori originari sono ormai sbiaditi, eppure rimangono visibili dei curiosi simboli che si rincorrono: chicchi di caffè e piccoli diamanti, 
cuciti in fila come a custodire un significato dimenticato. Nessuno sa davvero cosa contenga, ma scuotendolo si percepisce un tintinnio che fa pensare a qualcosa di prezioso',
true,true,false,false,true,false,-1),

(8, 'MONETA DI METALLO', 'Una moneta particolare, lontana dalle consuete monete europee, dal peso piacevolmente consistente. Da un lato è inciso un piccolo water, 
mentre dall''altro spicca un simbolo enigmatico che somiglia a una libreria.',true,false,false,false,false,true,-1)
(10, 'PENNA NULL','Una comunissima penna a sfera, con il tappo morsicato e l''inchiostro forse già secco.', false, false,false,false,false,false,-1),
--QUINTO PIANO
(11,'SCATOLA', 'Una semplice scatola bianca, sorprendentemente leggera, che non emette alcun suono quando la si scuote, come se fosse vuota o 
custodisse qualcosa di immobile. Sul retro è inciso in piccolo un simbolo misterioso: 7L.', true,true, false, false,true,false,-1)

(12, 'TESSERA SMAGNETIZZATA','Una tessera bianca, dall''aspetto anonimo, priva del chip che le darebbe vita. Un tempo poteva essere una carta di accesso o una vecchia tessera servizi,
 ora ridotta a un semplice pezzo di plastica apparentemente inutile.', false,false,false,false,false,true,-1 )

(13, 'CARTA IGIENICA', 'Un semplice rotolo di carta igienica, leggermente sgualcito ai bordi.', false,false,false,false,false,false,-1),
(14, 'TESSERA MAGICA', 'Dall''incontro tra una tessera priva di vita e una scheda madre ormai logora è nato questo curioso artefatto: un rettangolo di plastica bianca, attraversato da venature metalliche ossidate che paiono vibrare di un''energia invisibile. 
Basta sfiorarla per spalancare porte sigillate da tempo e far affiorare particolari nascosti agli occhi più attenti.
Ma la sua magia è fragile come un fiammifero al vento: può brillare solo poche volte prima che il mistero che la anima si consumi del tutto....',false,false,false,false,false,false,2),

(16, 'MACCHINETTA DEL CAFFE', 'Un''elegante colonna metallica dall''aspetto futuristico, impreziosita da un ampio touch screen lucido come uno specchio. Accetta pagamenti contactless con una rapidità quasi magica e 
permette di scegliere tra decine di aromi personalizzati: dal caffè intenso delle notti d''esame al più delicato decaffeinato del “tanto ormai è andata”. Sul display, brevi messaggi motivazionali compaiono all''improvviso, 
come bisbigli incoraggianti per studenti assonnati e docenti esausti. Un piccolo altare tecnologico dedicato al culto quotidiano della caffeina.', true,false,false, true, true,true,-1 );

insert into Contiene(idOggetto1, idOggetto2, quantità)
values
-- borsellino-moneta-penna
(7,8,4),(7,10,6),

--armadietto-candeggina
(15,4,1),
--scatola-carta smagnetizzata
(11,12,1),
--macchinetta-caffe
(16,2,1);
insert into Alias(id,alias)
values
(1,'mappa'), (1, 'fuoricorso'), (1, 'cartina'), (1,'map'), 
(4, '3000'), (4, 'prodotto'), (4, 'cande'),
(2, 'bevanda'), (2, 'energia' ) (2, 'coffee'),
(3, 'libro'), (3, 'cc'), (3,'calcolabilià'), (3,'complessità'), (3,'calcol'), (3,'comples'), (3,'lib'),
(15, 'mobile'), (15, 'armadio'), 
(5, 'bigliettino'), (5, 'foglio'),
(6, 'scheda'), (6, 'motherboard'), (6, 'madre'),
(7, 'borsel'), (7, 'astuccio'),
(8,'moneta'), (8, 'soldo'), (8,'metallo'), (8,'denaro'),
(10, 'penna'), (10,'null'), 
(11, 'contenitore'), (11, 'box'), (11, 'cassa'),
(12, 'tessera'), (12, 'scheda'),
(13, 'carta'), (13, 'rotolo'), (13, 'igienica')
(14, 'magica'), (14,'pass')
(16, 'macchinetta'), (16,'distributore');



insert into Evento(id, descrizioneAggiornata, idStanza)
values
-- bar
(13,'Il solito bar con  studenti che fanno colazione di fretta, altri che ripetono nervosamente appunti sgualciti, 
e baristi svogliati che riescono puntualmente a bruciare il caffè. ',2)
-- se viene raccolto il libro dal primo piano
(1, 'La solita biblioteca, stracolma di manuali, libri universitari e vecchie tesi di laurea, che occupano ogni scaffale fino all''orlo.',5),
(11, 'Il bagno è ancora sotto sequestro, forse dovremmo chiamare i carabinieri',12),
-- se viene presa la candeggina dall'armadietto al secondo piano
(2, ' Il braccio robotico continua a scatenarsi in una breakdance sfrenata ogni volta che gli passi accanto. Forse vuole sfidarti... ',9),
(3, 'I ricordi riaffiorano alla mente, e ti tornano in mente l''esame di fisica e quei famigerati bigliettini volanti.',16 ),
-- terzo piano
(4, 'Ora che hai dato una mano a Dario Tremolanti, puoi tornare alla tua nobile missione: trovare il bagno…
ma stavolta con qualche indizio in più e (si spera) meno giri a vuoto!',15),
(5, 'Ti sei perso?!? Occhio a non farti mummificare tra questi relitti polverosi... l''uscita è verso EST!',30),
-- quarto piano
(6,' il solito bagno senza carta igienica e con il disegno di un palazzo con il punto interrogativo punto all''ultimo piano',11),
(7, 'Sei di nuovo nell Laboratorio Boole con i tre studenti fuori corso',13),
(10,'La solita fila Kilometrica di studenti ansiosi',10),
--quinto piano
(8, 'La stanza è troppo disordianta, ti sei perso tra libri e appunti? per uscire dalla stanza vai a EST',18),
(9, 'Niente di nuovo, per uscire vai a OVEST',19),

-- sesto piano
(12, 'Ti trovi sempre al sesto piano con Dottor Cravattone che a momenti esplode',20);

insert into stanza(id, nome, descrizioneIniziale, descrizioneAggiuntiva,aperta,visibile)
values
(1, 'Ingresso del Campus', 'Davanti a te si apre il cancello del campus universitario, imponente ma familiare. Oltre il cancello si vede un viale lungo, da grandi palazzi e piccioni prepotenti, pronti a colpirti. A pochi passi dall''ingresso, un piccolo bar brulica di studenti già assonnati e inservienti carichi di pacchi. Il dipartimento di informatica si staglia più avanti, grigio e severo, come un labirinto di vetro e cemento che sembra nascondere più segreti che aule.\n TUTORIAL \n Muoviti tramite i comandi Nord (N), Est (E), Ovest (O), Sud (S), ed arrivare il prima possibile al bagno.\n Guarda attentamente ciò che ti circonda, può sempre essere utile!',true,true),
(2, 'Bar', 'Un locale stretto ma accogliente, con il profumo persistente di caffè bruciato e cornetti caldi. Alle pareti, volantini scoloriti pubblicizzano vecchi eventi universitari. Un orologio sopra la macchina del caffè segna sempre le 8:15, bloccato da anni. Dietro il bancone, il barista prepara distrattamente cappuccini, mentre un gruppetto di studenti chiacchiera a voce troppo alta. ', ' Vicino alla cassa, in ato al centro , un piccolo cartello con scritto: “Chi cerca… trova.”\n Ci sono molti studenti , è un buon posto per raccogliere voci di corridoio o chiedere informazioni....',true,true),

(3, 'Viale verso il dipartimento\n. stai andando a verso Est \n. Un viale lungo, quasi interminabile, che conduce al cortile interno del dipartimento. Le foglie secche si raccolgono agli angoli, mosse dal vento. Sui lati del percorso, vecchie bacheche arrugginite espongono orari, comunicati e qualche annuncio misterioso. In fondo, le porte a vetri del dipartimento invitano a entrare… o forse a perdersi.', 'Sul lato destro, una bacheca ha una mappa del campus, ma è strappata proprio dove c''è segnato il dipartimento di informatica.',true,true ),

(4, 'Dipartimento di Informtica', 'Un atrio ampio ma freddo, pavimento in marmo consumato e neon tremolanti. Dietro un vetro spesso, il portinaio osserva chi entra e chi esce, sfogliando distrattamente un giornale. Alla sua sinistra, un vecchio ascensore con porte rumorose e delle scala che portano verso i piani superiori.', ' Ti ritrovi di nuovo al piano terra. Il portinaio, mezzo addormentato, armeggia con un mazzo di chiavi che sembra non finire mai, mentre si trascina verso l''ultima pagina del giornale',true,true),

--piano terra
(5, 'Aula studio piano terra', 'Un''aula rettangolare con tavoli lunghi e sedie rovinate. Libri dimenticati, fogli sparsi e zaini abbandonati. Ci sono diversi cartelloni sulla parete.', 'Ci sono tanti cartelloni attaccati al muro e ci sono anche scritte strane su alcuni tavoli: "Punta in alto per la felicità" , "7FN"" ecc',true,true);-- la mappa la facciamo trovare  solo se passa l'indovinello
-- primo piano 
(6, 'Primo piano', 'Scale conducono qui. \nAppena arrivi, il corridoio si apre su un bagno, che però scopri subito essere occupato dallo Studente Modello, intento a porre enigmi filosofico-informatici. ', 'A nord e ovest ci sono altre due porte, chissa cosa nascondono.',true,true),
(7, 'Biblioteca','Una stanza lunga, con scaffali traboccanti di manuali di informatica, riviste accademiche e vecchie tesi impolverate. La luce filtra da grandi finestre, e il silenzio è interrotto solo dal fruscio di pagine e dal ticchettio di una tastiera. Un ottimo posto per scoprire curiosità… o nascondere indizi.', 'Al secondo sguardo noti un libro che sta per cadere , il quale si intitola Calcolabilità e Complessita. Chissa che segreti  nasconde....\n',true,true),-- se prende il libro può usarlo per risolvere l'indovinello di p=np
(12, 'Bagno primo piano', 'Finalemnte il bagno!. Ma purtroppo pare sia occupato da uno studente', ' alla tua sinistra c''è una luce accecante , IL BAGNO  sulla porta è appeso un foglietto con scritto: “Risolvi, e potrai muoverti con maestria all''interno del dipartimento.”',true,true ),

-- secondo piano 
(8, 'Secondo piano','Prendendo le scale sei al secodo piano. noti verso NORD una garnde stanza.' , 'Non c''è nulla di nuovo',true,true),
(9, 'Robotica', 'Un laboratorio pieno di bracci meccanici spenti, monitor sfarfallanti e componenti elettronici sparsi.' , 'Un braccio meccanico inattivo si muove di scatto quando passi vicino, il quale punta sempre verso un armadietto a destra.',true,true), -- trova la varechina richiesta dall'inserviente 
(16, 'Aula A', ' Sei nella tua vecchia aula , dove hai seguito le tanto amata lezioni di fisica. Ci sono due lavagne , una con il pennarello e una con il gesso , sulla quale ci sono scritti dei geroglifici. Semprerebbe una formula del moto del proiettila o qualcosa del genere. Chissà se possono essere criptati....', 'Osservando meglio la stanze noti un bigliettino sotto il banco dell''utlima fila è il biglittino che permette di criptare la formula del moto del...qualcosa.(fisica non era la tua materia preferita...)',true,true),-- prende il bigliettino per poi poter aiutare lo stundete . se no deve sperare nelle sue conoscenze 

-- terzo piano 
(14, 'Terzo piano', 'Sei finalemente arrivato al terzo piano.\n Nel corridoio  non c''è nessuno. Del bagno nemmeno l''ombra. Noti a destra (EST) l''aula studio e sulla tua sinistra (OVEST) intravedi il museo di informatica',' Senti dei schiamazzi provenire dall''aula stuido , forse c''è qualcuno.'true,true ),
(15, ' Aula studio terzo piano', 'La stanza è piena di tavoli scricchiolanti, ma almeno qui le sedie non sono rotte.... Noti uno studente ansioso e spaventato che sta studiando qualcosa, potresti aiutarlo , chissà se per sdebitarsi ti darebbe delle informazioni utili....', 'C''è uno studente ansioso e spaventato sulla tua destra potresti aiutarlo'true,true),-- se viene aiutato da un indizio su una scheda magica che può aprire qualsiasi porta e che ti fa vedere cose fuori dal comune(per il bagno magico) serviranno componenti obselete per costruirala 
(30, 'Museo di informatica', ' Sei in una stanza piena di vecchi computer, di fronte a te, un monitor a tubo catodico grande quanto un forno a microonde, schede madri imbalsamate e sulle pareti  manuali ingialliti che giurano di spiegare come installare Windows 95, ma solo se sai leggere il sanscrito.',  ' Osservando la stanza noti una scheda madre rovinata e l''uscita verso EST!' true,true),-- se fa raccogli prende la scheda madre rovinata che sarà utile per costruire la scheda magica 


-- quarto piano
(10, 'Quarto piano','Corridorio stretto e affollato, con una fila interminabile che si snoda fuori dal bagno. L''aria è un misto di ansia ed esausta rassegnazione. Tra la fila, studenti leggono libri di algoritmi o ripassano appunti. Potresti parlare con qualcuno... magari capiscono la tua urgenza e ti lasciano passare.', 'La solita fila Kilometrica di studenti ansiosi, e un inserviente arrabbiato per via del bagno sporco. È alla ricerca di CANDEGGINA ',true,true),
(11, ' Bagno quarto piano', 'Un''altro bagno! Può essere la volta buona. All''interno, l''odore forte di disinfettante si mescola a quello più pungente dell''ansia collettiva\n. Specchi graffiati riflettono volti stanchi, e le porte cigolano ad ogni movimento.', 'Maledizione , manca la carta igienica.\nTra i muri, qualcuno ha disegnato un piccolo graffito, un palazzo con un punto interrogativo all''ultimo piano Chissà......',true,true),
(13, 'Laboratorio Boole', 'Nascosto dalla lunga fila di studenti di fronte a te (NORD) si intravede un laboratorio di informatica , dove all''interno ci sono studenti fuori corso cercano di crare la macchina di turing universale che risolva il problema della fermata.', 'I soliti tre studenti che vogliono rislvere il problema della fermata... Opsss stavi inciampato su qualcosa. Un borsellino chissa che sta dentro.',true,true),-- se lo raccoglie al suo interno ci sono 5 monete e varie penne (il suo contenuto è noto solo se lo apre)

-- quinto piano 
(17, 'Quinto piano', 'Un corridoio silenzioso con porte chiuse e targhette in ottone.', 'Sei di nuovo al quinto piano, non noti niente di nuovo , a sinistra(OVEST) e a destra (EST) ci sono gli uffici dei professori. Potrebbero nascondere qualcosa di utile. Chissà se questi oggetti sono davvero indipendenti.... o se dietro le quinte hanno implementato un composite pattern!',true,true),
(18, 'Primo uffico', 'Sei all''interno del primo ufficio (a OVEST del corridoio) dentro, scaffali stracolmi di libri e pile di appunti ovunque come piccole torri di carta pronte a crollare.', 'La stanza è troppo disordianta, ti sei perso tra libri e appunti? per uscire dalla stanza vai a EST.\nUscendo sei inciampato su un quadro il quale rappresenta un inserviente con un mantello di carta igienica con uno spazzolone in mano. Chissà se esite questo supereroe.....', false,true),--la stanza è chiusa solo con la carta viene aperta. il quadro da l'indizio sull'inserviente che può droppare la carta igienica se la carta è esaurita finendo il gico con il bagno del 4 piano
(19, 'Secondo ufficio', 'Qui l''ordine regna sovrano: pochi libri, una scrivania lucidata e un''unica sedia per i visitatori. Sul muro, un grande orologio vintage scandisce il tempo con ticchettii regolari. Una foto in cornice mostra il docente accanto a una squadra di studenti sorridenti, immortalati in tempi più sereni.', 'A destra c''è una librerira , che oltre ai libri ha una scatola. Ma la scatola è chiusa. Dovresti aprirla',true,true),-- se prende la scatola e la apre ha la tessera che combinata alla scheda madre crea una tessera magica con 3 aperture e se ha ancore le aperture può vedere il bagno magico 

-- sesto piano
(20, 'Sesto piano',  'Corridoio stretto e dall''aria sospesa. Un rumore di sottofondo ti accoglie al sesto piano è il ronzio continuo dei computer dei laboratori, talmente fitto da sembrare quasi un coro di zanzare tecnologiche in piena estate. Ci sono Due porte con targhette metalliche indicano i laboratori di ricerca, uno di Intelligenza Artificiale e l''altro di Calcolo Quantistico.', 'Sei di nuovo all''ingrsso del sesto piano, tra un bip e l''altro, qualche led lampeggia come a salutarti. Inoltre vedi un ragazzo ben vestito e con sguardo minacciso che ti fissa chissà che vorrà. Forse dovrei paralrci',true,true),
(21, 'Laboratorio di Intelligenza Artificiale', ' Inizialmente una luce bianca e accecante ti accoglie, causati da una sfilza di computer accesi. Essi mostrano stringhe di codice e grafici in tempo reale ', 'Osservando meglio, noti che su uno dei monitor lampeggia lentamente l''immagine di una tazza di caffè fumante. Per un attimo ti fermi a guardarla, ipnotizzato...poi la realtà ti colpisce di nuovo: maledizione, ti ricordi all''improvviso della tua urgente necessità di trovare un bagno'true,true),-- qui la barra che indica fra quanto si caga a dosso aumenta di 1/4 perchè ha visto il caffe 
(22, 'Laboratorio di Calcolo Quantistico', 'Un ambiente più buio, illuminato solo da spie verdi e blu provenienti da strane macchine cilindriche. Cavi intrecciati come radici si diramano lungo il pavimento. Sul tavolo principale, un laptop mostra simulazioni che sembrano incomprensibili.', 'Sei sempre nel solito laborario di ricerca situato a NORD del corridoio. Questa volta noti un cassetto semiaperto con un bigliettino sul quale c''è scritto  “Il segreto è in alto, non fermarti 7FN”.'true,true),

-- settimo piano 
(25, 'Settimo piano', 'Appena salite le scale, ti accoglie un corridoio largo e luminoso, illuminato da grandi finestre. L''aria qui è più silenziosa, quasi solenne. Sulla sinistra, in netto contrasto con i muri rivestiti in legno scuro e le targhe eleganti, 
spicca una macchinetta del caffè ultramoderna: touch screen, pagamenti contactless, possibilità di scegliere tra decine di aromi personalizzati e persino un display che mostra messaggi motivazionali.\n Poco più avanti, una porta imponente segnata da una targhetta 
placcata d''oro indica l''ufficio del direttore del dipartimento, mentre sull''altro lato del corridoio si apre la sala consiglio, 
riconoscibile dal vetro smerigliato che lascia solo intuire sedie imbottite e un lungo tavolo di acacia.', 'Sei ancora nello splendido e incantato settimo piano: a destra (EST) c''è l''imponente ufficio del direttore, mentre difronte a te (NORD)si trova la riservata sala consiglio, avvolta da un''atmosfera di mistero e autorità.',true,true),

(26, 'Ufficio del direttore', 'Vieni accolto da un ambiente elegante e curato nei minimi dettagli. Le pareti sono rivestite da pannelli di legno scuro, su cui sono appesi diplomi, riconoscimenti e fotografie di momenti importanti del dipartimento. Al centro domina una grande scrivania in mogano sulla destra una libreria di legno raffinato con fascicoli e libri.', 'Noti qualcosa di strano nella libreria. Forse stai solo svarionando , l''urgenza al bagno ti sta dando alla testa',true,true),

(27, 'Sala Consiglio','Ti trovi nella sala consiglio: al centro campeggia un grande tavolo rotondo di quercia massiccia, circondato dalle migliori sedie ergonomiche sul mercato, eleganti e rivestite in pelle scura. Sul tavolo sono poggiate numerose cartelle, alcune lasciate aperte con fogli sparsi che raccontano frammenti di discussioni accese. Alle pareti spiccano diversi quadri: alcuni ritraggono professori intenti a festeggiare eventi importanti del dipartimento, 
altri immortalano celebri vincitori del premio Turing, simboli di eccellenza e prestigio. Di fronte, una grande LIM cattura subito lo sguardo: sullo schermo troneggia l''immagine surreale di un WATER DIAMANTATO, enigmatico e quasi provocatorio, come se fosse un messaggio in codice nascosto tra le formalità accademiche.' , 
'Se osservi con attenzione tra i tanti quadri, uno di questi raffigura una libreria con sopra un rotolo di carta igienica. La libreria non è un semplice mobile: è in realtà una porta segreta, leggermente socchiusa, dalla quale filtra una luce accecante.',false,true)--chiusa 
-- stanza segreta settimo piano 
(28, 'Il bagno Diamantato','Sei finalemte arrivato a un bagno e non un bagno qualsiasi...IL BAGNO. Water diamantato con  accanto un rotolo di carta igienica d''oro zecchino il quale brilla in tutta il sua sfarzo. E poi, l''oggetto più raro e prezioso di tutti: una saponetta appoggiata sul lavandino.
L’aria profuma di fiori esotici, e persino il getto del rubinetto sembra scorrere più elegante qui dentro. È il trionfo, la meta, la fine del viaggio: il bagno segreto del settimo piano. ' , '',false,false );

(29, 'Aula d''esame', 'Qui si tiene l''esame più temuto dell''anno. Una grande aula con sette righe per lato di banchi ognuna da sei posti. Più del 50% dei posti sono occupati da alunni pronti a sostenere il loro esame orale.L''aria è densa di tensione e speranza: l''arrivo in tempo dipende da ogni scelta fatta lungo il percorso.','',true,true);

--dopo esser andato in bagno deve paralre con il portinaio e che li dirà la stanza dell'esame(se non la sa) e sperare nel non esser arrivato in ritardo. 

insert into CollecgamentoStanze(idStanzaIniziale,idStanzaFinale,direzione)
values
(1,2,'s'), (2,3,'e'), (3,2,'o'),
-- pianto terra collegamento
(3,4,'e'),(4,5,'n'), (4,3,'o'),(5,4,'s'),(4,29,'o'),(29,4,'e'),

--primo piano collegamento
(6,7,'n'),(6,12,'o'),(7,6,'s'), (12,6,'e'),

-- secondo piano 
(8,9,'n'),(8,16,'o'),(9,8,'s'), (16,8,'e'),

--terzo piano 
(14,15,'e'), (14,30,'o'), (15,14,'o'),(30,14,'e'),

--quarto piano 
(10,11,'o'), (10,13,'n'), (11,10,'e'), (13,10,'s'),

-- quinto piano 
(17,18,'o'),(17,19,'e'),(18,17,'e'),(19,17,'o'),

--sesto piano
(20,21,'e'), (20,22,'n'), (21,20,'o'),(22,20,'s'),

--settimo piano 
(25,26,'e'),(25,27,'n'), (26,25,'o'), (27,25,'s'), (26,28,'o'),(28,26,'e');

insert into stanza_oggetto(idStanza, idOggetto)
values
(5,1,1), (7,3,1), (9,15,1), (16,5,1),(30,6,1), (13,7,1), (19,11,1), (25,16,1);

-----------------------------
insert into Npc(id, nome, idStanza )
values 
(7, 'Studente di storia',2),
(1, 'Bruno il portinaio',4 ),
(2, 'Ernesto Sapientoni', 12), -- bagno primo piano  
(3,'Dario Tremolanti', 15),
(4, 'Javanna Garbage', 10), -- studente in fila
(5,'Ivano Ipoclorito (Inserviente)',10),
(6, 'Dottor Cravattone', 20);

insert into DialogoNpc(Npc, dialogo, iterazione, risposta1, risposta2, passoDialogo, rispostaPrecedente )-- risposta precedente si riferisce alla risposta 1 o 2 che abbiamo scelto 
values 

-- dialogo bar 

(7, 'Ehi, scusa se ti disturbo… conosci un bagno qui vicino? È… una questione di vita o di imbarazzo!', 1, 'Un bagno? Uhm… fammi pensare… Allora, 
nell''antica Roma i bagni pubblici si chiamavano thermae… e poi c''erano anche le latrinae...', '', 1,0),

(7, '…No, intendo un bagno vero. Qui. Adesso. Nel campus!', 1,'Ah! Allora… credo… no aspetta, forse era dietro il dipartimento di Filosofia… o 
forse davanti alla segreteria…', '', 2,1),
(7, 'Quindi...non sai davvero dove sia?',1,'Ehm... no, scusa... sono solo al primo anno di Storia e credo di aver sbagliato strada… il dipartimento mio 
non è nemmeno qui nel campus!\n "In bocca al lupo! E ricordati: la vera storia… la scrive chi arriva in tempo!', '',3,1),

---------------------------------------------------------

-- dialoghi alunno - portinaio
( 1, 'Ehilà, ragazzo. Hai l’aria di chi ha urgente bisogno... di informazioni. Posso aiutarti, ma solo se mi dici con esattezza cosa cerchi. Qui si gira per ore senza meta, sa?"
', 1, 'Mi scusi, dove posso trovare un bagno funzionante?','Sa mica dove si tiene l’esame di Metodi Avanzati di Programmazione?', 1,0 ),

(1, 'Eh... il piano terra non ha bagni, per motivi di mistero burocratico. Se sali al primo, magari trovi qualcosa, ma pare che il bagno sia in ostaggio di uno studente con la settimana enigmistica. Buona fortuna.', 1, '','', 2,1),

(1, ' Quello? piano terra , a sinistra. Se ci arrivi in orario e asciutto, hai già fatto metà dell''impresa.', 1,'', '' 2,2);

-- secondo indovinello

(1, 'Ti vedo provato il primo piano non ti ha regalato la gioia sperata eh? Il dipartimento è come un labirinto: sali per cercare una risposta e scendi con più domande.',
 2,'Lei si sta divertendo, ma io rischio di esplodere. Aiuti uno studente in difficoltà!', 'Conosce scorciatoie o bagni ''non ufficiali''?',1,0 ),

(1,'Va bene, va bene… al quarto piano c''è un bagno quasi sempre libero. Nessuno ci va perché dicono sia infestato da uno studente fuori corso, ma è solo leggenda.',2, '', '', 2,1),
(1,'Hmm... forse.potrebbe esistere un bango segreto. ma non diffondo segreti mistci in maniera gratuita.  Hai per caso un caffè per un povero portinaio stanco?', 2, '', '', 2,2),

-- terzo indovinello 
(1, 'Guarda chi torna... Hai la faccia di chi ha capito che le scale non sono sempre l''opzione migliore, eh?
Purtroppo, per sbloccare l''ascensore serve rispondere a una domanda che tormenta anche i più bravi.
Se sbagli, mi dispiace, niente corsa verso l''alto. Allora, senti qua:\n“In Java, quale tra queste forme di ereditarietà non è permessa?”', 3, 
'L''ereditarietà multipla (una classe con più super-classi dirette).','L''ereditarietà semplice (una classe che estende una sola super-classe).', 1,0),

-- risposta corretta 
(1, 'Bravo! L''ereditarietà multipla è bandita in Java: troppi casini col diamante, dicono.
Va bene, prendi questa chiave: ti apre l''ascensore fino al settimo piano.Ma occhio: più sali, più i misteri si complicano.', 3, '','',2,1),

--risposta errata
(1, 'Mi dispiace, ragazzo. Se Java vietasse pure l''ereditarietà semplice, non resterebbe molto da estendere, eh?
Riprova quando ti ricordi come funziona davvero l''O.O.', 3,'', '', 2,2),

-----------------------------------
-- primo piano 

-- dialogo alunno bagno in ostaggio 
(2,'Chiunque tu sia, fermati! Il sapere è più urgente di qualunque bisogno fisico. Solo chi risponde con intelligenza sarà degno del sapere perduto. Risolvi il mio enigma e avrai una ricompensa.\n"
 Indovinello:
"Mi trovi in aula e anche tra le mani, disegno corridoi, scale e piani. Non ti parlo, ma ti dico dove andare… Chi sono? Prova a indovinare."',1,'Una piantina vera!', 'Una mappa!',1,0 ),

(2, 'Una piantina vera????Errore. La piantina ti nutre, ma non ti guida. Mi dispiace, il bisogno di sapere è stato sconfitto dal bisogno… dell''altro genere!',1,'','',2,1),
(2, 'Bravo. Hai fiuto per l''orientamento, oltre che per l''urgenza. C''è una mappa del dipartimento appesa nell''aula studio al piano terra, ma è coperta da un cartellone pubblicitario. Trovala e saprai dove andare."', 1, '' ,'',2,2),


------------------------------------------------------
-- Terzo piano (studente ansioso)

(3,'Oh… tu sembri meno disperato di me… Forse puoi aiutarmi!Ho questo maledetto dubbio sul moto parabolico. Se mi aiuti, 
potrei ricompensarti… con informazioni che pochi conoscono.', 1, 'Dimmi pure, vediamo se riesco ad aiutarti.', 'Scusa, non ho tempo, devo trovare un bagno!',1,0)

--prima scelta:

(3, 'La formula della gittata di un proiettile lanciato con velocità iniziale v0 e angolo θ è\n 
G= (v^20 sin2θ)/g  \nQuale di queste affermazioni è vera?', 1, 'La gittata è massima quando 𝜃=45', 'La gittata aumenta sempre se aumento l''angolo θ', 1,1),

-- risposta corretta

(3,'AH GIÀ, È VERO! La gittata è massima proprio a 45 gradi.\n
Come promesso, ti svelo questo:
Si dice che in un stanze ci siano componenti obsolete, roba fuori produzione, 
i quali servono per costruire una scheda magica che può aprire qualsiasi porta e mostrare cose che sfuggono agli altri.', 1,'', '', 2,1),

-- risposta errata
(3, 'Eh no… se aumenti troppo l''angolo, la gittata in realtà diminuisce… Mi sa che non posso aiutarti.', 1,'','', 2,2),

-- se decide di non aiutarlo 
(3, 'Capisco… buona fortuna allora',1, '', '', 2,2),



-------------------------------------------------------------------
--Quarto piano 

(4, 'Non finisce più questa fila…\n Solo chi capisce i grandi misteri della computazione può bypassare la coda. 
Rispondi al mio enigma e potrai passare avanti.\n Problemi difficili da calcolare,ma facili da verificare.
Da sempre ci si chiede con fervore:\n esiste una scorciatoia, oppure è solo un errore? Il mondo attende, ma la risposta non c''è…
Allora dimmi: P è uguale a…\n', 1, 'NP', 'Non si sa', 1,0),

-- risposta errata
(4, 'Troppo sicuro di te. Se bastasse affermarlo così, saremmo tutti a casa a scrivere algoritmi perfetti. Torna in coda.', 1,'','',2,1),

--risposta corretta

(4, 'Giusto. P potrebbe essere NP… o forse no. Finché non lo dimostriamo, rimane il più grande enigma della nostra epoca. Vai pure, ti sei guadagnato il diritto di passare.'
, 1, '', '', 2,2),

-- secondo indovinello ( SE IL PRIMO è STATO SBAGLIATO e ha la carta igienica)
(4, 'Oh, bentornato! spero che questa volta ti vada meglio, rispondi a quesata domanda facile facile ,  roba del primo semestre e potrai saltare la fila. 
Se rappresenti un grafo con una matrice di adiacenza, qual è la complessità dell''aggiunta o rimozione di un nodo?', 2, 'O(n^2)', 'O(n)', 1,0),

--risposta corretta
(4,'Bravo! In una matrice di adiacenza devi aggiungere o rimuovere un''intera riga e colonna: O(n^2)
Come promesso, vieni: facciamo saltare la fila… ma non dirlo in giro!', 2,'', '', 2,1), 

--risposta errata
(4,'Eh no… per una lista di adiacenza sarebbe O(n), ma con una matrice è più pesante:
O(n^2). Mi dispiace, resta in coda come tutti gli altri!', 2,'','',2,2), 

-- Terzo indovinello
(4, 'Ok, va bene… ti vedo proprio sull''orlo.\nPer pietà, ti faccio un''ultima domanda. Se rispondi giusto, giuro che ti lascio 
passare subito! Allora, senti bene:\n
Cos?è una classe astratta?', 3, 'È una classe che non può essere istanziata, poichè ha metodi non implementati.', 'È una classe che non ha metodi o attributi.'
, 1,0),

-- risposta corretta
(4,'Bravo! Esatto: non puoi creare oggetti direttamente da una classe astratta.
Dai, passa… corri! Che la forza sia con te (e col tuo intestino).', 3, '', '', 2,1),

-- risposta errata (perde)
(4, 'Eh no… una classe astratta può avere attributi e anche metodi implementati.Mi dispiace, ma la fila resta fila…', 3,'','',2,2),

--INSERVIENTE QUARTO PIANO:

-- prima domanda
(5,'Salve… il bagno è un inferno. Sa per caso se c''è un altro?\n Potrei saperlo. Ma le verità profonde vanno pulite come i pavimenti: con varechina. Tu ce l''hai?', 1,
'Ecco la varechina. L’ho trovata nel laboratorio di robotica.', 'Mi dispiace, non ho la candeggina con me.', 1,0),

-- se ha la varechina
(5, 'Questa sì che profuma di dedizione.\n"
Ascolta bene, ragazzo: Sette sono i piani, ma non tutti mostrano il vero. Dove il sapere si tiene alto, una porta si apre solo a chi ha la chiave della pulizia.' ,'','',2,1),

-- se non ha la candeggina
(5, 'Io non lavoro per aria fritta. Torna con qualcosa che disinfetti, o resta nel tuo sudore.', 1,'','' 2,2), 

-- IL SALVATORE :

(5, 'Mi scusi… io… non ce la faccio più… non so dove andare… mi sa che… mi scappa…', 1, 'Calma, ragazzo. Ricorda: nessuna corsa può essere vinta 
se prima non si respira.Persino il bisogno più urgente va affrontato con dignità… e carta igienica.Tieni, giovane guerriero. Non è molto… ma nelle mani giuste, 
può fare miracoli.', '' , 1,0), 
(5, 'Grazie… grazie davvero…',1,'Vai. Corri. E ricorda:\nIl vero eroe non è chi trattiene……ma chi arriva in tempo!', '', 2,1),


--------------------------------------------

-- Sesto piano
(6, 'Scusa… ma perché mi guardi in quel modo? Sembra quasi che voglia picchiarmi…', 1,'Ah… scusami! Non ce l''ho con te… è che ho un dolor di pancia pazzesco…
Sarà stato quel maledetto caffè del bar… Fra poco devo pure laurearmi… Non è che, per caso, sai dove sia un bagno qui vicino?', '', 1,0),

(6,'Magari! Lo sto cercando anch’io… mi dispiace, davvero… sto peggio di te.', 1,'Capisco… va bene… speriamo di trovarlo in tempo.
Anche se, a dirla tutta… credo che per me sia già troppo tardi…"Buona fortuna, collega… che il destino, e il rotolo di carta igienica, 
siano con te!', '', 2,1);



