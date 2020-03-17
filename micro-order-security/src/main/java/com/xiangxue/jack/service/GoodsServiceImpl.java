package com.xiangxue.jack.service;

import com.xiangxue.jack.bean.ZgGoods;
import com.xiangxue.jack.dao.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    CommonMapper commonMapper;

    @Transactional
    @Override
    public int addGoods(ZgGoods zgGoods) {
        int i = commonMapper.addGood(zgGoods);
        return i;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ZgGoods> queryAll() {
        return commonMapper.queryAll();
    }
}
