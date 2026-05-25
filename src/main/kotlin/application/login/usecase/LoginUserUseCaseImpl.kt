package com.khrix.application.login.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.security.PasswordHasher
import com.khrix.domain.user.model.LoginTypes
import com.khrix.domain.user.model.User
import com.khrix.domain.user.repository.UserRepository
import com.khrix.domain.user.usecase.InvalidCredentialsException
import com.khrix.domain.user.usecase.LoginUserUseCase

class LoginUserUseCaseImpl(
    private val userRepository: UserRepository,
    private val passwordHasher: PasswordHasher
) : LoginUserUseCase,
    BaseUseCaseImpl<LoginTypes, User>() {

    override suspend fun internalExecute(command: LoginTypes): User {
        val user = when (command) {
            is LoginTypes.EmailCredentials -> handleLoginWithEmail(command)
            is LoginTypes.CpfCredentials -> handleLoginWithCpf(command)
            is LoginTypes.CNPJCredentials -> handleLoginWithCnpj(command)
        }

        if (user == null) {
            throw InvalidCredentialsException()
        }

        val isValid = passwordHasher.verify(command.password.value, user.password.value)

        if (!isValid) {
            throw InvalidCredentialsException()
        }

        return user
    }

    private suspend fun handleLoginWithCnpj(command: LoginTypes.CNPJCredentials): User? {
        return userRepository.getByCnpj(command.cnpj)
    }

    private suspend fun handleLoginWithEmail(command: LoginTypes.EmailCredentials): User? {
        return userRepository.getByEmail(command.email)
    }

    private suspend fun handleLoginWithCpf(command: LoginTypes.CpfCredentials): User? {
        return userRepository.getByCpf(command.cpf)
    }

    override suspend fun useCaseDescription(): String {
        return "Hash password and create new user"
    }
}
