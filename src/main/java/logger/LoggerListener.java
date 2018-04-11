package logger;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * LogBack listener
 * @author Florent Moisson
 */
public class LoggerListener extends AppenderBase<ILoggingEvent> {

	private List<LoggerReceiver> loggerReceiverList = new ArrayList<>();

	public LoggerListener() {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		setContext(lc);
		start();

		lc.getLogger("ROOT").addAppender(this);
	}

	public void addListener(LoggerReceiver l) {
		this.loggerReceiverList.add(l);
	}

	@Override
	public void append(ILoggingEvent event) {
		for (LoggerReceiver l : loggerReceiverList) {
			l.receive(event);
		}
	}

}

interface LoggerReceiver {
	void receive(ILoggingEvent event);
}
