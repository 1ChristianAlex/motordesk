package com.khrix.application.notification

import com.khrix.domain.email.model.EmailQueueItem
import com.khrix.domain.email.model.ServiceOrderEmailMetadata
import com.khrix.domain.valueobject.user.Email

data class EmailMessageBody(
    val toRecipients: List<Email>,
    val subject: String,
    val body: String,
    val senderAddress: Email = Email("donotreply@motordesk.azurecomm.net"),
)

fun ServiceOrderEmailMetadata.toHtml(): String =
    buildString {
        append(
            """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Atualização da Ordem de Serviço</title>
            </head>
            <body style="margin:0; padding:0; background-color:#f4f4f5; font-family:Arial, Helvetica, sans-serif; color:#18181b;">
                <table width="100%" cellpadding="0" cellspacing="0" style="background-color:#f4f4f5; padding:32px 0;">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="0" cellspacing="0"
                                   style="max-width:600px; background-color:#ffffff; border-radius:8px; overflow:hidden;">

                                <!-- Header -->
                                <tr>
                                    <td style="background-color:#18181b; padding:24px 32px;">
                                        <h1 style="margin:0; color:#ffffff; font-size:24px;">
                                            MotorDesk
                                        </h1>
                                        <p style="margin:8px 0 0; color:#d4d4d8; font-size:14px;">
                                            Atualização da Ordem de Serviço
                                        </p>
                                    </td>
                                </tr>

                                <!-- Greeting -->
                                <tr>
                                    <td style="padding:32px;">
                                        <p style="margin:0 0 16px; font-size:16px;">
                                            Olá, <strong>${client.firstName} ${client.lastName}</strong>!
                                        </p>

                                        <p style="margin:0; font-size:15px; line-height:1.6; color:#52525b;">
                                            Sua ordem de serviço recebeu uma atualização.
                                            Confira abaixo os detalhes.
                                        </p>
                                    </td>
                                </tr>

                                <!-- Status -->
                                <tr>
                                    <td style="padding:0 32px 24px;">
                                        <table width="100%" cellpadding="0" cellspacing="0"
                                               style="background-color:#f4f4f5; border-radius:6px;">
                                            <tr>
                                                <td style="padding:20px;">
                                                    <p style="margin:0 0 6px; font-size:12px; color:#71717a;">
                                                        STATUS DA ORDEM DE SERVIÇO
                                                    </p>
                                                    <p style="margin:0; font-size:20px; font-weight:bold;">
                                                        $status
                                                    </p>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>

                                <!-- Vehicle -->
                                <tr>
                                    <td style="padding:0 32px 24px;">
                                        <h2 style="margin:0 0 16px; font-size:18px;">
                                            Veículo
                                        </h2>

                                        <table width="100%" cellpadding="0" cellspacing="0">
                                            <tr>
                                                <td style="padding:6px 0; color:#71717a;">
                                                    Marca
                                                </td>
                                                <td align="right" style="padding:6px 0; font-weight:bold;">
                                                    ${vehicle.brand}
                                                </td>
                                            </tr>

                                            <tr>
                                                <td style="padding:6px 0; color:#71717a;">
                                                    Modelo
                                                </td>
                                                <td align="right" style="padding:6px 0; font-weight:bold;">
                                                    ${vehicle.model}
                                                </td>
                                            </tr>

                                            <tr>
                                                <td style="padding:6px 0; color:#71717a;">
                                                    Placa
                                                </td>
                                                <td align="right" style="padding:6px 0; font-weight:bold;">
                                                    ${vehicle.plate}
                                                </td>
                                            </tr>

                                            <tr>
                                                <td style="padding:6px 0; color:#71717a;">
                                                    Ano
                                                </td>
                                                <td align="right" style="padding:6px 0; font-weight:bold;">
                                                    ${vehicle.year}
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>

                                <!-- Complaint -->
                                <tr>
                                    <td style="padding:0 32px 24px;">
                                        <h2 style="margin:0 0 12px; font-size:18px;">
                                            Solicitação
                                        </h2>

                                        <p style="margin:0; padding:16px; background-color:#fafafa; border-left:4px solid #18181b; color:#52525b; line-height:1.6;">
                                            $complaint
                                        </p>
                                    </td>
                                </tr>

                                ${
                diagnosis?.let {
                    """
                                    <!-- Diagnosis -->
                                    <tr>
                                        <td style="padding:0 32px 24px;">
                                            <h2 style="margin:0 0 12px; font-size:18px;">
                                                Diagnóstico
                                            </h2>

                                            <p style="margin:0; padding:16px; background-color:#fafafa; color:#52525b; line-height:1.6;">
                                                $it
                                            </p>
                                        </td>
                                    </tr>
                                    """
                } ?: ""
            }

                                <!-- Services -->
                                <tr>
                                    <td style="padding:0 32px 24px;">
                                        <h2 style="margin:0 0 16px; font-size:18px;">
                                            Serviços
                                        </h2>

                                        <table width="100%" cellpadding="0" cellspacing="0">
                                            ${
                tasks.joinToString("") { task ->
                    """
                                                <tr>
                                                    <td style="padding:10px 0; border-bottom:1px solid #e4e4e7;">
                                                        <strong>${task.name}</strong>
                                                        ${
                        task.description?.let {
                            "<br><span style=\"font-size:13px; color:#71717a;\">$it</span>"
                        } ?: ""
                    }
                                                    </td>

                                                    <td align="right"
                                                        style="padding:10px 0; border-bottom:1px solid #e4e4e7; font-weight:bold;">
                                                        R$ ${task.price}
                                                    </td>
                                                </tr>
                                                """
                }
            }
                                        </table>
                                    </td>
                                </tr>

                                ${
                if (inventoryItems.isNotEmpty()) {
                    """
                                    <!-- Parts -->
                                    <tr>
                                        <td style="padding:0 32px 24px;">
                                            <h2 style="margin:0 0 16px; font-size:18px;">
                                                Peças e insumos
                                            </h2>

                                            <table width="100%" cellpadding="0" cellspacing="0">
                                                ${
                        inventoryItems.joinToString("") { item ->
                            """
                                                        <tr>
                                                            <td style="padding:10px 0; border-bottom:1px solid #e4e4e7;">
                                                                <strong>${item.name}</strong>
                                                            </td>

                                                            <td align="right"
                                                                style="padding:10px 0; border-bottom:1px solid #e4e4e7;">
                                                                ${item.quantity}x
                                                            </td>

                                                            <td align="right"
                                                                style="padding:10px 0; border-bottom:1px solid #e4e4e7; font-weight:bold;">
                                                                R$ ${item.total}
                                                            </td>
                                                        </tr>
                                                        """
                        }
                    }
                                            </table>
                                        </td>
                                    </tr>
                                    """
                } else {
                    ""
                }
            }

                                <!-- Total -->
                                <tr>
                                    <td style="padding:0 32px 32px;">
                                        <table width="100%" cellpadding="0" cellspacing="0"
                                               style="border-top:2px solid #18181b;">
                                            <tr>
                                                <td style="padding-top:16px; font-size:18px; font-weight:bold;">
                                                    Total
                                                </td>

                                                <td align="right"
                                                    style="padding-top:16px; font-size:22px; font-weight:bold;">
                                                    R$ $totalAmount
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>

                                <!-- Footer -->
                                <tr>
                                    <td style="background-color:#f4f4f5; padding:24px 32px;">
                                        <p style="margin:0; font-size:13px; color:#71717a; line-height:1.5;">
                                            Este é um e-mail automático enviado pelo MotorDesk.
                                            Caso tenha dúvidas, entre em contato com a oficina.
                                        </p>
                                    </td>
                                </tr>

                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """.trimIndent(),
        )
    }

fun EmailQueueItem.toEmailMessageBody(): EmailMessageBody =
    EmailMessageBody(
        toRecipients = listOf(Email(recipient)),
        subject = subject,
        body = metadata.toHtml(),
    )

interface EmailSender {
    suspend fun send(message: EmailMessageBody)
}
