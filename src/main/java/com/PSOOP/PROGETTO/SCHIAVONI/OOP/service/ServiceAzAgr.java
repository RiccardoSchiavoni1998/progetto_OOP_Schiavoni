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
    static List<String> Time=new ArrayList<>();
    private static List<Map> Metadati = new ArrayList(); //creo lista di mappe per i metadati degli attributi, ogni mappa conterrà i metadati di un singolo attributo : nome nel file, nome e tipo nella classe
    private List<AziendaAgricola> Dati = new ArrayList<>();

    /**
     * Costruttore della classe, esegue la codifica del file JSON ed esegue il download, in seguito riempie la lista dei Dati e dei Metadati
     */
    public ServiceAzAgr() throws Exception {
        Time.add(Integer.toString(2005));
        Time.add(Integer.toString(2007));
        Time.add(Integer.toString(2010));
        Time.add(Integer.toString(2013));
        if(Files.exists ( Paths.get ( fileTSV) )){ //controllo se il file è presente in loacele
            Dati = (List<AziendaAgricola>) new Parsing(fileTSV); //eseguo il parsing del file, generando la lista dei dati
            Metadati = (List<Map>) new GeneratoreMetadati(fileTSV); //genero la lista dei metadati
        }else{
            try {//La classe astratta URL Connection è la superclasse di tutte le classi che rappresentano un collegamento di comunicazione tra l'applicazione e un URL. Le istanze di questa classe possono essere utilizzate sia per leggere che per scrivere nella risorsa a cui fa riferimento l'URL.
                URLConnection openConnection = new URL(url).openConnection(); //L'oggetto connessione openConnection viene creato richiamando sull'url di igresso openConnection (metodo di URLConnection)
                openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");//aggiungo user-agent alla connessione.( permetterà di visualizzare contenuti che sono stati concepiti per altre piattaforme)
                InputStream in = openConnection.getInputStream(); //rinidirizzo il flusso di input  creando oggetto in e apro canale di input del json ottenuto dall'url
                StringBuilder data = new StringBuilder(); // data è oggetto della classe StringBuilder, a differenza della classe String mi permette di definire una stringa di lunghezza variabile
                String line = "";
                try { //lettura JSON e salvataggio su stringa
                    InputStreamReader inR = new InputStreamReader(in); //creo oggetto inR , uno stream basico per la lettura , al costruttore passo l'oggetto in che lo collega al flusso di dati in input del file json
                    BufferedReader buf = new BufferedReader(inR); //creo oggetto buf , stream bufferizzato , al costruttore del quale passo lo stream basico in
                    while ((line = buf.readLine()) != null) { //vado a leggere il file json , il quale è su un unica riga, salvandolo sulla stringa line
                        data.append(line); //aggiungo il contenuto di line a data (data += line)
                    }
                } finally {
                    in.close(); //chiudo lo stream
                }
                //lettura del file salvato su stringa e conversione in oggetto
                JSONObject objText = (JSONObject) JSONValue.parseWithException(data.toString()); //creo un oggetto JSON (di tipo JSONObject) e al costruttore passo il contenuto della stringa data
                JSONObject objResult = (JSONObject) (objText.get("result")); //creo il JSONObject objResult e attraverso il costruttore gli assegno il valori dell'array associativo "result" all'interno del file JSON
                JSONArray objResources = (JSONArray) (objResult.get("resources")); //creo objResources , oggetto di tipo JSONArray. Prendo il conenuto dell' array associativo resources (all'interno di objResult )e lo passo al costruttore di objResult

                for (Object obj : objResources) {  //scorro tutti gli oggetti che rappresentano array associativi resources all'interno del file fino a trovare quello che contiene il la chiave "format" associata al corretto indirizzo
                    if (obj instanceof JSONObject) {
                        JSONObject o = (JSONObject) obj;
                        String format = (String) o.get("format"); // l'oggetto di tipo String format conterrà il valore associato alla chiave format
                        String urlD = (String) o.get("url"); // l'oggetto di tipo String urlD conterrà il valore associato alla chiave url
                        if (format.toLowerCase().contains("tsv")) {
                            downloadTSV(urlD, fileTSV ); //eseguo il download
                        }
                    }
                }
                Dati = (List<AziendaAgricola>) new Parsing(fileTSV); //eseguo il parsing del file scaricato, generando la lista dei dati
                Metadati = (List<Map>) new GeneratoreMetadati(fileTSV); //genero la lista dei metadati
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private static void downloadTSV(String url, String fileName) throws Exception {
        HttpURLConnection openConnection = (HttpURLConnection) new URL(url).openConnection();
        openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        InputStream in = openConnection.getInputStream();
        String data = "";
        String line = "";
        try {
            if(openConnection.getResponseCode() >= 300 && openConnection.getResponseCode() < 400) {
                downloadTSV(openConnection.getHeaderField("Location"),fileName);        //Richiama il metodo downloadTSV
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
     * Metodo che genera la lista dei Dati
     */
     public List<AziendaAgricola> getDati(){return Dati;}

    /**
     * Metodo che genera la lista dei Metadati
     */
    public List<Map> getMetadati(){return Metadati;}

    /**
     * Metodo che crea una lista con tutti i valori di un singolo attributo della classe
     */
    private List listaValoriCampo(String Campo){
            List<Object> valoriCampo = new ArrayList<>();
            try {
                if(Time.contains(Campo)){ //verifico il caso in cui il nome del campo si uno degli anni all'interno del vettore time
                    for(AziendaAgricola Azienda : Dati){
                        Object o = Azienda.getTime()[Integer.parseInt(Campo)];
                        valoriCampo.add(o);
                    }
                }else{
                    for(AziendaAgricola Azienda : Dati){
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
     * Metodo che genera un mappa<k,v> con tutte le statistiche dell'attributo che gli viene passato
     */
    public Map<String, Object> getStatisticheSingoloCampo(String Campo){
        return Statistiche.statisticheSingoloCampo(listaValoriCampo(Campo), Campo);
    }
}

