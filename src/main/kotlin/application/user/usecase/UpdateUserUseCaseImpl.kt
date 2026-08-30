package com.khrix.application.user.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.user.model.User
import com.khrix.domain.user.port.repository.UserRepository
import com.khrix.domain.user.port.security.SecurityHasher
import com.khrix.domain.user.port.usecase.UpdateUserUseCase
import com.khrix.domain.user.port.usecase.VerifyIsUserDataAvailableUseCase
import com.khrix.domain.user.port.usecase.VerifyIsUserDataAvailableUseCaseCommand

class UpdateUserUseCaseImpl(
    private val userRepository: UserRepository,
    private val securityHasher: SecurityHasher,
    private val verifyIsUserDataAvailableUseCase: VerifyIsUserDataAvailableUseCase,
) : BaseUseCaseImpl<User, Unit>(),
    UpdateUserUseCase {
    override suspend fun internalExecute(command: User) {
        val oldUser = userRepository.read(command.id)

        if (oldUser?.email != command.email || oldUser.cpf != command.cpf) {
            verifyIsUserDataAvailableUseCase
                .execute(
                    VerifyIsUserDataAvailableUseCaseCommand(
                        email = command.email,
                        cpf = command.cpf,
                    ),
                ).getOrThrow()
        }

        val passwordIsArgon = securityHasher.isHashedPassword(command.password.value)
        val password =
            if (passwordIsArgon) {
                command.password.value
            } else {
                securityHasher.hash(command.password.value)
            }

        val updateUser = command.updatePassword(password)

        userRepository.update(command.id, updateUser)
    }

    override suspend fun useCaseDescription(): String = "Update user data with new info"
}
