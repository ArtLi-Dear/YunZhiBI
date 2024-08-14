package com.artli.springbootinit.manager;


import cn.hutool.core.util.StrUtil;
import com.artli.springbootinit.common.ErrorCode;
import com.artli.springbootinit.exception.ThrowUtils;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AiManager {

    @Resource
    private YuCongMingClient yuCongMingClient;
    private static final Long AI_ID = 1823539496730746882L;


    public String doChat(String message){

        DevChatRequest chatRequest = new DevChatRequest();
        chatRequest.setModelId(AI_ID);
        chatRequest.setMessage(message);

        BaseResponse<DevChatResponse> responseBaseResponse = yuCongMingClient.doChat(chatRequest);

        ThrowUtils.throwIf(responseBaseResponse == null, ErrorCode.SYSTEM_ERROR,"AI 响应错误");



        return responseBaseResponse.getData().getContent();



    }
}
