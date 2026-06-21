package com.khrix.application.user.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.user.model.User
import com.khrix.domain.user.repository.UserRepository
import com.khrix.domain.user.security.PasswordHasher
import com.khrix.domain.user.usecase.UpdateUserUseCase
import com.khrix.domain.user.usecase.VerifyIsUserDataAvailableUseCase
import com.khrix.domain.user.usecase.VerifyIsUserDataAvailableUseCaseCommand

class UpdateUserUseCaseImpl(
    private val userRepository: UserRepository,
    private val passwordHasher: PasswordHasher,
    private val verifyIsUserDataAvailableUseCase: VerifyIsUserDataAvailableUseCase
) : UpdateUserUseCase,
    BaseUseCaseImpl<User, Unit>() {
    override suspend fun internalExecute(command: User) {
        verifyIsUserDataAvailableUseCase.execute(
            VerifyIsUserDataAvailableUseCaseCommand(
                email = command.email,
                cpf = command.cpf
            )
        ).getOrThrow()

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
