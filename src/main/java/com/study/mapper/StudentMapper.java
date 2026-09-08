package com.study.mapper;

import com.study.pojo.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {
    /*
    查询总记录数
     */
    Long Count(String name, String studentNo, Integer education, Integer classId);
    /*
    分页查询学生列表
     */
    List<Student> PageStudent(Integer start, Integer pageSize, String name, String studentNo, Integer education, Integer classId);
    /*
    新增学生记录
     */
    @Insert("insert into student(name,student_no,gender,education,class_id,create_time,update_time,phone)" +
            "            values(#{name},#{studentNo},#{gender},#{education},#{classId},#{createTime},#{updateTime},#{phone})")
    void insertStudent(Student student);
    /*
    根据id查询学生信息
     */
    @Select("select * from student where id=#{id}")
    Student getById(Integer id);
    /*
    更新学生记录
     */
    void updateStudent(Student student);
    /*
    删除学生记录
     */
    @Delete("delete from student where id=#{id}")
    void deleteById(Integer id);
    /*
    根据id更改学生的违纪信息
     */
    @Update("update student set update_time=#{updateTime},violation_count=#{violationCount},violation_score=#{violationScore}" +
            " where id=#{id}")
    void updateViolation(Student student);
}
