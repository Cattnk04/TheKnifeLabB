***********************************************************************************
PROGETTO THE KNIFE
LABORATORIO B, CORSO DI LAUREA TRIENNALE IN INFORMATICA
UNIVERSITA' DEGLI STUDI DELL'INSUBRIA

Link repository GitHub
https://github.com/Cattnk04/TheKnife.git
***********************************************************************************

++REQUISITI++
L'applicazione richiede Java JDK 23 e il sistema operativo Windows 10 o superiori.
NOTA: l'applicazione è stata sviluppata e testata su Windows 10 e 11.

++AVVIARE L'APPLICAZIONE++
Doppio click sul jar eseguibile o tramite linea di comando digitare:
cd {percorso in cui è stato estratto l’archivio}
java -jar TheKnife.jar

++AVVIO DATABASE++
Per avviare il server è necessario avere PostgreSQL installato e un database chiamato theknife.

Ogni sviluppatore deve configurare le seguenti variabili d'ambiente nella propria configurazione di esecuzione:

DB_URL=jdbc:postgresql://localhost:5432/postgres
DB_USER=postgres
DB_PASSWORD=la_propria_password_postgres

In IntelliJ IDEA:
Run → Edit Configurations... → selezionare ServerMain → Modify options → Environment variables

Esempio:
DB_URL=jdbc:postgresql://localhost:5432/postgres;DB_USER=postgres;DB_PASSWORD=postgres

Non salvare password reali nel codice sorgente.