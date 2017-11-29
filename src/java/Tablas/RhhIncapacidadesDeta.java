/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import apiSiweb.Operaciones.TableMaster;

/**
 *
 * @author ZeusSIWEB
 */
public class RhhIncapacidadesDeta extends TableMaster {

   public RhhIncapacidadesDeta() {
      super("rhh_incapacidades_deta", "RID_ID", "", "");
      this.Fields.put("RID_ID", 0);
      this.Fields.put("RID_FECHA", "");
      this.Fields.put("RID_NUM_DIAS", 0);
      this.Fields.put("RHIN_ID", 0);
      this.Fields.put("RHIN_CERTIFICADO_INCAPACIDAD", "");
      this.Fields.put("RTI_ID", 0);
      this.Fields.put("RTR_ID", 0);
      this.Fields.put("RTD_ID", 0);
      this.Fields.put("RTC_ID", 0);
   }
}
