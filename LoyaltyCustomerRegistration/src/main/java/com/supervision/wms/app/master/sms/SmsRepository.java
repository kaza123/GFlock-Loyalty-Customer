/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.wms.app.master.sms;

import com.supervision.wms.app.master.sms.model.MSms;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author kasun
 */
public interface SmsRepository extends JpaRepository<MSms, Integer>{

    public MSms findByName(String name);
    
}
