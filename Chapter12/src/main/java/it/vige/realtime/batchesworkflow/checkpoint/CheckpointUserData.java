package it.vige.realtime.batchesworkflow.checkpoint;

public class CheckpointUserData {

	private String startedBeforeJob;

	private String startedAfterStep;
	
	private int counter;

	public String getStartedBeforeJob() {
		return startedBeforeJob;
	}

	public void setStartedBeforeJob(String startedBeforeJob) {
		this.startedBeforeJob = startedBeforeJob;
	}

	public String getStartedAfterStep() {
		return startedAfterStep;
	}

	public void setStartedAfterStep(String startedAfterStep) {
		this.startedAfterStep = startedAfterStep;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	
}
