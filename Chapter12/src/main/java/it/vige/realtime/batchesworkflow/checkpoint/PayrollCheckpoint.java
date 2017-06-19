package it.vige.realtime.batchesworkflow.checkpoint;

import javax.batch.api.chunk.AbstractCheckpointAlgorithm;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public final class PayrollCheckpoint extends AbstractCheckpointAlgorithm {

	public final static String REAL_EXIT_STATUS = "myRealExitStatus";

	@Inject
	private JobContext jobContext;

	@Override
	public int checkpointTimeout() throws Exception {
		CheckpointUserData checkpointUserData = (CheckpointUserData) jobContext.getTransientUserData();
		if (checkpointUserData.getCounter() == 6)
			return 1;
		else
			return 0;
	}

	@Override
	public void beginCheckpoint() throws Exception {
		super.beginCheckpoint();
	}

	@Override
	public void endCheckpoint() throws Exception {
		super.endCheckpoint();
	}

	@Override
	public boolean isReadyToCheckpoint() throws Exception {
		CheckpointUserData checkpointUserData = (CheckpointUserData) jobContext.getTransientUserData();
		if (checkpointUserData.getCounter() % 3 == 0) {
			jobContext.setExitStatus(REAL_EXIT_STATUS);
			return true;
		} else
			return false;
	}
}