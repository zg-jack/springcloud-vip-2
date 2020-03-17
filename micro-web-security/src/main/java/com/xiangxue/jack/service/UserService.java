package com.xiangxue.jack.service;

import com.xiangxue.jack.bean.ConsultContent;
import rx.Observable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.Future;

public interface UserService {
    List<ConsultContent> queryContents();

    List<ConsultContent> queryContents(HttpServletRequest request);

    Future<String> queryContentsAsyn();

    List<ConsultContent> queryContent();

    String queryMonitor();

    public Observable<String> mergeResult();
}
