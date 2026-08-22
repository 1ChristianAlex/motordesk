package com.khrix.domain

import com.khrix.domain.company.port.usecase.CreateNewCompanyUseCaseCommand
import com.khrix.domain.company.port.usecase.CreateNewCompanyUseCaseError
import com.khrix.domain.company.port.usecase.SearchCompanyByCnpjUseCaseCommand
import com.khrix.domain.inventory.port.usecase.DecrementItemInventoryCommand
import com.khrix.domain.serviceorder.model.ServiceOrderStatus
import com.khrix.domain.serviceorder.port.usecase.CreateServiceOrderCommand
import com.khrix.domain.serviceorder.port.usecase.DeleteServiceOrderCommand
import com.khrix.domain.serviceorder.port.usecase.GetClientServiceOrdersByCodeCommand
import com.khrix.domain.serviceorder.port.usecase.UpdateServiceOrderCommand
import com.khrix.domain.serviceorder.port.usecase.UpdateServiceOrderTaskCommand
import com.khrix.domain.serviceorder.task.model.TaskProgressStatus
import com.khrix.domain.user.model.Role
import com.khrix.domain.user.port.usecase.CreateNewUserUseCaseCommand
import com.khrix.domain.user.port.usecase.InvalidCredentialsException
import com.khrix.domain.user.port.usecase.UserNotFoundException
import com.khrix.domain.user.port.usecase.VerifyIsUserDataAvailableUseCaseCommand
import com.khrix.domain.valueobject.company.CNPJ
import com.khrix.domain.valueobject.user.CPF
import com.khrix.domain.valueobject.user.CompanyName
import com.khrix.domain.valueobject.user.Email
import com.khrix.testutils.sampleAddress
import com.khrix.testutils.sampleCompany
import com.khrix.testutils.sampleUser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class CreateNewCompanyUseCaseCommandTest {
    @Test
    fun `retains company input`() {
        val command = CreateNewCompanyUseCaseCommand(CompanyName("Company"), CNPJ("12345678000195"), userId = 0)
        assertEquals("Company", command.name.value)
    }
}

class CreateNewCompanyUseCaseErrorTest {
    @Test
    fun `identifies duplicate company`() {
        val error = CreateNewCompanyUseCaseError.CompanyAlreadyExists(CNPJ("12345678000195"))
        assertIs<CreateNewCompanyUseCaseError>(error)
        assertEquals("Company 12345678000195 already exists", error.message)
    }
}

class SearchCompanyByCnpjUseCaseCommandTest {
    @Test
    fun `retains search CNPJ`() = assertEquals("12345678000195", SearchCompanyByCnpjUseCaseCommand(CNPJ("12345678000195")).cnpj.value)
}

class DecrementItemInventoryCommandTest {
    @Test
    fun `retains item and quantity`() = assertEquals(2, DecrementItemInventoryCommand(1, 2).quantity)
}

class CreateServiceOrderCommandTest {
    @Test
    fun `retains service order references`() {
        val command = CreateServiceOrderCommand(1, 2, 3, "Long complaint", tasksIds = listOf(4))
        assertEquals(listOf(4), command.tasksIds)
    }
}

class DeleteServiceOrderCommandTest {
    @Test
    fun `retains cancellation reason`() = assertEquals("Cancelled", DeleteServiceOrderCommand("Cancelled", 1).complaint)
}

class GetClientServiceOrdersByCodeCommandTest {
    @Test
    fun `retains ownership constraint`() = assertEquals(2, GetClientServiceOrdersByCodeCommand("#code", 2).userId)
}

class UpdateServiceOrderCommandTest {
    @Test
    fun `retains operator authorization context`() {
        val command =
            UpdateServiceOrderCommand("#code", null, status = ServiceOrderStatus.CREATED, operatorRole = Role.MANAGER)
        assertEquals(Role.MANAGER, command.operatorRole)
    }
}

class UpdateServiceOrderTaskCommandTest {
    @Test
    fun `retains progress update`() {
        val command = UpdateServiceOrderTaskCommand(TaskProgressStatus.COMPLETE, 1, 2)
        assertEquals(TaskProgressStatus.COMPLETE, command.status)
    }
}

class CreateNewUserUseCaseCommandTest {
    @Test
    fun `retains aggregate input`() {
        val command = CreateNewUserUseCaseCommand(sampleUser(), sampleAddress(), sampleCompany())
        assertEquals(command.user.companyId, null)
        assertEquals(1, command.address.id)
    }
}

class VerifyIsUserDataAvailableUseCaseCommandTest {
    @Test
    fun `retains unique user data`() {
        val command = VerifyIsUserDataAvailableUseCaseCommand(Email("user@example.com"), CPF("11144477735"))
        assertEquals("user@example.com", command.email.value)
    }
}

class UserNotFoundExceptionTest {
    @Test
    fun `contains missing user id`() = assertEquals("No user found with id 7", UserNotFoundException(7).message)
}

class InvalidCredentialsExceptionTest {
    @Test
    fun `does not disclose credential details`() =
        assertEquals("User not found with provided credentials", InvalidCredentialsException().message)
}
