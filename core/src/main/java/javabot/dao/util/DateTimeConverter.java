package javabot.dao.util;

import java.util.Date;

import org.mongodb.morphia.converters.DateConverter;
import org.mongodb.morphia.mapping.MappedField;
import org.mongodb.morphia.mapping.MappingException;
import org.joda.time.DateTime;

public class DateTimeConverter extends DateConverter {
  public DateTimeConverter() {
    super(DateTime.class);
  }

  @Override
  public Object decode(final Class targetClass, final Object val, final MappedField optionalExtraInfo)
      throws MappingException {
    Date d = (Date) super.decode(targetClass, val, optionalExtraInfo);
    return new DateTime(d.getTime());
  }

  @Override
  public Object encode(final Object value, final MappedField optionalExtraInfo) {
    if (value == null) {
      return null;
    }
    return ((DateTime) value).toDate();
  }
}
