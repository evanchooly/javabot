package javabot;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.irc.IrcEndpoint;
import org.apache.camel.component.irc.IrcMessage;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ProcessorDefinition;

public class CamelBot extends RouteBuilder {
    private String nick = "muleknuckle";
    private String url = "irc:" + nick + "@irc.freenode.net/#basementcoders";

    @Override
    public void configure() throws Exception {
        final ProcessorDefinition defn = from(url)
            .process(new Processor() {
                public void process(Exchange exchange) {
                    if (exchange.getIn().getBody() != null) {
                        final IrcMessage msg = exchange.getIn().getBody(IrcMessage.class);
                        System.out.println("CamelBot.process: msg = " + msg);
                    }
                }
            });
        defn.to(url);
    }

    public static void main(String[] args) throws Exception {
        final CamelContext camel = new DefaultCamelContext();
        camel.addRoutes(new CamelBot());
        camel.start();
        System.out.println("camel = " + camel);
    }
}
