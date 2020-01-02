package com.general_module.common_login.controller

import com.general_module.common_login.models.Authentication
import com.general_module.common_login.models.User
import com.general_module.common_login.repository.AuthRepository
import com.general_module.common_login.repository.UserRepository
import com.general_module.common_login.utils.ParsingException
import com.general_module.common_login.utils.encryptPassword
import com.general_module.common_login.utils.getUUID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.time.ZoneId


@Controller
@RequestMapping(path = ["/gen"])
class MainController {

    @Autowired
    private lateinit var mUserRepository: UserRepository

    @Autowired
    private lateinit var mAuthRepository: AuthRepository

    @PostMapping(path = ["/login"], consumes = ["application/json"], produces = ["application/json"])
    @ResponseBody
    fun login(@RequestBody request: HashMap<String, String>): ResponseEntity<HashMap<String, String>> {
        val id = request["id"]?.toLong() ?: 0
        if (id > 0) {
            val ul = mUserRepository.getUserById(id)
            if (ul.isNotEmpty()) {
                val pw = request["password"] ?: ""
                if (pw.length !in 6..20) {
                    return ResponseEntity(hashMapOf("error" to "Invalid password length."), HttpStatus.BAD_REQUEST)
                }

                val encPwd = encryptPassword(pw)
                if (encPwd != ul[0].password) {
                    return ResponseEntity(hashMapOf("error" to "Invalid password."), HttpStatus.BAD_REQUEST)
                }

                return try {
                    val token = doAuth(ul[0], mAuthRepository)
                    val resp = User.toHashmap(ul[0])
                    resp["token"] = token
                    resp["message"] = "${ul[0].userName} has been logged in successfully."
                    ResponseEntity.ok(resp)
                } catch (ex: ParsingException) {
                    ResponseEntity(hashMapOf("error" to ex.reason), ex.statusCode)
                }
            } else {
                return ResponseEntity(hashMapOf("error" to "No such user found. Register first."), HttpStatus.BAD_REQUEST)
            }
        } else {
            return ResponseEntity(hashMapOf("error" to "Invalid user id."), HttpStatus.BAD_REQUEST)
        }
    }


    @PostMapping(path = ["/register"], consumes = ["application/json"], produces = ["application/json"])
    @ResponseBody
    fun register(@RequestBody request: HashMap<String, String>): ResponseEntity<HashMap<String, String>> {
        try {
            val user = User.parseUser(request)

            val li: List<User> = mUserRepository.getUser(user.userName)

            if (li.isNotEmpty()) {
                return ResponseEntity(hashMapOf("error" to "userName all ready exist."), HttpStatus.BAD_REQUEST)
            }

            mUserRepository.save(user)

            val token = doAuth(user, mAuthRepository)
            val resp = User.toHashmap(user)
            resp["message"] = "${user.userName} has been successfully register."
            resp["token"] = token
            return ResponseEntity.ok(resp)

        } catch (ex: ParsingException) {
            return ResponseEntity(hashMapOf("error" to ex.reason), ex.statusCode)
        }

    }

}


@Throws(ParsingException::class)
private fun doAuth(user: User, authRepo: AuthRepository): String {
    val authList = authRepo.getAuth(user.id)
    var rauth: Authentication
    if (authList.isEmpty()) {
        val auth = Authentication().apply {
            id = user.id
            token = getUUID()
        }
        rauth = auth
        authRepo.save(auth)
    } else {
        rauth = authList[0]
        val ut1 = rauth.updateTime?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli() ?: 0
        val utn = System.currentTimeMillis()

        if (((utn - ut1)/1000).toLong() > Authentication.DeltaSession) {
            throw ParsingException(HttpStatus.UNAUTHORIZED, "Session Expired. Login again.")
        }

        authRepo.save(rauth)
    }
    return rauth.token
}


