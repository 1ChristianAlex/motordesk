package com.khrix.domain.user.port.repository

import com.khrix.domain.core.BaseCreate
import com.khrix.domain.core.BaseCreateReturn
import com.khrix.domain.core.BaseDelete
import com.khrix.domain.core.BaseRead
import com.khrix.domain.core.BaseUpdate
import com.khrix.domain.user.model.User
import com.khrix.domain.valueobject.company.CNPJ
import com.khrix.domain.valueobject.user.CPF
import com.khrix.domain.valueobject.user.Email

interface UserRepository :
    BaseRead<User>,
    BaseUpdate<User>,
    BaseDelete,
    BaseCreateReturn<User>,
    BaseCreate<User> {
    suspend fun getByEmail(email: Email): User?

    suspend fun getByCpf(cpf: CPF): User?

    suspend fun getByCnpj(cnpf: CNPJ): User?
}
