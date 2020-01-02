package com.general_module.common_login.utils

import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import kotlin.collections.HashMap

private val salt = byteArrayOf(5,89,56,5,56,5,76,16)

fun encryptPassword(password:String):String{
    val spec = PBEKeySpec(password.toCharArray(), salt, 64000, 128)

    val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")

    val enc = factory.generateSecret(spec).encoded
    return enc.byteArr2HexStr()
}


fun getUUID():String= UUID.randomUUID().toString()

