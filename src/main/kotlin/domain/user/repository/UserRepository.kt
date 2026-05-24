package com.khrix.domain.user.repository

import com.khrix.domain.core.*
import com.khrix.domain.user.model.User
import com.khrix.domain.valueobject.CPF
import com.khrix.domain.valueobject.Email

interface UserRepository :
    BaseRead<User>,
    BaseUpdate<User>,
    BaseDelete,
    BaseCreateReturn<User>,
    BaseCreate<User> {
    suspend fun getByEmail(email: Email): User?
    suspend fun getByCpf(cpf: CPF): User?
}
