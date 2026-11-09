package com.study.mapper;

import com.study.pojo.Classes;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClassesMapper {
    /*
    查询班级总数
     */

    long count(String className, Integer status);
    /*
    分页查询班级列表
     */
    List<Classes> list(String className, Integer status, Integer start, Integer pageSize);
    /*
    新增一条班级记录
     */
    @Insert("insert into classes(class_name, status,main_teacher, class_room, class_number,start_date,end_date,create_time,update_time) " +
            "values(#{className},#{status},#{mainTeacher},#{classRoom},#{classNumber},#{startDate},#{endDate},#{createTime},#{updateTime})"
    )
    void insertClasses(Classes classes);
    /*
    根据id查询班级记录
     */
    @Select("select * from classes where id=#{id}")
    Classes getById(Integer id);
    /*
    更新班级记录
     */
    void updateClasses(Classes classes);
    /*
    删除班级记录
     */
    @Delete("delete from classes where id=#{id}")
    void deleteById(Integer id);
}
