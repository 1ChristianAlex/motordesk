package com.khrix.domain.user.usecase

import com.khrix.domain.address.model.Address
import com.khrix.domain.company.model.Company
import com.khrix.domain.core.BaseUseCase
import com.khrix.domain.user.model.User

data class CreateNewUserUseCaseCommand(
    val user: User,
    val address: Address,
    val company: Company?,
)

interface CreateNewUserUseCase : BaseUseCase<CreateNewUserUseCaseCommand, User>