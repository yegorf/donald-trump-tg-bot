package org.example

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import java.time.LocalDate
import java.time.temporal.ChronoUnit


fun daysUntilWarEnd(): Long {
    val targetDate = LocalDate.of(2025, 5, 2)
    val today = LocalDate.now()
    return ChronoUnit.DAYS.between(today, targetDate)
}

fun generateProgressBar(barWidth: Int = 10): String {
    val days = daysUntilWarEnd().toInt()
    val percentage = 100 - days
    val safePercentage = percentage.coerceIn(0, 100)
    val filledCount = (barWidth * safePercentage) / 100
    val filled = "▰".repeat(filledCount)
    val empty = "▱".repeat(barWidth - filledCount)

    return "$days left until the war end\n $filled$empty $safePercentage%"
}

fun main() {
    bot {
        token = "7892383164:AAEaGEfKiUw0PbJP1oJV7LWRKRlpsHD3lWs"
        dispatch {
            command("start") {
                bot.sendMessage(
                    chatId = ChatId.fromId(message.chat.id),
                    text = "Hello, I provide everyday war end progress update"
                )
            }

            command("progress") {
                bot.sendMessage(
                    chatId = ChatId.fromId(message.chat.id),
                    text = generateProgressBar()
                )
            }
        }
    }.startPolling()
}