package javabot.dao.impl;

import javabot.dao.AbstractDaoImpl;
import javabot.dao.EventDao;
import javabot.model.AdminEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventDaoImpl extends AbstractDaoImpl<AdminEvent> implements EventDao {
    protected EventDaoImpl() {
        super(AdminEvent.class);
    }

    @Override
    public List<AdminEvent> findUnprocessed() {
        return (List<AdminEvent>) getEntityManager()
                .createNamedQuery(EventDao.FIND_ALL)
                .getResultList();
    }
}
