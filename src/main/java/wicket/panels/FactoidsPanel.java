package wicket.panels;

import javabot.dao.FactoidDao;
import javabot.dao.model.Factoid;
import javabot.dao.util.QueryParam;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.*;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.text.SimpleDateFormat;
import java.util.Iterator;

// User: joed
// Date: May 24, 2007
// Time: 1:28:37 PM

// 
public class FactoidsPanel extends Panel {

    private class UpdatedPanel extends Panel {

        public UpdatedPanel(String s, IModel iModel) {
            super(s, iModel);
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Factoid factoid = (Factoid) iModel.getObject();
            add(new Label("updated", sdf.format(factoid.getUpdated())));
        }
    }

    @SpringBean
    FactoidDao dao;


    public FactoidsPanel(String id) {
        super(id);


        IColumn[] columns = new IColumn[4];

        // columns[0] = new PropertyColumn(new Model("Id"),
        //         "id", "id");

        // creates a column with a text filter
        columns[0] = new TextFilteredPropertyColumn(new Model("Name"),
                "name", "name") {

        };

        columns[1] = new TextFilteredPropertyColumn(new Model("Value"),
                "value", "value") {
        };

        columns[2] = new TextFilteredPropertyColumn(new Model("Added by"),
                "userName", "userName") {
        };


        columns[3] = new FilteredAbstractColumn(new Model("Updated")) {
            // return the go-and-clear filter for the filter toolbar
            public Component getFilter(String componentId, FilterForm form) {
                return new GoAndClearFilter(componentId, form);
            }

            // add the ActionPanel to the cell item
            public void populateItem(Item cellItem, String componentId,
                                     IModel model) {
                cellItem.add(new UpdatedPanel(componentId, model));
            }

        };


        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SortableFactoidProvider dp = new SortableFactoidProvider(dao);
        final DataTable dataView = new DataTable("dataView", columns, dp, 40);

        dataView.addTopToolbar(new FilterToolbar(dataView, dp));

        dataView.addTopToolbar(new NavigationToolbar(dataView));

        dataView.addTopToolbar(new HeadersToolbar(dataView, dp));

        add(dataView);

    }


    private class SortableFactoidProvider extends SortableDataProvider implements IFilterStateLocator {


        private FactoidDao m_dao;

        private Factoid filter = new Factoid();

        public Object getFilterState() {
            return filter;
        }

        public void setFilterState(Object state) {
            filter = (Factoid) state;
        }


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

            return (Iterator) m_dao.getFactoidsFiltered(qp, filter);
        }

        /**
         * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
         */
        public int size() {
            return m_dao.factoidCountFiltered(filter).intValue();
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

