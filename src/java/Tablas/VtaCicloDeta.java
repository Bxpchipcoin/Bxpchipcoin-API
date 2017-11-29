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
public class VtaCicloDeta extends TableMaster {

    public VtaCicloDeta() {
        super("vta_ciclico_deta", "CCD_ID", "", "");
        this.Fields.put("CC_ID", 0);
        this.Fields.put("CCD_ID", 0);
        this.Fields.put("PR_CODIGO", "");
        this.Fields.put("PR_DESCRIPCION", "");
        this.Fields.put("CCD_CANT_EXIST", 0.0);
        this.Fields.put("CCD_CONTEO1", 0);
        this.Fields.put("CCD_CONTEO2", 0);
        this.Fields.put("CCD_ADERIBLE", 0);
        this.Fields.put("CCD_TEXTIL", 0);
        this.Fields.put("CCD_ADERIBLE2", 0);
        this.Fields.put("CCD_TEXTIL2", 0);
    }
}