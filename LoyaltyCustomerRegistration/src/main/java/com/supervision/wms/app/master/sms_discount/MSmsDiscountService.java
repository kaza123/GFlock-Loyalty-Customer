/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.wms.app.master.sms_discount;

import com.supervision.wms.app.master.sms_discount.model.MSmsDiscount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kasun
 */
@Service
public class MSmsDiscountService {

    @Autowired
    public MSmsDiscountRepository discountRepository;
    
    public MSmsDiscount save(MSmsDiscount discount) {
        return discountRepository.save(discount);
        
    }
    public MSmsDiscount findByLoyaltyCustomerAndSmsCode(Integer loyaltyIndex, String smsCode) {
        return discountRepository.findByLoyaltyCustomerAndSmsCode(loyaltyIndex,smsCode);
        
    }
    
}
