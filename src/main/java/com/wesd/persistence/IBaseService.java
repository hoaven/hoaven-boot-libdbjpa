package com.wesd.persistence;

import java.util.List;
import java.util.Map;

public interface IBaseService {
    default <T> T insert(final T model) {
        throw new WesdJpaNotSupportMethodException("not support method create");
    }

    default <T> T update(final T model) {
        throw new WesdJpaNotSupportMethodException("not support method update");
    }

    default <T> void delete(final T model) {
        throw new WesdJpaNotSupportMethodException("not support method delete");
    }

    default <T extends BaseModel> void remove(final T model) {
        //逻辑删除
        throw new WesdJpaNotSupportMethodException("not support method remove");
    }

    default <T> T getById(Class<T> modelClass, final Long id) {
        throw new WesdJpaNotSupportMethodException("not support method getById");
    }

    default <T> List<T> listQuery(final String hql, RowBounds rowBounds, Map<String, Object> params) {
        throw new WesdJpaNotSupportMethodException("not support method listQuery RowBounds name params");
    }

    default <T> List<T> listQuery(final String hql, RowBounds rowBounds, Object... params) {
        throw new WesdJpaNotSupportMethodException("not support method listQuery RowBounds");
    }

    default <T> List<T> listQuery(final String hql, Object... params) {
        throw new WesdJpaNotSupportMethodException("not support method listQuery");
    }

    default <T> T singleBySQL(final String hql, Object... params) {
        throw new WesdJpaNotSupportMethodException("not support method singleQuery");
    }

    default <T> List<T> listQuery(final String hql, Map<String, Object> params) {
        throw new WesdJpaNotSupportMethodException("not support method listQuery by param");
    }

    default Integer update(String hql, Object... params) {
        throw new WesdJpaNotSupportMethodException("not support method update by param");
    }
}
