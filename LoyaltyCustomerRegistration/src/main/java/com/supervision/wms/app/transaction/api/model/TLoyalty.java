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
public class TLoyalty {
 private String type; //(GV,DV,CN,RN,LC)
    private String barcode;// sms-code
    private Integer branch;
    private String valid;//true,false
    private String status; // error status
    private BigDecimal value;
    
    //invoice details
    private String invoiceNo;
    private String invoiceDate;
    private String trasctionType; //(issue,redeem)
    
    //discount voucher
    private BigDecimal discountVoucherMaxValue;
    private BigDecimal discountVoucherDiscountPercentage;
    
    //loyalty customer
    private String loyaltyCustomer;//mobile no
    private BigDecimal itemQty;
    private String pointType; //(normal,b'day,sms code)
    private BigDecimal itemValue;  

    public TLoyalty() {
    }

    public TLoyalty(String type, String barcode, Integer branch, String valid, String status, BigDecimal value, String invoiceNo, String invoiceDate, String trasctionType, BigDecimal discountVoucherMaxValue, BigDecimal discountVoucherDiscountPercentage, String loyaltyCustomer, BigDecimal itemQty, String pointType, BigDecimal itemValue) {
        this.type = type;
        this.barcode = barcode;
        this.branch = branch;
        this.valid = valid;
        this.status = status;
        this.value = value;
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        this.trasctionType = trasctionType;
        this.discountVoucherMaxValue = discountVoucherMaxValue;
        this.discountVoucherDiscountPercentage = discountVoucherDiscountPercentage;
        this.loyaltyCustomer = loyaltyCustomer;
        this.itemQty = itemQty;
        this.pointType = pointType;
        this.itemValue = itemValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getBranch() {
        return branch;
    }

    public void setBranch(Integer branch) {
        this.branch = branch;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getTrasctionType() {
        return trasctionType;
    }

    public void setTrasctionType(String trasctionType) {
        this.trasctionType = trasctionType;
    }

    public BigDecimal getDiscountVoucherMaxValue() {
        return discountVoucherMaxValue;
    }

    public void setDiscountVoucherMaxValue(BigDecimal discountVoucherMaxValue) {
        this.discountVoucherMaxValue = discountVoucherMaxValue;
    }

    public BigDecimal getDiscountVoucherDiscountPercentage() {
        return discountVoucherDiscountPercentage;
    }

    public void setDiscountVoucherDiscountPercentage(BigDecimal discountVoucherDiscountPercentage) {
        this.discountVoucherDiscountPercentage = discountVoucherDiscountPercentage;
    }

    public String getLoyaltyCustomer() {
        return loyaltyCustomer;
    }

    public void setLoyaltyCustomer(String loyaltyCustomer) {
        this.loyaltyCustomer = loyaltyCustomer;
    }

    public BigDecimal getItemQty() {
        return itemQty;
    }

    public void setItemQty(BigDecimal itemQty) {
        this.itemQty = itemQty;
    }

    public String getPointType() {
        return pointType;
    }

    public void setPointType(String pointType) {
        this.pointType = pointType;
    }

    public BigDecimal getItemValue() {
        return itemValue;
    }

    public void setItemValue(BigDecimal itemValue) {
        this.itemValue = itemValue;
    }
    
}
