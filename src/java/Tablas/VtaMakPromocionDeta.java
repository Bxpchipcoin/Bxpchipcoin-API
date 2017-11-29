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
public class VtaMakPromocionDeta extends TableMaster {

    public VtaMakPromocionDeta() {
        super("vta_mak_promocion_deta", "PRMD_ID", "", "");
        this.Fields.put("PRMD_ID", 0);
        this.Fields.put("PRM_ID", 0);
        this.Fields.put("PR_ID", 0);
        this.Fields.put("PR_CANT_DESDE", 0.0);
        this.Fields.put("PR_CANT_HASTA", 0.0);
        this.Fields.put("PR_MULTIPLO", 0.0);
        this.Fields.put("PR_PORC_DESC", 0.0);
        this.Fields.put("PR_UN_GRATIS", 0.0);
        this.Fields.put("PR_PESOS_TDA", 0.0);
        this.Fields.put("PR_PESOS_MAYR", 0.0);
        this.Fields.put("PR_PESOS_DIST", 0.0);
        this.Fields.put("PR_PESOS_PUB", 0.0);
        this.Fields.put("PR_USD_TDA", 0.0);
        this.Fields.put("PR_USD_MAYR", 0.0);
        this.Fields.put("PR_USD_DIST", 0.0);
        this.Fields.put("PR_USD_PUB", 0.0);
        this.Fields.put("PR_ONLY_USD", 0);
        this.Fields.put("PR_CODIGO", "");
    }
}