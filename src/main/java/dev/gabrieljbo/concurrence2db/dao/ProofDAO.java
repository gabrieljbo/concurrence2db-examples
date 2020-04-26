package dev.gabrieljbo.concurrence2db.dao;

import java.sql.Connection;
import javax.sql.DataSource;

public interface ProofDAO {

    DataSource getDataSource();

    int getNextUUID(Connection dbConnection);

    void updateUUID(Connection dbConnection, int uuid);

}