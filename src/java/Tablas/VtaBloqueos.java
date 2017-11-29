/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import apiSiweb.Operaciones.TableMaster;

/**
 *
 * @author casajosefa
 */
public class VtaBloqueos extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public VtaBloqueos() {
        super("vta_bloqueos", "BLQ_ID", "", "");
        this.Fields.put("BLQ_ID", 0);
        this.Fields.put("BLQ_MODULO", "");
        this.Fields.put("BLQ_USUARIO", 0);
        this.Fields.put("BLQ_ID_EDIT", 0);
        this.Fields.put("BLQ_FECHA", "");
        this.Fields.put("BLQ_HORA", "");
        this.Fields.put("BLQ_ESTATUS", 0);
        this.Fields.put("LASTSESSIONID", "");
    }
   // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    // </editor-fold>
}
