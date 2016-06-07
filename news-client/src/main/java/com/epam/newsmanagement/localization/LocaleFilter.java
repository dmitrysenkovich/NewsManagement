package com.epam.newsmanagement.localization;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Changes locale.
 */
@WebFilter("/news-client/*")
public class LocaleFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String language = httpServletRequest.getParameter("language");
        if (language != null) {
            ((HttpServletRequest) request).getSession().setAttribute("language", language);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
