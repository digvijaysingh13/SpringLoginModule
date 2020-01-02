package com.general_module.common_login.models

import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="authentication")
class Authentication {

    @Id
    @Column(name = "id", unique = true)
    var id :Long = 0

    @Column(name="token", length = 50)
    var token :String = ""

    @UpdateTimestamp
    var updateTime:LocalDateTime?=null


    companion object{
        const val DeltaSession:Long = 30*24*60*60 //seconds in 30 day
    }

}