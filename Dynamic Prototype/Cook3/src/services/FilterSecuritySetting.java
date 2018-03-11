package services;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class FilterSecuritySetting
 */
@WebFilter("/FilterSecuritySetting")
public class FilterSecuritySetting implements Filter {

    public FilterSecuritySetting() {
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
			HttpServletResponse resp = (HttpServletResponse) response; 
			resp.setHeader("Content-Security-Policy", "frame-ancestors 'none'; reflected-xss 'block'");
			chain.doFilter(request, resp);
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
