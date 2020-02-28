package com.xiangxue.jack.service;

import com.xiangxue.jack.bean.ConsultContent;

import java.util.List;

public interface UserService {
    List<ConsultContent> queryContents();

    List<ConsultContent> queryContent();

    String queryMonitor();
}
