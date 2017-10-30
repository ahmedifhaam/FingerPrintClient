/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hisp.biometric.util;

import com.hisp.biometric.login.LoginCredentials;
import com.hisp.biometric.main.FingerPrint;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 *This class to be used only in client application 
 * @author Ahmed
 */
public class NetworkCall {
    public static LoginCredentials lc;
    public static final String DEFAULT_DOMAIN= "http://localhost:8080/fingerprint";
    public static final String API_SUFFIX = "/api/identify";
    public static String apiUrl;
    
    public static void initString(LoginCredentials lcp){
        lc = lcp;
        String domain=lc.getUrl();
        if(domain==null){
            domain=DEFAULT_DOMAIN;
        }
        apiUrl=domain+API_SUFFIX;
    }
    
    public static FingerPrint sendEnrollment(String template) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        FingerPrint fp = new FingerPrint();
        fp.setTemplate(template);
        fp.setFid(-1);
        try{
            
            HttpPut request = new HttpPut(apiUrl);
            
            String json = fp.toString();
            /*String json = "{\"fid\": 3," +
            "\"template\": \"Sv9TUjIyAAADvL8FBQUHCc7QAAALvW0AAAAAg0sihbysAUsOXwB"
                    + "5AGCxlQHLAEsJawCyvEEONgCcAHAMe7zmAWcMvAB7AD2yugFwADUPaAD"
                    + "ovMwD0gCNAOsPfbwOAOwOUgDIAImyewGJAEMPiwDCvGUNPgChAFkMPby"
                    + "HAbwMOAB+AIGwngFsADcP7QC5vAMMbgD+ACcNLbxiAUAJNgCJABS1TQE"
                    + "nABgPvADDvE4NXQDOABwNSrzNAdsMnwAVAE23kgHcAMYKXwDcvM8KPAB"
                    + "oAPwJv7zaAVIGMACcABy1zgHXACoDUwAUvKEOjf5mAC8Kkr1gjCUXSP+"
                    + "8hGrBYASW9aL1JRqaRgs1aYYJo9eZ4r2E7moU0QO3CZRLkH9NAkoIP/"
                    + "m0TabzNQ5ShqIqQsv+7kt3cvriDHxKbYoRATmNueouo6TiFQoFI0eEg"
                    + "jwIIyGqUQd29GC3gIFSTF5oBPpywoIcCCNRCMgqvkS426HjMZMHA7x"
                    + "GCAe570EHqHlGySwAeHupBYBax7uAgZWIzPpYeH7ARQuBgGmFEPjuo"
                    + "QAmJSmNhjQXq180CYXpzdk77MHiahpbix9rut3ZaAIABQALho4aC7wBf"
                    + "cB5HIVbusrsw/6QIDABxNUVJRQBTwATwPv/RfhC/mn+FACmABBCwcH//"
                    + "sDAhsBDfz4FAHUAF4sDAzgBFv0KALHFIv18wWX+/gUACAAjQVgMAJUB"
                    + "IDvAW+RFDQCjASA6Q1lDOQYAwgEnOmL9rQFZDxM+ODvAXXz+eA0AKRY"
                    + "/Kf2EOsANAJsd50NBQsDA//8TAOYk80Ew/Eb//j4F/02tAVEoE//+/j"
                    + "3Dgv4wDwAfLjHA+EH+/v7A/cA6/lSxAQ9E7cH+O/r8QjI4EQA6TtX8/"
                    + "kMp//z7/P04/P5CBwE2Wx7+PiwHvDBfKfvBBcUsZfXDwsIEADGjPXmz"
                    + "AR5H6f39Pi4wjMEJAJ1vNwU+TbkBo28tKwnFvHeIwUvCZgYAvYxAj8E"
                    + "NAH2MOjv9NkH8QAQA05HoRAS8O6aQG/3/1wAl4+H2+v0n/jj7+kD9Kf"
                    + "4TACeV5/9F//z9wP4zOCL5R/4IAIOwTAX6wkE/CACIsEM7J/2HCgGrt"
                    + "ED/Bfz++FEEALG0OjoqBrwWvhzDpAbFYLzrH/wEAMDA/1sKvMTtV8L4"
                    + "wJf/B7y7wEAwBQBUzk+VBwG38Fz9OksFvJTkXjvBBcV25dvBJQQAeeu"
                    + "lNwa8qfFi/sDBwxC9snHBWgUQtthw/Hz+AURCAQHFAAOqAQEAAAIYxQA"
                    + "DvAEBRUI=\"" +
                    "}";*/
            StringEntity entity = new StringEntity(json);
            request.setEntity(entity);
            request.setHeader("Content-type","application/json");
            
            
            
            //Future<HttpResponse> future = httpclient.execute(request,null);
            //HttpResponse response = future.get();
            CloseableHttpResponse response = httpclient.execute(request);
            if(response.getStatusLine().getStatusCode()==200){
                System.out.println(response.getStatusLine());
                InputStream is = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String responseStr = "";
                while(reader.ready()){
                    responseStr +=reader.readLine();
                }
                fp = FingerPrint.fromJson(responseStr);
                System.out.println(responseStr);
            }else{
                System.out.println("Failed request");
            }
            
            
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(NetworkCall.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex) {
            Logger.getLogger(NetworkCall.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return fp;
    }
    
    public static FingerPrint recognize(String template){
        
        CloseableHttpClient httpclient = HttpClients.createDefault();
        FingerPrint fp = new FingerPrint();
        fp.setTemplate(template);
        
        try{
            
            HttpPost request = new HttpPost(apiUrl);
            
            String json = fp.toString();
            
            StringEntity entity = new StringEntity(json);
            request.setEntity(entity);
            request.setHeader("Content-type","application/json");
            
            
            
            //Future<HttpResponse> future = httpclient.execute(request,null);
            //HttpResponse response = future.get();
            CloseableHttpResponse response = httpclient.execute(request);
            if(response.getStatusLine().getStatusCode()==200){
                System.out.println(response.getStatusLine());
                InputStream is = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String responseStr = "";
                while(reader.ready()){
                    responseStr +=reader.readLine();
                }
                fp = FingerPrint.fromJson(responseStr);
                System.out.println(responseStr);
            }else{
                System.out.println("Failed request");
                return null;
            }
            
            
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(NetworkCall.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }catch (IOException ex) {
            Logger.getLogger(NetworkCall.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        return fp;
    }
    
}
