package com.xiangxue.jack.controller;

import com.xiangxue.jack.bean.ZgGoods;
import com.xiangxue.jack.service.GoodsService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {

	private org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	GoodsService goodsService;

	@RequestMapping("/queryGoods")
	public List<ZgGoods> queryGoods(HttpServletRequest request) {
		return goodsService.queryAll();
	}

	@RequestMapping(value = "/addGoods",method = RequestMethod.POST)
	public String addGoods(@RequestBody ZgGoods zgGoods) {
		return goodsService.addGoods(zgGoods);
	}
}
