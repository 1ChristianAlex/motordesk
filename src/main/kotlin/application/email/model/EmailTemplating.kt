package com.khrix.application.email.model

import com.khrix.domain.email.model.ServiceOrderEmailMetadata
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.br
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.meta
import kotlinx.html.p
import kotlinx.html.span
import kotlinx.html.stream.createHTML
import kotlinx.html.strong
import kotlinx.html.style
import kotlinx.html.table
import kotlinx.html.td
import kotlinx.html.title
import kotlinx.html.tr

sealed class EmailTemplating {
    class FromServiceOrderEmailMetadata(
        val data: ServiceOrderEmailMetadata,
    ) : EmailTemplating() {
        fun generateHtmlApprovalRequestTemplate(
            approvalWebhookUrl: String,
            newOrder: Boolean,
        ): String =
            createHTML(prettyPrint = false).html {
                attributes["lang"] = "pt-BR"

                head {
                    meta { charset = "UTF-8" }
                    meta {
                        name = "viewport"
                        content = "width=device-width, initial-scale=1.0"
                    }
                    title("Aprovação da Ordem de Serviço")
                }

                body {
                    style =
                        "margin:0; padding:0; background-color:#f4f4f5; font-family:Arial, Helvetica, sans-serif; color:#18181b;"

                    table {
                        attributes["width"] = "100%"
                        attributes["cellpadding"] = "0"
                        attributes["cellspacing"] = "0"
                        style = "background-color:#f4f4f5; padding:32px 0;"

                        tr {
                            td {
                                attributes["align"] = "center"

                                table {
                                    attributes["width"] = "600"
                                    attributes["cellpadding"] = "0"
                                    attributes["cellspacing"] = "0"
                                    style =
                                        "max-width:600px; background-color:#ffffff; border-radius:8px; overflow:hidden;"

                                    tr {
                                        td {
                                            style = "background-color:#18181b; padding:24px 32px;"
                                            h1 {
                                                style = "margin:0; color:#ffffff; font-size:24px;"
                                                +"MotorDesk"
                                            }
                                            p {
                                                style = "margin:8px 0 0; color:#d4d4d8; font-size:14px;"
                                                +"Solicitação de aprovação"
                                            }
                                        }
                                    }

                                    tr {
                                        td {
                                            style = "padding:32px;"
                                            p {
                                                style = "margin:0 0 16px; font-size:16px;"
                                                +"Olá, "
                                                strong { +"${data.client.firstName} ${data.client.lastName}" }
                                                +"!"
                                            }
                                            p {
                                                style =
                                                    "margin:0 0 20px; font-size:15px; line-height:1.6; color:#52525b;"
                                                if (newOrder) {
                                                    +"Uma nova ordem de serviço foi criada e aguarda sua aprovação. Você pode revisar os detalhes e aprová-la diretamente no botão abaixo."
                                                } else {
                                                    +"Há uma atualização na sua ordem de serviço. Você pode revisar os detalhes e aprová-la diretamente no botão abaixo."
                                                }
                                            }

                                            table {
                                                attributes["width"] = "100%"
                                                attributes["cellpadding"] = "0"
                                                attributes["cellspacing"] = "0"
                                                style = "background-color:#f4f4f5; border-radius:6px;"
                                                tr {
                                                    td {
                                                        style = "padding:20px;"
                                                        p {
                                                            style = "margin:0 0 6px; font-size:12px; color:#71717a;"
                                                            +"STATUS DA ORDEM DE SERVIÇO"
                                                        }
                                                        p {
                                                            style = "margin:0; font-size:20px; font-weight:bold;"
                                                            +data.status.toString()
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    tr {
                                        td {
                                            style = "padding:0 32px 24px;"
                                            h2 {
                                                style = "margin:0 0 12px; font-size:18px;"
                                                +"Solicitação"
                                            }
                                            p {
                                                style =
                                                    "margin:0; padding:16px; background-color:#fafafa; border-left:4px solid #18181b; color:#52525b; line-height:1.6;"
                                                +data.complaint
                                            }
                                        }
                                    }

                                    if (!data.diagnosis.isNullOrBlank()) {
                                        tr {
                                            td {
                                                style = "padding:0 32px 24px;"
                                                h2 {
                                                    style = "margin:0 0 12px; font-size:18px;"
                                                    +"Diagnóstico"
                                                }
                                                p {
                                                    style =
                                                        "margin:0; padding:16px; background-color:#fafafa; color:#52525b; line-height:1.6;"
                                                    +data.diagnosis
                                                }
                                            }
                                        }
                                    }

                                    tr {
                                        td {
                                            style = "padding:0 32px 32px;"
                                            table {
                                                attributes["width"] = "100%"
                                                attributes["cellpadding"] = "0"
                                                attributes["cellspacing"] = "0"
                                                tr {
                                                    td {
                                                        style = "padding-top:16px;"
                                                        a {
                                                            href = approvalWebhookUrl
                                                            target = "_blank"
                                                            style =
                                                                "display:inline-block; background-color:#18181b; color:#ffffff; text-decoration:none; padding:14px 22px; border-radius:6px; font-weight:bold; font-size:15px;"
                                                            +"Revisar e aprovar ordem de serviço"
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    tr {
                                        td {
                                            style = "background-color:#f4f4f5; padding:24px 32px;"
                                            p {
                                                style = "margin:0; font-size:13px; color:#71717a; line-height:1.5;"
                                                +"Se o botão não funcionar, copie e cole o link no navegador."
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        fun generateHtmlStatusUpdateTemplate(): String =
            createHTML(prettyPrint = false).html {
                attributes["lang"] = "pt-BR"
                head {
                    meta { charset = "UTF-8" }
                    meta {
                        name = "viewport"
                        content = "width=device-width, initial-scale=1.0"
                    }
                    title("Atualização da Ordem de Serviço")
                }

                body {
                    style =
                        "margin:0; padding:0; background-color:#f4f4f5; font-family:Arial, Helvetica, sans-serif; color:#18181b;"

                    table {
                        attributes["width"] = "100%"
                        attributes["cellpadding"] = "0"
                        attributes["cellspacing"] = "0"
                        style = "background-color:#f4f4f5; padding:32px 0;"

                        tr {
                            td {
                                attributes["align"] = "center"

                                table {
                                    attributes["width"] = "600"
                                    attributes["cellpadding"] = "0"
                                    attributes["cellspacing"] = "0"
                                    style =
                                        "max-width:600px; background-color:#ffffff; border-radius:8px; overflow:hidden;"

                                    tr {
                                        td {
                                            style = "background-color:#18181b; padding:24px 32px;"
                                            h1 {
                                                style = "margin:0; color:#ffffff; font-size:24px;"
                                                +"MotorDesk"
                                            }
                                            p {
                                                style = "margin:8px 0 0; color:#d4d4d8; font-size:14px;"
                                                +"Atualização da Ordem de Serviço"
                                            }
                                        }
                                    }

                                    tr {
                                        td {
                                            style = "padding:32px;"
                                            p {
                                                style = "margin:0 0 16px; font-size:16px;"
                                                +"Olá, "
                                                strong { +"${data.client.firstName} ${data.client.lastName}" }
                                                +"!"
                                            }
                                            p {
                                                style = "margin:0; font-size:15px; line-height:1.6; color:#52525b;"
                                                +"Sua ordem de serviço recebeu uma atualização. Confira abaixo os detalhes."
                                            }
                                        }
                                    }

                                    tr {
                                        td {
                                            style = "padding:0 32px 24px;"
                                            table {
                                                attributes["width"] = "100%"
                                                attributes["cellpadding"] = "0"
                                                attributes["cellspacing"] = "0"
                                                style = "background-color:#f4f4f5; border-radius:6px;"
                                                tr {
                                                    td {
                                                        style = "padding:20px;"
                                                        p {
                                                            style = "margin:0 0 6px; font-size:12px; color:#71717a;"
                                                            +"STATUS DA ORDEM DE SERVIÇO"
                                                        }
                                                        p {
                                                            style = "margin:0; font-size:20px; font-weight:bold;"
                                                            +data.status.toString()
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    tr {
                                        td {
                                            style = "padding:0 32px 24px;"
                                            h2 {
                                                style = "margin:0 0 16px; font-size:18px;"
                                                +"Veículo"
                                            }
                                            table {
                                                attributes["width"] = "100%"
                                                attributes["cellpadding"] = "0"
                                                attributes["cellspacing"] = "0"
                                                tr {
                                                    td {
                                                        style = "padding:6px 0; color:#71717a;"
                                                        +"Marca"
                                                    }
                                                    td {
                                                        attributes["align"] = "right"
                                                        style = "padding:6px 0; font-weight:bold;"
                                                        +data.vehicle.brand
                                                    }
                                                }
                                                tr {
                                                    td {
                                                        style = "padding:6px 0; color:#71717a;"
                                                        +"Modelo"
                                                    }
                                                    td {
                                                        attributes["align"] = "right"
                                                        style = "padding:6px 0; font-weight:bold;"
                                                        +data.vehicle.model
                                                    }
                                                }
                                                tr {
                                                    td {
                                                        style = "padding:6px 0; color:#71717a;"
                                                        +"Placa"
                                                    }
                                                    td {
                                                        attributes["align"] = "right"
                                                        style = "padding:6px 0; font-weight:bold;"
                                                        +data.vehicle.plate.toString()
                                                    }
                                                }
                                                tr {
                                                    td {
                                                        style = "padding:6px 0; color:#71717a;"
                                                        +"Ano"
                                                    }
                                                    td {
                                                        attributes["align"] = "right"
                                                        style = "padding:6px 0; font-weight:bold;"
                                                        +data.vehicle.year.toString()
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    tr {
                                        td {
                                            style = "padding:0 32px 24px;"
                                            h2 {
                                                style = "margin:0 0 12px; font-size:18px;"
                                                +"Solicitação"
                                            }
                                            p {
                                                style =
                                                    "margin:0; padding:16px; background-color:#fafafa; border-left:4px solid #18181b; color:#52525b; line-height:1.6;"
                                                +data.complaint
                                            }
                                        }
                                    }

                                    data.diagnosis?.let { diagnosis ->
                                        tr {
                                            td {
                                                style = "padding:0 32px 24px;"
                                                h2 {
                                                    style = "margin:0 0 12px; font-size:18px;"
                                                    +"Diagnóstico"
                                                }
                                                p {
                                                    style =
                                                        "margin:0; padding:16px; background-color:#fafafa; color:#52525b; line-height:1.6;"
                                                    +diagnosis
                                                }
                                            }
                                        }
                                    }

                                    tr {
                                        td {
                                            style = "padding:0 32px 24px;"
                                            h2 {
                                                style = "margin:0 0 16px; font-size:18px;"
                                                +"Serviços"
                                            }
                                            table {
                                                attributes["width"] = "100%"
                                                attributes["cellpadding"] = "0"
                                                attributes["cellspacing"] = "0"
                                                data.tasks.forEach { task ->
                                                    tr {
                                                        td {
                                                            style = "padding:10px 0; border-bottom:1px solid #e4e4e7;"
                                                            strong { +task.name }
                                                            task.description?.let {
                                                                br()
                                                                span {
                                                                    style = "font-size:13px; color:#71717a;"
                                                                    +it
                                                                }
                                                            }
                                                        }

                                                        td {
                                                            attributes["align"] = "right"
                                                            style =
                                                                "padding:10px 0; border-bottom:1px solid #e4e4e7; font-weight:bold;"
                                                            +"R$ ${task.price}"
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if (data.inventoryItems.isNotEmpty()) {
                                        tr {
                                            td {
                                                style = "padding:0 32px 24px;"
                                                h2 {
                                                    style = "margin:0 0 16px; font-size:18px;"
                                                    +"Peças e insumos"
                                                }
                                                table {
                                                    attributes["width"] = "100%"
                                                    attributes["cellpadding"] = "0"
                                                    attributes["cellspacing"] = "0"
                                                    data.inventoryItems.forEach { item ->
                                                        tr {
                                                            td {
                                                                style =
                                                                    "padding:10px 0; border-bottom:1px solid #e4e4e7;"
                                                                strong { +item.name }
                                                            }
                                                            td {
                                                                attributes["align"] = "right"
                                                                style =
                                                                    "padding:10px 0; border-bottom:1px solid #e4e4e7;"
                                                                +"${item.quantity}x"
                                                            }
                                                            td {
                                                                attributes["align"] = "right"
                                                                style =
                                                                    "padding:10px 0; border-bottom:1px solid #e4e4e7; font-weight:bold;"
                                                                +"R$ ${item.total}"
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    tr {
                                        td {
                                            style = "padding:0 32px 32px;"
                                            table {
                                                attributes["width"] = "100%"
                                                attributes["cellpadding"] = "0"
                                                attributes["cellspacing"] = "0"
                                                style = "border-top:2px solid #18181b;"
                                                tr {
                                                    td {
                                                        style = "padding-top:16px; font-size:18px; font-weight:bold;"
                                                        +"Total"
                                                    }
                                                    td {
                                                        attributes["align"] = "right"
                                                        style = "padding-top:16px; font-size:22px; font-weight:bold;"
                                                        +"R$ ${data.totalAmount}"
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    tr {
                                        td {
                                            style = "background-color:#f4f4f5; padding:24px 32px;"
                                            p {
                                                style = "margin:0; font-size:13px; color:#71717a; line-height:1.5;"
                                                +"Este é um e-mail automático enviado pelo MotorDesk. Caso tenha dúvidas, entre em contato com a oficina."
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
    }
}
