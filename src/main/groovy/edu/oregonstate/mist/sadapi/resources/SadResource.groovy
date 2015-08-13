package edu.oregonstate.mist.sadapi.resources

import edu.oregonstate.mist.sadapi.core.Sad
import edu.oregonstate.mist.sadapi.db.SadDAO
import javax.validation.Valid
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.PUT
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
        Object result
        if (termCodeEntry && applNo && seqNo) {
            result = sadDAO.queryOne(pidm, termCodeEntry, applNo, seqNo)
            if (!result) {
                throw new WebApplicationException(Response.Status.NOT_FOUND) }
        } else if (termCodeEntry || applNo || seqNo) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST)
        } else {
            result = sadDAO.queryAll(pidm)
            if (result.isEmpty()) {
                throw new WebApplicationException(Response.Status.NOT_FOUND) }
        }
        result
    }

    @PUT
    @Path('{pidm: \\d+}')
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Object update(@PathParam('pidm') Long pidm, @Valid Sad sad) {
        Object result = sadDAO.update(pidm,
                                      sad.termCodeEntry,
                                      sad.applNo,
                                      sad.seqNo,
                                      sad.apdcDate,
                                      sad.apdcCode,
                                      sad.maintInd)
        if (!result) {
            throw new WebApplicationException(Response.Status.NOT_FOUND)
        }
        result
    }
}
