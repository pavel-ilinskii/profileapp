package com.ilinskii.profileapp.util;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.stream.Collectors;

public class MultipleReadHttpRequestWrapper extends HttpServletRequestWrapper {

    private final String body;

    public MultipleReadHttpRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = request.getReader().lines().collect(Collectors.joining());
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        ServletInputStream servletInputStream = new ServletInputStream() {
            public int read() {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {
            }
        };
        return servletInputStream;
    }
}
