/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.chipcoin;

import apiSiweb.Operaciones.TableMaster;

/**
 *
 * @author casajosefa
 */
public class MlmLiberacionMercancia extends TableMaster {

    public MlmLiberacionMercancia() {
            super("mlm_liberacion_mercancias", "LM_ID", "", "");
        this.Fields.put("LM_ID",0);
        this.Fields.put("CT_ID",0);
        this.Fields.put("LM_NUMERO_CONTRATO","");
        this.Fields.put("LM_PRODUCTO","");
        this.Fields.put("LM_FECHA","");
        this.Fields.put("LM_HORA","");
        this.Fields.put("LM_FRACCION_ARCELARIA","");
        this.Fields.put("LM_PAIS_ORIGEN","");
        this.Fields.put("PA_ID_ORIGEN",0);
        this.Fields.put("LM_PAIS_DESTINO","");
        this.Fields.put("PA_ID_DESTINO",0);
        this.Fields.put("LM_REQUISITOS_IMPORTACION","");
        this.Fields.put("LM_REQUISITOS_EXPORTACION","");
        this.Fields.put("LM_COMPRADOR","");
        this.Fields.put("LM_REGISTRO_FISCAL_COMPRADOR","");
        this.Fields.put("LM_VENDEDOR","");
        this.Fields.put("LM_REGISTRO_FISCAL_VENDEDOR","");
        this.Fields.put("LM_PEDIDO","");
        this.Fields.put("LM_ORDEN_COMPRA","");
        this.Fields.put("LM_FACTURA","");
        this.Fields.put("PM_ID",0);
        this.Fields.put("LM_VALOR_FACTURA",0.0);
        this.Fields.put("LM_MONEDA_FACTURA",0);
        this.Fields.put("LM_LETRA_FACTURA","");
        this.Fields.put("LM_CARTERA_PAGO",0);
        this.Fields.put("CW_ID_PAGO",0);
        this.Fields.put("LM_CARTERA_COBRO","");
        this.Fields.put("CW_ID_COBRO",0);
        this.Fields.put("LM_CERTIFICADO_SANITARIO","");
        this.Fields.put("LM_CERTIFICADO_ETIQUETA","");
        this.Fields.put("LM_OTROS_CERTIFICADOS","");
        this.Fields.put("LM_FECHA_COMPROMISO_ENVIO","");
        this.Fields.put("LM_PENALIZACION_RETRASO_ENVIO","");
        this.Fields.put("LM_COSTO_TRASLADO",0.0);
        this.Fields.put("LM_MONEDA_TRASLADO",0);
        this.Fields.put("LM_LETRA_TRASLADO","");
        this.Fields.put("LM_PENALIZACION_NO_LIBERACION","");
        this.Fields.put("LM_MONEDA_NO_LIBERACION",0);
        this.Fields.put("LM_LETRA_NO_LIBERACION","");
        this.Fields.put("LM_FECHA_PENALIZACION_NO_LIBERACION","");
        this.Fields.put("LM_CLAUSULAS","");
        this.setBolGetAutonumeric(true);
    }
}
