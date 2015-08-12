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
import oracle.jdbc.OracleCallableStatement
import oracle.sql.DATE

class SadDAO extends AbstractSadDAO implements Managed {
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
        CallableStatement statement = connection.prepareCall(QUERY_ALL)
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
        CallableStatement statement = connection.prepareCall(QUERY_ONE)
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

    public Sad update(Long pidm, String termCodeEntry, Long applNo, Long seqNo, Date apdcDate, String apdcCode, String maintInd) {
        Sad sad = null
        OracleCallableStatement statement = (OracleCallableStatement)connection.prepareCall(UPDATE)
        try {
            statement.setLong(1, pidm)
            statement.setString(2, termCodeEntry)
            statement.setLong(3, applNo)
            statement.setLong(4, seqNo)
            statement.setDATE(5, toDATE(apdcDate))
            statement.setString(6, apdcCode)
            statement.setString(7, maintInd)
            statement.registerOutParameter(8, OracleTypes.CURSOR)
            statement.execute()
            ResultSet result = (ResultSet) statement.getObject(8)
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
                pidm:          result.getLong(PIDM),
                termCodeEntry: result.getString(TERMCODEENTRY),
                applNo:        result.getLong(APPLNO),
                seqNo:         result.getLong(SEQNO),
                apdcDate:      result.getDate(APDCDATE),
                apdcCode:      result.getString(APDCCODE),
                maintInd:      result.getString(MAINTIND)
        )
    }

    private static DATE toDATE(Date date) {
        new DATE(new java.sql.Date(date.getTime() + 1000*60*60*8))
    }
}
