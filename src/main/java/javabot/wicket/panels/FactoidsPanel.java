package javabot.wicket.panels;

import javabot.dao.FactoidDao;
import javabot.dao.util.QueryParam;
import javabot.model.Factoid;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilteredAbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.GoAndClearFilter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.text.SimpleDateFormat;
import java.util.Iterator;

public class FactoidsPanel extends Panel {

    private class UpdatedPanel extends Panel {

        public UpdatedPanel(String s, IModel iModel) {
            super(s, iModel);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Factoid factoid = (Factoid) iModel.getObject();
            add(new Label("updated", sdf.format(factoid.getUpdated())));
        }
    }

    @SpringBean
    FactoidDao dao;

    public FactoidsPanel(String id) {
        super(id);

        IColumn[] columns = new IColumn[4];

        columns[0] = new TextFilteredPropertyColumn(new Model("Name"), "name", "name");
        columns[1] = new TextFilteredPropertyColumn(new Model("Value"), "value", "value");
        columns[2] = new TextFilteredPropertyColumn(new Model("Added by"), "userName", "userName");
        columns[3] = new FilteredAbstractColumn(new Model("Updated")) {
            public Component getFilter(String componentId, FilterForm form) {
                return new GoAndClearFilter(componentId, form);
            }

            public void populateItem(Item cellItem, String componentId, IModel model) {
                cellItem.add(new UpdatedPanel(componentId, model));
            }
        };
        SortableFactoidProvider dp = new SortableFactoidProvider(dao);
        DataTable dataView = new DataTable("dataView", columns, dp, 40);

//        dataView.addTopToolbar(new FilterToolbar(dataView, dp));
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

        public SortableFactoidProvider(FactoidDao dao) {
            setSort("name", true);
            this.m_dao = dao;
        }

        public Iterator iterator(int first, int count) {
            SortParam sp = getSort();
            QueryParam qp = new QueryParam(first, count, sp.getProperty(), sp.isAscending());

            return m_dao.getFactoidsFiltered(qp, filter).iterator();
        }

        public int size() {
            return m_dao.factoidCountFiltered(filter).intValue();
        }

        public IModel model(Object object) {
            return new DetachableFactoidModel((Factoid) object);
        }
    }

    private class DetachableFactoidModel extends LoadableDetachableModel {
        private long id;

        public DetachableFactoidModel(Factoid f) {
            this(f.getId());
        }

        public DetachableFactoidModel(long factoidId) {
            if (factoidId == 0) {
                throw new IllegalArgumentException();
            }
            this.id = factoidId;
        }

        @Override
        public int hashCode() {
            return new Long(id).hashCode();
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
            } else if (obj instanceof DetachableFactoidModel) {
                DetachableFactoidModel other = (DetachableFactoidModel) obj;
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