package com.khrix.application.user.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.user.repository.UserRepository
import com.khrix.domain.user.usecase.VerifyIsUserDataAvailableUseCase
import com.khrix.domain.user.usecase.VerifyIsUserDataAvailableUseCaseCommand
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class VerifyIsUserDataAvailableUseCaseImpl(
    private val userRepository: UserRepository,
) : VerifyIsUserDataAvailableUseCase,
    BaseUseCaseImpl<VerifyIsUserDataAvailableUseCaseCommand, Unit>() {
    override suspend fun internalExecute(command: VerifyIsUserDataAvailableUseCaseCommand) {
        return coroutineScope {
            val userWithEmail = async { userRepository.getByEmail(command.email) }
            val userWithCpf = async { userRepository.getByCpf(command.cpf) }

            if (userWithEmail.await() != null) {
                throw Exception("Email already in use")
            }

            if (userWithCpf.await() != null) {
                throw Exception("CPF already in use")
            }
        }
    }

    override suspend fun useCaseDescription(): String {
        return "Check if email and cpf are available for registration"
    }
}