/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.chipcoin;

import apiSiweb.Operaciones.TableMaster;

/**
 *
 * @author Fernando
 */
public class MlmWebPage extends TableMaster {

    public MlmWebPage() {
        super("mlm_web_page", "PAGE_ID", "", "");
        this.Fields.put("PAGE_ID", 0);
        this.Fields.put("PV_ID", 0);
        this.Fields.put("PAGE_TITULO", "");
        this.Fields.put("PAGE_IMG_PRINCIPAL", "");
        this.Fields.put("PAGE_TXT_INTRODUCCION", "");
        this.Fields.put("PAGE_TXT_BIOGRAFIA", "");
        this.Fields.put("PAGE_IMG1_BIOGRAFIA", "");
        this.Fields.put("PAGE_BIOGRAFIA1", "");
        this.Fields.put("PAGE_IMG2_BIOGRAFIA", "");
        this.Fields.put("PAGE_BIOGRAFIA2", "");
        this.Fields.put("PAGE_BIOGRAFIA3", "");
        this.Fields.put("PAGE_IMG3_BIOGRAFIA", "");
        this.Fields.put("PAGE_TXT_OBJETIVOS", "");
        this.Fields.put("PAGE_IMG1_OBJETIVOS", "");
        this.Fields.put("PAGE_OBJETIVOS1", "");
        this.Fields.put("PAGE_IMG2_OBJETIVOS", "");
        this.Fields.put("PAGE_OBJETIVOS2", "");
        this.Fields.put("PAGE_IMG3_OBJETIVOS", "");
        this.Fields.put("PAGE_OBJETIVOS3", "");
        this.Fields.put("PAGE_TXT_LOGROS", "");
        this.Fields.put("PAGE_IMG1_LOGROS", "");
        this.Fields.put("PAGE_LOGROS1", "");
        this.Fields.put("PAGE_IMG2_LOGROS", "");
        this.Fields.put("PAGE_LOGROS2", "");
        this.Fields.put("PAGE_IMG3_LOGROS", "");
        this.Fields.put("PAGE_LOGROS3", "");
        this.Fields.put("PAGE_TWITTER", "");
        this.Fields.put("PAGE_FACEBOOK", "");
        this.Fields.put("PAGE_GOOGLE", "");
        this.setBolGetAutonumeric(true);
    }
}