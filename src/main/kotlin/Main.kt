package org.example

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*

val subscribedChats = mutableSetOf<Long>()

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

    return "$days days left until the war end\n $filled$empty $safePercentage%"
}

fun getNext10amDate(): Date = Calendar.getInstance().apply {
    set(Calendar.HOUR_OF_DAY, 10)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)

    if (time.before(Date())) {
        add(Calendar.DAY_OF_MONTH, 1)
    }
}.time

fun main() {
    val bot = bot {
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

            command("subscribe") {
                subscribedChats.add(message.chat.id)
                bot.sendMessage(
                    chatId = ChatId.fromId(message.chat.id),
                    text = "You have been subscribed to progress updates"
                )
            }

            command("unsubscribe") {
                subscribedChats.add(message.chat.id)
                bot.sendMessage(
                    chatId = ChatId.fromId(message.chat.id),
                    text = "You have been unsubscribed from progress updates"
                )
            }
        }
    }
    bot.startPolling()

    Timer().scheduleAtFixedRate(
        object : TimerTask() {
            override fun run() {
                subscribedChats.forEach { chatId ->
                    bot.sendMessage(
                        chatId = ChatId.fromId(chatId),
                        text = generateProgressBar()
                    )
                }
            }
        },
        getNext10amDate(),
        24 * 60 * 60 * 1000L
    )
}