package com.khrix.application.serviceorder

import com.khrix.domain.history.model.HistoryChanges
import com.khrix.domain.history.model.RegisterChange
import com.khrix.domain.serviceorder.ServiceOrderDiffResolver
import com.khrix.domain.serviceorder.model.ServiceOrder
import kotlinx.serialization.json.Json
import kotlin.time.Clock

inline fun <reified T> simpleSerialize(data: T): String = Json.encodeToString(data)

class ServiceOrderDiffResolverImpl : ServiceOrderDiffResolver {
    override fun shallowDiff(
        compareA: ServiceOrder,
        compareB: ServiceOrder,
    ): HistoryChanges {
        val changes =
            buildList<RegisterChange<Comparable<String>>> {
                if (compareA.status != compareB.status) {
                    add(RegisterChange(ServiceOrder::status.name, compareA.status.name))
                }

                if (compareA.complaint != compareB.complaint) {
                    add(RegisterChange(ServiceOrder::complaint.name, compareA.complaint))
                }

                if (compareA.diagnosis != compareB.diagnosis) {
                    add(RegisterChange(ServiceOrder::diagnosis.name, compareA.diagnosis ?: ""))
                }

                if (compareA.operator != compareB.operator) {
                    add(RegisterChange(ServiceOrder::operator.name, compareA.operator.id.toString()))
                }

                if (compareA.tasks != compareB.tasks) {
                    add(RegisterChange(ServiceOrder::tasks.name, simpleSerialize(compareA.tasks.map { it.id })))
                }

                if (compareA.inventoryItems != compareB.inventoryItems) {
                    add(
                        RegisterChange(
                            ServiceOrder::inventoryItems.name,
                            simpleSerialize(compareA.inventoryItems.map { it.id }),
                        ),
                    )
                }
            }

        return HistoryChanges(compareA.id, changedAt = Clock.System.now(), changes)
    }
}
