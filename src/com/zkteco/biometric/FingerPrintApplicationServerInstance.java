/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zkteco.biometric;

/**
 *
 * @author Ahmed
 */
public class FingerPrintApplicationServerInstance {
    
    private static final long serialVersionUID = 1L;
    private static final int SIZE_OF_TEMPLATE = 2048;
    
    private static long mhDB = 0;
    
    public static FingerPrintApplicationServerInstance instance = null;
    private FingerPrintApplicationServerInstance(){
        initializeDB();
    }
    
    public static FingerPrintApplicationServerInstance getInstance(){
        if(instance == null){
            instance = new FingerPrintApplicationServerInstance(); 
        }
        
        return instance;
    }
    
    public boolean initializeDB(){
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
    
    
    
}
