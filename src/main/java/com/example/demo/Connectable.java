package com.example.demo;

import java.sql.ResultSet;

public interface Connectable {
    void ProcessData(ResultSet rs, String opCode);
    SchoolController getSchoolController ();
}
