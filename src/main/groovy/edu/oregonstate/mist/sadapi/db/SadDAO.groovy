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
import java.sql.SQLException
import oracle.jdbc.OracleTypes
import oracle.jdbc.OracleCallableStatement
import oracle.sql.DATE

/**
 * Sad database access object class.
 */
class SadDAO extends AbstractSadDAO implements Managed {
    private SadApplicationConfiguration configuration
    private Environment environment
    private Handle handle
    private Connection connection

    /**
     * Constructs the object after receiving and storing configuration and environment variables.
     *
     * @param configuration
     * @param environment
     */
    public SadDAO(SadApplicationConfiguration configuration, Environment environment) {
        this.configuration = configuration
        this.environment = environment
    }

    /**
     * Initializes the database connection and stores the JDBC object.
     */
    @Override
    public void start() {
        DBIFactory factory = new DBIFactory()
        DBI jdbi = factory.build(environment, configuration.getDatabase(), 'oracle')
        handle = jdbi.open()
        connection = handle.getConnection()
    }

    /**
     * Closes the database connection.
     */
    @Override
    public void stop() {
        handle.close()
    }

    /**
     * Returns all Sads with the given pidm.
     *
     * @param pidm
     * @return list of Sads found, otherwise empty
     */
    public List<Sad> queryAll(Long pidm) {
        List<Sad> sadList = new ArrayList<Sad>()
        CallableStatement statement = connection.prepareCall(QUERY_ALL)
        try {
            statement.registerOutParameter(1, OracleTypes.CURSOR)
            statement.setLong(2, pidm)
            statement.execute()
            ResultSet result = (ResultSet)statement.getObject(1)
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

    /**
     * Returns the Sad with the given pidm, term code entry, application number, and sequence number.
     *
     * @param pidm
     * @param termCodeEntry
     * @param applNo
     * @param seqNo
     * @return found Sad, otherwise null
     */
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
            ResultSet result = (ResultSet)statement.getObject(1)
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

    /**
     * Updates the Sad and returns it.
     *
     * @param pidm
     * @param termCodeEntry
     * @param applNo
     * @param seqNo
     * @param apdcDate
     * @param apdcCode
     * @param maintInd
     * @return updated Sad, otherwise null
     * @throws SQLException
     * @throws IllegalArgumentException
     */
    public Sad update(Long pidm, String termCodeEntry, Long applNo, Long seqNo, Date apdcDate, String apdcCode, String maintInd)
            throws SQLException, IllegalArgumentException {
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
            ResultSet result = (ResultSet)statement.getObject(8)
            try {
                if (result?.next()) {
                    sad = map(result)
                }
            } finally {
                result?.close()
            }
        } finally {
            statement?.close()
        }
        sad
    }

    /**
     * Creates the Sad and returns it.
     *
     * @param pidm
     * @param termCodeEntry
     * @param applNo
     * @param apdcDate
     * @param apdcCode
     * @param maintInd
     * @return created Sad, otherwise null
     * @throws SQLException
     * @throws IllegalArgumentException
     */
    public Sad create(Long pidm, String termCodeEntry, Long applNo, Date apdcDate, String apdcCode, String maintInd)
            throws SQLException, IllegalArgumentException {
        Sad sad = null
        OracleCallableStatement statement = (OracleCallableStatement)connection.prepareCall(CREATE)
        try {
            statement.setLong(1, pidm)
            statement.setString(2, termCodeEntry)
            statement.setLong(3, applNo)
            statement.setDATE(4, toDATE(apdcDate))
            statement.setString(5, apdcCode)
            statement.setString(6, maintInd)
            statement.registerOutParameter(7, OracleTypes.CURSOR)
            statement.execute()
            ResultSet result = (ResultSet)statement.getObject(7)
            try {
                if (result?.next()) {
                    sad = map(result)
                }
            } finally {
                result?.close()
            }
        } finally {
            statement.close()
        }
        sad
    }

    /**
     * Deletes the Sad and returns it.
     *
     * @param pidm
     * @param termCodeEntry
     * @param applNo
     * @param seqNo
     * @return deleted Sad, otherwise null
     * @throws SQLException
     */
    public Sad delete(Long pidm, String termCodeEntry, Long applNo, Long seqNo)
            throws SQLException {
        Sad sad = null
        CallableStatement statement = connection.prepareCall(DELETE)
        try {
            statement.setLong(1, pidm)
            statement.setString(2, termCodeEntry)
            statement.setLong(3, applNo)
            statement.setLong(4, seqNo)
            statement.registerOutParameter(5, OracleTypes.CURSOR)
            statement.execute()
            ResultSet result = (ResultSet)statement.getObject(5)
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

    /**
     * Returns the current result as a Sad.
     *
     * @param result
     * @return Sad result
     */
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

    /**
     * Returns the oracle.sql.DATE represented by the java.util.Date argument.
     *
     * @param date
     * @return Oracle DATE
     */
    private static DATE toDATE(Date date) {
        new DATE(new java.sql.Date(date.getTime() + 1000*60*60*8))
    }
}
