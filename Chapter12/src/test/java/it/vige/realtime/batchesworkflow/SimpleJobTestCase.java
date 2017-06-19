package it.vige.realtime.batchesworkflow;

import static it.vige.realtime.batchesworkflow.process.PayrollItemProcessor.EXIT_STATUS;
import static it.vige.realtime.batchesworkflow.process.PayrollItemReader.INPUT_DATA_FILE_NAME;
import static it.vige.realtime.batchesworkflow.process.PayrollItemWriter.PAYROLL_TEMP_FILE;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.logging.Logger.getLogger;
import static javax.batch.runtime.BatchRuntime.getJobOperator;
import static javax.batch.runtime.BatchStatus.COMPLETED;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;
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

import it.vige.realtime.batchesworkflow.bean.Payroll;
import it.vige.realtime.batchesworkflow.process.PayrollItemProcessor;

@RunWith(Arquillian.class)
public class SimpleJobTestCase {

	private static final Logger logger = getLogger(SimpleJobTestCase.class.getName());

	private final static String JOB_NAME = "PayrollJob";

	private final static String INPUT_PROPERTIES = "src/main/resources/input.properties";

	@Deployment
	public static WebArchive createWebDeployment() {
		final WebArchive war = create(WebArchive.class, "simplejob-test.war");
		war.addPackage(Payroll.class.getPackage());
		war.addPackage(PayrollItemProcessor.class.getPackage());
		war.addAsWebInfResource(INSTANCE, "beans.xml");
		war.addAsWebInfResource(
				new FileAsset(new File("src/main/resources/META-INF/batch-jobs/" + JOB_NAME + ".xml")),
				"classes/META-INF/batch-jobs/" + JOB_NAME + ".xml");
		return war;
	}

	@Test
	public void startProcess() throws Exception {
		logger.info("starting process test");
		CountDownLatch latch = new CountDownLatch(1);
		JobOperator jobOperator = getJobOperator();
		Properties props = new Properties();
		props.setProperty(INPUT_DATA_FILE_NAME, INPUT_PROPERTIES);
		long executionId = jobOperator.start(JOB_NAME, props);
		latch.await(10, SECONDS);
		List<Long> runningExecutions = jobOperator.getRunningExecutions(JOB_NAME);
		assertEquals("running executions. The process is end", 0, runningExecutions.size());
		Set<String> jobNames = jobOperator.getJobNames();
		assertTrue("one or three jobs", jobNames.size() >= 1 && jobNames.size() <= 3);
		String strJobNames = "";
		for (String jobName : jobNames)
			strJobNames += jobName;
		assertTrue("one only job called", strJobNames.contains(JOB_NAME));
		List<StepExecution> stepExecutions = jobOperator.getStepExecutions(executionId);
		assertEquals("no step executions", 1, stepExecutions.size());
		stepExecutions(stepExecutions.get(0));
		Properties properties = jobOperator.getParameters(executionId);
		assertEquals("one property found", 1, properties.size());
		assertEquals("MY_NEW_PROPERTY found", INPUT_PROPERTIES, properties.get(INPUT_DATA_FILE_NAME));
		JobInstance jobInstance = jobInstance(jobOperator.getJobInstance(executionId), JOB_NAME);
		jobExecutions(jobOperator.getJobExecutions(jobInstance));
		assertNotNull("executionId not empty", executionId);
		assertTrue("Created file from writer 1", new File(PAYROLL_TEMP_FILE + "1.tmp").exists());
		assertTrue("Created file from writer 2", new File(PAYROLL_TEMP_FILE + "2.tmp").exists());
		assertTrue("Created file from writer 3", new File(PAYROLL_TEMP_FILE + "3.tmp").exists());
	}

	private void stepExecutions(StepExecution stepExecution) {
		assertEquals("the batch has completed", COMPLETED, stepExecution.getBatchStatus());
		assertEquals("the batch has completed", COMPLETED.name(), stepExecution.getExitStatus());
		Date startTime = stepExecution.getStartTime();
		Date endTime = stepExecution.getEndTime();
		assertNotNull("The step is started", startTime);
		assertNotNull("Tha batch has failed", endTime);
		assertTrue("Dates are ok", startTime.before(endTime));
		stepMetrics(stepExecution.getMetrics(), 2, 3, 3);
		assertNull("No user available", stepExecution.getPersistentUserData());
		assertNotNull("New step execution id", stepExecution.getStepExecutionId());
		assertEquals("The name of the step", "process", stepExecution.getStepName());

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
				assertEquals("Metric rollback count value", 0, metric.getValue());
				break;
			case READ_COUNT:
				assertEquals("Metric read count value", values[2], metric.getValue());
				break;
			case FILTER_COUNT:
				assertEquals("Metric filter count value", 0, metric.getValue());
				break;
			case PROCESS_SKIP_COUNT:
				assertEquals("Metric process skip count value", 0, metric.getValue());
				break;
			}

	}

	private JobInstance jobInstance(JobInstance jobInstance, String jobName) {
		assertNotNull("Job instance created", jobInstance.getInstanceId());
		assertEquals("Job instance created with name", jobName, jobInstance.getJobName());
		return jobInstance;
	}

	private void jobExecutions(List<JobExecution> jobExecutions) {
		for (JobExecution jobExecution : jobExecutions) {
			assertEquals("the batch has completed", COMPLETED, jobExecution.getBatchStatus());
			assertEquals("the batch has completed", EXIT_STATUS, jobExecution.getExitStatus());
			Date startTime = jobExecution.getStartTime();
			Date createdTime = jobExecution.getCreateTime();
			Date lastUpdatedTime = jobExecution.getLastUpdatedTime();
			Date endTime = jobExecution.getEndTime();
			assertNotNull("The step is started", startTime);
			assertNotNull("The job is started", createdTime);
			assertNotNull("The job is updated", lastUpdatedTime);
			assertNotNull("Tha batch has failed", endTime);
			assertTrue("Dates are ok", startTime.before(endTime));
			assertTrue("Dates are ok", createdTime.before(endTime));
			assertFalse("Dates are ok", createdTime.equals(lastUpdatedTime));
			assertEquals("one only job called", JOB_NAME, jobExecution.getJobName());
			assertNotNull("execution created", jobExecution.getExecutionId());
			Properties properties = jobExecution.getJobParameters();
			assertEquals("one property found", 1, properties.size());
			assertEquals("MY_NEW_PROPERTY found", INPUT_PROPERTIES, properties.get(INPUT_DATA_FILE_NAME));
		}
	}

}
