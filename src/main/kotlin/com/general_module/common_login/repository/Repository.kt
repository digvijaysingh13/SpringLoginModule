package com.general_module.common_login.repository

import com.general_module.common_login.models.Authentication
import com.general_module.common_login.models.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface UserRepository  : CrudRepository<User, Int>{

    @Query("SELECT u FROM User u WHERE u.userName = :userName")
    fun getUser(@Param("userName") userName:String):List<User>

    @Query("SELECT u FROM User u WHERE u.id = :id")
    fun getUserById(@Param("id") id:Long):List<User>

}

interface AuthRepository : CrudRepository<Authentication, Int>{

    @Query("SELECT a FROM Authentication a WHERE a.id = :id")
    fun getAuth(@Param("id") id:Long):List<Authentication>

}