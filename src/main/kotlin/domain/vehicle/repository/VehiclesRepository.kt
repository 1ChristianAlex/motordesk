package com.khrix.domain.vehicle.repository

import com.khrix.domain.core.BaseCreateReturn
import com.khrix.domain.core.BaseDelete
import com.khrix.domain.core.BaseRead
import com.khrix.domain.core.BaseUpdate
import com.khrix.domain.vehicle.model.Vehicle

interface VehiclesRepository :
    BaseRead<Vehicle>,
    BaseUpdate<Vehicle>,
    BaseDelete,
    BaseCreateReturn<Vehicle>