package com.pad.shapeless.dispatcher.security.oauth2

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuth2AuthenticationFailureHandler @Autowired constructor(
    private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository
) : SimpleUrlAuthenticationFailureHandler() {

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {

        val targetUrl = UriComponentsBuilder.fromUriString(
            CookieUtils.getCookie(
                request,
                HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME
            )?.value ?: ("/")
        )
            .queryParam("error", URLEncoder.encode(exception.message, "UTF-8"))
            .build().toUriString()

        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }
}