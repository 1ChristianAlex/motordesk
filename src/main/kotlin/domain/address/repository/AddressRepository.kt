package com.khrix.domain.address.repository

import com.khrix.domain.address.model.Address
import com.khrix.domain.core.*

interface AddressRepository :
    BaseRead<Address>,
    BaseUpdate<Address>,
    BaseCreate<Address>,
    BaseDelete,
    BaseCreateReturn<Address>
