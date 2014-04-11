package models;

import java.io.UnsupportedEncodingException;
import java.util.List;

import controllers.Encodings;
import play.api.mvc.Call;
import play.data.Form;

public class Page<A> {
  public List<A> items;

  public int page;

  public long offset;

  public long total;

  public String prev;

  public String next;

  public Page(Form<A> form, final Call baseUrl, final int page, final long offset, final long total,
      final List<A> items) throws UnsupportedEncodingException {
    this.items = items;
    this.page = page;
    this.offset = offset;
    this.total = total;
    if (page > 0) {
      prev = Encodings.encodeForm(baseUrl + "?page=" + (page - 1), form.data());
    }
    if (this.offset + items.size() < this.total) {
      next = Encodings.encodeForm(baseUrl + "?page=" + (page + 1), form.data());
    }
  }
}
