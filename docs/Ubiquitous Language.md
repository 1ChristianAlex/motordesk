## Ubiquitous Language

- Service order: `ServiceOrder` is a domain entity that represents an order for a service. It contains information about
  the task being ordered, the customer placing the order, and the status of the order.
- Task: `Task` is a domain entity that represents a specific task that needs to be performed as part of a service order.
  It contains information about the task description, the assigned worker, and the status of the task.
- Service Part: `ServicePart` is a domain entity that represents a specific part or component of a task. It contains
  information about the part name, description, and any associated costs.
- Client: `Client` is a domain entity that represents a person or organization that places an order for a service. It
  contains information about the customer's name, contact details, and any relevant preferences or requirements.
- Operator: `Operator` is a domain entity that represents a person or system responsible for managing and coordinating
  the service orders. It contains information about the operator's name, contact details, and any relevant permissions
  or roles.
- Service Order Status: `ServiceOrderStatus` is a domain entity that represents the current status of a service order.
  It contains information about the status name, description, and any relevant actions or transitions associated with
  the status.
- Manager: `Manager` is a domain entity that represents a person responsible for overseeing and managing the service
  orders. It contains information about the manager's name, contact details, and any relevant responsibilities or
  authority. Could be a real manager or a system administrator.
