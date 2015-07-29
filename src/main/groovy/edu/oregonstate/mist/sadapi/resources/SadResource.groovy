package edu.oregonstate.mist.sadapi.resources

import edu.oregonstate.mist.sadapi.db.SadDAO
import javax.ws.rs.GET
import javax.ws.rs.Path
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
}
