<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设备管理系统主页</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap3/css/bootstrap.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap3/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/equipment.css">
<script src="${pageContext.request.contextPath}/bootstrap3/js/jquery-1.11.2.min.js"></script>
<script src="${pageContext.request.contextPath}/bootstrap3/js/bootstrap.min.js"></script>
<%
    //牛逼了，直接在这可以加session验证，使其访问http://localhost:8080/EquipmentSys/main.jsp失败
	if(session.getAttribute("currentUser")==null){
		response.sendRedirect(request.getContextPath()+"/login.jsp");
		return;
	} 

	String mainPage=(String)request.getAttribute("mainPage");
	if(mainPage==null || mainPage.equals("")){
		mainPage="/common/default.jsp";
	}
%>
</head>
<body>
<div class="container">
    <!-- 第一部分 -->
  <div class="row">
	  <div class="col-md-12">
	  	<jsp:include page="/common/head.jsp"/>
	  </div>
  </div>
  <!-- 第二部分 -->
  <div class="row" style="padding-top: 45px">
       <!-- 第二部分左边 -->
	  <div class="col-md-3">
	  	<jsp:include page="/common/menu.jsp"/>
	  </div>
	  <!-- 第二部分右边 -->
	  <div class="col-md-9">
	  	<div>
	  	 <!--  路径导航 bootstrap3 特有的-->
			<ol class="breadcrumb">
			  <!--  glyphicon glyphicon-home 是一个头标 -->
			  <li><span class="glyphicon glyphicon-home"></span>&nbsp;<a href="${pageContext.request.contextPath}/main.jsp">主页</a></li>
			  <li class="active">${modeName }</li>
			</ol>
		</div>
		<%-- <%=mainPage %> 这是一个java表达式，用它可以取到上面mainPage，也可以取到后台的EL表达式； 如果用${mainPage}只能取到后台的EL表达式 --%>
		<jsp:include page="<%=mainPage %>"/>
	  </div>
  </div>
 <!--  第三部分 -->
  <div class="row">
	  <div class="col-md-12">
	  	<jsp:include page="/common/foot.jsp"/>
	  </div>
  </div>
</div>
</body>
</html>