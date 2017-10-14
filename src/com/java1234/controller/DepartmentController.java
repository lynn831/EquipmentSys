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
	//@RequestParam ʹ��ע��;springMVCѧϰ�ʼ��� ������require��false�����Ǳ���Ҫ�ģ�Ĭ����true����Ϊ��һ�ε����û������page���ᱨ��
	//����Ϊ�˱������ִ�����@RequestParam����false
	//Department s_department ��������װ��ѯ��Ϣ��
	public ModelAndView list(@RequestParam(value="page",required=false)String page,Department s_department,HttpServletRequest request){
		ModelAndView mav=new ModelAndView();
		HttpSession session=request.getSession();
		//�����һ��û��page�����������Ǿ͸���һ��Ĭ��ֵ����һ�ξ͸���������ҳ����һҳ
		if(StringUtil.isEmpty(page)){
			page="1";
			//��һ�λ�ȡ֮�󣬽���ѯ�õ�������s_department����sesssion�У���ô�����ȥ
			session.setAttribute("s_department", s_department);
		}else{
			//�������ڶ�ҳ��˵�������Ѿ��鵽�����Ժ�������ˣ���ʱû��Ҫ��ȥ����s_department��ȡ��Ϣ������ֱ�Ӵ�session����ȡ��
			s_department=(Department) session.getAttribute("s_department");
		}
		//��װ��ѯ��Ϣ
		//Integer.parseInt(page)���ַ������͵�����ת��Ϊ�������͵����֣���Ϊ�����page��String����
		//PageBean(Integer.parseInt(page),3); ��仰����˼��(�ڼ�ҳ��ÿҳ3����¼)
		PageBean pageBean=new PageBean(Integer.parseInt(page),3);
		List<Department> departmentList=departmentService.find(pageBean, s_department);
		//��Ҫ����ͨ���ܼ�¼����ÿҳ���ٸ���¼���bootstrap�������htmlҳ��
		int total=departmentService.count(s_department);
		
		String pageCode=PageUtil.getPagation(request.getContextPath()+"/department/list.do", total, Integer.parseInt(page), 3);
		mav.addObject("pageCode", pageCode);
		mav.addObject("modeName", "���Ź���");
		//departmentList��ҳ���ϱ�����ʾ������
		mav.addObject("departmentList", departmentList);
		//�����mainPage�Ǻ�˴���ǰ��main.jsp����Ķ���������ʾ��դ�񲼾�����ĵڶ�����3/9����9����
		mav.addObject("mainPage", "department/list.jsp");
		//ת����main.jsp
		mav.setViewName("main");
		return mav;
	}
	
	//��ӵĺ�˴������
	@RequestMapping("/preSave")
	public ModelAndView preSave(@RequestParam(value="id",required=false)String id){
		ModelAndView mav=new ModelAndView();
		mav.addObject("mainPage", "department/svae.jsp");
		mav.addObject("modeName", "���Ź���");
		
		mav.setViewName("main");
		if(StringUtil.isNotEmpty(id)){
			mav.addObject("actionName", "�����޸�");
			Department department=departmentService.loadById(Integer.parseInt(id));
			mav.addObject("department", department);
		}else{
			mav.addObject("actionName", "�������");			
		}
		return mav;
	}
	//�����ǰ�˴�����������
	@RequestMapping("/save")
	public String save(Department department){
		if(department.getId()==null){
			departmentService.add(department);			
		}else{
			departmentService.update(department);
		}
		return "redirect:/department/list.do";
	}
	
	@RequestMapping("/delete")//ʹ��ajaxʵ��
	public void delete(@RequestParam(value="id")String id,HttpServletResponse response)throws Exception{
		JSONObject result=new JSONObject();
		if(userService.existUserByDeptId(Integer.parseInt(id))){
			result.put("errorInfo", "�ò����´����û�������ɾ����");
		}else{
			departmentService.delete(Integer.parseInt(id));
			result.put("success", true);			
		}
		ResponseUtil.write(result, response);
	}
}
