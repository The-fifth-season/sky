package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {       //拦截器，得实现拦截器的接口HandlerInterceptor

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     * @param request 前端请求
     * @param response 返回前端数据
     * @param handler 处理
     * @return 返回结果是否通过
     * @throws Exception 抛异常
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /*System.out.println("当前线程1:::"+Thread.currentThread().getId());
        //查看前端传送的请求中，请求头都有哪些内容
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            System.out.println(headerNames.nextElement());
        }
*/
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌
        String token = request.getHeader("token");   //获取指定名称的请求头的信息

        //2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            //注意：：这里传入的密钥，要与JwtUtil.creatJWT中传入的一致，否则必定校验失败——————验证失败则直接报异常
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);    //claim 中包含了解析token后的信息
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());     //get(Object Key) get方法中填Map中的Key————获取Value

            System.out.println("当前线程2:::"+Thread.currentThread().getId());

            /*ThreadLocal<Long> threadLocal = BaseContext.threadLocal;
            threadLocal.set(empId);*/
            BaseContext.setCurrentId(empId);

            log.info("当前员工id：{}", empId);
            //3、通过，放行
            return true;
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            response.setStatus(401);
            return false;
        }
    }
}
