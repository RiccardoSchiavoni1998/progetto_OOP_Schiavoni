package com.PSOOP.PROGETTO.SCHIAVONI.OOP.Controller;

import com.PSOOP.PROGETTO.SCHIAVONI.OOP.Modello.AziendaAgricola;
import com.PSOOP.PROGETTO.SCHIAVONI.OOP.service.GeneratoreMetadati;
import com.PSOOP.PROGETTO.SCHIAVONI.OOP.service.ServiceAzAgr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
* RESTITUIRE METADATI , DATI , STATISTICHE SUI DATO, SPECIFICANDO L'ATTRIBUTO (COLONNA DEI DATI) SU CUI SI EFFETTUA LA COMPUTAZIONE */
/*
     @GetMapping("/risorsa") facendo una chiamata di tipo get posso ottenere una determinata risorsa che ci restituirà un determinato metodo
    public nomeClasse nomeMetodo (@RequestParam(name=parametro, defaultValue=valoreDefaul) Tipo nomeparam){.......}     ESEMPIO METODO RESTITUITO
    @RequestParam serve per poter chiedere di passare un parametro in ingresso
*/

@RestController
public class Controller {
    private ServiceAzAgr service;

    @Autowired
    public Controller(ServiceAzAgr service) {
        this.service = service;
    }

    /**
     * Metodo GET che rstituisce tutti i metadati
     *
     * @return lista dei Metadati
     */
    @GetMapping("/metadata")
    public List<Map> visualizzaMetadati(){
        return service.getMetadati();
    }

    /**
     *Metodo GET che restituisce la lista completa dei dati del dataset
     *
     * @return lista dei dati
     */
    @GetMapping("/data")
    public List<AziendaAgricola> visualizzaDati(){
        return service.getDati();
    }

    /*
    * Metodo GET che restituisce le statistiche di un determinato campo
    *
    * */
    @GetMapping("/statisticheCampo")
    public Map visualizzaStatisticheCampo(@RequestParam (value="campo", required = false,defaultValue ="") String Campo) {
        return service.getStatisticheSingoloCampo(Campo);
    }

}

