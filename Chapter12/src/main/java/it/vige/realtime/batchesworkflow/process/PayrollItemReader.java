package it.vige.realtime.batchesworkflow.process;

import static java.lang.Integer.parseInt;
import static java.util.logging.Logger.getLogger;
import static javax.batch.runtime.BatchRuntime.getJobOperator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

import it.vige.realtime.batchesworkflow.bean.PayrollInputRecord;

@Named
public class PayrollItemReader extends AbstractItemReader {

	private static final Logger logger = getLogger(PayrollItemReader.class.getName());

	public final static String INPUT_DATA_FILE_NAME = "payrollDataFileName";

	@Inject
	private JobContext jobContext;

	private int recordNumber;
	private BufferedReader br;
	private String currentLine;
	private Object[] stringLines;

	@Override
	public void open(Serializable prevCheckpointInfo) throws Exception {
		JobOperator jobOperator = getJobOperator();
		Properties jobParameters = jobOperator.getParameters(jobContext.getExecutionId());
		String resourceName = (String) jobParameters.get(INPUT_DATA_FILE_NAME);
		InputStream inputStream = new FileInputStream(resourceName);
		br = new BufferedReader(new InputStreamReader(inputStream));

		Stream<String> lines = br.lines();
		if (prevCheckpointInfo != null)
			recordNumber = (Integer) prevCheckpointInfo;
		else
			recordNumber = 0;
		stringLines = lines.toArray();
		logger.info("[SimpleItemReader] Opened Payroll file for reading from record number: " + recordNumber);
	}

	@Override
	public void close() throws Exception {
		br.close();
		recordNumber = 0;
		currentLine = null;
	}

	@Override
	public Object readItem() throws Exception {
		Object record = null;
		if (stringLines.length > recordNumber) {
			currentLine = stringLines[recordNumber] + "";
			String[] fields = currentLine.split("[, \t\r\n]+");
			PayrollInputRecord payrollInputRecord = new PayrollInputRecord();
			payrollInputRecord.setId(parseInt(fields[0]));
			payrollInputRecord.setBaseSalary(parseInt(fields[1]));
			record = payrollInputRecord;
			// Now that we could successfully read, Increment the record number
			recordNumber++;
		}
		return record;
	}

	@Override
	public Serializable checkpointInfo() throws Exception {
		return recordNumber;
	}
}
