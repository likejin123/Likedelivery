/**
 思想：
 1.过滤掉不需要处理的请求
    不需要处理：
        对后端所有的页面都不需要处理（页面都会初始化即访问后端）
        对登录登出不需要处理
    即只处理对controller的地址访问
 2.然后需要处理的请求时判断是否登录（用session域来判断），即在login后会在session中存放employee对象
 3.登录过则直接放行
 4.未登录返回前端需要的数据然后前端控制跳转
*/
package com.likejin.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.likejin.reggie.common.BaseContext;
import com.likejin.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author 李柯锦
 * @Date 2023/5/31 19:28
 * @Description 判断用户是否登录的过滤器
 */

@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter{

    //路径匹配器，支持通配符的写法
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("拦截到请求:{}", request.getRequestURI() );

        //1.获取本次请求的url
        String requestURI = request.getRequestURI();

        //2.判断本次请求是否需要处理（判断登录状态）
        boolean check = check(requestURI);

        //3.如果不需要处理，则直接放行
        if(check){
            filterChain.doFilter(request,response);
            return;
        }
        //4-1.判断用户是否登录，如果已登录，则直接放行
        if(request.getSession().getAttribute("employee")!=null){

            //利用一个http请求分配一个线程来通过线程存储empId方便在MetaObjectHandler中自动填充
            Object empId = request.getSession().getAttribute("employee");
            BaseContext.setCurrentId((Long)empId);

            long id = Thread.currentThread().getId();
            log.info("线程id为{}",id);

            filterChain.doFilter(request,response);
            return;
        }
        //4-2.判断用户是否登录，如果已登录，则直接放行
        if(request.getSession().getAttribute("user")!=null){

            //利用一个http请求分配一个线程来通过线程存储empId方便在MetaObjectHandler中自动填充
            Object userId = request.getSession().getAttribute("user");
            BaseContext.setCurrentId((Long)userId);

            long id = Thread.currentThread().getId();
            log.info("线程id为{}",id);

            filterChain.doFilter(request,response);
            return;
        }
        //5.如果未登录则返回未登录结果（前端有拦截器如果结果为0且msg为NOTLOGIN则直接跳转页面）
        //通过输出流的方式向客户端响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;



        //不能使用，因为还会请求/backend下面的多个静态文件你

        //自定义。。直接判断session域中是否有数据。。没有则直接跳转回login
        //如果不是请求后台管理系统的直接放行
//        if(!request.getRequestURI().startsWith("/backend/")){
//            filterChain.doFilter(request,response);
//            return;
//        }
//
//        //判断路径是否为登录，为登录则放行
//        if(request.getRequestURI().equals("/backend/page/login/login.html")||request.getRequestURI().equals("/employee/login")){
//            filterChain.doFilter(request,response);
//            return;
//        }
//        //不为登录则检查是否session域有数据，有数据则放行
//        if(request.getSession().getAttribute("employee")!=null){
//            filterChain.doFilter(request,response);
//            return;
//        }
//        //不为登录页且没登录，则直接返回前NOTLOGIN让前端控制跳转
//        response.sendRedirect("/backend/page/login/login.html");
//        return;
    }

    /*
     * @Description 检查本次请求是否需要放行
     * @param requestURI
     * @return boolean
     **/
    public boolean check(String requestURI){
        //定义不需要处理的请求路径
        //只拦截controller的其他请求（即获取不到数据）,拦截到则跳转登录页面
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login",
        };
        for(String url:urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match == true){
                return true;
            }
        }
        return false;
    }
}
