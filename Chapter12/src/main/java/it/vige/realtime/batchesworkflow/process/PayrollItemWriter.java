package it.vige.realtime.batchesworkflow.process;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.logging.Logger;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Named;

import it.vige.realtime.batchesworkflow.bean.PayrollRecord;

@Named
public class PayrollItemWriter extends AbstractItemWriter {

	private static final Logger logger = getLogger(PayrollItemWriter.class.getName());

	public final static String PAYROLL_TEMP_FILE = "target/payroll_serialized";

	@Override
	public void writeItems(List<Object> list) throws Exception {
		for (Object obj : list) {
			logger.info("PayrollRecord: " + obj);
			serialize(obj);
		}
	}

	private void serialize(Object obj) {
		PayrollRecord payrollRecord = (PayrollRecord) obj;
		try (FileOutputStream fo = new FileOutputStream(PAYROLL_TEMP_FILE + payrollRecord.getEmpID() + ".tmp")) {
			ObjectOutputStream so = new ObjectOutputStream(fo);
			so.writeObject(obj);
			so.flush();
		} catch (Exception ex) {
			logger.log(SEVERE, ex.getMessage(), ex);
		}

	}

}
