package com.general_module.common_login.models

import javax.persistence.*


@Entity
@Table(name="USER")
class User {

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    @Column(name="Id", unique = true)
    var id:Int=0

    @Column(name="UserName", unique = true)
    var userName:String = ""

    @Column(name="Email")
    var email:String =""

    @Column(name="Mobile", length = 10)
    var mobile:String = ""

    @Column(name="Password", length = 20)  // Password will be hashed with pbkdf2
    var password:String =""


}