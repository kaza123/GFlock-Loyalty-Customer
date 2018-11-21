/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supervision.wms.app.transaction.api;

import com.supervision.wms.app.master.loyalty.model.MLoyaltyCustomer;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author kasun
 */
public interface ApiRepository extends JpaRepository<MLoyaltyCustomer, Integer> {

    @Query(value = "select m_loyalty_type.name,\n"
            + "	m_loyalty_type.`type`,\n"
            + "	m_loyalty_type.item_qty,\n"
            + "	m_loyalty_type.percentage, \n"
            + "	m_loyalty_type.point \n"
            + "from m_loyalty_type\n"
            + "left join m_loyalty_customer on m_loyalty_customer.loyalty_type=m_loyalty_type.index_no\n"
            + "where m_loyalty_customer.loyalty_no=:loyaltyNo", nativeQuery = true)
    public ArrayList<Object[]> getNormalDiscount(@Param("loyaltyNo") String loyaltyNo);

    @Query(value = "select m_sms_discount.discount,\n"
            + "m_sms_discount.sms_code\n"
            + "from m_sms_discount\n"
            + "left join m_loyalty_customer on m_loyalty_customer.index_no=m_sms_discount.loyalty_customer\n"
            + "where m_loyalty_customer.loyalty_no=:loyaltyNo", nativeQuery = true)
    public ArrayList<Object[]> getSmsDiscount(@Param("loyaltyNo") String loyaltyNo);

    @Query(value = "SELECT if (\n"
            + "	(select m_loyalty_customer.index_no from m_loyalty_customer\n"
            + "	where m_loyalty_customer.loyalty_no=:loyaltyNo and m_loyalty_customer.b_month=:bMonth \n"
            + "	and m_loyalty_customer.b_date=:bDate)>0,\n"
            + "(select m_loyalty_setting.value from m_loyalty_setting where m_loyalty_setting.name='BIRTHDAY_POINT')\n"
            + ",0.00) as birthday_point", nativeQuery = true)
    public BigDecimal getBirthdayPoint(@Param("loyaltyNo") String loyaltyNo, @Param("bMonth") Integer bMonth, @Param("bDate") Integer bDate);

    @Query(value = "SELECT m_slab.point\n"
            + "from m_slab \n"
            + "where (SELECT ifnull(sum(t_loyalty_ledger.item_value),0.00) as item_value\n"
            + "from t_loyalty_ledger\n"
            + "left join m_loyalty_customer on m_loyalty_customer.index_no=t_loyalty_ledger.loyalty_costomer\n"
            + "where t_loyalty_ledger.date BETWEEN DATE_SUB(CURRENT_DATE(), INTERVAL \n"
            + "(select m_loyalty_setting.value from m_loyalty_setting where m_loyalty_setting.name='PROMOTION_DIVIDED_DAYS')\n"
            + " DAY) and CURRENT_DATE() and m_loyalty_customer.loyalty_no=:loyaltyNo) BETWEEN m_slab.value_start and m_slab.value_end", nativeQuery = true)
    public BigDecimal getSlabPoint(@Param("loyaltyNo") String loyaltyNo);

    @Query(value = "select m_loyalty_calander.point_value from m_loyalty_calander where \n"
            + "m_loyalty_calander.date=CURRENT_DATE()", nativeQuery = true)
    public BigDecimal getSpecialDayPoint();

    @Query(value = "select sum(t_loyalty_ledger.point_in)-sum(t_loyalty_ledger.point_out) as available_point_normal\n"
            + "from t_loyalty_ledger\n"
            + "left JOIN m_loyalty_customer on m_loyalty_customer.index_no=t_loyalty_ledger.loyalty_costomer\n"
            + "where m_loyalty_customer.loyalty_no=:loyaltyNo", nativeQuery = true)
    public BigDecimal getAvailablePoint(@Param("loyaltyNo") String loyaltyNo);

    @Query(value = "select m_sms_discount.discount,\n"
            + "	m_sms_discount.is_active\n"
            + "from m_sms_discount\n"
            + "left JOIN m_loyalty_customer on m_loyalty_customer.index_no=m_sms_discount.loyalty_customer\n"
            + "where m_loyalty_customer.mobile_no=:mobileNo and m_sms_discount.sms_code=:code", nativeQuery = true)
    public ArrayList<Object[]> getSmsDiscountFromMobileNoAndCode(@Param("mobileNo") String mobileNo, @Param("code") String code);

}
