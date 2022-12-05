package com.macro.mall.tiny.security.component;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.macro.mall.tiny.common.api.Result;
import com.macro.mall.tiny.common.api.ResultCode;
import com.macro.mall.tiny.modules.ums.dto.UmsAdminLoginParam;
import io.netty.util.internal.StringUtil;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 用户名与密码basic过滤器
 * 用于将请求头的base64加密信息解码，并传递给实际访问的接口
 * @author 李志靖
 * @date 2022/12/05
 */
public class BasicFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(BasicFilter.class);
    @Value("${basic.tokenHeader}")
    private String tokenHeader;

    @Value("${basic.targetUrl}")
    private String url;
    @Value("${basic.tokenHead}")
    private String tokenHead;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String path = request.getServletPath();
        if (url.equals(path)){
            String authHeader = request.getHeader(this.tokenHeader);
            if (authHeader != null && authHeader.startsWith(this.tokenHead)) {
                String authToken = authHeader.substring(this.tokenHead.length());// The part after "Basic "
                UmsAdminLoginParam paramObj = getLoginParamFromToken(authToken);
                String bodyObj = getBody(request);
                JSONObject paramJSON = JSONUtil.parseObj(paramObj);
                if (!StringUtil.isNullOrEmpty(bodyObj)){
                    JSONObject bodyJSON = JSONUtil.parseObj(bodyObj);
                    paramJSON.putAll(bodyJSON);
                }
                String body = JSONUtil.toJsonStr(paramJSON);
                LOGGER.info("checking param:{}", body);
                request = new BodyRequestWrapper(request,body);
                chain.doFilter(request, response);
            }else {
                String jsonStr = JSONUtil.toJsonStr(Result.error(ResultCode.PARAM_ERROR));
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.getWriter().write(jsonStr);
            }
        }else {
            chain.doFilter(request, response);
        }
    }

    /**
     * 从令牌获得登录参数
     *  将用户名与密码解码，并传给登录接口
     * @param authToken 身份验证令牌
     * @return {@link UmsAdminLoginParam}
     */
    private UmsAdminLoginParam getLoginParamFromToken(String authToken) {
        String[] userAndPass = new String(Base64.decodeBase64(authToken), StandardCharsets.UTF_8).split(":");
        String userName = null,password = null;
        if (userAndPass.length == 1){
            userName = userAndPass[0];
        }else if (userAndPass.length == 2){
            userName = userAndPass[0];
            password = userAndPass[1];
        }
        return new UmsAdminLoginParam(userName,password);
    }

    /**
     * 从请求中获取到body参数
     *
     * @param request 请求
     * @return {@link String}
     * @throws IOException ioexception
     */
    private String getBody (HttpServletRequest request) throws IOException{
        BufferedReader reader = request.getReader();
        String str = "";
        StringBuilder body = new StringBuilder();
        while ((str = reader.readLine()) != null){
            body.append(str);
        }
        return body.toString();
    }
}
