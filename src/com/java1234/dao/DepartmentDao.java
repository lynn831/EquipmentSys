package com.java1234.dao;

import java.util.List;

import com.java1234.model.Department;
import com.java1234.model.PageBean;

public interface DepartmentDao {
    //查询部门，返回的结果有多个所以用list
	public List<Department> find(PageBean pageBean,Department s_department);
	//查询记录数
	public int count(Department s_department);
	
	public void add(Department department);
	
	public void update(Department department);
	
	public void delete(int id);
	//loadById的意思是，点击修改页面，带过去一个id，将实体找出来。
	public Department loadById(int id);
}
