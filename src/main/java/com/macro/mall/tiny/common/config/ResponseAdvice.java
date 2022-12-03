package com.macro.mall.tiny.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.macro.mall.tiny.common.api.Result;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import javax.annotation.Resource;

@ControllerAdvice(annotations = {RestController.class, Controller.class},basePackages = "com.macro.mall.tiny.modules.ums.controller")
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !returnType.getDeclaringClass().getName().contains("springfox");
    }

    @SneakyThrows//将编译时异常包装成为运行时异常（RuntimeException）
    @Override
    public Object beforeBodyWrite(Object res, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (res instanceof String){
            return objectMapper.writeValueAsString(Result.success(res));
        }else if(res instanceof Result){
            return res;
        }else if (res instanceof Boolean){
            return (boolean)res?Result.success(null):Result.error();
        }
        return Result.success(res);
    }
}
