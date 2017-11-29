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
public class VtaMakVisitaClientes extends TableMaster {

    public VtaMakVisitaClientes() {
        super("vta_mak_visita_clientes", "VV_ID", "", "");
        this.Fields.put("VV_ID", 0);
        this.Fields.put("CT_ID", 0);
        this.Fields.put("DS_ID", 0);
        this.Fields.put("ME_ID", 0);
        this.Fields.put("VV_ANIO", "");
        this.Fields.put("VE_ID", 0);
        this.Fields.put("CC5_ID", 0);
        this.Fields.put("VV_PRIORIDAD", 0);
        this.Fields.put("VV_FECHA", "");
        this.Fields.put("VV_ACTIVIDAD", "");
        this.bolGetAutonumeric = true;
    }
}