package javabot.wicket.panels;

import java.text.SimpleDateFormat;
import java.util.Iterator;

import javabot.dao.KarmaDao;
import javabot.dao.util.QueryParam;
import javabot.model.Karma;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

// User: joed
// Date: May 24, 2007
// Time: 1:28:37 PM

//
public class KarmaPanel extends Panel {

    //Karma is currently stored as Factiods
    //karma nick | value

    @SpringBean
    private
    KarmaDao dao;

    public KarmaPanel(String id) {
        super(id);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SortableKarmaProvider dp = new SortableKarmaProvider(dao);
        final DataView dataView = new DataView("sorting", dp) {
            @Override
            protected void populateItem(final Item item) {
                Karma f = (Karma) item.getModelObject();
                //item.add(new Label("id", String.valueOf(f.getId())));
                item.add(new Label("Nick", f.getName()));
                item.add(new Label("Karma", String.valueOf(f.getValue())));
                item.add(new Label("Last Donor", f.getUserName()));
                item.add(new Label("Date", sdf.format(f.getUpdated())));

                item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
                    @Override
                    public Object getObject() {
                        return (item.getIndex() % 2 == 1) ? "spec" : "odd";
                    }
                }));
            }
        };

        dataView.setItemsPerPage(40);

        add(new OrderByBorder("orderByName", "name", dp) {
            @Override
            protected void onSortChanged() {
                dataView.setCurrentPage(0);
            }
        });

        add(new OrderByBorder("orderByDate", "updated", dp) {
            @Override
            protected void onSortChanged() {
                dataView.setCurrentPage(0);
            }
        });

        add(new OrderByBorder("orderByNick", "nick", dp) {
            @Override
            protected void onSortChanged() {
                dataView.setCurrentPage(0);
            }
        });

        add(new OrderByBorder("orderByValue", "value", dp) {
            @Override
            protected void onSortChanged() {
                dataView.setCurrentPage(0);
            }
        });

        add(dataView);
        add(new PagingNavigator("navigator", dataView));
    }

    private class SortableKarmaProvider extends SortableDataProvider {
        private KarmaDao dao;

        public SortableKarmaProvider(KarmaDao karmaDao) {
            setSort("value", true);
            dao = karmaDao;
        }

        public Iterator iterator(int first, int count) {
            SortParam sp = getSort();
            QueryParam qp = new QueryParam(first, count, sp.getProperty(), sp.isAscending());

            return dao.getKarmas(qp).iterator();
        }

        public int size() {
            return dao.getCount().intValue();
        }

        public IModel model(Object object) {
            return new DetachableKarmaModel((Karma) object);
        }
    }

    private class DetachableKarmaModel extends LoadableDetachableModel {
        private long id;

        public DetachableKarmaModel(Karma f) {
            this(f.getId());
        }

        public DetachableKarmaModel(long karmaId) {
            if (karmaId == 0) {
                throw new IllegalArgumentException();
            }
            this.id = karmaId;
        }

        @Override
        public int hashCode() {
            return new Integer((int) id).hashCode();
        }

        /**
         * used for dataview with ReuseIfModelsEqualStrategy item reuse strategy
         *
         * @see ReuseIfModelsEqualStrategy
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (obj == null) {
                return false;
            } else if (obj instanceof DetachableKarmaModel) {
                DetachableKarmaModel other = (DetachableKarmaModel) obj;
                return other.id == this.id;
            }
            return false;
        }

        @Override
        protected Object load() {
            return dao.find(id);
        }
    }
}