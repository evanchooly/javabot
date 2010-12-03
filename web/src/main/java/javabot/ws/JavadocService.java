package javabot.ws;

import java.util.List;
import javax.jws.WebService;

@WebService
public interface JavadocService {
    List<String> lookup(String request);
}
