/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.chipcoin;

import Core.Opalina;
import com.mx.siweb.mlm.compensacion.Periodos;
import apiSiweb.ContextoApt.VariableSession;
import apiSiweb.Operaciones.CIP_Tabla;
import apiSiweb.Operaciones.Conexion;
import apiSiweb.Utilerias.Fechas;
import apiSiweb.Utilerias.Mail;
import apiSiweb.Utilerias.generateData;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author casajosefa
 */
public class MlmContratoTestamentario {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(com.mx.siweb.mlm.compensacion.chipcoin.MlmContratoTestamentario.class.getName());

    @Context
    private HttpServletRequest servletRequest;

    private Conexion oConn;

    public Conexion getoConn() {
        return oConn;
    }

    public void setoConn(Conexion oConn) {
        this.oConn = oConn;
    }

    /**
     * ENVIA CORREOS INDICANDO QUE INGRESEN AL SISTEMA SI PASAN X NUMERO DE DIAS
     */
    public void avisoPruebaDeVida() throws SQLException {
        Mail mail = new Mail();
        Fechas fecha = new Fechas();
        log.debug("COMINEZA A BUSCAR LOS CONTRATOS QUE ESTEN FINALIZADOS Y SIN LIBERAR");
        String sqlContratos = "SELECT a.*,c.CHIP_DIAS_INACTIVO FROM chip_contrato_testamentario a, vta_cliente b, vta_empresas c "
                + "WHERE CCT_ESTATUS=1 AND CCT_LIBERADO=0 AND a.CT_ID=b.CT_ID AND b.EMP_ID=c.EMP_ID;";
        try (ResultSet rs = oConn.runQuery(sqlContratos, true)) {
            while (rs.next()) {
                String strFechaUltimoAviso = rs.getString("CCT_ULTIMO_AVISO");
                if (strFechaUltimoAviso.equals("")) {
                    long dif = Fechas.difDiasEntre2fechasStr(rs.getString("CCT_FECHA_FINALIZACION"), fecha.getFechaActual());
                    if (rs.getInt("CHIP_DIAS_INACTIVO") <= dif) {
                        log.debug("CONTRATO: " + rs.getInt("CCT_ID") + " SIN FECHA DE ULTIMO AVISO");
                        String strResult = "";
                        //Se envia el correo y se agrega la fecha de envio en ultimo envio   
                        log.debug("CLIENTE DEL CONTRATO: " + rs.getInt("CCT_ID"));
                        String sqlbeneficiarios = "SELECT * FROM chip_contrato_testamentario_beneficiario WHERE CCT_ID=" + rs.getInt("CCT_ID") + " AND CCTB_RECIBE_CARTERAS=1;";
                        ResultSet rs2 = oConn.runQuery(sqlbeneficiarios, true);
                        while (rs2.next()) {
                            if (mail.isEmail(rs2.getString("CCTB_CORREO"))) {
                                //Intentamos mandar el mail
                                mail.setBolDepuracion(false);
                                mail.getTemplate("PRIMER_CORREO", oConn);
                                mail.getMensaje();
                                String strSqlEmp = "SELECT * FROM chip_contrato_testamentario_beneficiario WHERE CCTB_ID=" + rs2.getInt("CCTB_ID") + ";";
                                try {
                                    ResultSet rs3 = oConn.runQuery(strSqlEmp);
                                    mail.setReplaceContent(rs3);
                                    rs3.close();
                                } catch (SQLException ex) {
                                    //this.strResultLast = "ERROR:" + ex.getMessage();
                                    ex.fillInStackTrace();
                                }
                                mail.setDestino(rs2.getString("CCTB_CORREO"));
                                boolean bol = mail.sendMail();
                                if (bol) {
                                    strResult = "MAIL ENVIADO.";
                                } else {
                                    strResult = "FALLO EL ENVIO DEL MAIL.";
                                }
                            }
                        }
                        rs2.close();
                        String updateUltimoAviso = "UPDATE chip_contrato_testamentario SET CCT_ULTIMO_AVISO=" + fecha.getFechaActual() + " WHERE CCT_ID=" + rs.getInt("CCT_ID");
                        oConn.runQueryLMD(updateUltimoAviso);
                    }
                } else {
                    //si ya tiene una fecha de ultimo envio se envia otro correo y se actualiza la fecha de ultimo envio 
                    long dif = Fechas.difDiasEntre2fechasStr(strFechaUltimoAviso, fecha.getFechaActual());
                    if (rs.getInt("CHIP_DIAS_INACTIVO") <= dif) {
                        log.debug("CONTRATO: " + rs.getInt("CCT_ID") + " CON FECHA DE ULTIMO AVISO");
                        String strResult = "";
                        log.debug("CLIENTE DEL CONTRATO: " + rs.getInt("CCT_ID"));
                        String sqlbeneficiarios = "SELECT * FROM chip_contrato_testamentario_beneficiario WHERE CCT_ID=" + rs.getInt("CCT_ID") + " AND CCTB_RECIBE_CARTERAS=1;";
                        ResultSet rs2 = oConn.runQuery(sqlbeneficiarios, true);
                        while (rs2.next()) {
                            if (mail.isEmail(rs2.getString("CCTB_CORREO"))) {
                                //Intentamos mandar el mail
                                mail.setBolDepuracion(false);
                                mail.getTemplate("SEGUNDO_CORREO", oConn);
                                mail.getMensaje();
                                String strSqlEmp = "SELECT * FROM chip_contrato_testamentario_beneficiario WHERE CCTB_ID=" + rs2.getInt("CCTB_ID") + ";";
                                try {
                                    ResultSet rs3 = oConn.runQuery(strSqlEmp);
                                    mail.setReplaceContent(rs3);
                                    rs3.close();
                                } catch (SQLException ex) {
                                    //this.strResultLast = "ERROR:" + ex.getMessage();
                                    ex.fillInStackTrace();
                                }
                                mail.setDestino(rs2.getString("CCTB_CORREO"));
                                boolean bol = mail.sendMail();
                                if (bol) {
                                    strResult = "MAIL ENVIADO.";
                                } else {
                                    strResult = "FALLO EL ENVIO DEL MAIL.";
                                }
                            }
                        }
                        rs2.close();
                        String updateUltimoAviso = "UPDATE chip_contrato_testamentario SET CCT_ULTIMO_AVISO=" + fecha.getFechaActual() + " WHERE CCT_ID=" + rs.getInt("CCT_ID");
                        oConn.runQueryLMD(updateUltimoAviso);
                    }
                }

            }
        }
    }

    /**
     * LIBERA EL CONTRATO TESTAMENTARIO, CREADON LOS USUARIOS NECESARIOS Y
     * REALIZANDO LAS TRANSACCIONES DE LAS CARTERAS EN EL CONTRATO
     *
     * @param strSecretWord Es EL strSecretWord DEL XML
     * @throws java.sql.SQLException
     * @throws javax.crypto.NoSuchPaddingException
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.InvalidKeyException
     * @throws javax.crypto.BadPaddingException
     * @throws javax.crypto.IllegalBlockSizeException
     */
    public void confirmaPruebaDeVida(String strSecretWord)
            throws SQLException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, Exception {
        MlmWallet transaccion = new MlmWallet();
        Fechas fecha = new Fechas();
        String transaccionOK = "";
        String sqlUltimoAcceso = "SELECT a.CT_ID,a.CT_FECHAEXIT,b.CCT_ID,b.CCT_DIAS_INACTIVIDAD from vta_cliente a, chip_contrato_testamentario b WHERE a.CT_ID=b.CT_ID AND b.CCT_ESTATUS=1 AND b.CCT_LIBERADO=0;";
        ResultSet rs = oConn.runQuery(sqlUltimoAcceso, true);
        while (rs.next()) {
            long dif = Fechas.difDiasEntre2fechasStr(rs.getString("CT_FECHAEXIT"), fecha.getFechaActual());
            if (rs.getInt("CCT_DIAS_INACTIVIDAD") <= dif) {
                String sqlCarteras = "SELECT a.CW_ID,CT_ID,CW_TOT_CHIPCOINS,CW_NOMBRE_CUENTA,CW_NUMERO_CUENTA,CW_PASS,CW_TOT_CHIPCOINSOLD "
                        + "FROM mlm_chipcoin_wallet a, chip_contrato_testamentario_carteras b WHERE a.CW_ID=b.CW_ID AND b.CCT_ID=" + rs.getInt("CCT_ID") + ";";
                ResultSet rsCarteras = oConn.runQuery(sqlCarteras, true);
                while (rsCarteras.next()) {
                    String strCuenta = rsCarteras.getString("CW_NUMERO_CUENTA");
                    String passCuenta = rsCarteras.getString("CW_PASS");
                    int intCT_ID = rsCarteras.getInt("CT_ID");
                    float totalCartera = Float.parseFloat(rsCarteras.getString("CW_TOT_CHIPCOINS"));
                    Opalina opa = new Opalina();
                    String strMyPassSecret = opa.DesEncripta(passCuenta, "9qT9yUhip1dsAi0FD6nYlw==");
                    String sqlBeneficiario = "SELECT * FROM chip_contrato_testamentario_beneficiario WHERE CCTB_RECIBECARTERAS=1 AND CCT_ID=" + rs.getInt("CCT_ID");
                    ResultSet rsBeneficiairio = oConn.runQuery(sqlBeneficiario, true);
                    while (rsBeneficiairio.next()) {
                        float flMonto = rsBeneficiairio.getFloat("CCTB_MONTO");
                        if (rsBeneficiairio.getInt("CT_NEW") == 0) {
                            String strNombre = rsBeneficiairio.getString("CCTB_NOMBRE");
                            String strPass = generateData.getPassword(12);
                            String strCorreo = rsBeneficiairio.getString("CCTB_CORREO");
                            String strCel = rsBeneficiairio.getString("CCTB_CELULAR");
                            String newCliente = newCliente(strNombre, strPass, strCorreo, strCel);
                            int newCCT_ID = Integer.parseInt(newCliente);
                            if (newCCT_ID > 0) {
                                String sqlUpdate = "UPDATE chip_contrato_testamentario_beneficiario SET CT_NEW=" + newCCT_ID + " WHERE CCTB_ID=" + rsBeneficiairio.getString("CCTB_ID") + ";";
                                oConn.runQueryLMD(sqlUpdate);
                                String newCartera = guardaCartera(newCCT_ID, "CUENTA BENEFICIO PARA: " + strNombre, strPass, 0, "", "9qT9yUhip1dsAi0FD6nYlw==");
                                int newWallet = Integer.parseInt(newCartera);
                                if (newWallet > 0) {
                                    String sqlCuentaDestino = "SELECT CW_NUMERO_CUENTA FROM mlm_chipcoin_wallet WHERE CW_ID=" + newWallet + ";";
                                    ResultSet rsCuentaDestino = oConn.runQuery(sqlCuentaDestino, true);
                                    String strCuentaDestino = "";
                                    while (rsCuentaDestino.next()) {
                                        strCuentaDestino = rsCuentaDestino.getString("CW_NUMERO_CUENTA");
                                    }
                                    rsCuentaDestino.close();

                                    float monto = (totalCartera * flMonto) / 100;
                                    transaccionOK = transaccion.transaccionWallet(intCT_ID, strCuenta, strMyPassSecret, strCuentaDestino, monto, oConn);

                                    log.debug(transaccionOK);
                                } else {
                                    log.debug("Error al crearla cartera del beneficiario: " + rsBeneficiairio.getInt("CCTB_ID") + " " + rsBeneficiairio.getString("CCTB_NOMBRE"));
                                }
                            } else {
                                log.debug("Error al crear el cliente del beneficiario: " + rsBeneficiairio.getInt("CCTB_ID") + " " + rsBeneficiairio.getString("CCTB_NOMBRE"));
                            }
                        } else {
                            String sqlCuentaDestino = "SELECT CW_NUMERO_CUENTA FROM mlm_chipcoin_wallet a, chip_contrato_testamentario_beneficiario b, vta_cliente c "
                                    + "WHERE a.CT_ID=c.CT_ID AND b.CT_NEW=c.CT_ID AND c.CT_ID=" + rsBeneficiairio.getInt("CT_NEW") + ";";
                            ResultSet rsCuentaDestino = oConn.runQuery(sqlCuentaDestino, true);
                            String strCuentaDestino = "";
                            while (rsCuentaDestino.next()) {
                                strCuentaDestino = rsCuentaDestino.getString("CW_NUMERO_CUENTA");
                            }
                            rsCuentaDestino.close();
                            float monto = (totalCartera * flMonto) / 100;
                            transaccionOK = transaccion.transaccionWallet(intCT_ID, strCuenta, strMyPassSecret, strCuentaDestino, monto, oConn);
                            log.debug(transaccionOK);
                        }

                    }
                    rsBeneficiairio.close();
                }
                rsCarteras.close();
                String updateFechaLiberacion = "UPDATE chip_contrato_testamentario SET CCT_FECHA_LIBERACION='" + fecha.getFechaActual() + "', CCT_LIBERADO=1 WHERE CCT_ID=" + rs.getInt("CCT_ID") + ";";
                oConn.runQueryLMD(updateFechaLiberacion);
            }
        }
        rs.close();
    }

    /**
     * Crea una cartera nueva
     *
     * @param intCT_ID Es el ID del cliente al que pertenecera la cuenta
     * @param strNombreCuenta Es el Nombre de la cuenta
     * @param strPassCuenta Es la contraseña de la cuenta
     * @param intPregunta Es el ID de la pregunta de recuperacion
     * @param strRespuesta Es la respuesta de la pregunta de recuperacion
     * @param strSecretWord Es el strSecretWord del xml
     * @return Nos regresa el ID de la cartera creada
     * @throws javax.crypto.NoSuchPaddingException
     * @throws java.security.NoSuchAlgorithmException
     * @throws javax.crypto.BadPaddingException
     * @throws java.security.InvalidKeyException
     * @throws javax.crypto.IllegalBlockSizeException
     */
    public String guardaCartera(int intCT_ID, String strNombreCuenta, String strPassCuenta, int intPregunta, String strRespuesta, String strSecretWord)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Fechas fecha = new Fechas();
        //Archivo de texto con el password
        String FILENAME = "c:/SIWEB/tmp/chipcoin/ethereum_" + intCT_ID + ".txt";
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            String content = strPassCuenta + "\n";
            fw = new FileWriter(FILENAME);
            bw = new BufferedWriter(fw);
            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        //Shell a ejecutar
        ExecuteShellComand obj = new ExecuteShellComand();
        String command = "\"C:/Program Files/Geth/geth\" --datadir \"C:/SIWEB/EthereumChipCoin/Data\" --password " + FILENAME + " account new";
        String output = obj.executeCommand(command);
        String strNumCta = "";
        String strResult;
        MlmChipcoinWallet objTabla = new MlmChipcoinWallet();
        if (output.contains("Address:")) {
            strNumCta = output.replace("Address: {", "").replace("}", "");
            //Encriptamos el password
            String strPassB64 = strSecretWord;
            Opalina opa = new Opalina();
            String strMyPass = opa.Encripta(strPassCuenta, strPassB64);
            //Validamos si ya existe a referencia strAutorizado
            objTabla.setFieldString("CW_NOMBRE_CUENTA", strNombreCuenta);
            objTabla.setFieldString("CW_NUMERO_CUENTA", strNumCta.trim());
            objTabla.setFieldString("CW_FECHA_REG", fecha.getFechaActual());
            objTabla.setFieldInt("CW_PREGUNTA", intPregunta);
            objTabla.setFieldString("CW_RESPUESTA", strRespuesta);
            objTabla.setFieldString("CW_HORA_REG", fecha.getHoraActual());
            objTabla.setFieldInt("CT_ID", intCT_ID);
            objTabla.setFieldString("CW_PASS", strMyPass);
            strResult = objTabla.Agrega(oConn);
            //Borramos el archivo
            File fBorrar = new File(FILENAME);
            if (fBorrar.exists()) {
                fBorrar.delete();
            }
        } else {
            strResult = "Error: No se puedo generar la cuenta";
        }
        if (strResult.equals("OK")) {
            strResult = objTabla.getValorKey();
            //strResult = strNumCta;
        } else {
            strResult = "0";
        }
        return strResult;
    }

    /**
     * Crea un cliente nuevo
     *
     * @param strNombre Es el nombre del cliente a crear
     * @param strPassword Es el password del cliente
     * @param strCorreo Es el correo del cliente
     * @param strCel Es el celular del cliente
     * @return Nos regresa el ID del cliente que se creo
     */
    public String newCliente(String strNombre, String strPassword, String strCorreo, String strCel) {
        VariableSession varSesiones = new VariableSession(servletRequest);
        Periodos periodo = new Periodos();
        String strResult = "";
        CIP_Tabla objTabla = new CIP_Tabla("", "", "", "", varSesiones);
        objTabla.Init("CLIENTES", true, true, false, oConn);
        objTabla.setBolGetAutonumeric(true);
        objTabla.setFieldInt("EMP_ID", 1);
        objTabla.setFieldInt("SC_ID", 12);
        objTabla.setFieldInt("MPE_ID", periodo.getPeriodoActual(oConn));
        objTabla.setFieldString("CT_RAZONSOCIAL", strNombre);
        objTabla.setFieldString("CT_PASSWORD", strPassword);
        objTabla.setFieldInt("CT_UPLINE", 10);
        objTabla.setFieldString("CT_EMAIL2", strCorreo);
        objTabla.setFieldString("CT_TELEFONO2", strCel);
        objTabla.setFieldInt("CHIP_MINERO", 0);
        objTabla.setFieldInt("CHIP_MINERO_ACTIVO", 0);
        strResult = objTabla.Agrega(oConn);

        Mail mail = new Mail();
        if (!strCorreo.isEmpty()) {
            String strLstMail = "";
            //Validamos si el mail del cliente es valido
            if (mail.isEmail(strCorreo)) {
                strLstMail = strCorreo;
            }
            //Intentamos mandar el mail
            mail.setBolDepuracion(false);
            mail.getTemplate("MSG_ING_MIN", oConn);

            mail.getMensaje();
            String strSqlEmp = "SELECT * FROM vta_cliente"
                    + " where CT_ID=" + objTabla.getValorKey() + "";
            try {
                ResultSet rs1 = oConn.runQuery(strSqlEmp);
                mail.setReplaceContent(rs1);
                rs1.close();
            } catch (SQLException ex) {
                //this.strResultLast = "ERROR:" + ex.getMessage();
                ex.fillInStackTrace();
            }
            mail.setDestino(strLstMail);
            boolean bol = mail.sendMail();
            if (bol) {
                //strResp = "MAIL ENVIADO.";
            } else {
                //strResp = "FALLO EL ENVIO DEL MAIL.";
            }

        } else {
            //strResp = "ERROR: INGRESE UN MAIL";
        }

        oConn.close();
        if (strResult.equals("OK")) {
            strResult = objTabla.getValorKey();
        } else {
            strResult = "0";
        }
        return strResult;
    }

}
