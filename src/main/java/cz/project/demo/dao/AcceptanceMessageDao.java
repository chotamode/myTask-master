package cz.project.demo.dao;

import cz.project.demo.model.AcceptanceMessage;
import org.springframework.stereotype.Repository;

@Repository
public class AcceptanceMessageDao extends BaseDao<AcceptanceMessage> {


    public AcceptanceMessageDao() {
        super(AcceptanceMessage.class);
    }
}
