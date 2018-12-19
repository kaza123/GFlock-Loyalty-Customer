/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.wms.app.transaction.api;

import com.supervision.wms.app.master.loyalty.LoyaltyService;
import com.supervision.wms.app.master.loyalty.model.MLoyaltyCustomer;
import com.supervision.wms.app.master.sms_discount.MSmsDiscountService;
import com.supervision.wms.app.master.sms_discount.model.MSmsDiscount;
import com.supervision.wms.app.master.sms_discount_update.MSmsDiscountUpdateService;
import com.supervision.wms.app.master.sms_discount_update.model.MSmsDiscountUpdate;
import com.supervision.wms.app.transaction.api.model.MLoyaltyCheck;
import com.supervision.wms.app.transaction.api.model.SmsRespond;
import com.supervision.wms.app.transaction.api.model.TLoyalty;
import com.supervision.wms.app.transaction.loyalty_ledger.LoyaltyLedgerService;
import com.supervision.wms.app.transaction.loyalty_ledger.model.TLoyaltyLedger;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kasun
 */
@Service
public class ApiService {

    @Autowired
    public ApiRepository apiRepository;

    @Autowired
    public MSmsDiscountService smsDiscountService;

    @Autowired
    public LoyaltyService loyaltyService;

    @Autowired
    public LoyaltyLedgerService loyaltyLedgerService;
    
    @Autowired
    public MSmsDiscountUpdateService discountUpdateService;

    public MLoyaltyCheck getDetailByLoyaltyNo(String type, String loyaltyNo) {
        MLoyaltyCustomer customer = new MLoyaltyCustomer();
        if ("Mobile_No".equals(type)) {
            customer = loyaltyService.findByMobileNo(loyaltyNo);
            if (null == customer) {
                MLoyaltyCheck mLoyaltyCheck = new MLoyaltyCheck();
                return mLoyaltyCheck.nullModel();
            } else {
                loyaltyNo = customer.getLoyaltyNo();
            }
        } else if ("Loyalty_No".equals(type)) {
            customer = loyaltyService.findByLoyaltyNo(loyaltyNo);
            if (null == customer) {
                MLoyaltyCheck mLoyaltyCheck = new MLoyaltyCheck();
                return mLoyaltyCheck.nullModel();
            }
        } else {
            throw new RuntimeException("Parameeter type must be Mobile_No or Loyalty_No");
        }
        // get loyalty customer detail
        //get item count and value for this month
        ArrayList<Object[]> values = loyaltyLedgerService.getItemCountAndValueForThisMonth(loyaltyNo);

        //discount
        //normal
        String discType="Normal_Disc";
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
       
        //points
        BigDecimal point = new BigDecimal(loyaltyTypeDetails.get(0)[4].toString());
        String pointType = "Normal_Point";
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
//        System.out.println("normalPoint " + normalPoint);

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
        loyaltyCheck.setDiscountType(discType);
        loyaltyCheck.setDiscountPer(normalDisc);
        loyaltyCheck.setDefaultPoint(new BigDecimal(loyaltyTypeDetails.get(0)[4].toString()));
        loyaltyCheck.setPointType(pointType);
        loyaltyCheck.setAvailablePoint(normalPoint);
        loyaltyCheck.setPoint(point);

        return loyaltyCheck;
    }

    public SmsRespond findSmsCode(String mobileNo, String code) {
        SmsRespond smsRespond = new SmsRespond();

        ArrayList<Object[]> output = apiRepository.getSmsDiscountFromMobileNoAndCode(mobileNo, code);
        if (0 == output.size()) {
            smsRespond.setType("Invalied");
            smsRespond.setValue(new BigDecimal(0));
        } else if (1 == Integer.parseInt(output.get(0)[1].toString())) {
            smsRespond.setType("Valied");
            smsRespond.setValue(new BigDecimal(output.get(0)[0].toString()));
        } else {
            smsRespond.setType("Used");
            smsRespond.setValue(new BigDecimal(output.get(0)[0].toString()));
        }
        return smsRespond;
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer save(List<TLoyalty> loyaltyList) {
        Integer returnCount = 0;
        for (TLoyalty tLoyalty : loyaltyList) {
            TLoyaltyLedger save = new TLoyaltyLedger();
            //LC for Loyalty Customer
            if ("LC".equals(tLoyalty.getType())) {
                try {
                    MLoyaltyCustomer customer = loyaltyService.findByMobileNo(tLoyalty.getLoyaltyCustomer());
                    if (null == customer) {
//                        returnModel.setValid("false");
//                        returnModel.setStatus("Cant find loyalty customer from mobileNo - " + tLoyalty.getLoyaltyCustomer());
//                        return returnModel;
                        throw new RuntimeException("Cant find loyalty customer from mobileNo - " + tLoyalty.getLoyaltyCustomer());
                    }
                    //common
                    save.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                    save.setInvDate(tLoyalty.getInvoiceDate());
                    save.setLoyaltyCostomer(customer.getIndexNo());
                    save.setItemQty(tLoyalty.getItemQty().intValue());
                    save.setItemValue(tLoyalty.getItemValue());
                    save.setPointType(tLoyalty.getPointType());
                    save.setInvBranch(tLoyalty.getBranch());
                    save.setInvNo(tLoyalty.getInvoiceNo());
                    save.setType("point");
                    save.setType2("point");
                    save.setDiscountPre(new BigDecimal(BigInteger.ZERO));
                    save.setDiscountValue(new BigDecimal(BigInteger.ZERO));

                    // check transaction type
                    if ("issue".equals(tLoyalty.getTrasctionType())) {
                        save.setPointIn(tLoyalty.getValue());
                        save.setPointOut(BigDecimal.ZERO);
                    } else if ("redeem".equals(tLoyalty.getTrasctionType())) {
                        save.setPointIn(BigDecimal.ZERO);
                        save.setPointOut(tLoyalty.getValue());
                    } else {
//                        returnModel.setValid("false");
//                        returnModel.setStatus("Cant find loyalty transaction type - " + tLoyalty.getTrasctionType());
//                        return returnModel;
                        throw new RuntimeException("Cant find loyalty transaction type - " + tLoyalty.getTrasctionType());
                    }

                    // check point type
                    if ("Birthday".equals(tLoyalty.getPointType())) {
                        save.setPointType("Normal_Point");
                        save.setType2("Birthday");
                        loyaltyLedgerService.save(save);
                    } else if ("Slab".equals(tLoyalty.getPointType())) {
                        save.setPointType("Normal_Point");
                        save.setType2("Slab");
                        loyaltyLedgerService.save(save);
                    } else if ("Special_Day".equals(tLoyalty.getPointType())) {
                        save.setPointType("Normal_Point");
                        save.setType2("Special_Day");
                        loyaltyLedgerService.save(save);
                    } else if ("Normal_Point".equals(tLoyalty.getPointType())) {
                        save.setPointType("Normal_Point");
                        save.setType2("Normal_Point");
                        loyaltyLedgerService.save(save);
                    } else if ("Coins".equals(tLoyalty.getPointType())) {
                        save.setType("coins");
                        save.setType2("coins");
                        save.setPointType("Normal_Point");
                        loyaltyLedgerService.save(save);
                    } else if ("Sms_Code".equals(tLoyalty.getPointType())) {
                        save.setType2("discount");
                        SmsRespond findSmsCode = findSmsCode(tLoyalty.getLoyaltyCustomer(), tLoyalty.getBarcode());
                        MSmsDiscount smsDisc = smsDiscountService.findByLoyaltyCustomerAndSmsCode(customer.getIndexNo(), tLoyalty.getBarcode());
                        if ("Valied".equals(findSmsCode.getType())) {
//                        save sms discount
                            smsDisc.setCheckBranch(tLoyalty.getBranch());
                            smsDisc.setCheckDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                            smsDisc.setInvNo(tLoyalty.getInvoiceNo());
                            smsDisc.setIsActive(false);
                            MSmsDiscount save1 = smsDiscountService.save(smsDisc);
                            
                            MSmsDiscountUpdate mSmsDiscountUpdate = new MSmsDiscountUpdate();
                            mSmsDiscountUpdate.setRedeem("redeem");
                            mSmsDiscountUpdate.setSms_discount(save1.getIndexNo());
                            discountUpdateService.save(mSmsDiscountUpdate);
                            
                        } else if ("Invalied".equals(findSmsCode.getType())) {
//                            returnModel.setValid("false");
//                            returnModel.setStatus("Invalied sms code - " + tLoyalty.getBarcode());
//                            return returnModel;
                            throw new RuntimeException("Invalied sms code - " + tLoyalty.getBarcode());
                        } else if ("Used".equals(findSmsCode.getType())) {
//                            returnModel.setValid("false");
//                            returnModel.setStatus("Used sms code " + smsDisc.getInvNo() + " - " + smsDisc.getCheckDate());
//                            return returnModel;
                            throw new RuntimeException("Used sms code " + smsDisc.getInvNo() + " - " + smsDisc.getCheckDate());
                        } else {
//                            returnModel.setValid("false");
//                            returnModel.setStatus("unknown error");
//                            return returnModel;
                            throw new RuntimeException("unknown error");
                        }
                    } else {
//                        returnModel.setValid("false");
//                        returnModel.setStatus("Cant find loyalty point type - " + tLoyalty.getPointType());
//                        return returnModel;
                        throw new RuntimeException("Cant find loyalty point type - " + tLoyalty.getPointType());
                    }
                } catch (RuntimeException e) {
//                    returnModel.setValid("false");
//                    returnModel.setStatus("save fail " + e.getMessage());
//                    return returnModel;
                    throw new RuntimeException("save fail " + e.getMessage());
                }
                returnCount++;
            }

        }
        return returnCount;

    }

}
