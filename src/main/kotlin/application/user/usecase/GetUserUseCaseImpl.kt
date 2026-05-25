package com.khrix.application.user.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.user.model.User
import com.khrix.domain.user.repository.UserRepository
import com.khrix.domain.user.usecase.GetUserUseCase
import com.khrix.domain.user.usecase.UserNotFoundException

class GetUserUseCaseImpl(
    private val userRepository: UserRepository,
) : GetUserUseCase, BaseUseCaseImpl<Int, User>() {
    override suspend fun internalExecute(command: Int): User {
        return userRepository.read(
            command
        ) ?: throw UserNotFoundException(command)
    }

    override suspend fun useCaseDescription(): String {
        return "Retriever user info searching by id"
    }
}
