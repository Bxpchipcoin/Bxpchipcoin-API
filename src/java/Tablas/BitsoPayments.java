/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import apiSiweb.Operaciones.TableMaster;

/**
 *Representa un registro de pago de bitso
 * @author ZeusSIWEB
 */
public class BitsoPayments extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public BitsoPayments() {
      super("bitso_payments", "BT_ID", "", "");
      this.Fields.put("BT_ID", 0);
      this.Fields.put("BT_ADDRESS", "");
      this.Fields.put("BT_RATE", "");
      this.Fields.put("BT_VALID", 0);
      this.Fields.put("BT_NEXT_POLL", 0);
      this.Fields.put("BT_STATUS", "");
      this.Fields.put("BT_RECEIVED", 0.0);
      this.Fields.put("BT_AMOUNT", 0.0);
      this.Fields.put("BT_TRANSACTIONS", 0);
      this.Fields.put("BT_UPDATE", "");
      this.Fields.put("CT_ID", 0);
      this.Fields.put("BT_FECHA", "");
      this.Fields.put("BT_HORA", "");
      this.Fields.put("BT_MLM_PAGOS_ID", 0);
      this.Fields.put("BT_FECHA_COMPLETE", "");
      this.Fields.put("BT_HORA_COMPLETE", "");
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
