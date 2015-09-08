package edu.oregonstate.mist.sadapi.db

import edu.oregonstate.mist.sadapi.SadApplication
import edu.oregonstate.mist.sadapi.core.Sad
import edu.oregonstate.mist.sadapi.SadApplicationConfiguration
import io.dropwizard.testing.ResourceHelpers
import io.dropwizard.testing.junit.DropwizardAppRule
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test
import static org.junit.Assert.*
import java.sql.SQLException

class SadDAOTest {
    private static SadDAO sadDAO

    private static final Sad good = new Sad(
            pidm: 321,
            termCodeEntry: '199202',
            applNo: 1,
            seqNo: 1,
            apdcDate: Date.parse('yyyy-MM-dd', '1991-12-10'),
            apdcCode: 'A',
            maintInd: 'U'
    )

    private static final Sad nonExistent = new Sad(
            pidm: 10000000,
            termCodeEntry: '199201',
            applNo: 99,
            seqNo: 99,
            apdcDate: Date.parse('yyyy-MM-dd', '1991-12-10'),
            apdcCode: 'A',
            maintInd: 'U'
    )

    private static final Sad bad = new Sad(
            pidm: 123456789, // too many digits
            termCodeEntry: '1234567', // too many characters
            applNo: 123, // too many digits
            seqNo: 123, // too many digits
            apdcDate: Date.parse('yyyyy-MM-dd', '10000-01-01'), // invalid year value
            apdcCode: 'VWX', // too many characters
            maintInd: 'YZ' // too many characters
    )

    @ClassRule
    public static final DropwizardAppRule<SadApplicationConfiguration> APPLICATION =
            new DropwizardAppRule<SadApplicationConfiguration>(
                    SadApplication.class,
                    ResourceHelpers.resourceFilePath('configuration.yaml'))

    @BeforeClass
    public static void setUpClass() {
        sadDAO = new SadDAO(APPLICATION.configuration, APPLICATION.environment)
        sadDAO.start()
        // FIXME: ensure consistent state
    }

    @AfterClass
    public static void tearDownClass() {
        sadDAO.stop()
        // FIXME: restore original state
    }

    @Test
    public void testQueryAllGood() {
        assertEquals(3, sadDAO.queryAll(good.pidm).size()) // FIXME: assumed state
    }

    @Test
    public void testQueryAllNonExistent() {
        assertTrue(sadDAO.queryAll(nonExistent.pidm).isEmpty())
    }

    @Test
    public void testQueryOneGood() {
        Sad sad = sadDAO.queryOne(good.pidm, good.termCodeEntry, good.applNo, good.seqNo)
        assertNotNull(sad)
        assertEquals(good.pidm, sad.pidm)
        assertEquals(good.termCodeEntry, sad.termCodeEntry)
        assertEquals(good.applNo, sad.applNo)
        assertEquals(good.seqNo, sad.seqNo)
        assertEquals(good.apdcDate, sad.apdcDate)
        assertEquals(good.apdcCode, sad.apdcCode)
        assertEquals(good.maintInd, sad.maintInd)
    }

    @Test
    public void testQueryOneNonExistent() {
        assertNull(sadDAO.queryOne(nonExistent.pidm, nonExistent.termCodeEntry, nonExistent.applNo, nonExistent.seqNo))
    }

    @Test
    public void testUpdateGood() {
        Date date = Date.parse('yyyy-MM-dd', '2015-08-19')
        Sad sad = sadDAO.update(good.pidm, good.termCodeEntry, good.applNo, good.seqNo, date, good.apdcCode, good.maintInd)
        assertNotNull(sad)
        assertEquals(date, sad.apdcDate)
        // FIXME: side effects on failure
        sad = sadDAO.update(good.pidm, good.termCodeEntry, good.applNo, good.seqNo, good.apdcDate, good.apdcCode, good.maintInd)
        assertNotNull(sad)
        assertEquals(good.apdcDate, sad.apdcDate)
    }

    @Test
    public void testUpdateNonExistent() {
        assertNull(sadDAO.update(nonExistent.pidm, nonExistent.termCodeEntry, nonExistent.applNo, nonExistent.seqNo, nonExistent.apdcDate, nonExistent.apdcCode, nonExistent.maintInd))
    }

    @Test
    public void testUpdateBadPidm() {
        try {
            sadDAO.update(bad.pidm, good.termCodeEntry, good.applNo, good.seqNo, good.apdcDate, good.apdcCode, good.maintInd)
            fail()
        } catch (SQLException sqlException) {
            assertTrue(sqlException.message.contains('number precision too large'))
        }
    }

    @Test
    public void testUpdateBadTermCodeEntry() {
        try {
            sadDAO.update(good.pidm, bad.termCodeEntry, good.applNo, good.seqNo, good.apdcDate, good.apdcCode, good.maintInd)
            fail()
        } catch (SQLException sqlException) {
            assertTrue(sqlException.message.contains('character string buffer too small'))
        }
    }

    @Test
    public void testUpdateBadApplNo() {
        try {
            sadDAO.update(good.pidm, good.termCodeEntry, bad.applNo, good.seqNo, good.apdcDate, good.apdcCode, good.maintInd)
            fail()
        } catch (SQLException sqlException) {
            assertTrue(sqlException.message.contains('number precision too large'))
        }
    }

    @Test
    public void testUpdateBadSeqNo() {
        try {
            sadDAO.update(good.pidm, good.termCodeEntry, good.applNo, bad.seqNo, good.apdcDate, good.apdcCode, good.maintInd)
            fail()
        } catch (SQLException sqlException) {
            assertTrue(sqlException.message.contains('number precision too large'))
        }
    }

    @Test
    public void testUpdateBadApdcDate() {
        try {
            sadDAO.update(good.pidm, good.termCodeEntry, good.applNo, good.seqNo, bad.apdcDate, good.apdcCode, good.maintInd)
            fail()
        } catch (IllegalArgumentException illegalArgumentException) {
            assertTrue(illegalArgumentException.message.contains('Invalid year value'))
        }
    }

    @Test
    public void testUpdateBadApdcCode() {
        try {
            sadDAO.update(good.pidm, good.termCodeEntry, good.applNo, good.seqNo, good.apdcDate, bad.apdcCode, good.maintInd)
            fail()
        } catch (SQLException sqlException) {
            assertTrue(sqlException.message.contains('character string buffer too small'))
        }
    }

    @Test
    public void testUpdateBadMaintInd() {
        try {
            sadDAO.update(good.pidm, good.termCodeEntry, good.applNo, good.seqNo, good.apdcDate, good.apdcCode, bad.maintInd)
            fail()
        } catch (SQLException sqlException) {
            assertTrue(sqlException.message.contains('character string buffer too small'))
        }
    }

    @Test
    public void testCreateGood() {
        Sad sad = sadDAO.create(good.pidm, good.termCodeEntry, good.applNo, good.apdcDate, good.apdcCode, good.maintInd)
        assertNotNull(sad)
        assertEquals(good.seqNo + 1, sad.seqNo)
        // FIXME: side effects on failure
        sadDAO.delete(sad.pidm, sad.termCodeEntry, sad.applNo, sad.seqNo)
    }

    @Test
    public void testCreateNonExistent() {
        assertNull(sadDAO.create(nonExistent.pidm, nonExistent.termCodeEntry, nonExistent.applNo, nonExistent.apdcDate, nonExistent.apdcCode, nonExistent.maintInd))
    }

    @Test
    public void testDeleteGood() {
        Sad sad = sadDAO.create(good.pidm, good.termCodeEntry, good.applNo, good.apdcDate, good.apdcCode, good.maintInd)
        assertNotNull(sadDAO.delete(sad.pidm, sad.termCodeEntry, sad.applNo, sad.seqNo))
        assertEquals(good.pidm, sad.pidm)
        assertEquals(good.termCodeEntry, sad.termCodeEntry)
        assertEquals(good.applNo, sad.applNo)
        assertEquals(good.seqNo + 1, sad.seqNo)
        assertEquals(good.apdcDate, sad.apdcDate)
        assertEquals(good.apdcCode, sad.apdcCode)
        assertEquals(good.maintInd, sad.maintInd)
        assertNull(sadDAO.delete(sad.pidm, sad.termCodeEntry, sad.applNo, sad.seqNo))
    }

    @Test
    public void testDeleteNonExistent() {
        assertNull(sadDAO.delete(nonExistent.pidm, nonExistent.termCodeEntry, nonExistent.applNo, nonExistent.seqNo))
    }
}
