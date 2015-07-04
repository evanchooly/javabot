package javabot.web.views;

import com.google.inject.Injector;
import javabot.dao.KarmaDao;
import javabot.dao.util.QueryParam;
import javabot.model.Karma;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class KarmaView extends PagedView<Karma> {
    @Inject
    private KarmaDao karmaDao;

    public KarmaView(final Injector injector, final HttpServletRequest request, final int page) {
        super(injector, request, page);
    }

    @Override
    public Long countItems() {
        return karmaDao.count();
    }

    @Override
    protected String getPageUrl() {
        return "/karma";
    }

    @Override
    public List<Karma> getPageItems() {
        return karmaDao.getKarmas(new QueryParam(getIndex(), ITEMS_PER_PAGE, "value", false));
    }

    @Override
    public String getPagedView() {
        return "karma.ftl";
    }
}
