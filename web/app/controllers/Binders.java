package controllers;

import org.bson.types.ObjectId;
import play.api.mvc.PathBindable;
import scala.Function1;
import scala.util.Either;
import scala.util.Left;
import scala.util.Right;

public class Binders {
  public static PathBindable<ObjectId> objectIdBinder = new PathBindable<ObjectId>() {
    @Override
    public Either<String, ObjectId> bind(final String key, final String value) {
      return (ObjectId.isValid(value))
          ? new Right(new ObjectId(value))
          : new Left("Cannot parse parameter " + key + " as BSON ObjectId");
    }

    @Override
    public String unbind(final String key, final ObjectId value) {
      return value.toString();
    }

    @Override
    public String javascriptUnbind() {
      return null;
    }

    @Override
    public <B> Object transform(final Function1<ObjectId, B> toB, final Function1<B, ObjectId> toA) {
      return null;
    }
  };
}
