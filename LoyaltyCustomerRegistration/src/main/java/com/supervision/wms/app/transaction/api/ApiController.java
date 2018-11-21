/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.wms.app.transaction.api;

import com.supervision.wms.app.transaction.api.model.MLoyaltyCheck;
import com.supervision.wms.app.transaction.api.model.SmsRespond;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author kasun
 */
@RestController
@CrossOrigin
@RequestMapping("/api/sv/api")
public class ApiController {
    @Autowired
    public ApiService apiService;
    
    @RequestMapping(value = "/find-by-loyalty-no/{type}/{loyaltyNo}" , method = RequestMethod.GET)
    public MLoyaltyCheck getDetailByLoyaltyNo(@PathVariable String type, @PathVariable String loyaltyNo){
        return apiService.getDetailByLoyaltyNo(type,loyaltyNo);
    }
    @RequestMapping(value = "/find-sms-code/{mobileNo}/{code}" , method = RequestMethod.GET)
    public SmsRespond findSmsCode(@PathVariable String mobileNo,@PathVariable String code){
        return apiService.findSmsCode(mobileNo,code);
    }
}
