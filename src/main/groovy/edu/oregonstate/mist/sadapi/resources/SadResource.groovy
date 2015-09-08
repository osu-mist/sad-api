package edu.oregonstate.mist.sadapi.resources

import edu.oregonstate.mist.sadapi.core.Sad
import edu.oregonstate.mist.sadapi.core.Error
import edu.oregonstate.mist.sadapi.db.SadDAO
import javax.validation.Valid
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.ResponseBuilder
import javax.ws.rs.core.MediaType
import java.sql.SQLException

@Path('/')
class SadResource {
    private SadDAO sadDAO

    public SadResource(SadDAO sadDAO) {
        this.sadDAO = sadDAO
    }

    @GET
    @Path('{pidm: \\d+}')
    @Produces(MediaType.APPLICATION_JSON)
    public Response read(@PathParam('pidm') Long pidm,
                         @QueryParam('termCodeEntry') String termCodeEntry,
                         @QueryParam('applNo') Long applNo,
                         @QueryParam('seqNo') Long seqNo) {
        ResponseBuilder responseBuilder
        if (termCodeEntry && applNo && seqNo) {
            Sad result = sadDAO.queryOne(pidm, termCodeEntry, applNo, seqNo)
            responseBuilder = (result) ? ok(result) : notFound()
        } else if (termCodeEntry || applNo || seqNo) {
            responseBuilder = badRequest('termCodeEntry, applNo, and seqNo query parameters are required')
        } else {
            List<Sad> result = sadDAO.queryAll(pidm)
            responseBuilder = (result) ? ok(result) : notFound()
        }
        responseBuilder.build()
    }

    @PUT
    @Path('{pidm: \\d+}')
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam('pidm') Long pidm, @Valid Sad sad) {
        ResponseBuilder responseBuilder
        try {
            Sad result = sadDAO.update(pidm, sad.termCodeEntry, sad.applNo, sad.seqNo, sad.apdcDate, sad.apdcCode, sad.maintInd)
            responseBuilder = (result) ? ok(result) : notFound()
        } catch (SQLException|IllegalArgumentException exception) {
            responseBuilder = badRequest(exception.message)
        }
        responseBuilder.build()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Valid Sad sad) {
        ResponseBuilder responseBuilder
        try {
            Sad result = sadDAO.create(sad.pidm, sad.termCodeEntry, sad.applNo, sad.apdcDate, sad.apdcCode, sad.maintInd)
            responseBuilder = (result) ? created(result) : badRequest('Bad Request')
        } catch (SQLException|IllegalArgumentException exception) {
            responseBuilder = badRequest(exception.message)
        }
        responseBuilder.build()
    }

    @DELETE
    @Path('{pidm: \\d+}')
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam('pidm') Long pidm,
                           @QueryParam('termCodeEntry') String termCodeEntry,
                           @QueryParam('applNo') Long applNo,
                           @QueryParam('seqNo') Long seqNo) {
        ResponseBuilder responseBuilder
        try {
            Sad result = sadDAO.delete(pidm, termCodeEntry, applNo, seqNo)
            responseBuilder = (result) ? ok(result) : notFound()
        } catch (SQLException sqlException) {
            responseBuilder = badRequest(sqlException.message)
        }
        responseBuilder.build()
    }

    private static ResponseBuilder ok(Object entity) {
        ResponseBuilder responseBuilder = Response.ok()
        responseBuilder.entity(entity)
    }

    private static ResponseBuilder created(Object entity) {
        ResponseBuilder responseBuilder = Response.status(Response.Status.CREATED)
        responseBuilder.entity(entity)
    }

    private static ResponseBuilder notFound() {
        ResponseBuilder responseBuilder = Response.status(Response.Status.NOT_FOUND)
        responseBuilder.entity(new Error(
                status: 404,
                developerMessage: 'Not Found',
                userMessage: 'Not Found',
                code: 1404,
                details: 'http://example.com/errors/1404'
        ))
    }

    private static ResponseBuilder badRequest(String message) {
        ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST)
        responseBuilder.entity(new Error(
                status: 400,
                developerMessage: message,
                userMessage: 'Bad Request',
                code: 1400,
                details: 'http://example.com/errors/1400'
        ))
    }
}
