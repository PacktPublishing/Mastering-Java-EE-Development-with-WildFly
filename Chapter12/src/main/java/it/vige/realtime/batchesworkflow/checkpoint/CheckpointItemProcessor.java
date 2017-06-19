package it.vige.realtime.batchesworkflow.checkpoint;

import javax.batch.api.chunk.ItemProcessor;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

import it.vige.realtime.batchesworkflow.checkpoint.exception.NoRollbackException;
import it.vige.realtime.batchesworkflow.checkpoint.exception.SkippedException;

@Named
public class CheckpointItemProcessor implements ItemProcessor {

	@Inject
	private JobContext jobContext;

	@Override
	public Object processItem(Object item) throws Exception {
		CheckpointUserData checkpointUserData = (CheckpointUserData) jobContext.getTransientUserData();
		if (checkpointUserData.getCounter() == 10)
			throw new SkippedException();
		if (checkpointUserData.getCounter() == 12)
			throw new NoRollbackException();
		return "process ok";
	}

}
