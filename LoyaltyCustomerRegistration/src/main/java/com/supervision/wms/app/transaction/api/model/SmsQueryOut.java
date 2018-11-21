/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.wms.app.transaction.api.model;

import java.math.BigDecimal;

/**
 *
 * @author kasun
 */
public class SmsQueryOut {
    private BigDecimal value;
    private boolean isActive;

    public SmsQueryOut() {
    }

    public SmsQueryOut(BigDecimal value, boolean isActive) {
        this.value = value;
        this.isActive = isActive;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return value+" - "+isActive;
    }
    
    
}
