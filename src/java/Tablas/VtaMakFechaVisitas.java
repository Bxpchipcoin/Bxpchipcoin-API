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
public class VtaMakFechaVisitas extends TableMaster {
    public VtaMakFechaVisitas() {
        super("vta_mak_fecha_visitas", "VVD_ID", "", "");
        this.Fields.put("VVD_ID", 0);
        this.Fields.put("VV_ID", 0);
        this.Fields.put("VVD_FECHA", "");
        this.Fields.put("VVD_VISITADO", 0);
    }
}