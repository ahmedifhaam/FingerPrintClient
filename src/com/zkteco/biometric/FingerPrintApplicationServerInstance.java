/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zkteco.biometric;

import com.hisp.biometric.main.FingerPrint;

/**
 *
 * @author Ahmed
 */
public class FingerPrintApplicationServerInstance {
    
    private static final long serialVersionUID = 1L;
    private static final int TEMPLATE_SIZE = 2048;
    
    private static long mhDB = 0;
    
    public static FingerPrintApplicationServerInstance instance = null;
    private FingerPrintApplicationServerInstance(){
        initializeDB();
    }
    
    /* removing things to make the class thread safe
    public static FingerPrintApplicationServerInstance getInstance(){
        if(instance == null){
            instance = new FingerPrintApplicationServerInstance(); 
        }
        
        return instance;
    }*/
    
    public static FingerPrintApplicationServerInstance getInstance(){
        if(instance==null){
            synchronized(FingerPrintApplicationServerInstance.class){
                if(null==instance){
                    instance =new FingerPrintApplicationServerInstance();
                }
            }
        }
        return instance;
    }
    
    public synchronized boolean initializeDB(){
        if (FingerprintSensorErrorCode.ZKFP_ERR_OK != FingerprintSensorEx.Init())
        {
            System.out.println("Init failed!");
            
        }
        if (0 == (mhDB = FingerprintSensorEx.DBInit())){
            System.out.println("DB inti fail");
            return false;
        }else{
            System.out.println("DB inti success");
            return true;
        }
        
    }
    
    public boolean freeDB(){
        FingerprintSensorEx.DBFree(mhDB);
        mhDB = 0;
        return true;
        
    }
    
    public boolean addToDb(byte[] template,int fid){
        int ret = -1;
        if(0 == (ret = FingerprintSensorEx.DBAdd(mhDB, fid, template))){
            return true;
        }
        return false;
    }
    
    public int countDBIns(){
        return FingerprintSensorEx.DBCount(mhDB);
    }
    
    public int identify(byte[] template){
        
        int[] fid = new int[1];
        int[] score = new int [1];
        int ret = FingerprintSensorEx.DBIdentify(mhDB, template, fid, score);
        if(ret==0)return fid[0];
        else return -1;
    }
    
    
    //methods to call from server
    /**
     * 
     * @param modelJSON takes a model object in json string format and try to enroll it in to the system  
     * @return if succeeds fid for the fingerprint 
     *          else -1
     */
    public synchronized String enroll(String modelJSON){
        FingerPrintApplicationServerInstance instance = FingerPrintApplicationServerInstance.getInstance();
        //if(instance.initializeDB())System.out.println("Success");
        byte[] template = new byte[2048];
        FingerPrint fp = FingerPrint.fromJson(modelJSON);
        FingerprintSensorEx.Base64ToBlob(fp.getTemplate(), template, 2048);
        int fid = instance.countDBIns()+1;
        boolean ret = instance.addToDb(template, fid);
        System.out.println(modelJSON);
        if(ret){
            System.out.println("Object enrolled");
            fp.setFid(fid);
            return fp.toString();
        }
        else{
            System.out.println("Object failed");
            fp.setFid(-1);
            return fp.toString();
        }
    }
    
    /**
     * 
     * @param modelJson takes a model object in json string format and try recognize it in to the system 
     * @return if recognized returns the fid or else returns -1
     */
    
    public synchronized String recognize(String modelJson){
        
        FingerPrint fingerPrint = FingerPrint.fromJson(modelJson);
        String tempStr = fingerPrint.getTemplate();
        byte[] template = new byte[TEMPLATE_SIZE];
        FingerprintSensorEx.Base64ToBlob(tempStr, template, TEMPLATE_SIZE);
        FingerPrintApplicationServerInstance instance = FingerPrintApplicationServerInstance.getInstance();
        int fid =instance.identify(template);
        fingerPrint.setFid(fid);
        return fingerPrint.toString();
        
    }
    
    
}
