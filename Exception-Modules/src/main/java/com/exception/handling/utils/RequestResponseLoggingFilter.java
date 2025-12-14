package com.exception.handling.utils;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RequestResponseLoggingFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) request;
		log.info("Incoming Request: {} {}", httpReq.getMethod(), httpReq.getRequestURI());
		chain.doFilter(request, response);
		HttpServletResponse httpRes = (HttpServletResponse) response;
		log.info("Outgoing Response: Status {}", httpRes.getStatus());
	}

}
