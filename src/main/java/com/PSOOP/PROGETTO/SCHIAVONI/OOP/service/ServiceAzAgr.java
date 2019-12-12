package com.PSOOP.PROGETTO.SCHIAVONI.OOP.service;

import com.PSOOP.PROGETTO.SCHIAVONI.OOP.Modello.AziendaAgricola;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ServiceAzAgr {
    private String fileTSV= "dataset.tsv";
    private String url= "http://data.europa.eu/euodp/data/api/3/action/package_show?id=PsCGW1qneloocoRKWv6ZYA"; //url di ingresso (del file JSON)
    static List<String> Anni=new ArrayList<>();
    private static List<Map> listaMetadati = new ArrayList(); //creo lista di mappe per i metadati degli attributi, ogni mappa conterrà i metadati di un singolo attributo : nome nel file, nome e tipo nella classe
    private List<AziendaAgricola> listaDati = new ArrayList<>();

    /**
     * Costruttore della classe, esegue la codifica del file JSON ed esegue il download, in seguito riempie la lista dei Dati e dei Metadati
     *
     * @throws Exception
     */
    public ServiceAzAgr() throws Exception {
        Anni.add(Integer.toString(2005)); // creo il vettore degli anni che mi servirà nella creazione delle statistiche
        Anni.add(Integer.toString(2007));
        Anni.add(Integer.toString(2010));
        Anni.add(Integer.toString(2013));
        if(Files.exists ( Paths.get ( fileTSV) )){//controllo se il file è presente in loacele
            Parsing parse= new Parsing(fileTSV); //creo un istanza della classe parsing e richiamo su di esso il costruttore andando a valorizzare i dati presenti nel fileTSV l'attributo "dati" (lista di Oggetti)
            listaDati = parse.getListaDati(); //genero la lista dei dati: richiamo il metodo getDati sull'oggetto parse, assegnando l'attributo "dati" a ListaDati
            GeneratoreMetadati meta= new GeneratoreMetadati(fileTSV);///creo un istanza della classe GeneratoreMetadati e richiamo su di esso il costruttore andando a valorizzare l'attributo "dati" (lista di Map) con i metadati dei dati presenti nel fileTSV
            listaMetadati = meta.getMetadata(); //genero la lista dei metadati: richiamo il metodo getMetadati sull'oggetto meta, assegnando l'attributo "dati" a Metadata
        }else{
            try {
                String line = getContent(url);
                JSONObject objText = (JSONObject) JSONValue.parseWithException(line); //creo un oggetto JSON (di tipo JSONObject) e al costruttore passo il contenuto della stringa data
                JSONObject objResult = (JSONObject) (objText.get("result")); //creo il JSONObject objResult e attraverso il costruttore gli assegno il valori dell'array associativo "result" all'interno del file JSON
                JSONArray objResources = (JSONArray) (objResult.get("resources")); //creo objResources , oggetto di tipo JSONArray. Prendo il conenuto dell' array associativo resources (all'interno di objResult )e lo passo al costruttore di objResult

                for (Object obj : objResources) {  //scorro tutti gli oggetti che rappresentano array associativi resources all'interno del file fino a trovare quello che contiene il la chiave "format" associata al corretto indirizzo
                    if (obj instanceof JSONObject) {
                        JSONObject o = (JSONObject) obj;
                        String format = (String) o.get("format"); // l'oggetto di tipo String format conterrà il valore associato alla chiave format
                        String urlD = (String) o.get("url"); // l'oggetto di tipo String urlD conterrà il valore associato alla chiave url
                        if (format.toLowerCase().contains("tsv")) {
                            downloadTSV(urlD , fileTSV ) ;
                        }
                    }
                }
                listaDati = (List<AziendaAgricola>) new Parsing(fileTSV); //eseguo il parsing del file scaricato, generando la lista dei dati
                listaMetadati  = (List<Map>) new GeneratoreMetadati(fileTSV); //genero la lista dei metadati
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
    *Metodo che effettua il download
     *  @param url url del sito dal quale scaricare il file
     *  @param fileName nome del file
     *  @throws Exception lancia l'eccezione
    *  */
    private static void downloadTSV(String url, String fileName) throws Exception {
        HttpURLConnection openConnection = (HttpURLConnection) new URL(url).openConnection();  //L'oggetto connessione di rete al server HTTP openConnection viene creato richiamando il metodo openConnection sull'url di ingresso
        openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0"); //aggiungo user-agent alla connessione.( permetterà di visualizzare contenuti che sono stati concepiti per altre piattaforme)
        InputStream in = openConnection.getInputStream(); //rinidirizzo il flusso di input creando oggetto in e apro canale di input del json ottenuto dall'url
        String data = "";
        String line = "";
        try {
            if(openConnection.getResponseCode() >= 300 && openConnection.getResponseCode() < 400) {
                downloadTSV(openConnection.getHeaderField("Location"),fileName);  //Richiama il metodo downloadTSV
                in.close();
                openConnection.disconnect();
                return;
            }
            Files.copy(in, Paths.get(fileName));
            System.out.println("File size " + Files.size(Paths.get(fileName)));
        } finally {
            in.close();
        }
    }

    /**
     *  Metodo che gestisce il redirect
     *
     *  @param url url del sito dal quale scaricare il file
     * @throws Exception
     * */
    private static String getContent(String url) throws Exception {
        HttpURLConnection openConnection = (HttpURLConnection) new URL(url).openConnection(); //L'oggetto connessione di rete al server HTTP openConnection viene creato richiamando il metodo openConnection sull'url di ingresso
        openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0"); //aggiungo user-agent alla connessione.( permetterà di visualizzare contenuti che sono stati concepiti per altre piattaforme)
        InputStream in = openConnection.getInputStream(); //rinidirizzo il flusso di input  creando oggetto in e apro canale di input del json ottenuto dall'url
        String data = "";
        String line = "";
        try {
            //gestione del redirect
            if(openConnection.getResponseCode() >= 300 && openConnection.getResponseCode() < 400) {
                data = getContent(openConnection.getHeaderField("Location")); // attraverso questo assegnamento ora all'interno di data ora avrò gli header file HTTP : componenti della sezione di intestazione dei messaggi di richiesta e risposta in HTTP, essi definiscono i parametri operativi di una transazione HTTP.)
                in.close(); //chiudo il buffer in input
                openConnection.disconnect(); //disconnect metodo che serve per disconnettere il server
                return data;
            }
            try { //lettura JSON e salvataggio su stringa
                InputStreamReader inR = new InputStreamReader(in); //creo oggetto inR , uno stream basico per la lettura , al costruttore passo l'oggetto in che lo collega al flusso di dati in input del file json
                BufferedReader buf = new BufferedReader(inR); //creo oggetto buf , stream bufferizzato , al costruttore del quale passo lo stream basico in
                while ((line = buf.readLine()) != null) { //vado a leggere il file json , il quale è su un unica riga, salvandolo sulla stringa line
                    data+=line; //aggiung ogni volta il contenuto attuale di line a data (data += line)
                }
            } finally {
                in.close(); //chiudo lo stream
            }
        } finally {
            in.close();
        }
        return data; //mi restitusce data contente il file JSON
    }

    /**
     * Metodo che genera la lista dei Dati
     *
     * @return ListaDati
     */
     public List<AziendaAgricola> getDati(){return listaDati;}

    /**
     * Metodo che genera la lista di mappe ognuna con i Metadati di un attributo
     *
     * @return List dei metadati
     */
    public List<Map> getMetadati(){return listaMetadati ;}

    /**
     * Metodo che crea una lista con tutti i valori di un singolo attributo della classe
     *
     * @param Campo di cui voglio ottenere i valori
     * @return lista con valori del campo
      */
    private List listaValoriCampo(String Campo){
            List<Object> valoriCampo = new ArrayList<>();
            try {
                if(Anni.contains(Campo)){ //verifico il caso in cui il nome del campo si uno degli anni all'interno del vettore time
                    for(AziendaAgricola Azienda : listaDati){
                        int var_controllo = Integer.parseInt(Campo) ;
                        int k = 0;
                        switch (var_controllo){
                            case 2005 :
                                break;
                            case 2007 : k = 1;
                                break;
                            case 2010 : k = 2;
                                break;
                            case 2013 : k = 3;
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + var_controllo);
                        }
                        Object o = Azienda.getTime()[k]; //Integer.parseInt(Campo)
                        valoriCampo.add(o);
                    }
                }else{
                    for(AziendaAgricola Azienda : listaDati ){
                        Method getter = AziendaAgricola.class.getMethod("get" + Campo.substring(0, 1).toUpperCase() + Campo.substring(1)); //costruisco il metodo get del modello di riferimento
                        Object value = getter.invoke(Azienda); //invoco il metodo get sull'oggetto della classe modellante
                        valoriCampo.add(value); //aggiungo il valore alla lista
                    }
                    }

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        return valoriCampo;
        }

    /**
     * Metodo che genera un mappa con tutte le statistiche dell'attributo che gli viene passato
     *
     * @param Campo
     * @return Map contentente statistiche di quel campo
     */
    public Map<String, Object> getStatisticheSingoloCampo(String Campo){
        return Statistiche.statisticheSingoloCampo(listaValoriCampo(Campo), Campo);
    }
}

