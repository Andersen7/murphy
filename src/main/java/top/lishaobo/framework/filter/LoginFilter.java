package top.lishaobo.framework.filter;

import org.springframework.beans.factory.annotation.Autowired;
import top.lishaobo.framework.util.RedisUtil;

import javax.servlet.*;
import java.io.IOException;

public class LoginFilter implements Filter {

    @Autowired
    RedisUtil redisUtil;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        /*HashMap<String, HttpSession> sessionMap = new HashMap<>();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = null;

        String sid = request.getParameter("Sid");

        if (sid != null && !sid.isEmpty()) {
            session = (HttpSession) sessionMap.get(sid);
        }

        if (session == null) {
            session = request.getSession();
        }

        User user = (User) redisUtil.get(sid != null ? sid : session.getId());

        if(user!=null){
            // 保存用户信息到threadLocal
            ClientInfoBean clientInfoBean = new ClientInfoBean();
            clientInfoBean.setUser(user);
            ThreadLocalClient.put(clientInfoBean);

            filterChain.doFilter(servletRequest, servletResponse);

            // 从threadLocal remove 用户信息
            ThreadLocalClient.remove();
        }else{

            response.setContentType("application/json;charset=utf-8");
            response.getWriter()
                    .write(JSON.toJSONString("登录已失效，请重新登录", SerializerFeature.WriteDateUseDateFormat,
                            SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteMapNullValue,
                            SerializerFeature.WriteNullStringAsEmpty));
        }*/
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
