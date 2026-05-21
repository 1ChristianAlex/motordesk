package com.khrix.application.register.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.user.repository.UserRepository
import com.khrix.domain.user.usecase.VerifyIsEmailAvailableUseCase
import com.khrix.domain.valueobject.Email

class VerifyIsEmailAvailableUseCaseImpl constructor(
    private val userRepository: UserRepository,
) : VerifyIsEmailAvailableUseCase,
    BaseUseCaseImpl<Email, Boolean>() {
    override suspend fun internalExecute(command: Email): Boolean {
        val findUser = userRepository.getByEmail(command)

        return findUser == null
    }

    override suspend fun useCaseDescription(): String {
        return "Check if email is available"
    }
}
