package it.vige.realtime.batchesworkflow.checkpoint.exception;

public class NoSkippedException extends Exception {

	private static final long serialVersionUID = -5977839964875500639L;

	@Override
	public String getMessage() {
		return "no skipped exception";
	}

}
