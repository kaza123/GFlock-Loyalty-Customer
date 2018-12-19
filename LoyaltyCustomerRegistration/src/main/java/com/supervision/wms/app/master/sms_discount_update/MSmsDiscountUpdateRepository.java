/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.wms.app.master.sms_discount_update;

import com.supervision.wms.app.master.sms_discount_update.model.MSmsDiscountUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author kasun
 */
public interface MSmsDiscountUpdateRepository extends JpaRepository<MSmsDiscountUpdate, Integer>{
    
}
