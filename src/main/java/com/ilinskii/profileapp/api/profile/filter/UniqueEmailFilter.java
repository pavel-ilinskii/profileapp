package com.ilinskii.profileapp.api.profile.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.ilinskii.profileapp.api.ErrorMsg;
import com.ilinskii.profileapp.api.profile.ProfileCreateRequest;
import com.ilinskii.profileapp.service.ProfileService;
import com.ilinskii.profileapp.util.MultipleReadHttpRequestWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class UniqueEmailFilter implements Filter {

    private final ProfileService profileService;

    private final ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = HttpServletRequest.class.cast(request);
        HttpServletResponse httpResponse = HttpServletResponse.class.cast(response);

        MultipleReadHttpRequestWrapper wrappedRequest = new MultipleReadHttpRequestWrapper(httpRequest);

        try {
            ProfileCreateRequest profileCreateRequest = objectMapper.readValue(wrappedRequest.getInputStream(), ProfileCreateRequest.class);

            String email = profileCreateRequest.getEmail();

            if (null != email && profileService.existByEmail(email)) {
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                objectMapper.writeValue(httpResponse.getOutputStream(), new ErrorMsg("Email already in use"));
            }
        } catch (MismatchedInputException e) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(httpResponse.getOutputStream(), new ErrorMsg("Could not read request"));
        }

        filterChain.doFilter(wrappedRequest, httpResponse);
    }
}
