package com.PSOOP.PROGETTO.SCHIAVONI.OOP.service;

import com.PSOOP.PROGETTO.SCHIAVONI.OOP.Modello.AziendaAgricola;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Classe per generare i metadati
 */
public class GeneratoreMetadati {
        private static List<Map> Metadati = new ArrayList<>(); //creo lista di mappe per i metadati degli attributi, ogni mappa conterrà i metadati di un singolo attributo : nome nella classe, nome nel file, tipo

    /**
        * Attraverso il costruttore genero la lista dei metadati: nome dei campi nel dataset , il nome e il tipo della variabile nella classe modello
        *  */
        GeneratoreMetadati(String fileTSV) throws IOException {
            //List <Map> Metadati = new ArrayList<>();
            final String TAB_DELIMITER = "\t"; //definisco il separatore TSV
            int k = 0; //inizializzo variabile contatore
            Field[] fields = AziendaAgricola.class.getDeclaredFields(); //creo un array di oggetti Field i cui valori saranno gli attributi della classe modellante
            BufferedReader br = new BufferedReader(new FileReader(fileTSV));
            String line = br.readLine() ; // vado a leggere il file TSV
            line = line.replace (",", TAB_DELIMITER );
            line = line.replace ("/", TAB_DELIMITER );
            String[] orderedline = line.trim().split(TAB_DELIMITER); //trim elimina gli spazi bianchi alle estremità di una stringa mentre split spezza la stringa ogni volta che incontra un "\t"
            for (Field f : fields) { //scorro tutti i campi dell'array (considero singolarmente ogni attributo della classe)
                Map<String, String> map = new HashMap<>();  //creo una mappa chiave valore , che conterrà i metadati relativi all'attributo corrente (contenuto nell'oggetto f)
                map.put("alias",f.getName()); //associo la chiave "alias" al nome nella classe dell'attributo
                map.put("SourcedFile", orderedline[k]); //associo la chiave "sourcedFile" al nome nel dataset dell'attributo
                map.put("Type", f.getType().getSimpleName()); //associo la chiave "Type" al tipo dell'attributo
                Metadati.add(map); //vado ad aggiungere la mappa con i metadati relativi all'attributo corrente nella lista dei metadati
                k++;
            }
        }

    /**
     * metodo per restituire i metadati
     *  */
        public List<Map> getMetadata(){
            return Metadati;
        }
}



