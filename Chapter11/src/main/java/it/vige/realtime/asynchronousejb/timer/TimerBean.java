package it.vige.realtime.asynchronousejb.timer;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerHandle;
import javax.ejb.TimerService;

@Singleton
public class TimerBean {

	@Resource
	private TimerService timerService;
	
	private boolean timeoutDone;

	private static final Logger logger = getLogger(TimerBean.class.getName());

	@Timeout
	public void timeout(Timer timer) {
		logger.info("TimerEJB: timeout occurred");
		timeoutDone = true;
	}

	public void programmaticTimeout() {
		logger.info("TimerEJB: programmatic timeout occurred");
		timeoutDone = false;
		long duration = 60;
		Timer timer = timerService.createSingleActionTimer(duration, new TimerConfig());
		timer.getInfo();
		try {
			timer.getSchedule();
		} catch (IllegalStateException e) {
			logger.log(SEVERE, "it is not a scheduler", e);
		}
		TimerHandle timerHandle = timer.getHandle();
		timerHandle.getTimer();
		timer.isCalendarTimer();
		timer.isPersistent();
		timer.getTimeRemaining();
	}

	public void timerConfig() {
		logger.info("TimerEJB: timer configuration");
		timeoutDone = false;
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy 'at' HH:mm");
		Date date = null;
		try {
			date = formatter.parse("05/01/2010 at 12:05");
			TimerConfig timerConfig = new TimerConfig();
			timerConfig.setInfo("my configuration");
			timerConfig.setPersistent(false);
			Timer timer = timerService.createSingleActionTimer(date, timerConfig);
			timer.getTimeRemaining();
		} catch (ParseException e) {
			logger.log(SEVERE, "formatting error", e);
		}
	}

	public boolean isTimeoutDone() {
		return timeoutDone;
	}
}
