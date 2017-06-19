package it.vige.realtime.batchesworkflow.checkpoint.listener;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.logging.Level.WARNING;
import static java.util.logging.Logger.getLogger;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.listener.ChunkListener;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;

import it.vige.realtime.batchesworkflow.checkpoint.CheckpointUserData;

@Named
public class PayrollListener implements ChunkListener {

	private static final Logger logger = getLogger(PayrollListener.class.getName());

	public final static String CHUNK_EXIT_STATUS = "chunkExitStatus";

	@Inject
	@BatchProperty(name = "name1")
	private String listenerProp;

	@Inject
	private JobContext jobContext;

	@Inject
	private StepContext stepContext;

	private CountDownLatch latch = new CountDownLatch(1);

	@Override
	public void beforeChunk() throws Exception {
		logger.info("batch status: " + jobContext.getBatchStatus());
		jobContext.setExitStatus(CHUNK_EXIT_STATUS);
		CheckpointUserData checkpointUserData = (CheckpointUserData) jobContext.getTransientUserData();
		if (checkpointUserData.getCounter() == 6)
			latch.await(2, SECONDS);
		logger.info("step listener prop: " + listenerProp);
	}

	@Override
	public void onError(Exception ex) throws Exception {
		logger.log(WARNING, "error on payroll listener", ex);
	}

	@Override
	public void afterChunk() throws Exception {
		Properties jobProperties = jobContext.getProperties();
		Properties stepProperties = stepContext.getProperties();
		logger.info("job properties:" + jobProperties);
		logger.info("step properties:" + stepProperties);
		logger.info("step listener prop: " + listenerProp);
	}

}
