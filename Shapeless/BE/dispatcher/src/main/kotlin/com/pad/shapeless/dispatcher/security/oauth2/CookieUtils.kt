package com.pad.shapeless.dispatcher.security.oauth2

import org.springframework.util.SerializationUtils
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

object CookieUtils {
    fun getCookie(request: HttpServletRequest, name: String): Cookie? =
        request.cookies?.find { it.name == name }


    fun addCookie(response: HttpServletResponse, name: String, value: String, maxAge: Int) {
        val cookie = Cookie(name, value)
        cookie.path = "/"
        cookie.isHttpOnly = true
        cookie.maxAge = maxAge
        response.addCookie(cookie)
    }

    fun deleteCookie(request: HttpServletRequest, response: HttpServletResponse, name: String) =
        request.cookies?.filter { it.name == name }?.forEach { cookie ->
            cookie.value = ""
            cookie.path = "/"
            cookie.maxAge = 0
            response.addCookie(cookie)
        }


    fun serialize(obj: Any?): String = Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(obj))

    fun <T> deserialize(cookie: Cookie, cls: Class<T>): T =
        cls.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.value)))

}