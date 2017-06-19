package it.vige.realtime.batchesworkflow.process;

import javax.batch.api.chunk.ItemProcessor;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

import it.vige.realtime.batchesworkflow.bean.Payroll;
import it.vige.realtime.batchesworkflow.bean.PayrollInputRecord;
import it.vige.realtime.batchesworkflow.bean.PayrollRecord;

@Named
public class PayrollItemProcessor implements ItemProcessor {
	
	public final static String EXIT_STATUS = "my_new_exit_status";

	@Inject
	private JobContext jobContext;

	public Object processItem(Object obj) throws Exception {
		contextOperations();
		PayrollInputRecord inputRecord = (PayrollInputRecord) obj;
		PayrollRecord payrollRecord = new PayrollRecord();

		int base = inputRecord.getBaseSalary();
		float tax = base * 27 / 100.0f;
		float bonus = base * 15 / 100.0f;

		payrollRecord.setEmpID(inputRecord.getId());
		payrollRecord.setBase(base);
		payrollRecord.setTax(tax);
		payrollRecord.setBonus(bonus);
		payrollRecord.setNet(base + bonus - tax);
		return payrollRecord;
	}

	private void contextOperations() {
		jobContext.setTransientUserData(new Payroll());
		jobContext.getBatchStatus();
		jobContext.setExitStatus(EXIT_STATUS);
		jobContext.getBatchStatus();
		jobContext.getExecutionId();
		jobContext.getInstanceId();
		jobContext.getJobName();
		jobContext.getProperties();
		jobContext.getTransientUserData();
	}
}
