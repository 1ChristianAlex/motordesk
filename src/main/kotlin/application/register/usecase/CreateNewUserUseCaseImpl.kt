package com.khrix.application.register.usecase

import com.khrix.domain.address.repository.AddressRepository
import com.khrix.domain.core.BaseUseCaseImpl
import com.khrix.domain.security.PasswordHasher
import com.khrix.domain.user.model.User
import com.khrix.domain.user.repository.UserRepository
import com.khrix.domain.user.usecase.CreateNewUserUseCase
import com.khrix.domain.user.usecase.CreateNewUserUseCaseCommand

class CreateNewUserUseCaseImpl constructor(
    private val userRepository: UserRepository,
    private val passwordHasher: PasswordHasher,
    private val addressRepository: AddressRepository,
) : CreateNewUserUseCase,
    BaseUseCaseImpl<CreateNewUserUseCaseCommand, User>() {
    override suspend fun internalExecute(command: CreateNewUserUseCaseCommand): User {
        val hashedPass = passwordHasher.hash(command.user.password.value)
        val addressId = addressRepository.create(command.address)

        val userWithHashedPassword = command.user
            .updatePassword(hashedPass, true)
            .updateAddress(addressId)
        
        val userId = userRepository.create(userWithHashedPassword)

        val user = userRepository.read(userId) ?: throw Exception("User not found after creation")

        return user
    }

    override suspend fun useCaseDescription(): String {
        return "Hash password and create new user"
    }
}
