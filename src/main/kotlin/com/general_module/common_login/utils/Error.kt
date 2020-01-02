package com.general_module.common_login.utils

import org.springframework.http.HttpStatus


class ParsingException(var statusCode:HttpStatus =HttpStatus.OK, var reason:String ="") : Exception()