package alde.commons.logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * Use this class to listen to global logger events.
 * 
 * Inspired by Florent Moisson and ProgramCreek
 * 
 * https://www.programcreek.com/java-api-examples/?api=ch.qos.logback.core.AppenderBase
 */
public class LoggerListener {
	
	private static List<Consumer<ILoggingEvent>> loggerReceiverList = new ArrayList<>();

	static {
		org.slf4j.Logger rootLogger = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
		ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) rootLogger;
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		AppenderBase<ILoggingEvent> appender = new AppenderBase<ILoggingEvent>() {
			@Override
			protected void append(ILoggingEvent eventObject) {
				for (Consumer<ILoggingEvent> c : loggerReceiverList) {
					c.accept(eventObject);
				}
			}
		};
		appender.setContext(context);
		appender.start();
		log.addAppender(appender);
		log.info("Applet log appender attached");
	}

	public static void addListener(Consumer<ILoggingEvent> l) {
		loggerReceiverList.add(l);
	}

}
