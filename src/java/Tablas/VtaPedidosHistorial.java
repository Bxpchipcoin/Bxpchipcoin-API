/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import apiSiweb.ContextoApt.VariableSession;
import apiSiweb.Operaciones.Conexion;
import apiSiweb.Operaciones.TableMaster;
import apiSiweb.Utilerias.Fechas;

/**
 *
 * @author ZeusSIWEB
 */
public class VtaPedidosHistorial extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   public static void guardaHistorial(Fechas fecha, Conexion oConn, VariableSession varSesiones,
      int intIdDocumento, String strTipoVta, String strComentario) {

      //Guardamos quien creo el documento
      VtaPedidosHistorial hist = new VtaPedidosHistorial();
      hist.setFieldString("PHIS_FECHA", fecha.getFechaActual());
      hist.setFieldString("PHIS_HORA", fecha.getHoraActual());
      hist.setFieldString("PHIS_NOM_USER", varSesiones.getStrUser());
      hist.setFieldString("PHIS_MOVIMIENTO", strComentario);
      if (strTipoVta.equals("1")) {
         hist.setFieldInt("FAC_ID", intIdDocumento);
         hist.setFieldString("PHIS_MODULO", "FACTURA");
      } else {
         if (strTipoVta.equals("3")) {
            hist.setFieldInt("PD_ID", intIdDocumento);
            hist.setFieldString("PHIS_MODULO", "PEDIDOS");
         } else {
            hist.setFieldInt("COT_ID", intIdDocumento);
            hist.setFieldString("PHIS_MODULO", "COTIZACION");
         }

      }
      hist.setFieldInt("ID_USER", varSesiones.getIntNoUser());
      hist.setFieldInt("EMP_ID", varSesiones.getIntIdEmpresa());
      hist.setFieldInt("SC_ID", varSesiones.getIntSucursalDefault());
      hist.Agrega(oConn);

   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public VtaPedidosHistorial() {
      super("vta_pedidos_historial", "PHIS_ID", "", "");
      this.Fields.put("PHIS_ID", 0);
      this.Fields.put("ID_USER", 0);
      this.Fields.put("PHIS_NOM_USER", "");
      this.Fields.put("PHIS_MOVIMIENTO", "");
      this.Fields.put("PHIS_MODULO", "");
      this.Fields.put("PHIS_FECHA", "");
      this.Fields.put("PHIS_HORA", "");
      this.Fields.put("PD_ID", 0);
      this.Fields.put("SC_ID", 0);
      this.Fields.put("EMP_ID", 0);

   }
// </editor-fold>

}
