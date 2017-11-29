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
public class vta_mak_precio_especial_ct extends TableMaster {

    public vta_mak_precio_especial_ct() {
        super("vta_mak_precio_especial_ct", "PE_ID", "", "");
        this.Fields.put("PE_ID", 0);
        this.Fields.put("PE_CT_ID", 0);
        this.Fields.put("PE_CT_PR_ID", 0);
        this.Fields.put("PE_CT_PR_DESC", "");
        this.Fields.put("PE_CT_PR_CODIGO", "");
        this.Fields.put("PE_CT_PR_MARCA", "");
        this.Fields.put("PE_CT_CANT_DESDE", 0.0);
        this.Fields.put("PE_CT_CANT_HASTA", 0.0);
        this.Fields.put("PE_CT_FEC_DESDE", "");
        this.Fields.put("PE_CT_FEC_HASTA", "");
        this.Fields.put("PE_CT_PREC_MXN",0.0);
        this.Fields.put("PE_CT_PREC_USD",0.0);
        this.Fields.put("PE_CT_MON_FIJA", 0);
        this.Fields.put("PE_CT_ACTIVO", 0);
        this.Fields.put("PE_PR_COSTO", 0.0);
    }
}