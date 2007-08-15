package javabot.wicket.panels;

import java.text.SimpleDateFormat;
import java.util.Iterator;

import javabot.dao.ChangeDao;
import javabot.dao.util.QueryParam;
import javabot.model.Change;
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

public class ChangesPanel extends Panel {
    @SpringBean
    ChangeDao dao;

    private class UpdatedPanel extends Panel {
        public UpdatedPanel(String s, IModel iModel) {
            super(s, iModel);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Change change = (Change)iModel.getObject();
            add(new Label("updated", sdf.format(change.getChangeDate())));
        }
    }

    public ChangesPanel(String id) {
        super(id);
        IColumn[] columns = new IColumn[2];
        columns[0] = new TextFilteredPropertyColumn(new Model("Message"), "message", "message");
        columns[1] = new FilteredAbstractColumn(new Model("Updated")) {
            public Component getFilter(String componentId, FilterForm form) {
                return new GoAndClearFilter(componentId, form);
            }

            public void populateItem(Item cellItem, String componentId, IModel model) {
                cellItem.add(new UpdatedPanel(componentId, model));
            }

        };
        SortableChangeProvider dp = new SortableChangeProvider(dao);
        DataTable dataView = new DataTable("dataView", columns, dp, 40);
        dataView.addTopToolbar(new FilterToolbar(dataView, dp));
        dataView.addTopToolbar(new NavigationToolbar(dataView));
        dataView.addTopToolbar(new HeadersToolbar(dataView, dp));
        add(dataView);
    }

    private class SortableChangeProvider extends SortableDataProvider implements IFilterStateLocator {
        private ChangeDao dao;
        private Change filter = new Change();

        public Object getFilterState() {
            return filter;
        }

        public void setFilterState(Object state) {
            filter = (Change)state;
        }

        public SortableChangeProvider(ChangeDao changeDao) {
            setSort("changeDate",false);
            this.dao = changeDao;
        }

        public Iterator iterator(int first, int count) {
            SortParam sp = getSort();
            QueryParam qp = new QueryParam(first, count, sp.getProperty(), sp.isAscending());
            return dao.getChanges(qp, filter).iterator();
        }

        public int size() {
            return dao.count(filter).intValue();
        }

        public IModel model(Object object) {
            return new DetachableChangeModel((Change)object);
        }
    }

    private class DetachableChangeModel extends LoadableDetachableModel {
        private long id;

        public DetachableChangeModel(Change f) {
            this(f.getId());
        }

        public DetachableChangeModel(long changeId) {
            if(changeId == 0) {
                throw new IllegalArgumentException();
            }
            this.id = changeId;
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
            if(obj == this) {
                return true;
            } else if(obj == null) {
                return false;
            } else if(obj instanceof DetachableChangeModel) {
                DetachableChangeModel other = (DetachableChangeModel)obj;
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