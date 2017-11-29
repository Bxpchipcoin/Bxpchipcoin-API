/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import apiSiweb.Operaciones.TableMaster;

/**
 *
 * @author Fernando
 */
public class VtaProducto extends TableMaster {

    public VtaProducto() {
        super("vta_producto", "PR_ID", "", "");
        this.Fields.put("PR_ID", 0);
        this.Fields.put("PR_CODIGO", "");
        this.Fields.put("PR_DESCRIPCION", "");
        this.Fields.put("PR_DESCRIPCIONCORTA", "");
        this.Fields.put("PR_DESCRIPCIONCOMPRA", "");
        this.Fields.put("PR_CATEGORIA1", 0);
        this.Fields.put("SC_ID", 0);
        this.Fields.put("PR_CODBARRAS", "");
        this.Fields.put("PR_MINIMA", 0);
        this.Fields.put("PR_MAXIMA", 0);
        this.Fields.put("PR_PAQUETE", 0);
        this.Fields.put("PR_DESGLOSAPAQ", 0);
        this.Fields.put("PR_ECOMM", 0);
        this.Fields.put("PR_ACTIVO", 0);
        this.Fields.put("PR_REQEXIST", 0);
        this.Fields.put("PR_EXENTO1", 0);
        this.Fields.put("PR_EXENTO2", 0);
        this.Fields.put("PR_EXENTO3", 0);
        this.Fields.put("PR_COSTOPEPS", 0.0);
        this.Fields.put("PR_COSTOUEPS", 0.0);
        this.Fields.put("PR_COSTOPROM", 0.0);
        this.Fields.put("PR_EXISTENCIA", 0.0);
        this.Fields.put("PR_NOMIMG1", "");
        this.Fields.put("PR_NOMIMG2", "");
        this.Fields.put("PR_APARTADOS", 0);
        this.Fields.put("PR_USANOSERIE", 0);
        this.Fields.put("PR_CATEGORIA2", 0);
        this.Fields.put("PR_CATEGORIA3", 0);
        this.Fields.put("PR_CATEGORIA4", 0);
        this.Fields.put("PR_CATEGORIA5", 0);
        this.Fields.put("PR_CATEGORIA6", 0);
        this.Fields.put("PR_CLIEFINAL", 0);
        this.Fields.put("PR_CTA_VENTA", "");
        this.Fields.put("PR_CTA_COSTO", "");
        this.Fields.put("PR_CUENTAVTACRED", "");
        this.Fields.put("PR_CTA_GASTOS", "");
        this.Fields.put("PR_UNIDADMEDIDA", 0);
        this.Fields.put("PR_INVENTARIO", 0);
        this.Fields.put("PR_CATEGORIA7", 0);
        this.Fields.put("PR_CATEGORIA8", 0);
        this.Fields.put("PR_CATEGORIA9", 0);
        this.Fields.put("PR_CATEGORIA10", 0);
        this.Fields.put("PR_COSTOCOMPRA", 0.0);
        this.Fields.put("MON_ID", 0);
        this.Fields.put("PR_COSTOREPOSICION", 0.0);
        this.Fields.put("PR_PORC_UTILIDAD", 0.0);
        this.Fields.put("EMP_ID", 0);
        this.Fields.put("PV_ID", 0);
        this.Fields.put("PR_RESURTIDO", 0);
        this.Fields.put("PR_COMISION", 0.0);
        this.Fields.put("PR_COMISION_ESP", 0.0);
        this.Fields.put("PR_PARTIDA_ARANCELARIA", "");
        this.Fields.put("PR_ESKIT", 0);
        this.Fields.put("PR_GPO_MODI_PREC", "");
        this.Fields.put("PR_USO_NOSERIE", 0);
        this.Fields.put("PR_UNIDADMEDIDA_COMPRA", 0);
        this.Fields.put("PR_IMSGEN1", "");
        this.Fields.put("PR_IMSGEN2", "");
        this.Fields.put("PR_TIEMPO_PROD", 0);
        this.Fields.put("PR_ALTO", 0.0);
        this.Fields.put("PR_ANCHO", 0.0);
        this.Fields.put("PR_LARGO", 0.0);
        this.Fields.put("PR_CRECEPCION", 0.0);
        this.Fields.put("PR_FLETE", 0.0);
        this.Fields.put("PR_VALORES", 0.0);
        this.Fields.put("PR_DTA", 0.0);
        this.Fields.put("PR_MADUANAL", 0.0);
        this.Fields.put("PR_DPROVEEDOR", 0.0);
        this.Fields.put("PR_CODIGO_CORTO", "");
        this.Fields.put("PR_MONEDA_COSTO", 0);
        this.Fields.put("PR_AGRUPA_ECOMM", 0);
        this.Fields.put("PR_MONEDA_VTA", 0);
        this.Fields.put("PR_ESKITINC", 0);
        this.Fields.put("PR_TCAMBIO_PROV", 0.0);
        this.Fields.put("PR_TCAMBIO_TDA", 0.0);
        this.Fields.put("PR_IMG1", 0);
        this.Fields.put("PR_IMG2", 0);
        this.Fields.put("PR_MUESTRA_REPO", 0);
        this.Fields.put("GT_ID", 0);
        this.Fields.put("PR_TASA_IVA", 0);
        this.Fields.put("PR_PREC_PZA", 0.0);
        this.Fields.put("PR_CUIDADO1", "");
        this.Fields.put("PR_CUIDADO2", "");
        this.Fields.put("PR_CUIDADO3", "");
        this.Fields.put("PR_CUIDADO4", "");
        this.Fields.put("PR_CUIDADO5", "");
        this.Fields.put("PR_CUIDADO6", "");
        this.Fields.put("PR_PRECAUCION1", "");
        this.Fields.put("PR_PRECAUCION2", "");
        this.Fields.put("PR_PRECAUCION3", "");
        this.Fields.put("PR_PRECAUCION4", "");
        this.Fields.put("PR_PRECAUCION5", "");
        this.Fields.put("PR_AVISOS1", "");
        this.Fields.put("PR_AVISOS2", "");
        this.Fields.put("PR_AVISOS3", "");
        this.Fields.put("PR_AVISOS4", "");
        this.Fields.put("PR_AVISOS5", "");
        this.Fields.put("PR_NUM_EMPAQUES", 0);
        this.Fields.put("PR_ORDEN_MINIMA", 0);
        this.Fields.put("PR_PORCENTAJE_SEG", 0.0);
        this.Fields.put("PR_NOMIMG3", "");
        this.Fields.put("PR_NOMIMG4", "");
        this.Fields.put("PR_NOMIMG5", "");
        this.Fields.put("PR_IMG3", 0);
        this.Fields.put("PR_IMG4", 0);
        this.Fields.put("PR_IMG5", 0);
        this.Fields.put("PR_IMSGEN3", "");
        this.Fields.put("PR_IMSGEN4", "");
        this.Fields.put("PR_IMSGEN5", "");
        this.Fields.put("PR_FACTORSOBRECOSTO", 0.0);
        this.Fields.put("PR_PORCENTAJE_MAX", 0.0);
        this.Fields.put("PR_ES_FLETE", 0);
        this.Fields.put("PR_ES_CICLICO", 0);
    }
}