package it.vige.realtime.batchesworkflow;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static javax.batch.runtime.BatchRuntime.getJobOperator;
import static javax.batch.runtime.BatchStatus.COMPLETED;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.jboss.shrinkwrap.resolver.api.maven.Maven.resolver;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.JobInstance;
import javax.batch.runtime.Metric;
import javax.batch.runtime.StepExecution;
import javax.mail.Session;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.MessageHandlerFactory;
import org.subethamail.smtp.RejectException;
import org.subethamail.smtp.server.SMTPServer;

import it.vige.realtime.batchesworkflow.mail.MailBatchlet;

@RunWith(Arquillian.class)
public class MailJobTestCase {

	private static final Logger logger = getLogger(MailJobTestCase.class.getName());

	private MyMessageHandlerFactory myFactory = new MyMessageHandlerFactory();
	private SMTPServer smtpServer = new SMTPServer(myFactory);

	private final static String JOB_NAME = "Sendmail";

	private final static String INPUT_PROPERTIES = "src/main/resources/input.properties";

	@Deployment
	public static WebArchive createWebDeployment() {
		final WebArchive war = create(WebArchive.class, "mailjob-test.war");
		File[] files = resolver().loadPomFromFile("pom.xml").importRuntimeDependencies()
				.resolve("org.subethamail:subethasmtp:3.1.7").withTransitivity().asFile();
		war.addPackage(MailBatchlet.class.getPackage());
		war.addAsWebInfResource(INSTANCE, "beans.xml");
		war.addAsWebInfResource(new FileAsset(new File("src/main/resources/META-INF/batch-jobs/" + JOB_NAME + ".xml")),
				"classes/META-INF/batch-jobs/" + JOB_NAME + ".xml");
		war.addAsLibraries(files);
		return war;
	}

	@Resource(name = "java:jboss/mail/Default")
	private Session session;

	@Before
	public void init() {
		session.getProperties().put("mail.smtp.port", "25000");
		smtpServer.setPort(25000);
		smtpServer.start();
	}

	@After
	public void end() {
		session.getProperties().put("mail.smtp.port", "25");
		smtpServer.stop();
	}

	@Test
	public void startProcess() throws Exception {
		logger.info("starting mail process test");
		CountDownLatch latch = new CountDownLatch(1);
		long executionId = 0;
		JobOperator jobOperator = getJobOperator();

		Properties p = new Properties();
		p.setProperty("source", INPUT_PROPERTIES);
		p.setProperty("destination", "target/output.properties");
		p.setProperty("filesystem", "target");
		executionId = jobOperator.start(JOB_NAME, p);
		latch.await(2, SECONDS);
		List<Long> runningExecutions = jobOperator.getRunningExecutions(JOB_NAME);
		assertEquals("running executions. The process is end", 0, runningExecutions.size());
		Set<String> jobNames = jobOperator.getJobNames();
		assertTrue("one or two job", jobNames.size() >= 1 && jobNames.size() <= 3);
		String strJobNames = "";
		for (String jobName : jobNames)
			strJobNames += jobName;
		assertTrue("one only job called", strJobNames.contains(JOB_NAME));
		List<StepExecution> stepExecutions = jobOperator.getStepExecutions(executionId);
		assertEquals("no step executions", 2, stepExecutions.size());
		stepExecutions(stepExecutions.get(0), false);
		stepExecutions(stepExecutions.get(1), true);
		Properties properties = jobOperator.getParameters(executionId);
		assertEquals("one property found", 3, properties.size());
		assertEquals("MY_NEW_PROPERTY found", INPUT_PROPERTIES, properties.get("source"));
		JobInstance jobInstance = jobInstance(jobOperator.getJobInstance(executionId));
		jobExecutions(jobOperator.getJobExecutions(jobInstance));
		assertNotNull("executionId not empty", executionId);
		assertTrue("the destination file is created", new File(properties.get("destination") + "").exists());
		assertTrue("message received",
				myFactory.getBody().contains("Job Execution id " + executionId + " warned disk space getting low!"));
	}

	private void stepExecutions(StepExecution stepExecution, boolean last) {
		assertEquals("the batch has completed", COMPLETED, stepExecution.getBatchStatus());
		assertEquals("the batch has completed", COMPLETED.name(), stepExecution.getExitStatus());
		Date startTime = stepExecution.getStartTime();
		Date endTime = stepExecution.getEndTime();
		assertNotNull("The step is started", startTime);
		assertNotNull("Tha batch has failed", endTime);
		assertTrue("Dates are ok", startTime.before(endTime));
		stepMetrics(stepExecution.getMetrics());
		assertNull("No user available", stepExecution.getPersistentUserData());
		assertNotNull("New step execution id", stepExecution.getStepExecutionId());
		assertEquals("The name of the step", last ? "sendemail" : "copyfiles", stepExecution.getStepName());

	}

	private void stepMetrics(Metric[] metrics) {
		assertEquals("Metrics are", 8, metrics.length);
		for (Metric metric : metrics)
			switch (metric.getType()) {
			case COMMIT_COUNT:
				assertEquals("Metric commit count value", 0, metric.getValue());
				break;
			case READ_SKIP_COUNT:
				assertEquals("Metric read skip count value", 0, metric.getValue());
				break;
			case WRITE_SKIP_COUNT:
				assertEquals("Metric write skip count value", 0, metric.getValue());
				break;
			case WRITE_COUNT:
				assertEquals("Metric write count value", 0, metric.getValue());
				break;
			case ROLLBACK_COUNT:
				assertEquals("Metric rollback count value", 0, metric.getValue());
				break;
			case READ_COUNT:
				assertEquals("Metric read count value", 0, metric.getValue());
				break;
			case FILTER_COUNT:
				assertEquals("Metric filter count value", 0, metric.getValue());
				break;
			case PROCESS_SKIP_COUNT:
				assertEquals("Metric process skip count value", 0, metric.getValue());
				break;
			}

	}

	private JobInstance jobInstance(JobInstance jobInstance) {
		assertNotNull("Job instance created", jobInstance.getInstanceId());
		assertEquals("Job instance created with name", JOB_NAME, jobInstance.getJobName());
		return jobInstance;
	}

	private void jobExecutions(List<JobExecution> jobExecutions) {
		for (JobExecution jobExecution : jobExecutions) {
			assertEquals("the batch has failed", COMPLETED, jobExecution.getBatchStatus());
			assertEquals("the batch has failed", "DSK_SPACE_OK", jobExecution.getExitStatus());
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
			assertEquals("one property found", 3, properties.size());
			assertEquals("MY_NEW_PROPERTY found", INPUT_PROPERTIES, properties.get("source"));
		}
	}

	private class MyMessageHandlerFactory implements MessageHandlerFactory {

		private String body;

		public MessageHandler create(MessageContext ctx) {
			return new Handler(ctx);
		}

		class Handler implements MessageHandler {

			public Handler(MessageContext ctx) {
			}

			public void from(String from) throws RejectException {
				logger.info("FROM:" + from);
			}

			public void recipient(String recipient) throws RejectException {
				logger.info("RECIPIENT:" + recipient);
			}

			public void data(InputStream data) throws IOException {
				logger.info("MAIL DATA");
				logger.info("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");
				body = this.convertStreamToString(data);
				logger.info(body);
				logger.info("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");
			}

			public void done() {
				logger.info("Finished");
			}

			public String convertStreamToString(InputStream is) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();

				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
				} catch (IOException e) {
					logger.log(SEVERE, "activiti diagram", e);
				}
				return sb.toString();
			}

		}

		public String getBody() {
			return body;
		}
	}
}
