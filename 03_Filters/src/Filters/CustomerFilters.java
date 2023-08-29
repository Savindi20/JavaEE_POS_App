package Filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/Customer")
public class CustomerFilters implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Customer Filter Init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Do Filter Method Invoked");

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String servletPath = req.getServletPath();

        String name = servletRequest.getParameter("name");

        if (name.equals("ijse")&&servletPath.equals("/Customer")){
            filterChain.doFilter(servletRequest,servletResponse);
        }else {
            resp.setStatus(500);
        }
    }

    @Override
    public void destroy() {
        System.out.println("Customer Filter Destroy");
    }
}
