/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.wms.app.master.sms_discount_update;

import com.supervision.wms.app.master.sms_discount_update.model.MSmsDiscountUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kasun
 */
@Service
public class MSmsDiscountUpdateService {
    @Autowired
    public MSmsDiscountUpdateRepository discountUpdateRepository;
    
    public MSmsDiscountUpdate save(MSmsDiscountUpdate discount) {
        return discountUpdateRepository.save(discount);
        
    }
}
