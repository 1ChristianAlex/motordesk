package com.khrix.application.login.usecase

import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.user.model.LoginTypes
import com.khrix.domain.user.model.User
import com.khrix.domain.user.port.repository.UserRepository
import com.khrix.domain.user.port.security.SecurityHasher
import com.khrix.domain.user.port.usecase.InvalidCredentialsException
import com.khrix.domain.user.port.usecase.LoginUserUseCase

class LoginUserUseCaseImpl(
    private val userRepository: UserRepository,
    private val securityHasher: SecurityHasher,
) : BaseUseCaseImpl<LoginTypes, User>(),
    LoginUserUseCase {
    override suspend fun internalExecute(command: LoginTypes): User {
        val user =
            when (command) {
                is LoginTypes.EmailCredentials -> handleLoginWithEmail(command)
                is LoginTypes.CpfCredentials -> handleLoginWithCpf(command)
                is LoginTypes.CNPJCredentials -> handleLoginWithCnpj(command)
            }

        if (user == null) {
            throw InvalidCredentialsException()
        }

        val isValid = securityHasher.verify(command.password.value, user.password.value)

        if (!isValid) {
            throw InvalidCredentialsException()
        }

        return user
    }

    private suspend fun handleLoginWithCnpj(command: LoginTypes.CNPJCredentials): User? = userRepository.getByCnpj(command.cnpj)

    private suspend fun handleLoginWithEmail(command: LoginTypes.EmailCredentials): User? = userRepository.getByEmail(command.email)

    private suspend fun handleLoginWithCpf(command: LoginTypes.CpfCredentials): User? = userRepository.getByCpf(command.cpf)

    override suspend fun useCaseDescription(): String = "Find user by credentials and compare password hash"
}
