package com.general_module.common_login.repository

import com.general_module.common_login.models.User
import org.springframework.data.repository.CrudRepository

interface UserRepository  : CrudRepository<User, Int>{
}