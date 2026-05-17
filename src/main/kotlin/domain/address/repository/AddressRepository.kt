package com.khrix.domain.address.repository

import com.khrix.domain.address.model.Address
import com.khrix.domain.core.BaseDelete
import com.khrix.domain.core.BaseRead
import com.khrix.domain.core.BaseUpdate

interface AddressRepository :
    BaseRead<Address>,
    BaseUpdate<Address>,
    BaseDelete {
    suspend fun createRead(
        data: Address,
        userId: Int
    ): Address
}
