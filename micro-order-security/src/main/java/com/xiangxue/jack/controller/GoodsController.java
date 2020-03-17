package com.xiangxue.jack.controller;

import com.xiangxue.jack.bean.ZgGoods;
import com.xiangxue.jack.service.GoodsService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
		logger.info("==================已经调用==========" + request.getRemotePort());
		return goodsService.queryAll();
	}

	@RequestMapping("/addGoods")
	public Integer addGoods(@RequestBody ZgGoods zgGoods) {
		return goodsService.addGoods(zgGoods);
	}
}
