<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:message code="login.pageTitle" var="page_title" />
<spring:message code="login.loginError" var="login_error" />
<spring:message code="login.usernameLabel" var="username_label" />
<spring:message code="login.passwordLabel" var="password_label" />
<spring:message code="login.submit" var="submit" />
<spring:message code="login.defNickName" var="def_nickname" />
<spring:message code="login.defPass" var="def_pass" />

<div id="login-box">

	<h1>${page_title}</h1>
	<c:if test="${not empty error}">
		<div class="error">${login_error}</div>
	</c:if>

	<div class="input-group">
		<form name='loginForm' action="<c:url value='/login' />" method='POST'>

			<table>
				<tr>
					<td>${username_label}:</td>
					<td><input type='text' name='username' value=''
						class="form-control" placeholder="${def_nickname}" /></td>
				</tr>
				<tr>
					<td>${password_label}:</td>
					<td><input type='password' name='password'
						class="form-control" placeholder="${def_pass}" /></td>
				</tr>
				<tr>
					<td colspan='2'><input name="submit" type="submit"
						class="btn btn-default" value="${submit}" /></td>
				</tr>
			</table>


		</form>
	</div>
</div>