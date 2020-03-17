package com.xiangxue.jack.service;


import com.xiangxue.jack.bean.ZgGoods;

import java.util.List;

public interface GoodsService {

    String addGoods(ZgGoods zgGoods);

    List<ZgGoods> queryAll();
}
