package dev.gabrieljbo.concurrence2db;

import java.sql.Connection;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dev.gabrieljbo.concurrence2db.dao.ProofDAO;

@Component
public class ActionImpl implements Action {

    @Autowired
    ProofDAO proofDAO;

    @Autowired
    TaskScheduler taskScheduler;
    ScheduledFuture<?> scheduledFuture;

    public void start(long executionInterval) {
	this.scheduledFuture = this.taskScheduler.scheduleAtFixedRate(this.execute(), executionInterval);
    }

    public void stop() {
	this.scheduledFuture.cancel(false);
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    private Runnable execute() {
	return () -> {
	    Connection dbConnection = null;

	    try {
		dbConnection = this.proofDAO.getDataSource().getConnection();
		int nextUUID = this.proofDAO.getNextUUID(dbConnection);
		System.out.println("next UUID is " + nextUUID);
		
		Thread.sleep(10000);

		System.out.println("UUID " + nextUUID + " will be updated");
		this.proofDAO.updateUUID(dbConnection, nextUUID);
		System.out.println("UUID " + nextUUID + " has been updated!");
	    } catch (Exception ex1) {
		ex1.printStackTrace();
	    } finally {
		if (dbConnection != null) {
		    try {
			dbConnection.close();
		    } catch (Exception ex2) {
			ex2.printStackTrace();
		    }
		}
	    }
	};
    }

}