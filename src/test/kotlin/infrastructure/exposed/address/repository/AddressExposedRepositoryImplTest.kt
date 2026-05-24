package com.khrix.infrastructure.exposed.address.repository

import com.khrix.domain.address.model.Address
import com.khrix.domain.core.getCurrentUtcDateTime
import com.khrix.infrastructure.exposed.connections.MemoryConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class AddressExposedRepositoryImplTest {

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `create read update delete address lifecycle`() = runTest {
        val database = MemoryConnection(true).getConnection()
        val repo = AddressExposedRepositoryImpl(database)

        val now = getCurrentUtcDateTime()

        val input = Address(
            id = null,
            street = "Main St",
            number = "123",
            complement = null,
            neighborhood = "Downtown",
            city = "City",
            state = "State",
            country = "Country",
            zipCode = "12345",
            createdAt = now,
            updatedAt = now,
        )

        // create
        val created = repo.createRead(input)
        assertNotNull(created.id)
        assertEquals(input.street, created.street)
        assertEquals(input.number, created.number)

        // read
        val read = repo.read(created.id!!)
        assertEquals(created.id, read.id)
        assertEquals(created.city, read.city)

        // update
        val updatedData = read.copy(city = "NewCity", street = "New St")
        repo.update(read.id!!, updatedData)

        val afterUpdate = repo.read(read.id)
        assertEquals("NewCity", afterUpdate.city)
        assertEquals("New St", afterUpdate.street)

        // delete
        repo.delete(read.id)

        // reading after delete should throw
        assertFailsWith<Exception> {
            repo.read(read.id)
        }
    }
}

