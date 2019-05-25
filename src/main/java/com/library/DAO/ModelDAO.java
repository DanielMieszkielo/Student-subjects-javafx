package com.library.DAO;

import com.library.Models.Student;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class ModelDAO<T> {
    public abstract String createTableSql();
    public abstract T get(HashMap<String, String> params);
    public abstract ArrayList<T> all();
    public abstract void update(T obj);
    public abstract int create(T obj);
}
