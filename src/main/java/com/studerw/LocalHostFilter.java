package com.studerw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

/**
 * Only allow direct requests from the local Finagle Proxy
 * Borrowed from http://stackoverflow.com/questions/5009426/java-using-a-filter-to-check-remote-address
 * @author William Studer
 */
public class LocalHostFilter implements Filter{
    private static final Logger log = LoggerFactory.getLogger(LocalHostFilter.class);
    private Set<String> localAddresses = new HashSet<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Init LocalHostFilter...");
        try {
            localAddresses.add(InetAddress.getLocalHost().getHostAddress());
            for (InetAddress inetAddress : InetAddress.getAllByName("localhost")) {
                localAddresses.add(inetAddress.getHostAddress());
            }
            localAddresses.stream().forEach(i -> log.info(i));
        } catch (IOException e) {
            throw new ServletException("Unable to lookup local addresses");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        String remoteAddr = request.getRemoteAddr();
        log.debug("checking remoteAddr: {}", remoteAddr);
        if (localAddresses.contains(remoteAddr)) {
            chain.doFilter(request, response);
        } else {
            HttpServletResponse wrapped = (HttpServletResponse) response;
            wrapped.sendError(HttpServletResponse.SC_FORBIDDEN, "Only local requests accepted");
        }
    }

    @Override
    public void destroy() {
        log.info("Destroying LocalHostFilter...");
    }


}
