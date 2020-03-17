package com.xiangxue.jack.dao;

import com.xiangxue.jack.bean.ZgOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ZgOrderMapper {

    @Select("select * from zg_order where orderId=#{orderId}")
    public List<ZgOrder> queryOrderByOrderId(String orderId);

    @Insert("insert into zg_order(orderMoney,orderTime,orderStatus,version) values(#{orderMoney},now(),#{orderStatus},#{version})")
    public int addOrder(ZgOrder zgOrder);

    @Update("update zg_order set orderStatus=#{orderStatus} where orderId=#{orderId}")
    public int updateOrder(ZgOrder zgOrder);

    @Update("update zg_order set orderStatus = #{orderStatus} ,version=version+1 where orderId=#{orderId} and version=#{version}")
    public int updateOrderByLock(ZgOrder zgOrder);
}
