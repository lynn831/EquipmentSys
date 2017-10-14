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
	private JdbcTemplate jdbcTemplate;//��ѯ���ݿ�
	
	@Override
	public List<Department> find(PageBean pageBean, Department s_department) {
		StringBuffer sb=new StringBuffer("select * from t_department");//��Ҫ��ƴ��
		if(s_department!=null){
			//��̬�����ĵ���,ֻ���ú���.������������newһ�������Ķ���Ȼ�����������.������
			if(StringUtil.isNotEmpty(s_department.getDeptName())){
				//ƴ�ӵ�д��  select * from t_department and deptName like '% s_department.getDeptName() %'
				sb.append(" and deptName like '%"+s_department.getDeptName()+"%'");
			}
		}
		if(pageBean!=null){
			//limitǿ�� SELECT ��䷵��ָ���ļ�¼�� 
			//SELECT * FROM table LIMIT 5,10; // ������¼�� 6-15  
			//SELECT * FROM table LIMIT 5;//�������ؽ����ǰ 5 ����¼��
			//pageBean.getStart()����ʼҳ pageBean.getPageSize()��ÿҳ��¼��
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getPageSize());
		}
		//���ص���һ������
		final List<Department> departmentList=new ArrayList<Department>();
		jdbcTemplate.query(sb.toString().replaceFirst("and", "where"), new Object[]{}, new RowCallbackHandler() {
			
			@Override
			//ResutlSet�ǲ�ѯ�󷵻صĶ�������Ĵ����൱��jdbc����ResultSet��Ȼ�󷵻��б�
			public void processRow(ResultSet rs) throws SQLException {
				Department department=new Department();
				department.setId(rs.getInt("id"));
				department.setDeptName(rs.getString("deptName"));
				department.setRemark(rs.getString("remark"));
			    //�б���ӵķ�ʽ
				departmentList.add(department);
			}
		});
		return departmentList;
	}

	@Override
	//��ȡ�ܼ�¼��
	public int count(Department s_department) {
		//select count(*) from t_department  �����ܵļ�¼�������Ǹ�����
		StringBuffer sb=new StringBuffer("select count(*) from t_department");
		if(s_department!=null){
			if(StringUtil.isNotEmpty(s_department.getDeptName())){
				sb.append(" and deptName like '%"+s_department.getDeptName()+"%'");
			}
		}
		//���صĵ����ܼ�¼�������������ַ���������������Integer.class ����Interger�����͡�
		return jdbcTemplate.queryForObject(sb.toString().replaceFirst("and", "where"), Integer.class);
	}

	@Override
	public void add(Department department) {
		//ʹ��JDBCTemplate
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
		//���ڷ��ص�ֻ��һ������
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
