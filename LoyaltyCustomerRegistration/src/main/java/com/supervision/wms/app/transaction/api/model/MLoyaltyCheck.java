/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.wms.app.transaction.api.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author kasun
 */
public class MLoyaltyCheck implements Serializable{

    private String titel;//Mr Miss Mrs
    private String name;
    private String nic;
    private String loyaltyNo;
    private String mobileNo;
    private String birthDay;// 1992-04-27
    private String loyaltyType;
    private BigDecimal itemValue;
    private Integer itemCount;
    private Integer maxItem;
    private String discountType;//Normal_Disc SMS_Code
    private BigDecimal discountPer;
    private String pointType;//Birthday Slab Special_Day
    private BigDecimal point;
    private BigDecimal availablePoint;
    private BigDecimal defaultPoint;

    public MLoyaltyCheck() {
    }

    public MLoyaltyCheck(String titel, String name, String nic, String loyaltyNo, String mobileNo, String birthDay, Integer itemCount, BigDecimal itemValue, String discountType, BigDecimal discountPer, String pointType, BigDecimal point, BigDecimal availablePoint, String loyaltyType, Integer maxItem) {
        this.titel = titel;
        this.name = name;
        this.nic = nic;
        this.loyaltyNo = loyaltyNo;
        this.mobileNo = mobileNo;
        this.birthDay = birthDay;
        this.itemCount = itemCount;
        this.itemValue = itemValue;
        this.discountType = discountType;
        this.discountPer = discountPer;
        this.pointType = pointType;
        this.point = point;
        this.availablePoint = availablePoint;
        this.loyaltyType = loyaltyType;
        this.maxItem = maxItem;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getLoyaltyNo() {
        return loyaltyNo;
    }

    public void setLoyaltyNo(String loyaltyNo) {
        this.loyaltyNo = loyaltyNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public BigDecimal getItemValue() {
        return itemValue;
    }

    public void setItemValue(BigDecimal itemValue) {
        this.itemValue = itemValue;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(BigDecimal discountPer) {
        this.discountPer = discountPer;
    }

    public String getPointType() {
        return pointType;
    }

    public void setPointType(String pointType) {
        this.pointType = pointType;
    }

    public BigDecimal getPoint() {
        return point;
    }

    public void setPoint(BigDecimal point) {
        this.point = point;
    }

    public BigDecimal getAvailablePoint() {
        return availablePoint;
    }

    public void setAvailablePoint(BigDecimal availablePoint) {
        this.availablePoint = availablePoint;
    }

    public BigDecimal getDefaultPoint() {
        return defaultPoint;
    }

    public void setDefaultPoint(BigDecimal defaultPoint) {
        this.defaultPoint = defaultPoint;
    }

    public String getLoyaltyType() {
        return loyaltyType;
    }

    public void setLoyaltyType(String loyaltyType) {
        this.loyaltyType = loyaltyType;
    }

    public Integer getMaxItem() {
        return maxItem;
    }

    public void setMaxItem(Integer maxItem) {
        this.maxItem = maxItem;
    }
   
      public MLoyaltyCheck nullModel() {
          return new MLoyaltyCheck(titel, name, nic,loyaltyNo,mobileNo, birthDay, 0, new BigDecimal(0), discountType, BigDecimal.ZERO, pointType, BigDecimal.ZERO, BigDecimal.ZERO, loyaltyType, 0);
      }

  
}
