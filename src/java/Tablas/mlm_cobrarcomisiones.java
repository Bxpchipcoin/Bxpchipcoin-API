/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import apiSiweb.Operaciones.TableMaster;

/**
 *
 * @author Fernando
 */
public class mlm_cobrarcomisiones extends TableMaster {

    public mlm_cobrarcomisiones() {
        super("mlm_cobrarcomisiones", "COM_ID", "", "");
        this.Fields.put("COM_ID", 0);
        this.Fields.put("ID_CLIENTE", "0");
        this.Fields.put("COM_FECHA", "");
        this.Fields.put("NOM_CLIENTE", "");
        this.Fields.put("COM_MONTO", 0.0);
        this.Fields.put("COM_ESTATUS", "");
        this.setBolGetAutonumeric(true);
    }
}