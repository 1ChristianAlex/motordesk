package com.khrix.application.serviceorder.task

import com.khrix.domain.history.model.HistoryChanges
import com.khrix.domain.history.model.RegisterChange
import com.khrix.domain.serviceorder.task.TaskDiffResolver
import com.khrix.domain.serviceorder.task.model.Task
import kotlinx.serialization.json.Json
import kotlin.time.Clock

class TaskDiffResolverImpl : TaskDiffResolver {
    private inline fun <reified T> simpleSerialize(data: T): String = Json.encodeToString(data)

    override fun shallowDiff(
        compareA: Task,
        compareB: Task,
    ): HistoryChanges {
        val changes =
            buildList<RegisterChange<Comparable<String>>> {
                if (compareA.status != compareB.status) {
                    add(RegisterChange(Task::status.name, simpleSerialize(compareA.status)))
                }

                if (compareA.estimatedMinutes != compareB.estimatedMinutes) {
                    add(RegisterChange(Task::estimatedMinutes.name, simpleSerialize(compareA.estimatedMinutes)))
                }

                if (compareA.price != compareB.price) {
                    add(RegisterChange(Task::price.name, simpleSerialize(compareA.price)))
                }

                if (compareA.category != compareB.category) {
                    add(RegisterChange(Task::category.name, simpleSerialize(compareA.category)))
                }

                if (compareA.category != compareB.category) {
                    add(RegisterChange(Task::category.name, simpleSerialize(compareA.category)))
                }
            }

        return HistoryChanges(compareA.id, changedAt = Clock.System.now(), changes)
    }
}
