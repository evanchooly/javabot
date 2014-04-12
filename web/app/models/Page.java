package models;

import java.io.UnsupportedEncodingException;
import java.util.List;

import controllers.Encodings;
import play.api.mvc.Call;
import play.data.Form;
import play.libs.F.Tuple;

public class Page<A> {
  public List<A> items;

  public int page;

  public long offset;

  public long total;

  public String prev;

  public String next;

  public Page(Form<A> form, final Call baseUrl, final int page, final long offset, final Tuple<Long, List<A>> pair) {
    this.page = page;
    this.offset = offset;
    this.total = pair._1;
    this.items = pair._2;
    try {
      if (page > 0) {
        prev = Encodings.encodeForm(baseUrl + "?page=" + (page - 1), form.data());
      }
      if (this.offset + this.items.size() < this.total) {
        next = Encodings.encodeForm(baseUrl + "?page=" + (page + 1), form.data());
      }
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
