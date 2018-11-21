/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.wms.app.master.loyalty;

import com.supervision.wms.app.master.loyalty.model.MLoyaltyCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author kasun
 */
public interface LoyaltyRepository extends JpaRepository<MLoyaltyCustomer,Integer>{

    public MLoyaltyCustomer findByMobileNo(String mobile);

    public MLoyaltyCustomer findByLoyaltyNo(String loyalty);

    
}
