package org.moson.checktx.app.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import org.moson.checktx.app.bean.StackBean;

import org.moson.checktx.app.dao.StackBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig stackBeanDaoConfig;

    private final StackBeanDao stackBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        stackBeanDaoConfig = daoConfigMap.get(StackBeanDao.class).clone();
        stackBeanDaoConfig.initIdentityScope(type);

        stackBeanDao = new StackBeanDao(stackBeanDaoConfig, this);

        registerDao(StackBean.class, stackBeanDao);
    }
    
    public void clear() {
        stackBeanDaoConfig.clearIdentityScope();
    }

    public StackBeanDao getStackBeanDao() {
        return stackBeanDao;
    }

}
