package it.vige.realtime.batchesworkflow.checkpoint;

import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class CheckpointItemReader extends AbstractItemReader {

	@Inject
	private JobContext jobContext;

	private int counter;

	@Override
	public Object readItem() throws Exception {
		counter++;
		CheckpointUserData checkpointUserData = (CheckpointUserData) jobContext.getTransientUserData();
		checkpointUserData.setCounter(counter);
		if (counter < 20)
			return "ok read";
		else
			return null;
	}

	public int getCounter() {
		return counter;
	}

}
