package com.general_module.common_login.controller

import com.general_module.common_login.models.User
import com.general_module.common_login.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping(path= ["/gen"])
class MainController {

    @Autowired
    private lateinit var mUserRepository: UserRepository

    @PostMapping(path=["/addUser"], consumes = ["application/json"], produces = ["application/json"])
    @ResponseBody fun addUser(@RequestBody request:Any):  String{
        val u = User().apply {

            email="ramesh@gmail.com"
            mobile="9898989898"
        }
        mUserRepository.save(u)
        return "Added"
    }


    fun login():String{

        return ""
    }

    @PostMapping(path=["/register"], consumes = ["application/json"], produces = ["application/json"])
    @ResponseBody fun register(@RequestBody request:HashMap<String, String>):HashMap<String, String>{
        for((k,v) in request){
            println("${k}=${v}")
        }
        return hashMapOf()
    }


}