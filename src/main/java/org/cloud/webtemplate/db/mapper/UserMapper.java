package org.cloud.webtemplate.db.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.cloud.webtemplate.db.SuperMapper;
import org.cloud.webtemplate.db.entity.User;

import com.baomidou.mybatisplus.core.conditions.Wrapper;

/**
 * User 表数据库控制层接口
 */
public interface UserMapper extends SuperMapper<User> {

    /**
     * 自定义注入方法
     */
    int deleteAll();

    @Select("select test_id as id, name, age, test_type from user")
    List<User> selectListBySQL();

    List<User> selectListByWrapper(@Param("ew") Wrapper wrapper);

}