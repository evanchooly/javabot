package javabot;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import javabot.dao.ChannelDao;

@Path("/")
public class JavabotResource {
  @Inject
  private ChannelDao channelDao;

  /**
   * Method handling HTTP GET requests. The returned object will be sent to the client as "text/plain" media type.
   *
   * @return String that will be returned as a text/plain response.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("channels")
  public String channels() {
    return "Got it: " + channelDao;
  }
}

