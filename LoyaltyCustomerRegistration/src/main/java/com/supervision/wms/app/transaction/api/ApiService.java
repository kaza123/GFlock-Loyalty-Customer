/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.wms.app.transaction.api;

import com.supervision.wms.app.master.loyalty.LoyaltyService;
import com.supervision.wms.app.master.loyalty.model.MLoyaltyCustomer;
import com.supervision.wms.app.transaction.api.model.MLoyaltyCheck;
import com.supervision.wms.app.transaction.api.model.SmsRespond;
import com.supervision.wms.app.transaction.loyalty_ledger.LoyaltyLedgerService;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kasun
 */
@Service
public class ApiService {

    @Autowired
    public ApiRepository apiRepository;

    @Autowired
    public LoyaltyService loyaltyService;

    @Autowired
    public LoyaltyLedgerService loyaltyLedgerService;

    public MLoyaltyCheck getDetailByLoyaltyNo(String type, String loyaltyNo) {
        MLoyaltyCustomer customer = new MLoyaltyCustomer();
        if (!"Mobile_No".equals(type)) {
            customer = loyaltyService.findByMobileNo(loyaltyNo);
            if (null == customer) {
                MLoyaltyCheck mLoyaltyCheck = new MLoyaltyCheck();
                return mLoyaltyCheck.nullModel();
            } else {
                loyaltyNo = customer.getLoyaltyNo();
            }
        } else if ("Mobile_No".equals(type)) {
            customer = loyaltyService.findByLoyaltyNo(loyaltyNo);
            if (null == customer) {
                MLoyaltyCheck mLoyaltyCheck = new MLoyaltyCheck();
                return mLoyaltyCheck.nullModel();
            }
        } else {
            throw new RuntimeException("Parameeter type must be Mobile_No or Mobile_No");
        }
        // get loyalty customer detail
        //get item count and value for this month
        ArrayList<Object[]> values = loyaltyLedgerService.getItemCountAndValueForThisMonth(loyaltyNo);

        //discount
        //normal
        ArrayList<Object[]> loyaltyTypeDetails = apiRepository.getNormalDiscount(loyaltyNo);
        BigDecimal normalDisc = new BigDecimal(0.00);
//        System.out.println(loyaltyTypeDetails.get(0)[0]);//type
//        System.out.println(loyaltyTypeDetails.get(0)[1]);//point/ discount
//        System.out.println(loyaltyTypeDetails.get(0)[2]);//maxItem
//        System.out.println(loyaltyTypeDetails.get(0)[3]);//percentage
//        System.out.println(loyaltyTypeDetails.get(0)[4]);//point
        if (null == loyaltyTypeDetails.get(0)[3]) {
            normalDisc = new BigDecimal(0.00);
        } else {
            normalDisc = new BigDecimal(loyaltyTypeDetails.get(0)[3].toString());
        }
        //sms code
        BigDecimal smsDisc = new BigDecimal(0);// discount;

        ArrayList<Object[]> smsDiscountDetail = apiRepository.getSmsDiscount(loyaltyNo);
        if (true) {

        }
        if (smsDiscountDetail.isEmpty()) {
            smsDisc = new BigDecimal(0.00);
        } else {
            smsDisc = new BigDecimal(smsDiscountDetail.get(0)[0].toString());
        }

        //points
        BigDecimal point = new BigDecimal(0);
        String pointType = "";
        //birthday
        BigDecimal birthdayPoint = apiRepository.getBirthdayPoint(loyaltyNo, customer.getbMonth(), customer.getbDate());
        if (null == birthdayPoint) {
            birthdayPoint = new BigDecimal(0);
        }
        //slab
        BigDecimal slabPoint = apiRepository.getSlabPoint(loyaltyNo);
        if (null == slabPoint) {
            slabPoint = new BigDecimal(0);
        }

        //special day
        BigDecimal specialDayPoint = apiRepository.getSpecialDayPoint();
        if (null == specialDayPoint) {
            specialDayPoint = new BigDecimal(0);
        }

        // normal points
        BigDecimal normalPoint = apiRepository.getAvailablePoint(loyaltyNo);
        System.out.println("normalPoint "+normalPoint);

//        check is birthday
        Integer month = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        Integer date = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

        if (Objects.equals(month, customer.getbMonth()) && Objects.equals(date, customer.getbDate())) {
            if (point.doubleValue() < birthdayPoint.doubleValue()) {
                point = birthdayPoint;
                pointType = "Birthday";
            }
        }
        if (point.doubleValue() < slabPoint.doubleValue()) {
            point = slabPoint;
            pointType = "Slab";
        }
        if (point.doubleValue() < specialDayPoint.doubleValue()) {
            point = specialDayPoint;
            pointType = "Special_Day";
        }
//        
        MLoyaltyCheck loyaltyCheck = new MLoyaltyCheck();
        loyaltyCheck.setTitel(customer.getRecidance());
        loyaltyCheck.setName(customer.getName());
        loyaltyCheck.setLoyaltyNo(customer.getLoyaltyNo());
        loyaltyCheck.setMobileNo(customer.getMobileNo());
        loyaltyCheck.setNic(customer.getNic());
        loyaltyCheck.setBirthDay(customer.getbYear() + "-" + customer.getbMonth() + "-" + customer.getbDate());
        loyaltyCheck.setItemCount(null == values.get(0)[0] ? 0 : Integer.parseInt(values.get(0)[0].toString()));
        loyaltyCheck.setItemValue(null == values.get(0)[1] ? new BigDecimal(0) : new BigDecimal(values.get(0)[1].toString()));
        loyaltyCheck.setLoyaltyType(loyaltyTypeDetails.get(0)[0].toString());
        loyaltyCheck.setMaxItem(Integer.parseInt(loyaltyTypeDetails.get(0)[2].toString()));
//        loyaltyCheck.setSmsCode(smsDiscountDetail.isEmpty() ? null : smsDiscountDetail.get(0)[1].toString());
//        loyaltyCheck.setDiscountType(normalDisc.doubleValue() > smsDisc.doubleValue() ? "Normal_Disc" : "SMS_Code");
//        loyaltyCheck.setDiscountPer(normalDisc.doubleValue() > smsDisc.doubleValue() ? normalDisc : smsDisc);
        loyaltyCheck.setDiscountType("Normal_Disc");
        loyaltyCheck.setDiscountPer(normalDisc);
        loyaltyCheck.setDefaultPoint(new BigDecimal(loyaltyTypeDetails.get(0)[4].toString()));
        loyaltyCheck.setPointType(pointType);
        loyaltyCheck.setAvailablePoint(normalPoint);
        loyaltyCheck.setPoint(point);

        return loyaltyCheck;
    }

    public SmsRespond findSmsCode(String mobileNo, String code) {
        SmsRespond smsRespond = new SmsRespond();
        
        ArrayList<Object[]> output=apiRepository.getSmsDiscountFromMobileNoAndCode(mobileNo,code);
        if (0==output.size()) {
            smsRespond.setType("Invalied");
            smsRespond.setValue(new BigDecimal(0));
        }else if(1==Integer.parseInt(output.get(0)[1].toString())){
            smsRespond.setType("Valied");
            smsRespond.setValue(new BigDecimal(output.get(0)[0].toString()));
        }else{
            smsRespond.setType("Used");
            smsRespond.setValue(new BigDecimal(output.get(0)[0].toString()));
        }
        return smsRespond;
    }

}
