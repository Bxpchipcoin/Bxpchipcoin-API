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
public class RhhHorasExtra extends TableMaster {

   public RhhHorasExtra() {
      super("rhh_horas_extra", "RHE_ID", "", "");
      this.Fields.put("RHE_ID", 0);
      this.Fields.put("RHIN_ID", 0);
      this.Fields.put("RHE_NUM_HORAS", 0);
      this.Fields.put("RHE_FECHA", "");
      this.Fields.put("RHE_NUM_SEMANA", 0);
   }
}
