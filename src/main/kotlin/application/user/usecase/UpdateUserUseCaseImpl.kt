package com.khrix.application.user.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.security.PasswordHasher
import com.khrix.domain.user.model.User
import com.khrix.domain.user.repository.UserRepository
import com.khrix.domain.user.usecase.UpdateUserUseCase

class UpdateUserUseCaseImpl(
    private val userRepository: UserRepository,
    private val passwordHasher: PasswordHasher
) : UpdateUserUseCase,
    BaseUseCaseImpl<User, Unit>() {
    override suspend fun internalExecute(command: User): Unit {
        val passwordIsArgon = passwordHasher.isHashedPassword(command.password.value)
        val password = if (passwordIsArgon) {
            command.password.value
        } else {
            passwordHasher.hash(command.password.value)
        }

        val updateUser = command.updatePassword(password, true)

        userRepository.update(command.id, updateUser)

    }


    override suspend fun useCaseDescription(): String {
        return "Update user data with new info"
    }
}
