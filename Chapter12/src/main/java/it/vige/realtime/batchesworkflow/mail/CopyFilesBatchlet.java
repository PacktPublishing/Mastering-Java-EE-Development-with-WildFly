package it.vige.realtime.batchesworkflow.mail;

import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static javax.batch.runtime.BatchRuntime.getJobOperator;
import static javax.batch.runtime.BatchStatus.COMPLETED;
import static javax.batch.runtime.BatchStatus.FAILED;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.batch.api.AbstractBatchlet;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class CopyFilesBatchlet extends AbstractBatchlet {
	
	private static final Logger logger = getLogger(CopyFilesBatchlet.class.getName());

	@Inject
	private JobContext jobContext;

	@Override
	public String process() {

		logger.info("Running inside SendBillBatchlet batchlet ");

		Properties parameters = getParameters();
		String source = parameters.getProperty("source");
		String destination = parameters.getProperty("destination");

		// JDK 1.7 API
		try {
			copy(new File(source).toPath(), new File(destination).toPath(), REPLACE_EXISTING);
			return COMPLETED.name();
		} catch (IOException e) {
			logger.log(SEVERE, "error copy file", e);
		}

		return FAILED.name();

	}

	private Properties getParameters() {
		JobOperator operator = getJobOperator();
		return operator.getParameters(jobContext.getExecutionId());

	}

}
