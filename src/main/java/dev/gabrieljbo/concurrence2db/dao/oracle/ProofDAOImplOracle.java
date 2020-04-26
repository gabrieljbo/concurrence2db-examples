package dev.gabrieljbo.concurrence2db.dao.oracle;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.gabrieljbo.concurrence2db.dao.ProofDAO;

@Component
public class ProofDAOImplOracle implements ProofDAO {

    @Autowired
    DataSource datasource;
    
    public DataSource getDataSource() {
	return this.datasource;
    }

    public int getNextUUID(Connection dbConnection) {
	int nextUUID = -1;

	try {
	    
	    //nextUUID = this.jdbcTemplate.queryForObject("select devusr.CONCURRENT_TABLE_NEXT_UUID() from DUAL", new Object[] { }, Integer.class);
	    
//	    SimpleJdbcCall jdbcCall = new SimpleJdbcCall(this.jdbcTemplate).withFunctionName("CONCURRENT_TABLE_NEXT_UUID").withSchemaName("devusr");
//	    BigDecimal xnum = jdbcCall.executeFunction(BigDecimal.class, new Object[] {});
//	    nextUUID = xnum.intValue();

	    String functionCall = "{? = call CONCURRENT_TABLE_NEXT_UUID()}";
	    CallableStatement callableStatement = dbConnection.prepareCall(functionCall);
	    callableStatement.registerOutParameter(1, 4);
	    callableStatement.execute();
	    nextUUID = callableStatement.getInt(1);

	    /*
	     * String selectSentence = "select session_id, object_id from gv$locked_object";
	     * PreparedStatement preparedStatement =
	     * dbConnection.prepareStatement(selectSentence); ResultSet rs =
	     * preparedStatement.executeQuery(); while (rs.next()) { int sessionId =
	     * rs.getInt("session_id"); System.out.println("sessionId is " + sessionId); }
	     * rs.close(); preparedStatement.close();
	     */	
	    
	    /*
	     * String selectSentence =
	     * "select UUID from CONCURRENT_TABLE where PROCESSED = 0 order by REGISTRATION_DATE for update skip locked"
	     * ; PreparedStatement preparedStatement =
	     * dbConnection.prepareStatement(selectSentence); ResultSet rs =
	     * preparedStatement.executeQuery(); while (rs.next()) { nextUUID =
	     * rs.getInt("UUID"); } rs.close(); preparedStatement.close();
	     */

	    /*
	     * String selectSentence =
	     * "select UUID from devusr.CONCURRENT_TABLE where PROCESSED = 0 order by REGISTRATION_DATE for update skip locked"
	     * ; this.jdbcTemplate.setMaxRows(1); nextUUID =
	     * this.jdbcTemplate.queryForObject(selectSentence, new Object[] { },
	     * Integer.class);
	     */
	} catch (Exception ex) {
	    ex.printStackTrace();
	}

	return nextUUID;
    }

    public void updateUUID(Connection dbConnection, int uuid) {
	String updateSentence = "update CONCURRENT_TABLE set PROCESSED = 1 where UUID = ?";

	try {
	    PreparedStatement preparedStatement = dbConnection.prepareStatement(updateSentence);
	    preparedStatement.setInt(1, uuid);
	    preparedStatement.executeUpdate();
	    preparedStatement.close();
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

}