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
public class VtaPromocionEncData extends TableMaster {

    public VtaPromocionEncData() {
        super("vta_promocion_enc_data", "ENCDATA_ID", "", "");
        this.Fields.put("FAC_ID", 0);
        this.Fields.put("PD_ID", 0);
        this.Fields.put("TKT_ID", 0);
        this.Fields.put("COT_ID", 0);
        this.Fields.put("ENCP_ID", 0);

    }
}
