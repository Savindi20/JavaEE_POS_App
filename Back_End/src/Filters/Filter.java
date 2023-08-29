package Filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
public class Filter implements javax.servlet.Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String servletPath = req.getServletPath();
        System.out.println(servletPath);
         filterChain.doFilter(servletRequest,servletResponse);
         resp.addHeader("Access-Control-Allow-Origin","*");
         resp.addHeader("Access-Control-Allow-Methods","DELETE,PUT");
         resp.addHeader("Access-Control-Allow-Headers","content-type");
         resp.addHeader("Content-Type","application/json");
    }

    @Override
    public void destroy() {

    }
}
