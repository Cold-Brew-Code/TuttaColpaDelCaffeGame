<div align="center">
    <h1>Tutta colpa del caffè</h1>
    <img src="docs/img/icon.png" alt="Icona Gioco" width="25%" />
</div>

---

## 📖 Trama del gioco

Tratto (quasi) da una storia vera.

È una calda 🥵 mattina di luglio. Uno studente di Informatica si sta dirigendo al Dipartimento per sostenere uno degli esami più temuti del corso di laurea: Metodi Avanzati di Programmazione.

Tutto sembra andare secondo i piani... finché, non appena varcato l'ingresso del campus, viene colto da un’improvvisa, impellente esigenza fisiologica 😰.

Inizia così un'odissea tragicomica tra i corridoi dell’università. Nessun bagno sembra essere facilmente accessibile, ogni porta è chiusa, ogni indicazione fuorviante. Lo studente dovrà esplorare a fondo il campus, raccogliere indizi, affrontare dialoghi surreali e cercare aiuto da personaggi secondari come studenti fuori corso, baristi svogliati, inservienti criptici e persino macchinette del caffè apparentemente senzienti.

Riuscirà a trovare un bagno funzionante prima che sia troppo tardi? E soprattutto, ce la farà ad arrivare in tempo all’esame senza compromettere il proprio futuro accademico?

Un’avventura testuale tra il grottesco e il quotidiano, dove ogni scelta può fare la differenza.

Preparati a ridere, riflettere... e correre💨.

---

## 📂 Struttura del progetto e documentazione

Il progetto è organizzato nelle seguenti cartelle principali:

* **`adventure/`**: Contiene il codice sorgente del gioco, organizzato come progetto Maven.
* **`docs/`**: Contiene la documentazione del progetto.
    * `Report.md`: Il file di documentazione dettagliata.
* **`javadoc/`**: La cartella con la documentazione generata dal Javadoc.
* **`img/`**: Le immagini utilizzate all'interno del file di documentazione (`docs/Report.md`).
* **`resources/`**: Contiene i file di risorse, come il diagramma delle classi.

Per accedere alla documentazione completa relativa al progetto **[clicca qui](docs/Report.md)**.

---

## 🛠️ Requisiti

Per compilare ed eseguire il gioco, assicurati di avere installato:

* **Java Development Kit (JDK) 21** (o superiore)
* **Apache Maven**

---

## 💻 Installazione

Segui questi semplici passaggi per avviare il gioco sulla tua macchina locale:

1.  Clona il repository:
    ```bash
    git clone [https://github.com/Cold-Brew-Code/TuttaColpaDelCaffeGame.git](https://github.com/Cold-Brew-Code/TuttaColpaDelCaffeGame.git)
    ```

2.  Accedi alla cartella del progetto Maven:
    ```bash
    cd TuttaColpaDelCaffeGame/adventure
    ```

3.  Compila il progetto e crea il file JAR eseguibile:
    ```bash
    mvn clean install
    ```

---

## 🚀 Utilizzo

Dopo aver compilato il progetto, esegui il file JAR dalla riga di comando:

```bash
java -jar target/TuttaColpaDelCaffeGame-1.0-SNAPSHOT.jar
