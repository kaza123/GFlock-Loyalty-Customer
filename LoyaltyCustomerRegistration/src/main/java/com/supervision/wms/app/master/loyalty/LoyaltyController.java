/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.wms.app.master.loyalty;

import com.supervision.wms.app.master.loyalty.model.MLoyaltyCustomer;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author kasun
 */
@RestController
@CrossOrigin
@RequestMapping("/api/sv/master/loyalty")
public class LoyaltyController {
    @Autowired
    private LoyaltyService loyaltyService;
    
    @RequestMapping(value = "/save" , method = RequestMethod.POST)
    public MLoyaltyCustomer getAllEmployee(@RequestBody MLoyaltyCustomer loyaltyCustomer){
        return loyaltyService.save(loyaltyCustomer); 
    }
   
    @RequestMapping(value = "/find-by-mobile/{mobile}" , method = RequestMethod.GET)
    public MLoyaltyCustomer fingByMobile(@PathVariable String mobile){
        return loyaltyService.findByMobileNo(mobile);
    }
    
    @RequestMapping(value = "/sent-otp/{mobile}" , method = RequestMethod.GET)
    public HashMap<String, String> sentOtp(@PathVariable String mobile){
        return loyaltyService.sentOtp(mobile);
        
    }
}
