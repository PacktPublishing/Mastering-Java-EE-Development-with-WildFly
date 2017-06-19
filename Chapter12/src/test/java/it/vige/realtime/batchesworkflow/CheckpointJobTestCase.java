package it.vige.realtime.batchesworkflow;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.logging.Logger.getLogger;
import static javax.batch.runtime.BatchRuntime.getJobOperator;
import static javax.batch.runtime.BatchStatus.FAILED;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.JobInstance;
import javax.batch.runtime.Metric;
import javax.batch.runtime.StepExecution;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.realtime.batchesworkflow.checkpoint.PayrollCheckpoint;
import it.vige.realtime.batchesworkflow.checkpoint.PersistentCheckpointUserData;
import it.vige.realtime.batchesworkflow.checkpoint.exception.SkippedException;
import it.vige.realtime.batchesworkflow.checkpoint.listener.PayrollListener;

@RunWith(Arquillian.class)
public class CheckpointJobTestCase {

	private static final Logger logger = getLogger(CheckpointJobTestCase.class.getName());

	private final static String JOB_NAME = "CheckpointJob";

	@Deployment
	public static WebArchive createWebDeployment() {
		final WebArchive war = create(WebArchive.class, "checkpointjob-test.war");
		war.addPackage(PayrollCheckpoint.class.getPackage());
		war.addPackage(SkippedException.class.getPackage());
		war.addPackage(PayrollListener.class.getPackage());
		war.addAsWebInfResource(INSTANCE, "beans.xml");
		war.addAsWebInfResource(new FileAsset(new File("src/main/resources/META-INF/batch-jobs/" + JOB_NAME + ".xml")),
				"classes/META-INF/batch-jobs/" + JOB_NAME + ".xml");
		return war;
	}

	private void stepMetrics(Metric[] metrics, int... values) {
		assertEquals("Metrics are", 8, metrics.length);
		for (Metric metric : metrics)
			switch (metric.getType()) {
			case COMMIT_COUNT:
				assertEquals("Metric commit count value", values[0], metric.getValue());
				break;
			case READ_SKIP_COUNT:
				assertEquals("Metric read skip count value", 0, metric.getValue());
				break;
			case WRITE_SKIP_COUNT:
				assertEquals("Metric write skip count value", 0, metric.getValue());
				break;
			case WRITE_COUNT:
				assertEquals("Metric write count value", values[1], metric.getValue());
				break;
			case ROLLBACK_COUNT:
				assertEquals("Metric rollback count value", values[3], metric.getValue());
				break;
			case READ_COUNT:
				assertEquals("Metric read count value", values[2], metric.getValue());
				break;
			case FILTER_COUNT:
				assertEquals("Metric filter count value", 0, metric.getValue());
				break;
			case PROCESS_SKIP_COUNT:
				assertEquals("Metric process skip count value", values[4], metric.getValue());
				break;
			}

	}

	private JobInstance jobInstance(JobInstance jobInstance, String jobName) {
		assertNotNull("Job instance created", jobInstance.getInstanceId());
		assertEquals("Job instance created with name", jobName, jobInstance.getJobName());
		return jobInstance;
	}

	@Test
	public void checkpointAlgorithm() throws Exception {
		logger.info("starting checkpoint test");
		CountDownLatch latch = new CountDownLatch(1);
		JobOperator jobOperator = getJobOperator();
		Properties props = new Properties();
		long executionId = jobOperator.start(JOB_NAME, props);
		latch.await(10, SECONDS);

		JobInstance jobInstance = jobInstance(jobOperator.getJobInstance(executionId), JOB_NAME);
		JobExecution jobExecution = jobOperator.getJobExecutions(jobInstance).get(0);
		Properties jobProperties = jobExecution.getJobParameters();
		assertEquals("properties are: ", 0, jobProperties.size());
		assertEquals("batch failed because a NoRollbackException is throwed", FAILED, jobExecution.getBatchStatus());
		StepExecution stepExecution = jobOperator.getStepExecutions(executionId).get(0);
		PersistentCheckpointUserData persistentCheckpointUserData = (PersistentCheckpointUserData) stepExecution
				.getPersistentUserData();
		assertNull("persistent user data after step", persistentCheckpointUserData.getStartedAfterStep());
		Metric[] stepMetrics = stepExecution.getMetrics();
		stepMetrics(stepMetrics, 3, 10, 12, 2, 1);
	}

}
