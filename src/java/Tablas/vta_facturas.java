package Tablas;

import apiSiweb.Operaciones.TableMaster;

/**
 * Representa las facturas del sistema
 *
 * @author zeus
 */
public class vta_facturas extends TableMaster {

   /**
    * Constructor
    */
   public vta_facturas() {
      super("vta_facturas", "FAC_ID", "", "");
      this.Fields.put("FAC_ID", new Integer(0));
      this.Fields.put("FAC_FOLIO", "");
      this.Fields.put("FAC_FOLIO_C", "");
      this.Fields.put("FAC_FECHA", "");
      this.Fields.put("FAC_HORA", "");
      this.Fields.put("FAC_HORANUL", "");
      this.Fields.put("CT_ID", new Integer(0));
      this.Fields.put("FAC_IMPORTE", new Double(0));
      this.Fields.put("FAC_IMPUESTO1", new Double(0));
      this.Fields.put("FAC_IMPUESTO2", new Double(0));
      this.Fields.put("FAC_IMPUESTO3", new Double(0));
      this.Fields.put("FAC_TOTAL", new Double(0));
      this.Fields.put("SC_ID", new Integer(0));
      this.Fields.put("EMP_ID", new Integer(0));
      this.Fields.put("FAC_RAZONSOCIAL", "");
      this.Fields.put("FAC_RFC", "");
      this.Fields.put("FAC_CALLE", "");
      this.Fields.put("FAC_COLONIA", "");
      this.Fields.put("FAC_LOCALIDAD", "");
      this.Fields.put("FAC_MUNICIPIO", "");
      this.Fields.put("FAC_ESTADO", "");
      this.Fields.put("FAC_CP", "");
      this.Fields.put("FAC_TASA1", new Double(0));
      this.Fields.put("FAC_TASA2", new Double(0));
      this.Fields.put("FAC_TASA3", new Double(0));
      this.Fields.put("FAC_O_REM", new Integer(0));
      this.Fields.put("FAC_GANANCIA", new Double(0));
      this.Fields.put("ES_ID", new Integer(0));
      this.Fields.put("FAC_ANULADA", new Integer(0));
      this.Fields.put("FAC_FECHAANUL", "");
      this.Fields.put("FAC_DIASCREDITO", new Integer(0));
      this.Fields.put("FAC_NOTAS", "");
      this.Fields.put("FAC_LPRECIOS", new Integer(0));
      this.Fields.put("FAC_IDNC", new Integer(0));
      this.Fields.put("FAC_ES_SURTIDO", new Integer(0));
      this.Fields.put("FAC_US_ALTA", new Integer(0));
      this.Fields.put("FAC_US_ANUL", new Integer(0));
      this.Fields.put("FAC_MONEDA", new Integer(0));
      this.Fields.put("FAC_TASAPESO", new Double(0));
      this.Fields.put("VE_ID", new Integer(0));
      this.Fields.put("FAC_FECHACREATE", "");
      this.Fields.put("FAC_ESSERV", new Integer(0));
      this.Fields.put("FAC_COSTO", new Double(0));
      this.Fields.put("FAC_DESCUENTO", new Double(0));
      this.Fields.put("FAC_CADENAORIGINAL", "");
      this.Fields.put("FAC_SELLO", "");
      this.Fields.put("FAC_SERIE", "");
      this.Fields.put("FAC_NOAPROB", "");
      this.Fields.put("FAC_FECHAAPROB", "");
      this.Fields.put("FAC_ESMASIVA", new Integer(0));
      this.Fields.put("PD_ID", new Integer(0));
      this.Fields.put("FAC_RETISR", new Double(0));
      this.Fields.put("FAC_RETIVA", new Double(0));
      this.Fields.put("FAC_NETO", new Double(0));
      this.Fields.put("FAC_NOTASPIE", "");
      this.Fields.put("FAC_REFERENCIA", "");
      this.Fields.put("FAC_CONDPAGO", "");
      this.Fields.put("FAC_NOMFORMATO", "");
      this.Fields.put("FAC_ESRECU", new Integer(0));
      this.Fields.put("FAC_PERIODICIDAD", new Integer(0));
      this.Fields.put("FAC_DIAPER", new Integer(0));
      this.Fields.put("FAC_NUMPEDI", "");
      this.Fields.put("FAC_FECHAPEDI", "");
      this.Fields.put("FAC_ADUANA", "");
      this.Fields.put("FAC_METODODEPAGO", "");
      this.Fields.put("FAC_FORMADEPAGO", "");
      this.Fields.put("TI_ID", new Integer(0));
      this.Fields.put("TI_ID2", new Integer(0));
      this.Fields.put("TI_ID3", new Integer(0));
      this.Fields.put("FAC_USO_IEPS", new Integer(0));
      this.Fields.put("FAC_TASA_IEPS", new Integer(0));
      this.Fields.put("FAC_IMPORTE_IEPS", new Double(0));
      this.Fields.put("FAC_REGIMENFISCAL", "");
      this.Fields.put("FAC_NUM_GUIA", "");
      this.Fields.put("FAC_TIPO_FLETE", new Integer(0));
      this.Fields.put("FAC_IMPORTE_FLETE", new Double(0));
      this.Fields.put("FAC_ES_POR_PEDIDOS", new Integer(0));
      this.Fields.put("FAC_IMPORTE_PUNTOS", new Double(0));
      this.Fields.put("FAC_IMPORTE_NEGOCIO", new Double(0));
      this.Fields.put("FAC_NO_MLM", new Integer(0));
      this.Fields.put("FAC_CONSIGNACION", new Integer(0));
      this.Fields.put("CDE_ID", new Integer(0));
      this.Fields.put("DFA_ID", new Integer(0));
      this.Fields.put("MPE_ID", new Integer(0));
      this.Fields.put("FAC_NO_EVENTOS", new Integer(0));
      this.Fields.put("FAC_RECU_FINAL", "");
      this.Fields.put("TR_ID", new Integer(0));
      this.Fields.put("ME_ID", new Integer(0));
      this.Fields.put("TF_ID", new Integer(0));
      this.Fields.put("FAC_ES_CFD", new Integer(0));
      this.Fields.put("FAC_ES_CBB", new Integer(0));
      this.Fields.put("PD_RECU_ID", new Integer(0));
      this.Fields.put("CTOA_ID", new Integer(0));
      this.Fields.put("FAC_EXEC_INTER_CP", new Integer(0));
      this.Fields.put("FAC_POR_DESCUENTO", new Double(0));
      this.Fields.put("COT_ID", new Integer(0));
      this.Fields.put("CC1_ID", new Integer(0));
      this.Fields.put("FAC_TIENE_FLETE", new Integer(0));
      this.Fields.put("FAC_TIENE_SEGURO", new Integer(0));
      this.Fields.put("FAC_MONTO_SEGURO", new Double(0));
   }
}
