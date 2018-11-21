/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.wms.app.master.loyalty;

import com.supervision.wms.app.master.loyalty.model.MLoyaltyCustomer;
import com.supervision.wms.app.master.loyalty_setting.LoyaltySettingService;
import com.supervision.wms.app.master.loyalty_setting.model.MLoyaltySetting;
import com.supervision.wms.app.master.loyalty_type.LoyaltyTypeService;
import com.supervision.wms.app.master.sms.SmsService;
import com.supervision.wms.app.master.sms_text.SmsTextService;
import com.supervision.wms.app.master.sms_text.model.MSmsText;
import com.supervision.wms.app.transaction.loyalty_ledger.LoyaltyLedgerService;
import com.supervision.wms.app.transaction.loyalty_ledger.model.TLoyaltyLedger;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kasun
 */
@Service
public class LoyaltyService {

    @Autowired
    public LoyaltyRepository loyaltyRepository;

    @Autowired
    public SmsService smsService;

    @Autowired
    public SmsTextService smsTextService;

    @Autowired
    public LoyaltySettingService loyaltySettingService;

    @Autowired
    public LoyaltyLedgerService loyaltyLedgerService;

    @Autowired
    public LoyaltyTypeService loyaltyTypeService;

    @Transactional
    public MLoyaltyCustomer save(MLoyaltyCustomer loyaltyCustomer) {
        loyaltyCustomer.setName(loyaltyCustomer.getName().toUpperCase());
        if (loyaltyCustomer.getIndexNo() != null) {
            // update
            return loyaltyRepository.save(loyaltyCustomer);
        } else {
//            new loyalty customer
            Boolean sms = false;
            loyaltyCustomer.setLoyaltyNo(loyaltyCustomer.getMobileNo());
            loyaltyCustomer.setLoyaltyType(loyaltyTypeService.findByName("CUSTOMER").getIndexNo());
            loyaltyCustomer.setIsSms(true);
            loyaltyCustomer.setProStartDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            loyaltyCustomer.setRegDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

            MLoyaltySetting mLoyaltySetting = new MLoyaltySetting();
            MLoyaltyCustomer save = loyaltyRepository.save(loyaltyCustomer);
            if (save.getIndexNo() > 0) {
                mLoyaltySetting = loyaltySettingService.findByName("WELCOME_POINT");
                if (mLoyaltySetting.getName() == null || "".equals(mLoyaltySetting.getName())) {
                    throw new RuntimeException("Can't find welcome point !");
                }
                // sent save success msg  
                MSmsText smsText = smsTextService.findByName("SAVE_SUCCESS");
                String sendText = smsText.getText1() + " " + (int) mLoyaltySetting.getValue() + " " + smsText.getText2();
                // add welcome loyalty points
                TLoyaltyLedger ledger = new TLoyaltyLedger();
                ledger.setDiscountPre(new BigDecimal(0));
                ledger.setDiscountValue(new BigDecimal(0));
                ledger.setInvBranch(null);
                ledger.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                ledger.setPointType("Normal_Point");
                ledger.setInvDate(null);
                ledger.setInvNo(null);
                ledger.setItemQty(0);
                ledger.setItemValue(new BigDecimal(0));
                ledger.setLoyaltyCostomer(save.getIndexNo());
                ledger.setPointIn(new BigDecimal(mLoyaltySetting.getValue()));
                ledger.setPointOut(new BigDecimal(0));
                ledger.setSmsDiscount(null);
                ledger.setType("point");
                ledger.setType2("point");
                TLoyaltyLedger ledgerSave = loyaltyLedgerService.save(ledger);
                if (ledgerSave.getIndexNo() > 0) {
                    Integer code = smsService.sendSms(loyaltyCustomer.getMobileNo(), sendText);
                    if (code == 200 || code == 0) {
                        sms = true;
                    } else {
                        throw new RuntimeException("sms Send failed !");
                    }

                } else {
                    throw new RuntimeException("loyalty ledger save fail !");
                }
            }
            if (sms) {
                return save;

            } else {
                return null;
            }
        }
    }

    public MLoyaltyCustomer findByMobileNo(String mobile) {
        return loyaltyRepository.findByMobileNo(mobile);
    }

    public MLoyaltyCustomer findByLoyaltyNo(String mobile) {
        return loyaltyRepository.findByLoyaltyNo(mobile);
    }

    public String getRandomNo() {
        Random r = new Random();
        String otp = "";
        String numbers = "123456789";

        for (int i = 0; i < 5; i++) {
            otp += numbers.charAt(r.nextInt(numbers.length()));
        }
        return otp;
    }

    public HashMap<String, String> sentOtp(String mobile) {
        String otp = getRandomNo();
        HashMap<String, String> map = new HashMap<>();
        MSmsText findByName = smsTextService.findByName("OTP_SENT");
        String sendText = findByName.getText1() + " " + otp + " " + findByName.getText2();
        Integer code = smsService.sendSms(mobile, sendText);

        if (code == 200 || code == 0) {
            map.put("otpNo", otp);
            return map;
        }
        throw new RuntimeException("otp validation code send fail !");
    }
}
