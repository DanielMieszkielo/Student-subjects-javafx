package com;

import com.library.DAO.*;
import com.library.Models.StudentClass;
import com.library.Models.Teacher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class settings {
    public static final String appTitle = "Student app";

    public static final String URL = "D:/";
    public static final String dbFileName = "db.sqlite";

    public static final List<Class<? extends ModelDAO>> models = Arrays.asList(
            StudentDAO.class,
            TeacherDAO.class,
            ClassDAO.class,
            StudentClassDAO.class
    );
}
