package it.vige.realtime.batchesworkflow.checkpoint.listener;

import static java.util.logging.Logger.getLogger;

import java.util.Properties;
import java.util.logging.Logger;

import javax.batch.api.listener.StepListener;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;

import it.vige.realtime.batchesworkflow.checkpoint.CheckpointUserData;
import it.vige.realtime.batchesworkflow.checkpoint.PersistentCheckpointUserData;

@Named
public class StepCheckpointListener implements StepListener {

	private static final Logger logger = getLogger(StepCheckpointListener.class.getName());

	public final static String STARTED_AFTER_STEP = "started after step";

	public final static String BEFORE_STEP_PROPERTY = "beforeStepProperty";

	@Inject
	private JobContext jobContext;

	@Inject
	private StepContext stepContext;

	@Override
	public void beforeStep() throws Exception {
		Properties jobProperties = jobContext.getProperties();
		Properties stepProperties = stepContext.getProperties();
		stepProperties.put(BEFORE_STEP_PROPERTY, "Before step property");
		logger.info("job properties:" + jobProperties);
		logger.info("step properties:" + stepProperties);
	}

	@Override
	public void afterStep() throws Exception {
		CheckpointUserData checkpointUserData = (CheckpointUserData) jobContext.getTransientUserData();
		checkpointUserData.setStartedAfterStep(STARTED_AFTER_STEP);
		logger.info("transient user data:" + checkpointUserData);
		PersistentCheckpointUserData persistentCheckpointUserData = new PersistentCheckpointUserData();
		persistentCheckpointUserData.setStartedAfterStep(STARTED_AFTER_STEP);
		stepContext.setPersistentUserData(persistentCheckpointUserData);
		logger.info("persistent user data:" + stepContext.getPersistentUserData());
	}

}
