package cn.enjoy.mall.service;

import cn.enjoy.mall.model.MessageLog;

public interface PayCompleteService {

    void payCompleteBusiness(String orderId, MessageLog messageLog);
}
