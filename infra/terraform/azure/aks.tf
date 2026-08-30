# resource "azurerm_kubernetes_cluster" "aks" {
#   name                = "aks-${var.environment}-${var.project_name}"
#   location            = azurerm_resource_group.rg.location
#   resource_group_name = azurerm_resource_group.rg.name
#   dns_prefix          = "aks-${var.environment}-${var.project_name}"
#
#   default_node_pool {
#     name           = "system"
#     node_count     = var.aks_node_count
#     vm_size        = var.aks_vm_size
#     vnet_subnet_id = azurerm_subnet.aks.id
#   }
#
#   node_provisioning_profile {
#     mode = "Manual"
#   }
#
#   identity {
#     type = "SystemAssigned"
#   }
#
#   network_profile {
#     network_plugin    = "azure"
#     load_balancer_sku = "standard"
#   }
#
#   tags = var.tags
# }
#
# resource "azurerm_role_assignment" "aks_acr_pull" {
#   principal_id                     = azurerm_kubernetes_cluster.aks.kubelet_identity[0].object_id
#   role_definition_name             = "AcrPull"
#   scope                            = azurerm_container_registry.acr.id
#   skip_service_principal_aad_check = true
# }
