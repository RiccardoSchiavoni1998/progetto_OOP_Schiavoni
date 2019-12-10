package com.PSOOP.PROGETTO.SCHIAVONI.OOP.Modello;

import java.util.Vector;

public class AziendaAgricola {
    private String agrarea; //area agricola totale
    private String croparea;  //area totale seminata in un determinato anno
    private String indic_ef;
    private String geo;
    private Double [] time;

    //costruttore della classe modellante
    public AziendaAgricola (String agrarea, String croparea, String indic_ef, String geo, Double[] time){
        this.agrarea = agrarea;
        this.croparea = croparea;
        this.indic_ef = indic_ef;
        this.geo = geo;
        this.time = time;
    }

    //metodi getter
    public String getAgrarea(){ return agrarea; }
    public String getCroparea(){ return croparea; }
    public String getIndic_ef(){ return indic_ef; }
    public String getGeo(){ return geo; }
    public Double[] getTime() {return time; }
    @Override
    public String toString() {
        return "agrarea: " + getAgrarea() + " , croparea: " + getCroparea() + " , indice_ef: " + getIndic_ef() +" , geo: " + getGeo() ;
    }
};