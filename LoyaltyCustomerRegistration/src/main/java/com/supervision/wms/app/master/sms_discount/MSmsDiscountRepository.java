/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.wms.app.master.sms_discount;

import com.supervision.wms.app.master.sms_discount.model.MSmsDiscount;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 *
 * @author kasun
 */

public interface MSmsDiscountRepository extends JpaRepository<MSmsDiscount, Integer>{

    public MSmsDiscount findByLoyaltyCustomerAndSmsCode(Integer loyaltyIndex, String smsCode);

    
}
