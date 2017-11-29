package Tablas;

import apiSiweb.Operaciones.TableMaster;

/**
 * Representa los tickets
 *
 * @author zeus
 */
public class vta_tickets extends TableMaster {

   /**
    * Constructor
    */
   public vta_tickets() {
      super("vta_tickets", "TKT_ID", "", "");
      this.Fields.put("TKT_ID", new Integer(0));
      this.Fields.put("TKT_FOLIO", "");
      this.Fields.put("TKT_FOLIO_C", "");
      this.Fields.put("TKT_FECHA", "");
      this.Fields.put("TKT_HORA", "");
      this.Fields.put("TKT_HORANUL", "");
      this.Fields.put("CT_ID", new Integer(0));
      this.Fields.put("TKT_IMPORTE", new Double(0));
      this.Fields.put("TKT_IMPUESTO1", new Double(0));
      this.Fields.put("TKT_IMPUESTO2", new Double(0));
      this.Fields.put("TKT_IMPUESTO3", new Double(0));
      this.Fields.put("TKT_TOTAL", new Double(0));
      this.Fields.put("SC_ID", new Integer(0));
      this.Fields.put("EMP_ID", new Integer(0));
      this.Fields.put("TKT_RAZONSOCIAL", "");
      this.Fields.put("TKT_RFC", "");
      this.Fields.put("TKT_CALLE", "");
      this.Fields.put("TKT_COLONIA", "");
      this.Fields.put("TKT_LOCALIDAD", "");
      this.Fields.put("TKT_MUNICIPIO", "");
      this.Fields.put("TKT_ESTADO", "");
      this.Fields.put("TKT_CP", "");
      this.Fields.put("TKT_TASA1", new Double(0));
      this.Fields.put("TKT_TASA2", new Double(0));
      this.Fields.put("TKT_TASA3", new Double(0));
      this.Fields.put("TKT_O_REM", new Integer(0));
      this.Fields.put("TKT_GANANCIA", new Double(0));
      this.Fields.put("ES_ID", new Integer(0));
      this.Fields.put("TKT_ANULADA", new Integer(0));
      this.Fields.put("TKT_FECHAANUL", "");
      this.Fields.put("TKT_DIASCREDITO", new Integer(0));
      this.Fields.put("TKT_NOTAS", "");
      this.Fields.put("TKT_LPRECIOS", new Integer(0));
      this.Fields.put("TKT_IDNC", new Integer(0));
      this.Fields.put("TKT_ES_SURTIDO", new Integer(0));
      this.Fields.put("TKT_US_ALTA", new Integer(0));
      this.Fields.put("TKT_US_ANUL", new Integer(0));
      this.Fields.put("TKT_MONEDA", new Integer(0));
      this.Fields.put("TKT_TASAPESO", new Double(0));
      this.Fields.put("VE_ID", new Integer(0));
      this.Fields.put("TKT_ESSERV", new Integer(0));
      this.Fields.put("TKT_FECHACREATE", "");
      this.Fields.put("TKT_COSTO", new Double(0));
      this.Fields.put("FAC_ID", new Integer(0));
      this.Fields.put("TKT_DESCUENTO", new Double(0));
      this.Fields.put("PD_ID", new Integer(0));
      this.Fields.put("TKT_NOTASPIE", "");
      this.Fields.put("TKT_REFERENCIA", "");
      this.Fields.put("TKT_CONDPAGO", "");
      this.Fields.put("TKT_ESRECU", new Integer(0));
      this.Fields.put("TKT_PERIODICIDAD", new Integer(0));
      this.Fields.put("TKT_DIAPER", new Integer(0));
      this.Fields.put("TKT_NUMPEDI", "");
      this.Fields.put("TKT_FECHAPEDI", "");
      this.Fields.put("TKT_ADUANA", "");
      this.Fields.put("TKT_NOMFORMATO", "");
      this.Fields.put("TI_ID", new Integer(0));
      this.Fields.put("TI_ID2", new Integer(0));
      this.Fields.put("TI_ID3", new Integer(0));
      this.Fields.put("TKT_USO_IEPS", new Integer(0));
      this.Fields.put("TKT_TASA_IEPS", new Integer(0));
      this.Fields.put("TKT_IMPORTE_IEPS", new Double(0));
      this.Fields.put("TKT_FORMADEPAGO", "");
      this.Fields.put("TKT_NUMCUENTA", "");
      this.Fields.put("TKT_METODODEPAGO", "");
      this.Fields.put("TKT_REGIMENFISCAL", "");
      this.Fields.put("TKT_NUM_GUIA", "");
      this.Fields.put("TKT_TIPO_FLETE", new Integer(0));
      this.Fields.put("TKT_IMPORTE_FLETE", new Double(0));
      this.Fields.put("TKT_ES_POR_PEDIDOS", new Integer(0));
      this.Fields.put("TKT_IMPORTE_PUNTOS", new Double(0));
      this.Fields.put("TKT_IMPORTE_NEGOCIO", new Double(0));
      this.Fields.put("TKT_NO_MLM", new Integer(0));
      this.Fields.put("TKT_CONSIGNACION", new Integer(0));
      this.Fields.put("CDE_ID", new Integer(0));
      this.Fields.put("DFA_ID", new Integer(0));
      this.Fields.put("MPE_ID", new Integer(0));
      this.Fields.put("TKT_NO_EVENTOS", new Integer(0));
      this.Fields.put("TKT_RECU_FINAL", "");
      this.Fields.put("TR_ID", new Integer(0));
      this.Fields.put("ME_ID", new Integer(0));
      this.Fields.put("TF_ID", new Integer(0));
      this.Fields.put("PD_RECU_ID", new Integer(0));
      this.Fields.put("TKT_EXEC_INTER_CP", new Integer(0));
      this.Fields.put("TKT_POR_DESCUENTO", new Double(0));
      this.Fields.put("COT_ID", new Integer(0));
      this.Fields.put("CC1_ID", new Integer(0));
      this.Fields.put("TKT_SERIE", "");
      this.Fields.put("TKT_TIENE_FLETE", new Integer(0));
      this.Fields.put("TKT_TIENE_SEGURO", new Integer(0));
      this.Fields.put("TKT_MONTO_SEGURO", new Double(0));
      
   }
}
