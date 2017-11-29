/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import apiSiweb.Operaciones.TableMaster;

/**
 *
 * @author CasaJosefa
 */
public class VtaCapturaVisitas extends TableMaster {

    public VtaCapturaVisitas() {
        super("vta_captura_visitas", "CV_ID", "", "");
        this.Fields.put("CV_ID", 0);
        this.Fields.put("CV_FECHA", "");
        this.Fields.put("CT_ID", 0);
        this.Fields.put("COT_ID", 0);
        this.Fields.put("PD_ID", 0);
        this.Fields.put("FAC_ID", 0);
        this.Fields.put("CV_ENTREGA", 0);
        this.Fields.put("CV_DEMO", 0);
        this.Fields.put("CV_GARANTIA", 0);
        this.Fields.put("CV_TELEFONO", 0);
        this.Fields.put("CV_MAIL", 0);
        this.Fields.put("CV_COBRANZA", 0);
        this.Fields.put("CV_OBSERVACION", 0);
        this.Fields.put("CV_HORA", "");
        this.Fields.put("VE_ID", 0);
        this.Fields.put("TVEN_ID", 0);
        this.Fields.put("CV_VENT_DIR", 0);
        this.Fields.put("CV_VISITADO", 0);       
        this.Fields.put("CV_VISITA_NO_AGENDADA", 0);       
        this.Fields.put("VV_ID", 0);       

        this.bolGetAutonumeric = true;
    }

}
