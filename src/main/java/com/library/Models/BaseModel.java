package com.library.Models;

import java.sql.SQLException;

public abstract class BaseModel {
    public abstract static class db {
        public abstract void save() throws SQLException;
    }
}
