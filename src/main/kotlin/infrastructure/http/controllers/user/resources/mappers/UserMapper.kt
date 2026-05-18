package com.khrix.infrastructure.http.controllers.user.resources.mappers

import com.khrix.domain.user.model.User
import com.khrix.infrastructure.http.controllers.user.resources.dto.UserOutputDto

fun User.toOutputDto(): UserOutputDto {
    return UserOutputDto(
        id = this.id,
        firstName = this.firstName.value,
        lastName = this.lastName.value,
        email = this.email.value,
        phone = this.phone.value,
        cpf = this.cpf.value,
        isActive = this.isActive,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
    )
}