package it.vige.realtime.batchesworkflow.checkpoint.listener;

import static java.util.logging.Logger.getLogger;

import java.util.Properties;
import java.util.logging.Logger;

import javax.batch.api.listener.JobListener;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

import it.vige.realtime.batchesworkflow.checkpoint.CheckpointUserData;

@Named
public class JobCheckpointListener implements JobListener {

	public final static String STARTED_BEFORE_JOB = "started before job";

	private static final Logger logger = getLogger(JobCheckpointListener.class.getName());

	@Inject
	private JobContext jobContext;

	@Override
	public void beforeJob() throws Exception {
		CheckpointUserData checkpointUserData = new CheckpointUserData();
		checkpointUserData.setStartedBeforeJob(STARTED_BEFORE_JOB);
		jobContext.setTransientUserData(checkpointUserData);
		logger.info("transient user data:" + jobContext.getTransientUserData());
	}

	@Override
	public void afterJob() throws Exception {
		Properties jobProperties = jobContext.getProperties();
		logger.info("job properties:" + jobProperties);
	}

}
