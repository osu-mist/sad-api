package edu.oregonstate.mist.sadapi.db

import edu.oregonstate.mist.sadapi.core.Sad
import edu.oregonstate.mist.sadapi.SadApplicationConfiguration
import io.dropwizard.jdbi.DBIFactory
import io.dropwizard.lifecycle.Managed
import io.dropwizard.setup.Environment
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.Handle
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

    @Override
    public void start() {
        DBIFactory factory = new DBIFactory()
        DBI jdbi = factory.build(environment, configuration.getDatabase(), 'oracle')
        handle = jdbi.open()
        connection = handle.getConnection()
    }

    @Override
    public void stop() {
        handle.close()
    }

    public List<Sad> queryAll(Long pidm) {
        List<Sad> sadList = new ArrayList<Sad>()
        String query = '''BEGIN
                            ? := SB_APPLICATION_DECISION.f_query_all(?);
                          END;'''
        CallableStatement statement = connection.prepareCall(query)
        try {
            statement.registerOutParameter(1, OracleTypes.CURSOR)
            statement.setLong(2, pidm)
            statement.execute()
            ResultSet result = (ResultSet) statement.getObject(1)
            try {
                while (result.next()) {
                    sadList.add(map(result))
                }
            } finally {
                result.close()
            }
        } finally {
            statement.close()
        }
        sadList
    }

    public Sad queryOne(Long pidm, String termCodeEntry, Long applNo, Long seqNo) {
        Sad sad = null
        String query = '''BEGIN
                            ? := SB_APPLICATION_DECISION.f_query_one(?,?,?,?);
                          END;'''
        CallableStatement statement = connection.prepareCall(query)
        try {
            statement.registerOutParameter(1, OracleTypes.CURSOR)
            statement.setLong(2, pidm)
            statement.setString(3, termCodeEntry)
            statement.setLong(4, applNo)
            statement.setLong(5, seqNo)
            statement.execute()
            ResultSet result = (ResultSet) statement.getObject(1)
            try {
                if (result.next()) {
                    sad = map(result)
                }
            } finally {
                result.close()
            }
        } finally {
            statement.close()
        }
        sad
    }

    private static Sad map(ResultSet result) {
        new Sad(
                pidm:          result.getLong('sarappd_pidm'),
                termCodeEntry: result.getString('sarappd_term_code_entry'),
                applNo:        result.getLong('sarappd_appl_no'),
                seqNo:         result.getLong('sarappd_seq_no'),
                apdcDate:      result.getDate('sarappd_apdc_date'),
                apdcCode:      result.getString('sarappd_apdc_code'),
                maintInd:      result.getString('sarappd_maint_ind'),
                user:          result.getString('sarappd_user'),
                dataOrigin:    result.getString('sarappd_data_origin')
        )
    }
}
