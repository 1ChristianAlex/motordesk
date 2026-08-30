package com.khrix.infrastructure.http.controllers.user.resources.mappers

import com.khrix.domain.user.model.User
import com.khrix.infrastructure.http.controllers.user.resources.dto.UserOutputDto

fun User.toOutputDto(mask: Boolean = false): UserOutputDto {
    val cpfValue = this.cpf.run { if (mask) this.mask() else this.value }
    val emailValue = this.email.run { if (mask) this.mask() else this.value }
    val phoneValue = this.phone.run { if (mask) this.mask() else this.value }

    return UserOutputDto(
        id = this.id,
        firstName = this.firstName.value,
        lastName = this.lastName.value,
        email = emailValue,
        phone = phoneValue,
        cpf = cpfValue,
        isActive = this.isActive,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        companyId = this.companyId,
        addressId = this.addressId
    )
}