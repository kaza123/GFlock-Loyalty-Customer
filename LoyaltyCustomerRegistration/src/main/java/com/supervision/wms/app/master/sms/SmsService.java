/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.wms.app.master.sms;

import com.supervision.wms.app.master.loyalty.LoyaltyService;
import com.supervision.wms.app.master.sms_text.SmsTextRepository;
import com.supervision.wms.app.master.sms_text.model.MSmsText;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kasun
 */
@Service
public class SmsService {

    @Autowired
    public SmsRepository smsRepository;
 
    @Autowired
    public SmsTextRepository smsTextRepository;

    public String findByName(String name) {
        return smsRepository.findByName(name).getValue();
    }

    public Integer sendSms(String mobile, String message) {
        try {
            message = message.replace(" ", "%20");
            String apikey = findByName("loyalty");
            String URL = "http://gflock-sms.supervisionglobal.com/send_sms.php?api_key=" + apikey + "&number=" + mobile + "&message=" + message;
            URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();

            return code;

        } catch (MalformedURLException ex) {
            Logger.getLogger(LoyaltyService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LoyaltyService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
;

    public String resentOtp(String mobile, String otp) {
        MSmsText smsText = smsTextRepository.findByName("OTP_SENT");
        String text=smsText.getText1()+" "+otp+" "+smsText.getText2();
        
        Integer respond = sendSms(mobile, text);
        if (respond==0 || respond==200) {
            return otp;
        }
        return null;
    }


}
