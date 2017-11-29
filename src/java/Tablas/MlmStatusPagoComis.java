/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import apiSiweb.Operaciones.TableMaster;

/**
 *
 * @author Siweb
 */
public class MlmStatusPagoComis extends TableMaster {

    public MlmStatusPagoComis() {
        super("mlm_status_pagocomis", "STACOMIS_ID", "", "");
        this.Fields.put("STACOMIS_ID", 0);
        this.Fields.put("STACOMIS_IMPORTE",0);
        this.Fields.put("STACOMIS_CUENTA", "");
        this.Fields.put("CT_ID", 0);
        this.Fields.put("STACOMIS_STATUS", 0);
        this.Fields.put("STACOMIS_FECHA", "");
    }
}
