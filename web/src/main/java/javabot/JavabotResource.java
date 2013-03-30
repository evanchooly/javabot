package javabot;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import javabot.dao.ChannelDao;
import javabot.json.Views.PUBLIC;

@Path("/")
public class JavabotResource {
  @Inject
  private ChannelDao channelDao;
  @Inject
  private JacksonMapper mapper;

  /**
   * Method handling HTTP GET requests. The returned object will be sent to the client as "text/plain" media type.
   *
   * @return String that will be returned as a text/plain response.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("channels")
  public String channels(@Context HttpServletRequest request) throws JsonProcessingException {
    return mapper.writerWithView(PUBLIC.class).writeValueAsString(channelDao.findLogged(Boolean.TRUE));
  }
}

