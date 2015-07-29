package edu.oregonstate.mist.sadapi.resources

import edu.oregonstate.mist.sadapi.core.Sad
import edu.oregonstate.mist.sadapi.db.SadDAO
import io.dropwizard.jersey.params.LongParam
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path('/')
class SadResource {
    private SadDAO sadDAO

    public SadResource(SadDAO sadDAO) {
        this.sadDAO = sadDAO
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String respond() {
        return "goodbye world"
    }

    @GET
    @Path('{pidm: \\d+}')
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sad> foo(@PathParam('pidm') LongParam pidm) {
        return sadDAO.queryAll(pidm.get())
    }
}
