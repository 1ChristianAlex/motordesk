package com.khrix.domain.user.usecase

import com.khrix.domain.address.model.Address
import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.user.model.User

data class CreateNewUserUseCaseCommand(
    val user: User,
    val address: Address,
    val cnpj: String?,
    val companyName: String?,
)

interface CreateNewUserUseCase : BaseUseCase<CreateNewUserUseCaseCommand, User>