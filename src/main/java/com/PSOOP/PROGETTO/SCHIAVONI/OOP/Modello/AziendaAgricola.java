package com.PSOOP.PROGETTO.SCHIAVONI.OOP.Modello;

public class AziendaAgricola {
    private String agrarea; //area agricola totale
    private String croparea;  //area totale seminata in un determinato anno
    private String indic_ef;
    private String geo;
    private double[] time;

    //costruttore della classe modellante
    public AziendaAgricola (String argrarea, String croprea, String indic_ef, String geo, double[] contributo){
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
    public double[] getTime() {return time; }

    @Override
    public String toString() {
        return "agrarea: " + getAgrarea() + " , croparea: " + getCroparea() + " , indice_ef: " + getIndic_ef() +" , geo: " + getGeo() ;
    }
};