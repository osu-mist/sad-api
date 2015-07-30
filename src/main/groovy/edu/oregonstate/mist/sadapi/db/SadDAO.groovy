package edu.oregonstate.mist.sadapi.db

import edu.oregonstate.mist.sadapi.core.Sad
import edu.oregonstate.mist.sadapi.SadApplicationConfiguration
import io.dropwizard.jdbi.DBIFactory
import io.dropwizard.lifecycle.Managed
import io.dropwizard.setup.Environment
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.Handle
import javax.ws.rs.core.Response
import javax.ws.rs.WebApplicationException
import java.sql.Connection
import java.sql.CallableStatement
import java.sql.ResultSet
import oracle.jdbc.OracleTypes

class SadDAO implements Managed {
    private SadApplicationConfiguration configuration
    private Environment environment
    private Handle handle
    private Connection connection

    public SadDAO(SadApplicationConfiguration configuration, Environment environment) {
        this.configuration = configuration
        this.environment = environment
    }

    public void start() {
        DBIFactory factory = new DBIFactory()
        DBI jdbi = factory.build(environment, configuration.getDatabase(), 'oracle')
        handle = jdbi.open()
        connection = handle.getConnection()
    }

    public List<Sad> queryAll(Long pidm) {
        String query = '''BEGIN
                            ? := SB_APPLICATION_DECISION.f_query_all(?);
                          END;'''
        CallableStatement statement = connection.prepareCall(query)
        statement.registerOutParameter(1, OracleTypes.CURSOR)
        statement.setLong(2, pidm)
        statement.execute()
        ResultSet result = (ResultSet)statement.getObject(1)
        List<Sad> list = new ArrayList<Sad>()
        while (result.next())
            list.add(map(result))
        if (list.isEmpty())
            throw new WebApplicationException(Response.Status.NOT_FOUND)
        return list
    }

    public Sad queryOne(Long pidm, String termCodeEntry, Long applNo, Long seqNo) {
        String query = '''BEGIN
                            ? := SB_APPLICATION_DECISION.f_query_one(?,?,?,?);
                          END;'''
        CallableStatement statement = connection.prepareCall(query)
        statement.registerOutParameter(1, OracleTypes.CURSOR)
        statement.setLong(2, pidm)
        statement.setString(3, termCodeEntry)
        statement.setLong(4, applNo)
        statement.setLong(5, seqNo)
        statement.execute()
        ResultSet result = (ResultSet)statement.getObject(1)
        if (!result.next())
            throw new WebApplicationException(Response.Status.NOT_FOUND)
        return map(result)
    }

    private static Sad map(ResultSet rs) {
        return new Sad(
                pidm:          rs.getLong('sarappd_pidm'),
                termCodeEntry: rs.getString('sarappd_term_code_entry'),
                applNo:        rs.getLong('sarappd_appl_no'),
                seqNo:         rs.getLong('sarappd_seq_no'),
                apdcDate:      rs.getDate('sarappd_apdc_date'),
                apdcCode:      rs.getString('sarappd_apdc_code'),
                maintInd:      rs.getString('sarappd_maint_ind'),
                user:          rs.getString('sarappd_user'),
                dataOrigin:    rs.getString('sarappd_data_origin')
        )
    }

    public void stop() {
        handle.close()
    }
}
