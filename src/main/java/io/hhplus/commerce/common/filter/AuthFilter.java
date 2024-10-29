package io.hhplus.commerce.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@WebFilter("/*")
@Component
public class AuthFilter implements Filter {
    private final String[] skipPaths = {
            "/api"
    };
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        log.info("---------- AuthFilter :: doFilter ----------");

        HttpServletRequest HttpRequest = (HttpServletRequest) request;

        String path = HttpRequest.getRequestURI().substring(HttpRequest.getContextPath().length());

        for (String skip : skipPaths) {
            if (path.startsWith(skip)) {
                log.info("---- AuthFilter :: skip path : {} ----", path);
                log.info("---- AuthFilter :: request : {} ----", ((HttpServletRequest) request).getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }
        }

        log.info("---------- AuthFilter :: doFilter ----------");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("---------- AuthFilter :: init ----------");
    }

    @Override
    public void destroy() {
        log.info("---------- AuthFilter :: destroy ----------");
    }
}
