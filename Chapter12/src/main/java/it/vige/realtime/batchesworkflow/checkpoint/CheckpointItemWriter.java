package it.vige.realtime.batchesworkflow.checkpoint;

import java.util.List;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Named;

@Named
public class CheckpointItemWriter extends AbstractItemWriter {

	@Override
	public void writeItems(List<Object> items) throws Exception {
		
	}

}
