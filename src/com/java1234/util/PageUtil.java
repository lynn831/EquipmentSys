package com.java1234.util;

/**
 * 分页工具类
 * @author Administrator
 *
 */
public class PageUtil {

	/**
	 * 获取分页代码
	 * @param targetUrl 目标地址
	 * @param totalNum 总记录数
	 * @param currentPage 当前页
	 * @param pageSize 每页大小
	 * @return
	 */
	public static String getPagation(String targetUrl,int totalNum,int currentPage,int pageSize){
		//代码处理逻辑，拼接bootstrap的html页面样式；处理几种极端的逻辑，首页，尾页，当前页为1，上一页不能点击，当前页为最后一页，不能点下一页
		//总页数
		int totalPage=totalNum%pageSize==0?totalNum/pageSize:totalNum/pageSize+1;
		//还能这么写，牛逼了
		if(totalPage==0){
			return "<font color=red>未查询到数据！</font>";
		}
		StringBuffer pageCode=new StringBuffer();
		//仿造bootstrap拼接翻页组件
		pageCode.append("<li><a href='"+targetUrl+"?page=1'>首页</a></li>");
		if(currentPage==1){
			pageCode.append("<li class='disabled'><a href='#'>上一页</a></li>");
		}else{
			pageCode.append("<li><a href='"+targetUrl+"?page="+(currentPage-1)+"'>上一页</a></li>");
		}
	     //中间页显示，显示前面两个，后面两个，加上中间一个，一共显示5个
		for(int i=currentPage-2;i<=currentPage+2;i++){
			if(i<1 || i>totalPage){
				continue;
			}
			if(i==currentPage){
				pageCode.append("<li class='active'><a href='#'>"+i+"</a></li>");
			}else{
				pageCode.append("<li><a href='"+targetUrl+"?page="+i+"'>"+i+"</a></li>");
			}
			
		}
		
		if(currentPage==totalPage){
			pageCode.append("<li class='disabled'><a href='#'>下一页</a></li>");
		}else{
			pageCode.append("<li><a href='"+targetUrl+"?page="+(currentPage+1)+"'>下一页</a></li>");
		}
		pageCode.append("<li><a href='"+targetUrl+"?page="+totalPage+"'>尾页</a></li>");
		return pageCode.toString();
	}
}
