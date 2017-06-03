package com.example.nanchen.bridgitchallenge.database;

import com.example.nanchen.bridgitchallenge.App;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by nanchen on 2017-06-02.
 */

public class BaseDao<T> {
    protected Class<T> clazz;
    protected Dao<T,Integer> dao;
    public BaseDao(){
        Class clazz = getClass();
        while (clazz != Object.class) {
            Type t = clazz.getGenericSuperclass();
            if (t instanceof ParameterizedType) {
                Type[] args = ((ParameterizedType) t).getActualTypeArguments();
                if (args[0] instanceof Class) {
                    this.clazz = (Class<T>) args[0];
                    break;
                }
            }
            clazz = clazz.getSuperclass();
        }

        try {
            if (App.getDatabaseHelper()== null) {
                throw new RuntimeException("No DbHelper Found!");
            }
            dao = App.getDatabaseHelper().getDao(this.clazz);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(T t) {
        try {
            dao.createOrUpdate(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addAll(List<T> list){
        try {
            for(T temp:list) {
                dao.createOrUpdate(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(T t) {
        try {
            dao.delete(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(T t) {
        try {
            dao.update(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<T> getAll() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public T getById(Integer i){
        try {
            return dao.queryForId(i);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<T> queryByColumn(String columnName, Object columnValue) {
        try {
            QueryBuilder builder = dao.queryBuilder();
            builder.where().eq(columnName, columnValue);
            return builder.query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
