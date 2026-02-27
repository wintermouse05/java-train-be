package com.example.javatrainbe.dao;

import com.example.javatrainbe.entity.Student;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Sql;
import org.seasar.doma.Update;
import org.seasar.doma.jdbc.Result;

import java.util.List;
import java.util.Optional;

/**
 * Doma DAO cho Student entity
 */
@Dao
public interface StudentDao {

    @Select
    @Sql("SELECT * FROM student")
    List<Student> selectAll();

    @Select
    @Sql("SELECT * FROM student WHERE student_id = /*studentId*/1")
    Student selectById(Integer studentId);

    @Select
    @Sql("SELECT * FROM student WHERE student_code = /*studentCode*/'SV001'")
    Optional<Student> findByStudentCode(String studentCode);

    @Insert
    Result<Student> insert(Student student);

    @Update
    Result<Student> update(Student student);

    @Delete
    Result<Student> delete(Student student);
}
