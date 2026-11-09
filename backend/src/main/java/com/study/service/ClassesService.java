package com.study.service;

import com.study.pojo.Classes;
import com.study.pojo.PageResult;

public interface ClassesService {
   PageResult<Classes> pageClasses(String className, Integer status, Integer pageNum, Integer pageSize);

   void addClass(Classes classes);

    Classes getById(Integer id);

    void updateClass(Classes classes);

    void deleteById(Integer id);
}
