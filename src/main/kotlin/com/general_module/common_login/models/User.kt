package com.general_module.common_login.models

import com.general_module.common_login.utils.ParsingException
import com.general_module.common_login.utils.encryptPassword
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.springframework.http.HttpStatus
import java.io.Serializable
import java.util.regex.Pattern
import javax.persistence.*


@Entity
@Table(name = "USER")
class User : Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true)
    @SerializedName("id")
    var id: Long = 0

    @Column(name = "user_name", unique = true)
    @SerializedName("userName")
    var userName: String = ""

    @Column(name = "email")
    @SerializedName("email")
    var email: String = ""

    @Column(name = "mobile", length = 10)
    @SerializedName("mobile")
    var mobile: String = ""

    @Column(name = "password", length = 40)  // Password will be hashed with pbkdf2
    var password: String = ""


    companion object {

        val emailPattern = Pattern.compile("^(.+)@(.+)\$")

        @Throws(ParsingException::class)
        fun parseUser(data: HashMap<String, String>): User {
            val uname = data["userName"] ?: ""

            if (uname.isEmpty()) {
                throw ParsingException(HttpStatus.BAD_REQUEST, "username is mandatory.")
            }


            val password = data["password"] ?: ""
            if (password.length !in 6..20) {
                throw ParsingException(HttpStatus.BAD_REQUEST, "Password should have minimum 6 digits and maximum 20 digits.")
            }

            val email = data["email"] ?: ""
            if (email.isNotEmpty() && !emailPattern.matcher(email).matches()) {
                throw ParsingException(HttpStatus.BAD_REQUEST, "Invalid email.")
            }

            val mobile = data["mobile"] ?: ""
            if (mobile.isNotEmpty()) {
                if (mobile.length != 10) {
                    throw ParsingException(HttpStatus.BAD_REQUEST, "Invalid mobile number. It should have 10 digits.")
                }
            }

            return User().apply {
                userName = uname
                this.password = encryptPassword(password)
                this.email = email
                this.mobile = mobile
            }
        }

        fun toHashmap(user: User): HashMap<String, String> {
            return hashMapOf("id" to user.id.toString(),
                    "userName" to user.userName,
                    "email" to user.email,
                    "mobile" to user.mobile
            )
        }
    
    }

}