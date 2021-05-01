package com.pad.shapeless.dispatcher.exception

import javax.naming.AuthenticationException

class OAuth2AuthenticationProcessingException(msg: String) : AuthenticationException(msg)