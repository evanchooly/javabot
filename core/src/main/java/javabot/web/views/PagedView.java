package javabot.web.views;

import com.google.inject.Injector;
import javabot.model.Factoid;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public abstract class PagedView<V> extends MainView {
    public static final int ITEMS_PER_PAGE = 50;
    private final Integer itemsPerPage = ITEMS_PER_PAGE;
    private int page;
    private Long itemCount;

    public PagedView(final Injector injector, final HttpServletRequest request, final int currentPage) {
        super(injector, request);
        this.page = currentPage;
    }

    @Override
    public final String getChildView() {
        return "paged.ftl";
    }

    public abstract String getPagedView();

    public final Long getItemCount() {
        if (itemCount == null) {
            itemCount = countItems();
        }
        return itemCount;
    }

    public abstract Long countItems();

    public V getFilter() {
        return null;
    }

    public int getPage() {
        if (page < 1) {
            page = 1;
        } else if (page > getPageCount()) {
            page = getPageCount();
        }
        return page;
    }

    public int getIndex() {
        int index = (getPage() - 1) * getItemsPerPage();
        if (getItemCount() == 0) {
            return -1;
        } else if (index > getItemCount()) {
            return (getPageCount() - 1) * getItemsPerPage();
        } else {
            return index;
        }
    }

    public int getPageCount() {
        return Double.valueOf(Math.ceil(1.0 * getItemCount() / getItemsPerPage())).intValue();
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public String getNextPage() {
        final int nextPage = getPage() + 1;
        return nextPage <= getPageCount() ? getPageUrl() + "?page=" + nextPage : null;
    }

    protected abstract String getPageUrl();

    public String getPreviousPage() {
        final int previousPage = getPage() == 1 ? -1 : getPage() - 1;
        return previousPage > 0 ? (getPageUrl() + "?page=" + previousPage) : null;
    }

    public long getEndRange() {
        return Math.min(getItemCount(), getStartRange() + getItemsPerPage() - 1);
    }

    public long getStartRange() {
        return getIndex() + 1L;
    }

    public abstract List<V> getPageItems();
}
