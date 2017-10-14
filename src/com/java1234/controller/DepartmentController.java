package com.java1234.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.java1234.model.Department;
import com.java1234.model.PageBean;
import com.java1234.service.DepartmentService;
import com.java1234.service.UserService;
import com.java1234.util.PageUtil;
import com.java1234.util.ResponseUtil;
import com.java1234.util.StringUtil;

@Controller
@RequestMapping("/department")
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;
	
	@Resource
	private UserService userService;
	
	@RequestMapping("/list")
	//@RequestParam 使用注解;springMVC学习笔记四 ，这里require是false代表不是必须要的，默认是true，因为第一次点击并没有生成page，会报错，
	//所以为了避免这种错误，用@RequestParam引入false
	//Department s_department 是用来封装查询信息的
	public ModelAndView list(@RequestParam(value="page",required=false)String page,Department s_department,HttpServletRequest request){
		ModelAndView mav=new ModelAndView();
		HttpSession session=request.getSession();
		//如果第一次没有page传过来，我们就给他一个默认值，第一次就给它来个首页，第一页
		if(StringUtil.isEmpty(page)){
			page="1";
			//第一次获取之后，将查询得到的数据s_department放入sesssion中，免得带来带去
			session.setAttribute("s_department", s_department);
		}else{
			//当你点击第二页，说明就是已经查到数据以后的事情了，此时没必要在去调用s_department获取信息，可以直接从session里面取。
			s_department=(Department) session.getAttribute("s_department");
		}
		//封装查询信息
		//Integer.parseInt(page)将字符串类型的数字转换为整形类型的数字，因为这里的page是String类型
		//PageBean(Integer.parseInt(page),3); 这句话的意思是(第几页，每页3个记录)
		PageBean pageBean=new PageBean(Integer.parseInt(page),3);
		List<Department> departmentList=departmentService.find(pageBean, s_department);
		//主要是想通过总记录数和每页多少个记录结合bootstrap组件生成html页面
		int total=departmentService.count(s_department);
		
		String pageCode=PageUtil.getPagation(request.getContextPath()+"/department/list.do", total, Integer.parseInt(page), 3);
		mav.addObject("pageCode", pageCode);
		mav.addObject("modeName", "部门管理");
		//departmentList在页面上遍历显示的数据
		mav.addObject("departmentList", departmentList);
		//这里的mainPage是后端传到前面main.jsp里面的东西，是显示在栅格布局里面的第二部分3/9开的9部分
		mav.addObject("mainPage", "department/list.jsp");
		//转发到main.jsp
		mav.setViewName("main");
		return mav;
	}
	
	//添加的后端处理代码
	@RequestMapping("/preSave")
	public ModelAndView preSave(@RequestParam(value="id",required=false)String id){
		ModelAndView mav=new ModelAndView();
		mav.addObject("mainPage", "department/svae.jsp");
		mav.addObject("modeName", "部门管理");
		
		mav.setViewName("main");
		if(StringUtil.isNotEmpty(id)){
			mav.addObject("actionName", "部门修改");
			Department department=departmentService.loadById(Integer.parseInt(id));
			mav.addObject("department", department);
		}else{
			mav.addObject("actionName", "部门添加");			
		}
		return mav;
	}
	//处理从前端传过来的数据
	@RequestMapping("/save")
	public String save(Department department){
		if(department.getId()==null){
			departmentService.add(department);			
		}else{
			departmentService.update(department);
		}
		return "redirect:/department/list.do";
	}
	
	@RequestMapping("/delete")//使用ajax实现
	public void delete(@RequestParam(value="id")String id,HttpServletResponse response)throws Exception{
		JSONObject result=new JSONObject();
		if(userService.existUserByDeptId(Integer.parseInt(id))){
			result.put("errorInfo", "该部门下存在用户，不能删除！");
		}else{
			departmentService.delete(Integer.parseInt(id));
			result.put("success", true);			
		}
		ResponseUtil.write(result, response);
	}
}
