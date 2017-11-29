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
public class VtaRutasDeta extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public VtaRutasDeta() {
      super("vta_rutas_deta", "RUD_ID", "", "");
      this.Fields.put("RUD_ID", 0);
      this.Fields.put("RU_ID", 0);
      this.Fields.put("PD_ID", 0);
      this.Fields.put("RUD_ESTATUS", "");
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
