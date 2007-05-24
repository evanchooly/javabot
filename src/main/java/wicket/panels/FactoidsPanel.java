package wicket.panels;

import javabot.dao.FactoidDao;
import javabot.dao.model.Factoid;
import javabot.dao.util.QueryParam;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.text.SimpleDateFormat;
import java.util.Iterator;

// User: joed
// Date: May 24, 2007
// Time: 1:28:37 PM

// 
public class FactoidsPanel extends Panel {

    @SpringBean
    FactoidDao dao;


    public FactoidsPanel(String id) {
        super(id);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SortableFactoidProvider dp = new SortableFactoidProvider(dao);
        final DataView dataView = new DataView("sorting", dp) {
            protected void populateItem(final Item item) {
                Factoid f = (Factoid) item.getModelObject();
                item.add(new Label("id", String.valueOf(f.getId())));
                item.add(new Label("name", f.getName()));
                item.add(new Label("value", f.getValue()));
                item.add(new Label("user", f.getUserName()));
                item.add(new Label("date", sdf.format(f.getUpdated())));

                item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
                    public Object getObject() {
                        return (item.getIndex() % 2 == 1) ? "spec" : "odd";
                    }
                }));
            }
        };

        dataView.setItemsPerPage(40);

        add(new OrderByBorder("orderByName", "name", dp) {
            protected void onSortChanged() {
                dataView.setCurrentPage(0);
            }
        });

        add(new OrderByBorder("orderByDate", "updated", dp) {
            protected void onSortChanged() {
                dataView.setCurrentPage(0);
            }
        });

        add(new OrderByBorder("orderByNick", "username", dp) {
            protected void onSortChanged() {
                dataView.setCurrentPage(0);
            }
        });

        add(new OrderByBorder("orderByValue", "value", dp) {
            protected void onSortChanged() {
                dataView.setCurrentPage(0);
            }
        });

        add(dataView);

        add(new PagingNavigator("navigator", dataView));
    }


    private class SortableFactoidProvider extends SortableDataProvider {


        private FactoidDao m_dao;

        /**
         * constructor
         */
        public SortableFactoidProvider(FactoidDao dao) {
            // set default sort
            setSort("name", true);
            this.m_dao = dao;
        }

        /**
         * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(int,int)
         */
        public Iterator iterator(int first, int count) {
            SortParam sp = getSort();
            QueryParam qp = new QueryParam(first, count, sp.getProperty(), sp.isAscending());

            return (Iterator) m_dao.getFactoids(qp);
        }

        /**
         * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
         */
        public int size() {
            return m_dao.getNumberOfFactoids().intValue();
        }

        /**
         * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
         */
        public IModel model(Object object) {
            return new DetachableFactoidModel((Factoid) object);
        }
    }

    private class DetachableFactoidModel extends LoadableDetachableModel {
        private long id;
        private transient Factoid contact;

        @SpringBean
        private FactoidDao m_dao;

        /**
         * @param f
         */
        public DetachableFactoidModel(Factoid f) {

            this(f.getId());
            contact = f;
        }

        /**
         * @param id
         */
        public DetachableFactoidModel(long id) {
            if (id == 0) {
                throw new IllegalArgumentException();
            }
            this.id = id;
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        public int hashCode() {
            return new Long(id).hashCode();
        }

        /**
         * used for dataview with ReuseIfModelsEqualStrategy item reuse strategy
         *
         * @see org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy
         * @see java.lang.Object#equals(java.lang.Object)
         */
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            } else if (obj == null) {
                return false;
            } else if (obj instanceof DetachableFactoidModel) {
                DetachableFactoidModel other = (DetachableFactoidModel) obj;
                return other.id == this.id;
            }
            return false;
        }

        /**
         * @see org.apache.wicket.model.LoadableDetachableModel#load()
         */
        protected Object load() {
            // loads contact from the database
            return getFactoidDB().get(id);

        }

        protected FactoidDao getFactoidDB() {
            InjectorHolder.getInjector().inject(this);
            return m_dao;
        }


    }

}

