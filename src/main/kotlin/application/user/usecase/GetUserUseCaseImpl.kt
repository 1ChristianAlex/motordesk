package com.khrix.application.user.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.user.model.User
import com.khrix.domain.user.port.repository.UserRepository
import com.khrix.domain.user.port.usecase.GetUserUseCase
import com.khrix.domain.user.port.usecase.UserNotFoundException

class GetUserUseCaseImpl(
    private val userRepository: UserRepository,
) : BaseUseCaseImpl<Int, User>(),
    GetUserUseCase {
    override suspend fun internalExecute(command: Int): User =
        userRepository.read(
            command,
        ) ?: throw UserNotFoundException(command)

    override suspend fun useCaseDescription(): String = "Retriever user info searching by id"
}
