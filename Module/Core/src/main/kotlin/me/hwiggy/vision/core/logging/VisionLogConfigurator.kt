package me.hwiggy.vision.core.logging

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.Configurator
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.spi.ContextAwareBase
import org.slf4j.Logger

class VisionLogConfigurator : ContextAwareBase(), Configurator {
    override fun configure(context: LoggerContext): Configurator.ExecutionStatus {
        val jansi = System.getProperty("withJansi")?.toBoolean() ?: false
        val coloredLayoutEncoder = PatternLayoutEncoder().apply {
            this.pattern = "[%d{yyyy-MM-dd HH:mm:ss.SSS}] %highlight([%level]) %logger - %msg%n"
            this.context = context
            start()
        }

        val basicLayoutEncoder = PatternLayoutEncoder().apply {
            this.pattern = "[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%level] %logger - %msg%n"
            this.context = context
            start()
        }

        val file = RollingFileAppender<ILoggingEvent>().also { fileAppender ->
            fileAppender.rollingPolicy = TimeBasedRollingPolicy<ILoggingEvent>().apply {
                setParent(fileAppender)
                this.fileNamePattern = "logs/Vision-%d{yyyy-MM-dd}.log.gz"
                this.context = context
                start()
            }
            fileAppender.encoder = basicLayoutEncoder
            fileAppender.context = context
            fileAppender.start()
        }

        val console = ConsoleAppender<ILoggingEvent>().apply {
            this.name = "Console"
            this.isWithJansi = jansi
            this.context = context
            this.encoder = coloredLayoutEncoder
            start()
        }

        context.getLogger(Logger.ROOT_LOGGER_NAME).apply {
            addAppender(file)
            addAppender(console)
        }
        context.getLogger("STDOUT").apply {
            addAppender(file)
        }
        return Configurator.ExecutionStatus.DO_NOT_INVOKE_NEXT_IF_ANY
    }
}