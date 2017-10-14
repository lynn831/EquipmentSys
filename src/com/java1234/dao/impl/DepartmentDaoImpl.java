package com.java1234.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.java1234.dao.DepartmentDao;
import com.java1234.model.Department;
import com.java1234.model.PageBean;
import com.java1234.util.StringUtil;

@Repository("departmentDao")
public class DepartmentDaoImpl implements DepartmentDao{

	@Resource
	private JdbcTemplate jdbcTemplate;//查询数据库
	
	@Override
	public List<Department> find(PageBean pageBean, Department s_department) {
		StringBuffer sb=new StringBuffer("select * from t_department");//主要是拼接
		if(s_department!=null){
			//静态方法的调用,只能用函数.方法名，不能new一个函数的对象，然后用这个对象.方法名
			if(StringUtil.isNotEmpty(s_department.getDeptName())){
				//拼接的写法  select * from t_department and deptName like '% s_department.getDeptName() %'
				sb.append(" and deptName like '%"+s_department.getDeptName()+"%'");
			}
		}
		if(pageBean!=null){
			//limit强制 SELECT 语句返回指定的记录数 
			//SELECT * FROM table LIMIT 5,10; // 检索记录行 6-15  
			//SELECT * FROM table LIMIT 5;//检索返回结果的前 5 个记录行
			//pageBean.getStart()是起始页 pageBean.getPageSize()是每页记录数
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getPageSize());
		}
		//返回的是一个集合
		final List<Department> departmentList=new ArrayList<Department>();
		jdbcTemplate.query(sb.toString().replaceFirst("and", "where"), new Object[]{}, new RowCallbackHandler() {
			
			@Override
			//ResutlSet是查询后返回的对象，下面的代码相当于jdbc遍历ResultSet，然后返回列表
			public void processRow(ResultSet rs) throws SQLException {
				Department department=new Department();
				department.setId(rs.getInt("id"));
				department.setDeptName(rs.getString("deptName"));
				department.setRemark(rs.getString("remark"));
			    //列表添加的方式
				departmentList.add(department);
			}
		});
		return departmentList;
	}

	@Override
	//获取总记录数
	public int count(Department s_department) {
		//select count(*) from t_department  返回总的记录数，就是个数据
		StringBuffer sb=new StringBuffer("select count(*) from t_department");
		if(s_department!=null){
			if(StringUtil.isNotEmpty(s_department.getDeptName())){
				sb.append(" and deptName like '%"+s_department.getDeptName()+"%'");
			}
		}
		//返回的的是总记录的条数，用这种方法，返回类型是Integer.class 就是Interger的类型。
		return jdbcTemplate.queryForObject(sb.toString().replaceFirst("and", "where"), Integer.class);
	}

	@Override
	public void add(Department department) {
		//使用JDBCTemplate
		String sql="insert into t_department values(null,?,?)";
		jdbcTemplate.update(sql, new Object[]{department.getDeptName(),department.getRemark()});
	}

	@Override
	public void update(Department department) {
		String sql="update t_department set deptName=?,remark=? where id=?";
		jdbcTemplate.update(sql, new Object[]{department.getDeptName(),department.getRemark(),department.getId()});
	}

	@Override
	public void delete(int id) {
		String sql="delete from t_department where id=?";
		jdbcTemplate.update(sql, new Object[]{id});
	}

	@Override
	public Department loadById(int id) {
		String sql="select * from t_department where id=?";
		//由于返回的只有一个对象
		final Department department=new Department();
		jdbcTemplate.query(sql, new Object[]{id}, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				department.setDeptName(rs.getString("deptName"));
				department.setId(rs.getInt("id"));
				department.setRemark(rs.getString("remark"));
			}
		});
		return department;
	}

}
