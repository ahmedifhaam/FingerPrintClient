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
public class Model {
    int id;
    String template;

    public Model(int id, String template) {
        this.id = id;
        this.template = template;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
    
    
    
}
