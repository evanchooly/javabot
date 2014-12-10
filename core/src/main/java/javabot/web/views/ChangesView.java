package javabot.web.views;

import com.google.inject.Injector;
import javabot.dao.ChangeDao;
import javabot.dao.util.QueryParam;
import javabot.model.Change;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ChangesView extends PagedView<Change> {
    private final Change filter;
    @Inject
    private ChangeDao changeDao;

    public ChangesView(final Injector injector, final HttpServletRequest request, final int page, Change filter) {
        super(injector, request, page);
        this.filter = filter;
    }

    @Override
    public String getPagedView() {
        return "changes.ftl";
    }

    @Override
    public Change getFilter() {
        return filter;
    }

    @Override
    public Long countItems() {
        return changeDao.count(filter);
    }

    @Override
    protected String getPageUrl() {
        return "/changes";
    }

    @Override
    public List<Change> getPageItems() {
        return changeDao.getChanges(new QueryParam(getIndex(), ITEMS_PER_PAGE, "updated", true), filter);
    }
}
