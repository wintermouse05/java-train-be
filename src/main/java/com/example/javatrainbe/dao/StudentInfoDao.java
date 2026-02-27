package com.example.javatrainbe.dao;

import com.example.javatrainbe.entity.StudentInfo;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Sql;
import org.seasar.doma.Update;
import org.seasar.doma.jdbc.Result;

import java.util.Optional;

/**
 * Doma DAO cho StudentInfo entity
 */
@Dao
public interface StudentInfoDao {

    @Select
    @Sql("SELECT * FROM student_info WHERE student_id = /*studentId*/1")
    Optional<StudentInfo> findByStudentId(Integer studentId);

    @Insert
    Result<StudentInfo> insert(StudentInfo studentInfo);

    @Update
    Result<StudentInfo> update(StudentInfo studentInfo);

    @Delete
    Result<StudentInfo> delete(StudentInfo studentInfo);
}
