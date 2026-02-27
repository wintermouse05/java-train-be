package com.example.javatrainbe.dao;

import com.example.javatrainbe.entity.User;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Sql;
import org.seasar.doma.jdbc.Result;

import java.util.List;
import java.util.Optional;

/**
 * Doma DAO cho User entity
 */
@Dao
public interface UserDao {

    @Select
    @Sql("SELECT * FROM `user` WHERE user_name = /*userName*/'admin'")
    Optional<User> findByUserName(String userName);

    @Select
    @Sql("SELECT * FROM `user`")
    List<User> selectAll();

    @Insert
    Result<User> insert(User user);
}
