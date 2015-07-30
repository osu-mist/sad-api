package edu.oregonstate.mist.sadapi.resources

import edu.oregonstate.mist.sadapi.db.SadDAO
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path('/')
class SadResource {
    private SadDAO sadDAO

    public SadResource(SadDAO sadDAO) {
        this.sadDAO = sadDAO
    }

    @GET
    @Path('{pidm: \\d+}')
    @Produces(MediaType.APPLICATION_JSON)
    public Object read(@PathParam('pidm') Long pidm,
                       @QueryParam('termCodeEntry') String termCodeEntry,
                       @QueryParam('applNo') Long applNo,
                       @QueryParam('seqNo') Long seqNo) {
        if (termCodeEntry && applNo && seqNo)
            return sadDAO.queryOne(pidm, termCodeEntry, applNo, seqNo)
        else if (termCodeEntry || applNo || seqNo)
            throw new WebApplicationException(Response.Status.BAD_REQUEST)
        else
            return sadDAO.queryAll(pidm)
    }
}
