# progetto_OOP_Schiavoni

## Funzionamento all'avvio
L'applicazione, una volta lanciata, esegue il download di un dataset in formato TSV contenuto in un JSON fornito tramite un [URL] : https://data.europa.eu/euodp/data/api/3/action/package_show?id=PsCGW1qneloocoRKWv6ZYA

## Packages e classi
-   `modello`: contiene la classe  `AziendaAgricola`che modella gli attributi del dataset
-   `service`: contiene le classi:   `ServiceAzAgr`  che gestisce il download, la classe `Parsing` che crea la lista di dati, la classe  `Statistiche`per il calcolo delle statistiche sui dati, la classe `GeneratoreMetadati`per generare e restituire i metadati 
-   `controller`: contiene la classe  `Controller` che si occupa dell'interazione con l'utente

### Richieste GET
attraverso le seguenti richieste GET avviene l'interazione con l'utente

 - **/data** - Restituisce l'intero dataset in formato JSON;
 - **/metadata** - Restituisce il JSON contenente tutti i metadati;
 - **/statisticheCampo?campo="nomeCampo"** -restituisce tutte le statistiche su un determinato attributo
