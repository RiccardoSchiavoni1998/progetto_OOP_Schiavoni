package com.PSOOP.PROGETTO.SCHIAVONI.OOP.service;

import com.PSOOP.PROGETTO.SCHIAVONI.OOP.Modello.AziendaAgricola;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistiche {

    /**
     * metodo per il calcolo del minimo
     */
    public static double valoreminimo(List<Double> lista) {
        double min;
        min = (double) lista.get(0); //inizializzo la variabile min con il primo valore della lista
        for (double n : lista) { //ogni iterazione del ciclo confronto il valore di min con quello dell'elemento corrente
            if (n < min) min = n;  //se il valore è minore di min vado a inserire quel valore all'interno di min
        }
        return min;
    }

    /**
     * metodo per il calcolo del massimo
     */
    public static double valoremassimo(List<Double> lista) {
        double max;
        max = (double) lista.get(0);
        for (double n : lista) {
            if (n > max) max = n;
        }
        return max;
    }

    /**
     * metodo per il calcolo del valor medio
     */
    public static double valormedio(List<Double> lista) {
        return sommavalori(lista) / numerovalori(lista) ;
    }

    /**
     * metodo per il calcolo della deviazione standard
     */
    public static double deviazionestandard(List<Double> lista) {
        double avg = valormedio(lista) ;
        double var = 0;
        for (double n : lista) {
            var += (Math.pow(n - avg, 2) / 18);
        }
        return Math.sqrt(var);
    }

    /**
     * metodo per il calcolo della somma
     */
    public static double sommavalori(List<Double> lista) {
        double somma = 0;
        for (double n : lista) {
            somma += n;
        }
        return somma;
    }

    /**
     * metodo per il calcolo del numero totale di elementi in una colonna di attributi
     */
    public static double numerovalori(List<Double> lista) {
        return lista.size();
    }

    /**
     * metodo per il conteggio degli elementi unici
     */
    public static Map< Object, Integer >  conteggioElementiUnici(List lista){ //considero ogni oggetto come una chiave associata ad un valore di tipo intero che indica quante volte è presente
        Map<Object, Integer> map = new HashMap<>();
        for (Object object : lista){
            Integer numero = map.get(object); // assegno a numero il valore associato alla chiave corrente (Object è chiave di un valore di tipo Integer)
            map.put(object, ( numero == null ? 1 : numero + 1));  //verifico se il valore associato alla chiave corrente sia null , se la condizione è soddifatta aggiungo l'oggetto appena trovato alla mappa con valore 1 altrimenti chiave si trova già nella mappa aumento di 1
        }
        return map;
    }

    /**
     * metodo per ottenere le statistiche (formato JSON) sui dati specificando su quale attributo (colonna di dati) si effettua la computazione
     */
    public static Map<String,Object> statisticheSingoloCampo(List lista, String attributo){
        Map<String,Object> map = new HashMap<>();
        map.put("attributo: ", attributo);
        if(!lista.isEmpty()){
            if(ServiceAzAgr.Anni.contains(attributo)){ //controllo se l'attributo è di uno degli anni
                List<Double> valori = new ArrayList<>();
                for (Object obj : lista){
                    valori.add(((Double) obj));
                }
                map.put("valor medio: ", valormedio(valori));
                map.put("valore minimo: ", valoreminimo(valori));
                map.put("valore massimo: ", valoremassimo(valori));
                map.put("deviazione standard: ", deviazionestandard(valori));
                map.put("numero valori: ", numerovalori(valori) );
                map.put("somma valori: ", sommavalori(valori ));
            }
            else{
                map.put("elementi unici: ", conteggioElementiUnici(lista));
                map.put("numero elementi: ", numerovalori(lista));
            }

        }
        return map;
    }
}
