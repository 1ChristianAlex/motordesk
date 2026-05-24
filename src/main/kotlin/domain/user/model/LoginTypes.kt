package com.khrix.domain.user.model

import com.khrix.domain.valueobject.CPF
import com.khrix.domain.valueobject.Email
import com.khrix.domain.valueobject.Password

sealed class LoginTypes(val password: Password) {
    class EmailCredentials(val email: Email, password: Password) : LoginTypes(password)
    class CpfCredentials(val cpf: CPF, password: Password) : LoginTypes(password)

    companion object {
        fun create(userName: String, password: String): LoginTypes {
            val passwordData = Password(password)
            val loginCredentials = runCatching {
                EmailCredentials(Email(userName), passwordData)
            }.getOrElse {
                runCatching {
                    CpfCredentials(
                        CPF(userName),
                        passwordData
                    )
                }.getOrElse { throw IllegalArgumentException("Invalid email or CPF format") }
            }

            return loginCredentials
        }
    }
}