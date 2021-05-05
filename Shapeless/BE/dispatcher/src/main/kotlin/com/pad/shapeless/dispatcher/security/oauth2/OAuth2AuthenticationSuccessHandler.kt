package com.pad.shapeless.dispatcher.security.oauth2

import com.pad.shapeless.dispatcher.config.AppProperties
import com.pad.shapeless.dispatcher.exception.BadRequestException
import com.pad.shapeless.dispatcher.security.TokenProvider
import com.pad.shapeless.dispatcher.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.Companion.REDIRECT_URI_PARAM_COOKIE_NAME
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuth2AuthenticationSuccessHandler @Autowired constructor(
    private val tokenProvider: TokenProvider,
    private val appProperties: AppProperties,
    private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository
) : SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val targetUrl = determineTargetUrl(request, response, authentication)
        if (response.isCommitted) {
            logger.debug("Response has already been committed. Unable to redirect to $targetUrl")
            return
        }
        clearAuthenticationAttributes(request, response)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }

    override fun determineTargetUrl(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ): String {
        val redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)?.value
        if (redirectUri != null && !isAuthorizedRedirectUri(redirectUri)) {
            throw BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication")
        }
        val targetUrl = redirectUri ?: defaultTargetUrl
        val token: String = tokenProvider.createToken(authentication)
        return UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("token", token)
            .build().toUriString()
    }

    protected fun clearAuthenticationAttributes(request: HttpServletRequest, response: HttpServletResponse) {
        super.clearAuthenticationAttributes(request)
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
    }

    private fun isAuthorizedRedirectUri(uri: String): Boolean =
        URI.create(uri).let { clientRedirectUri ->
            appProperties.oauth2.authorizedRedirectUris.any { authorizedRedirectUri ->
                val authorizedURI = URI.create(authorizedRedirectUri)
                authorizedURI.host.equals(clientRedirectUri.host, ignoreCase = true)
                        && authorizedURI.port == clientRedirectUri.port
            }
        }

}
