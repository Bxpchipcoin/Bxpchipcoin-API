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
public class MlmWebCat extends TableMaster {

    public MlmWebCat() {
        super("mlm_web_cat", "PAGE_ID", "", "");
        this.Fields.put("PAGE_ID", 0);
        this.Fields.put("CT_ID", 0);
        this.Fields.put("PAGE_TITULO", "");
        this.Fields.put("PAGE_IMG_PRINCIPAL", "");       
        this.Fields.put("PAGE_TXT_BIOGRAFIA", "");
        this.Fields.put("PAGE_IMG1_BIOGRAFIA", "");
        this.Fields.put("PAGE_BIOGRAFIA1", "");
        this.Fields.put("PAGE_IMG2_BIOGRAFIA", "");
        this.Fields.put("PAGE_BIOGRAFIA2", "");
        this.Fields.put("PAGE_BIOGRAFIA3", "");
        this.Fields.put("PAGE_IMG3_BIOGRAFIA", "");
        this.Fields.put("PAGE_BIOGRAFIA4", "");
        this.Fields.put("PAGE_IMG4_BIOGRAFIA", "");
        this.Fields.put("PAGE_BIOGRAFIA5", "");
        this.Fields.put("PAGE_IMG5_BIOGRAFIA", "");
        this.Fields.put("PAGE_BIOGRAFIA6", "");
        this.Fields.put("PAGE_IMG6_BIOGRAFIA", "");
        this.Fields.put("PAGE_BIOGRAFIA7", "");
        this.Fields.put("PAGE_BIOGRAFIA8", "");
        this.Fields.put("PAGE_BIOGRAFIA9", "");
        this.Fields.put("PAGE_BIOGRAFIA10", "");
        this.Fields.put("PAGE_BIOGRAFIA11", "");        
        this.Fields.put("PAGE_TWITTER", "");
        this.Fields.put("PAGE_FACEBOOK", "");
        this.Fields.put("PAGE_GOOGLE", "");
        this.setBolGetAutonumeric(true);
    }
}