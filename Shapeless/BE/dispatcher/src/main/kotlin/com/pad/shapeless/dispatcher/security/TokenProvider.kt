package com.pad.shapeless.dispatcher.security

import com.pad.shapeless.dispatcher.config.AppProperties
import io.jsonwebtoken.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenProvider(
    private val appProperties: AppProperties,
    private val logger: Logger = LoggerFactory.getLogger(TokenProvider::class.java)
) {

    fun createToken(authentication: Authentication): String =
        Jwts.builder()
            .setSubject((authentication.principal as UserPrincipal).getId().toString())
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + appProperties.auth.tokenExpirationMilliseconds))
            .signWith(SignatureAlgorithm.HS512, appProperties.auth.tokenSecret)
            .compact()


    fun getUserIdFromToken(token: String): UUID =
        UUID.fromString(
            Jwts.parser()
                .setSigningKey(appProperties.auth.tokenSecret)
                .parseClaimsJws(token)
                .body.subject
        )


    fun validateToken(authToken: String): Boolean {
        try {
            Jwts.parser().setSigningKey(appProperties.auth.tokenSecret).parseClaimsJws(authToken)
            return true
        } catch (ex: SignatureException) {
            logger.error("Invalid JWT signature")
        } catch (ex: MalformedJwtException) {
            logger.error("Invalid JWT token")
        } catch (ex: ExpiredJwtException) {
            logger.error("Expired JWT token")
        } catch (ex: UnsupportedJwtException) {
            logger.error("Unsupported JWT token")
        } catch (ex: IllegalArgumentException) {
            logger.error("JWT claims string is empty.")
        }
        return false
    }

}