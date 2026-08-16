package com.khrix.application.email.model

import com.khrix.domain.core.toCurrency
import com.khrix.domain.email.model.ServiceOrderEmailMetadata

fun ServiceOrderEmailMetadata.generateHtmlApprovalRequestTemplate(
    approvalWebhookUrl: String,
    newOrder: Boolean,
): String =
    htmlDocument("Aprovação da Ordem de Serviço") {
        bodyStyle()
        table(
            width = "100%",
            cellPadding = "0",
            cellSpacing = "0",
            style = "background-color:#f4f4f5; padding:32px 0;",
        ) {
            tr {
                td(align = "center") {
                    table(
                        width = "600",
                        cellPadding = "0",
                        cellSpacing = "0",
                        style = "max-width:600px; background-color:#ffffff; border-radius:8px; overflow:hidden;",
                    ) {
                        tr {
                            td(style = "background-color:#18181b; padding:24px 32px;") {
                                h1(style = "margin:0; color:#ffffff; font-size:24px;", "MotorDesk")
                                p(
                                    style = "margin:8px 0 0; color:#d4d4d8; font-size:14px;",
                                    "Solicitação de aprovação",
                                )
                            }
                        }

                        tr {
                            td(style = "padding:32px;") {
                                p(
                                    style = "margin:0 0 16px; font-size:16px;",
                                    "Olá, ${strongText("${client.firstName} ${client.lastName}")}!",
                                )
                                p(
                                    style = "margin:0 0 20px; font-size:15px; line-height:1.6; color:#52525b;",
                                    if (newOrder) {
                                        "Uma nova ordem de serviço foi criada e aguarda sua aprovação. Você pode revisar os detalhes e aprová-la diretamente no botão abaixo."
                                    } else {
                                        "Há uma atualização na sua ordem de serviço. Você pode revisar os detalhes e aprová-la diretamente no botão abaixo."
                                    },
                                )
                            }
                        }

                        generateServiceOrderDetailsTemplate(this@table)

                        tr {
                            td(style = "padding:0 32px 32px;") {
                                table(width = "100%", cellPadding = "0", cellSpacing = "0") {
                                    tr {
                                        td(style = "padding-top:16px;") {
                                            a(
                                                href = approvalWebhookUrl,
                                                target = "_blank",
                                                style = "display:inline-block; background-color:#18181b; color:#ffffff; text-decoration:none; padding:14px 22px; border-radius:6px; font-weight:bold; font-size:15px;",
                                                text = "Revisar e aprovar ordem de serviço",
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        tr {
                            td(style = "background-color:#f4f4f5; padding:24px 32px;") {
                                p(
                                    style = "margin:0; font-size:13px; color:#71717a; line-height:1.5;",
                                    "Se o botão não funcionar, copie e cole o link no navegador.",
                                )
                            }
                        }
                    }
                }
            }
        }
    }

fun ServiceOrderEmailMetadata.generateHtmlStatusUpdateTemplate(): String =
    htmlDocument("Atualização da Ordem de Serviço") {
        bodyStyle()
        table(
            width = "100%",
            cellPadding = "0",
            cellSpacing = "0",
            style = "background-color:#f4f4f5; padding:32px 0;",
        ) {
            tr {
                td(align = "center") {
                    table(
                        width = "600",
                        cellPadding = "0",
                        cellSpacing = "0",
                        style = "max-width:600px; background-color:#ffffff; border-radius:8px; overflow:hidden;",
                    ) {
                        tr {
                            td(style = "background-color:#18181b; padding:24px 32px;") {
                                h1(style = "margin:0; color:#ffffff; font-size:24px;", "MotorDesk")
                                p(
                                    style = "margin:8px 0 0; color:#d4d4d8; font-size:14px;",
                                    "Atualização da Ordem de Serviço",
                                )
                            }
                        }

                        tr {
                            td(style = "padding:32px;") {
                                p(
                                    style = "margin:0 0 16px; font-size:16px;",
                                    "Olá, ${strongText("${client.firstName} ${client.lastName}")}!",
                                )
                                p(
                                    style = "margin:0; font-size:15px; line-height:1.6; color:#52525b;",
                                    "Sua ordem de serviço recebeu uma atualização. Confira abaixo os detalhes.",
                                )
                            }
                        }

                        generateServiceOrderDetailsTemplate(this@table)

                        tr {
                            td(style = "background-color:#f4f4f5; padding:24px 32px;") {
                                p(
                                    style = "margin:0; font-size:13px; color:#71717a; line-height:1.5;",
                                    "Este é um e-mail automático enviado pelo MotorDesk. Caso tenha dúvidas, entre em contato com a oficina.",
                                )
                            }
                        }
                    }
                }
            }
        }
    }

private fun ServiceOrderEmailMetadata.generateServiceOrderDetailsTemplate(builder: StringBuilder) {
    builder.tr {
        td(style = "padding:0 32px 24px;") {
            table(
                width = "100%",
                cellPadding = "0",
                cellSpacing = "0",
                style = "background-color:#f4f4f5; border-radius:6px;",
            ) {
                tr {
                    td(style = "padding:20px;") {
                        p(
                            style = "margin:0 0 6px; font-size:12px; color:#71717a;",
                            "STATUS DA ORDEM DE SERVIÇO",
                        )
                        p(
                            style = "margin:0; font-size:20px; font-weight:bold;",
                            status.toString(),
                        )
                    }
                }
            }
        }
    }
    builder.tr {
        td(style = "padding:0 32px 24px;") {
            h2(style = "margin:0 0 16px; font-size:18px;", "Veículo")
            table(width = "100%", cellPadding = "0", cellSpacing = "0") {
                row("Marca", vehicle.brand)
                row("Modelo", vehicle.model)
                row("Placa", vehicle.plate.value)
                row("Ano", vehicle.year.value.toString())
            }
        }
    }

    builder.tr {
        td(style = "padding:0 32px 24px;") {
            h2(style = "margin:0 0 12px; font-size:18px;", "Solicitação")
            p(
                style = "margin:0; padding:16px; background-color:#fafafa; border-left:4px solid #18181b; color:#52525b; line-height:1.6;",
                complaint,
            )
        }
    }

    diagnosis?.takeIf { it.isNotBlank() }?.let { diagnosis ->
        builder.tr {
            td(style = "padding:0 32px 24px;") {
                h2(style = "margin:0 0 12px; font-size:18px;", "Diagnóstico")
                p(
                    style = "margin:0; padding:16px; background-color:#fafafa; color:#52525b; line-height:1.6;",
                    diagnosis,
                )
            }
        }
    }

    builder.tr {
        td(style = "padding:0 32px 24px;") {
            h2(style = "margin:0 0 16px; font-size:18px;", "Serviços")
            table(width = "100%", cellPadding = "0", cellSpacing = "0") {
                tasks.forEach { task ->
                    tr {
                        td(style = "padding:10px 0; border-bottom:1px solid #e4e4e7;") {
                            strongText(task.name)
                            task.description?.let {
                                br()
                                span(style = "font-size:13px; color:#71717a;", it)
                            }
                        }
                        td(
                            align = "right",
                            style = "padding:10px 0; border-bottom:1px solid #e4e4e7; font-weight:bold;",
                            "${task.price.toCurrency()}",
                        )
                    }
                }
            }
        }
    }

    if (inventoryItems.isNotEmpty()) {
        builder.tr {
            td(style = "padding:0 32px 24px;") {
                h2(style = "margin:0 0 16px; font-size:18px;", "Peças e insumos")
                table(width = "100%", cellPadding = "0", cellSpacing = "0") {
                    inventoryItems.forEach { item ->
                        tr {
                            td(style = "padding:10px 0; border-bottom:1px solid #e4e4e7;") {
                                strongText(item.name)
                            }
                            td(
                                align = "right",
                                style = "padding:10px 0; border-bottom:1px solid #e4e4e7;",
                                "${item.quantity}x",
                            )
                            td(
                                align = "right",
                                style = "padding:10px 0; border-bottom:1px solid #e4e4e7; font-weight:bold;",
                                "${item.total.toCurrency()}",
                            )
                        }
                    }
                }
            }
        }
    }

    builder.tr {
        td(style = "padding:0 32px 32px;") {
            table(
                width = "100%",
                cellPadding = "0",
                cellSpacing = "0",
                style = "border-top:2px solid #18181b;",
            ) {
                tr {
                    td(
                        style = "padding-top:16px; font-size:18px; font-weight:bold;",
                        text = "Total",
                    )
                    td(
                        align = "right",
                        style = "padding-top:16px; font-size:22px; font-weight:bold;",
                        text = totalAmount.toCurrency(),
                    )
                }
            }
        }
    }
}

private fun htmlDocument(
    title: String,
    body: StringBuilder.() -> Unit,
): String =
    buildString {
        append("<!DOCTYPE html>")
        append("<html lang=\"pt-BR\">")
        append("<head>")
        append("<meta charset=\"UTF-8\">")
        append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
        append("<title>").append(escapeHtml(title)).append("</title>")
        append("</head>")
        append("<body style=\"margin:0; padding:0; background-color:#f4f4f5; font-family:Arial, Helvetica, sans-serif; color:#18181b;\">")
        body()
        append("</body>")
        append("</html>")
    }

private fun StringBuilder.bodyStyle() = Unit

private fun StringBuilder.table(
    width: String? = null,
    cellPadding: String? = null,
    cellSpacing: String? = null,
    style: String? = null,
    body: StringBuilder.() -> Unit,
) {
    append("<table")
    width?.let { append(" width=\"").append(escapeHtml(it)).append("\"") }
    cellPadding?.let { append(" cellpadding=\"").append(escapeHtml(it)).append("\"") }
    cellSpacing?.let { append(" cellspacing=\"").append(escapeHtml(it)).append("\"") }
    style?.let { append(" style=\"").append(escapeHtml(it)).append("\"") }
    append(">")
    body()
    append("</table>")
}

private fun StringBuilder.tr(body: StringBuilder.() -> Unit) {
    append("<tr>")
    body()
    append("</tr>")
}

private fun StringBuilder.td(
    align: String? = null,
    style: String? = null,
    text: String? = null,
    body: (StringBuilder.() -> Unit)? = null,
) {
    append("<td")
    align?.let { append(" align=\"").append(escapeHtml(it)).append("\"") }
    style?.let { append(" style=\"").append(escapeHtml(it)).append("\"") }
    append(">")
    if (text != null) {
        append(escapeHtml(text))
    }
    body?.invoke(this)
    append("</td>")
}

private fun StringBuilder.h1(
    style: String? = null,
    text: String,
) {
    append("<h1")
    style?.let { append(" style=\"").append(escapeHtml(it)).append("\"") }
    append(">").append(escapeHtml(text)).append("</h1>")
}

private fun StringBuilder.h2(
    style: String? = null,
    text: String,
) {
    append("<h2")
    style?.let { append(" style=\"").append(escapeHtml(it)).append("\"") }
    append(">").append(escapeHtml(text)).append("</h2>")
}

private fun StringBuilder.p(
    style: String? = null,
    text: String,
) {
    append("<p")
    style?.let { append(" style=\"").append(escapeHtml(it)).append("\"") }
    append(">").append(text).append("</p>")
}

private fun StringBuilder.a(
    href: String,
    target: String? = null,
    style: String? = null,
    text: String,
) {
    append("<a href=\"").append(escapeHtml(href)).append("\"")
    target?.let { append(" target=\"").append(escapeHtml(it)).append("\"") }
    style?.let { append(" style=\"").append(escapeHtml(it)).append("\"") }
    append(">").append(escapeHtml(text)).append("</a>")
}

private fun StringBuilder.span(
    style: String? = null,
    text: String,
) {
    append("<span")
    style?.let { append(" style=\"").append(escapeHtml(it)).append("\"") }
    append(">").append(escapeHtml(text)).append("</span>")
}

private fun StringBuilder.strongText(text: String) {
    append("<strong>").append(escapeHtml(text)).append("</strong>")
}

private fun StringBuilder.br() {
    append("<br>")
}

private fun StringBuilder.row(
    left: String,
    right: String,
) {
    tr {
        td(style = "padding:6px 0; color:#71717a;", text = left)
        td(align = "right", style = "padding:6px 0; font-weight:bold;", text = right)
    }
}

private fun escapeHtml(text: String): String =
    buildString {
        text.forEach { ch ->
            when (ch) {
                '&' -> append("&amp;")
                '<' -> append("&lt;")
                '>' -> append("&gt;")
                '"' -> append("&quot;")
                '\'' -> append("&#39;")
                else -> append(ch)
            }
        }
    }
