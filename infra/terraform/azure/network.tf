# Create a virtual network within the resource group
resource "azurerm_virtual_network" "network" {
  name                = "network-${var.environment}-${var.project_name}"
  resource_group_name = azurerm_resource_group.rg.name
  location            = var.infra_location
  tags                = var.tags
  address_space       = ["10.20.0.0/16"]
}
resource "azurerm_subnet" "aks" {
  name                 = "aks-subnet-${var.environment}-${var.project_name}"
  resource_group_name  = azurerm_resource_group.rg.name
  virtual_network_name = azurerm_virtual_network.network.name
  address_prefixes     = ["10.20.0.0/22"]
}


resource "azurerm_subnet" "subnet" {
  count                = var.environment == var.environments.prod ? 1 : 0
  name                 = "${var.environment}-${var.project_name}-subnet-db"
  resource_group_name  = azurerm_resource_group.rg.name
  virtual_network_name = azurerm_virtual_network.network.name
  address_prefixes     = ["10.20.0.0/24"]

  delegation {
    name = "fs"
    service_delegation {
      name = "Microsoft.DBforPostgreSQL/flexibleServers"
      actions = [
        "Microsoft.Network/virtualNetworks/subnets/join/action",
      ]
    }
  }

  depends_on = [azurerm_resource_group.rg]
}

resource "azurerm_private_dns_zone" "dns_zone" {
  count               = var.environment == var.environments.prod ? 1 : 0
  name                = "${var.environment}-${var.project_name}.postgres.database.azure.com"
  resource_group_name = azurerm_resource_group.rg.name
  tags                = var.tags
}

resource "azurerm_private_dns_zone_virtual_network_link" "dns_zone_virtual_network_link" {
  count               = var.environment == var.environments.prod ? 1 : 0
  name                = "${var.environment}-${var.project_name}VnetZone.com"
  private_dns_zone_id = azurerm_private_dns_zone.dns_zone[0].id
  tags                = var.tags
  virtual_network_id  = azurerm_virtual_network.network.id
}
