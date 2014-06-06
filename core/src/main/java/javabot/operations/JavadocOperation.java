package javabot.operations;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import com.antwerkz.maven.SPI;
import com.antwerkz.sofia.Sofia;
import javabot.IrcEvent;
import javabot.Message;
import javabot.dao.ApiDao;
import javabot.dao.JavadocClassDao;
import javabot.javadoc.JavadocApi;
import javabot.javadoc.JavadocClass;
import javabot.javadoc.JavadocField;
import javabot.javadoc.JavadocMethod;
import net.swisstech.bitly.BitlyClient;

@SPI(BotOperation.class)
public class JavadocOperation extends BotOperation {
  @Inject
  private ApiDao apiDao;

  @Inject
  private JavadocClassDao dao;

  @Inject
  private BitlyClient client;

  private static final int RESULT_LIMIT = 5;

  @Override
  public List<Message> handleMessage(final IrcEvent event) {
    final String message = event.getMessage();
    final List<Message> responses = new ArrayList<>();
    if (message.toLowerCase().startsWith("javadoc")) {
      String key = message.substring("javadoc".length()).trim();
      if (key.startsWith("-list") || key.isEmpty()) {
        displayApiList(event, responses);
      } else {
        JavadocApi api = null;
        if (key.startsWith("-")) {
          if (key.contains(" ")) {
            api = apiDao.find(key.substring(1, key.indexOf(" ")));
            key = key.substring(key.indexOf(" ") + 1).trim();
            buildResponse(event, responses, api, key);
          } else {
            displayApiList(event, responses);
          }
        } else {
          buildResponse(event, responses, api, key);
        }
      }
    }
    return responses;
  }

  private void buildResponse(IrcEvent event, List<Message> responses, JavadocApi api, String key) {
    final List<String> urls = handle(api, key);
    if (!urls.isEmpty()) {
      StringBuilder urlMessage = new StringBuilder(event.getSender() + ": ");
      String destination = event.getChannel();
      if (urls.size() > RESULT_LIMIT) {
        responses.add(new Message(event.getChannel(), event, Sofia.tooManyResults(event.getSender())));
        destination = event.getSender().getNick();
      }
      urlMessage = buildResponse(event, responses, urls, urlMessage, destination);
      responses.add(new Message(destination, event, urlMessage.toString()));
    } else if (urls.isEmpty()) {
      responses.add(new Message(event.getChannel(), event, Sofia.noDocumentation(key)));
    }
  }

  private StringBuilder buildResponse(IrcEvent event, List<Message> responses, List<String> urls,
      StringBuilder urlMessage, String destination) {
    for (int index = 0; index < urls.size(); index++) {
      if ((urlMessage + urls.get(index)).length() > 400) {
        responses.add(new Message(destination, event, urlMessage.toString()));
        urlMessage = new StringBuilder();
      }
      urlMessage
          .append(index == 0 ? "" : "; ")
          .append(urls.get(index));
    }
    return urlMessage;
  }

  public List<String> handle(final JavadocApi api, final String key) {
    final List<String> urls = new ArrayList<>();
    final int openIndex = key.indexOf('(');
    if (openIndex == -1) {
      parseFieldOrClassRequest(urls, api, key);
    } else {
      parseMethodRequest(urls, api, key, openIndex);
    }
    return urls;
  }

  private void parseFieldOrClassRequest(final List<String> urls, JavadocApi api, final String key) {
    final int finalIndex = key.lastIndexOf('.');
    if (finalIndex == -1) {
      findClasses(api, urls, key);
    } else {
      final String className = key.substring(0, finalIndex);
      final String fieldName = key.substring(finalIndex + 1);
      if (Character.isUpperCase(fieldName.charAt(0)) && !fieldName.toUpperCase().equals(fieldName)) {
        findClasses(api, urls, key);
      } else {
        final List<JavadocField> list = dao.getField(api, className, fieldName);
        for (final JavadocField field : list) {
          urls.add(field.getDisplayUrl(field.toString(), apiDao, client));
        }
      }
    }
  }

  private void findClasses(JavadocApi api, final List<String> urls, final String key) {
    for (final JavadocClass javadocClass : dao.getClass(api, key)) {
      urls.add(javadocClass.getDisplayUrl(javadocClass.toString(), apiDao, client));
    }
  }

  private void parseMethodRequest(final List<String> urls, JavadocApi api, final String key, final int openIndex) {
    final int closeIndex = key.indexOf(')');
    if (closeIndex != -1) {
      int finalIndex = key.lastIndexOf('.', openIndex);
      String methodName;
      String className;
      if (finalIndex == -1) {
        methodName = key.substring(0, openIndex);
        className = methodName;
      } else {
        methodName = key.substring(finalIndex + 1, openIndex);
        className = key.substring(0, finalIndex);
      }
      final String signatureTypes = key.substring(openIndex + 1, closeIndex);
      final List<String> list = new ArrayList<>();
      for (final JavadocMethod method : dao.getMethods(api, className, methodName, signatureTypes)) {
        list.add(method.getDisplayUrl(method.toString(), apiDao, client));
      }
      if(list.isEmpty()) {
        className = methodName;
        for (final JavadocMethod method : dao.getMethods(api, className, methodName, signatureTypes)) {
          list.add(method.getDisplayUrl(method.toString(), apiDao, client));
        }
      }

      urls.addAll(list);
    }
  }

  private void displayApiList(final IrcEvent event, final List<Message> responses) {
    final StringBuilder builder = new StringBuilder();
    for (final JavadocApi api : apiDao.findAll()) {
      if (builder.length() != 0) {
        builder.append("; ");
      }
      String.format("%s(%s)", builder.append(api.getName()), api.getBaseUrl());
    }
    responses.add(new Message(event.getChannel(), event, event.getSender()
        + ", I know of the following APIs: " + builder));
  }
}