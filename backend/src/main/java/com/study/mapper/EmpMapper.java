package com.study.mapper;

import com.study.pojo.Emp;
import com.study.pojo.Result;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface EmpMapper {
    /*
    查询总记录
     */
    @Select("select count(*) from emp  e left join dept d on e.dept_id=d.id ")
    public Long count();
    /*
    条件统计总数（与 list 相同的筛选条件，保证分页 total 准确）
     */
    Long countEmp(String name, Integer gender, LocalDate begin, LocalDate end);
    /*
    分页查询
     */
//    @Select(" select e.*,d.name deptName from emp e left join dept d on e.dept_id=d.id\n" +
//            "                                   where e.name like concat('%',#{name},'%')\n" +
//            "                                   and e.gender =#{gender}\n" +
//            "                                   and e.entry_date between #{begin} and #{end}\n" +
//            "                                   order by e.update_time desc limit #{start},#{pageSize}")
    public List<Emp> list(Integer start, Integer pageSize, String name, Integer gender, LocalDate begin, LocalDate end);
    @Options(useGeneratedKeys = true,keyProperty = "id") //获取生成的主键
    @Insert("insert into emp(username,name, gender, phone, job, salary, entry_date, dept_id, imag, create_time, update_time) "+
            "values (#{username},#{name},#{gender},#{phone},#{job},#{salary},#{entryDate},#{deptId},#{imag},#{createTime},#{updateTime})"
    )
    void insert(Emp emp);
    /*
    根据id删除员工基本信息
     */
    void deleteByIds(List<Integer> ids);

    /*
    根据id 查询员工信息
     */
    Emp getById(Integer id);
    /*
    根据id更新员工基本信息
     */
    void updateById(Emp emp);

    /*
    统计员工岗位人数
     */
    List<Map<String,Object>> countEmpJobData();
    /*
    统计员工性别人数
     */
    List<Map<String, Object>> countEmpGenderData();

    /*
    根据用户名密码查询员工（登录用）
     */
    @Select("select * from emp where username=#{username} and password=#{password}")
    Emp login(String username, String password);
}

