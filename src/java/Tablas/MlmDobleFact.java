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
public class MlmDobleFact extends TableMaster {

    public MlmDobleFact() {
        super("mlm_doble_factor", "BD_ID", "", "");
        this.Fields.put("BD_ID", 0);
        this.Fields.put("DB_FECHA", "");
        this.Fields.put("DB_HORA", "");
        this.Fields.put("DB_NUMERO_APROB", "");
        this.Fields.put("DB_FECHA_VIGENCIA", "");
        this.Fields.put("DB_HORA_VIGENCIA", "");
        this.Fields.put("DB_USADO", "");
        this.Fields.put("CT_ID", 0);
        this.setBolGetAutonumeric(true);
    }
}
