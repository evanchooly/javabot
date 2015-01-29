package javabot.operations;

import com.antwerkz.sofia.Sofia;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Singleton;
import javabot.Message;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.pircbotx.Channel;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Displays RFC url and title
 */
@Singleton
public class RFCOperation extends BotOperation {
  public static final String prefix = "rfc ";
  LoadingCache<String, String> rfcTitleCache = CacheBuilder.newBuilder()
      .maximumSize(100)
      .expireAfterWrite(1, TimeUnit.HOURS)
      .recordStats()
      .build(
          new CacheLoader<String, String>() {
            @SuppressWarnings("NullableProblems")
            public String load(final String url)
                throws IOException {
              Document doc = Jsoup.connect(url).get();
              return doc.title();
            }
          });

  @Override
  public boolean handleMessage(final Message event) {
    final String message = event.getValue().toLowerCase();
    final Channel channel = event.getChannel();
    if (message.startsWith(prefix)) {
      final String rfcText = message.substring(prefix.length()).trim();
      try {
        int rfc = Integer.parseInt(rfcText);
        try {
          String url = String.format("http://www.faqs.org/rfcs/rfc%d.html", rfc);
          getBot().postMessage(channel, event.getUser(),
              Sofia.rfcSucceed(url, rfcTitleCache.get(url)),
              event.isTell());
        } catch (ExecutionException e) {
          // from rfc.fail
          getBot().postMessage(channel, event.getUser(),
              Sofia.rfcFail(rfcText), event.isTell());
        }
      } catch (NumberFormatException e) {
        getBot().postMessage(channel, event.getUser(),
            Sofia.rfcInvalid(rfcText), event.isTell());
      }
      return true;
    }
    return false;
  }
}