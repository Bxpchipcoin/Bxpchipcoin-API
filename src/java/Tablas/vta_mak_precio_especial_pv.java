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
public class vta_mak_precio_especial_pv extends TableMaster {

    public vta_mak_precio_especial_pv() {
        super("vta_mak_precio_especial_pv", "PEP_ID", "", "");
        this.Fields.put("PEP_ID", 0);
        this.Fields.put("PE_PV_ID", 0);
        this.Fields.put("PE_PV_PR_ID", 0);
        this.Fields.put("PE_PV_PR_DESC", "");
        this.Fields.put("PE_PV_PR_CODIGO", "");
        this.Fields.put("PE_PV_PR_MARCA", "");
        this.Fields.put("PE_PV_CANT_DESDE",0.0);
        this.Fields.put("PE_PV_CANT_HASTA", 0.0);
        this.Fields.put("PE_PV_FEC_DESDE", "");
        this.Fields.put("PE_PV_FEC_HASTA", "");
        this.Fields.put("PE_PV_PREC_MXN",0.0);
        this.Fields.put("PE_PV_PREC_USD", 0.0);
        this.Fields.put("PE_PV_MON_FIJA", 0);
        this.Fields.put("PE_PV_ACTIVO", 0);
    }
}
