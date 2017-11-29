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
public class VtaMakPrAlterno extends TableMaster {

    public VtaMakPrAlterno() {
        super("vta_mak_prod_alternos", "PR_ALT_ID", "", "");
        this.Fields.put("PR_ALT_ID", 0);
        this.Fields.put("PR_ID_MASTER", 0);
        this.Fields.put("PR_CODIGO_MASTER", "");
        this.Fields.put("PR_ID_ALTERNO", 0);
        this.Fields.put("PR_CODIGO", "");
        this.Fields.put("PR_DESCRIPCION", "");
        this.Fields.put("PR_UNIDADMEDIDA", "");
        this.Fields.put("PR_OBSERVACIONES", "");
    }
}