package com.PSOOP.PROGETTO.SCHIAVONI.OOP.service;

import com.PSOOP.PROGETTO.SCHIAVONI.OOP.Modello.AziendaAgricola;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parsing {
    private static final String TAB_DELIMITER = "\t";
    private List<AziendaAgricola> record = new ArrayList<>();

    /**
     * Legge il contenuto del dataset e genera la lista dei dati
     */
    public Parsing (String FileTSV) throws Exception { //vado a creare una lista di istanze della classe AziendaAgricola , i campi dell'istanza i-esima conterranno i rispettivi valori dell'i-esima riga del filE
        BufferedReader br = new BufferedReader(new FileReader(FileTSV) ); //creo oggetto br, straem bufferizzato, e passo al costruttore lo stream basico (va a leggere il fileTSV)
        try {
            String line;
            br.readLine(); //apro il flusso di input, e salto la prima riga
            while ((line = br.readLine()) != null) { //vado a leggere ogni singola riga del file
                line = line.replace(",", TAB_DELIMITER);  //sostituisco le virgole con '\t'
                line = line.replace(":", "0"); //sostituisco con 0 i valori mancanti
                String[] splittedLine = line.trim().split(TAB_DELIMITER); //trim elimina gli spazi bianchi alle estremità di una stringa mentre split spezza la stringa ogni volta che incontra un "\t"
                String agrarea = splittedLine[0].trim(); //ora prendo ogni valore della singola riga del file e la inserisco all'interno dei rispettivi attributi della classe
                String croparea = splittedLine[1].trim();
                String indic_ef = splittedLine[2].trim();
                String geo = splittedLine[3].trim();
                double[] time = new double[4]; //creo il vettore che contiene i valori di tipo double in corrispondenza degli anni
                for (int i = 0; i < 4; i++) {
                    time[i] = Double.parseDouble(splittedLine[4 + i].trim());
                }
                AziendaAgricola CM = new AziendaAgricola(agrarea, croparea, indic_ef, geo, time); // ogni ciclo creo un nuovo oggetto ,chiamato CM, che contiene i valori ottenuti dal parsing della riga del file considerata nello specifico passo del ciclo
                record.add(CM); // inserisco oggetto CM nella lista record
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
    }
    /*public ArrayList<AziendaAgricola> getListaDati() throws Exception {
        return (ArrayList<AziendaAgricola>) record;
    }*/

};
