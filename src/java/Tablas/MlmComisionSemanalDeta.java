/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import apiSiweb.Operaciones.TableMaster;

/**
 *Representa el detalle de las comisiones semanales
 * @author ZeusSIWEB
 */
public class MlmComisionSemanalDeta extends TableMaster {

   public MlmComisionSemanalDeta() {
      super("mlm_comision_semanal_deta", "COMI_ID", "", "");
      this.Fields.put("COMI_ID", 0);
      this.Fields.put("MSE_ID", 0);
      this.Fields.put("CT_ID", 0);
      this.Fields.put("COMI_FUENTE", 0);
      this.Fields.put("COMI_DESTINO", 0);
      this.Fields.put("COMI_PORCENTAJE", 0.0);
      this.Fields.put("COMI_IMPORTE", 0.0);
      this.Fields.put("COMI_NIVEL", 0);
      this.Fields.put("SC_ID", 0);
      this.Fields.put("COMI_ARMADOINI", 0);
      this.Fields.put("COMI_ARMADOFIN", 0);
      this.Fields.put("COMI_ARMADONUM", 0);
      this.Fields.put("COMI_ARMADODEEP", 0);
      this.Fields.put("COMI_CODIGO", "");
      this.Fields.put("COMI_IMPORTE_ORIGEN", 0.0);
      this.Fields.put("COMI_UPLINE", 0);
   }

}
