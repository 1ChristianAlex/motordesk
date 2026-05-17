package com.khrix.domain.address.repository

import com.khrix.domain.address.core.BaseDelete
import com.khrix.domain.address.core.BaseRead
import com.khrix.domain.address.core.BaseUpdate
import com.khrix.domain.address.model.Address

interface AddressRepository :
    BaseRead<Address>,
    BaseUpdate<Address>,
    BaseDelete {
    suspend fun createRead(
        data: Address,
        userId: Int
    ): Address
}
