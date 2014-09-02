<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:message code="registration.button" var="regbutton" />
<spring:message code="registration.defName" var="def_name" />
<spring:message code="registration.defNickname" var="def_nickname" />
<spring:message code="registration.defEmail" var="def_email" />
<spring:message code="registration.defPass" var="def_pass" />
<spring:message code="registration.textName" var="textName" />
<spring:message code="registration.textNickName" var="textNickName" />
<spring:message code="registration.textEmail" var="textEmail" />
<spring:message code="registration.textPassword" var="textPassword" />
<spring:message code="registration.textPassword2" var="textPassword2" />

<div id="login-box">

	<h1>${regtitle}</h1>

	<spring:url value="/registration" var="url" />
	<form:form name='registrationForm' action="${url}" method='POST'
		modelAttribute="userDto">

		<div class="form-group has-error has-feedback">
			<form:errors path="*" class="control-label" for="inputError" />
		</div>
		<br>
		<center>
			<table>
				<tr>
					<td>${textName}</td>
				</tr>

				<tr>
					<td><form:input path="name" id="inputError"
							class="form-control" placeholder="${def_name}" size="25" /></td>

				</tr>


				<tr>
					<td>${textNickName}</td>
				</tr>

				<tr>
					<td><form:input path="nickname" id="inputError"
							class="form-control" placeholder="${def_nickname}" size="25" /></td>

				</tr>

				<tr>
					<td>${textEmail}</td>
				</tr>

				<tr>
					<td><form:input path="email" id="inputError"
							class="form-control" placeholder="${def_email}" size="25" /></td>

				</tr>

				<tr>
					<td>${textPassword}</td>
				</tr>

				<tr>
					<td><form:password path="password" id="inputError"
							class="form-control" placeholder="${def_pass}" size="25" /></td>

				</tr>

				<tr>
					<td>${textPassword2}</td>
				</tr>

				<tr>
					<td><form:password path="confirmPassword" id="inputError"
							class="form-control" placeholder="${def_pass}" size="25" /></td>

				</tr>

				<tr>
					<td colspan='2'><input name="submit" type="submit"
						class="btn btn-primary"
						value="${regbutton}" /></td>
				</tr>
			</table>
		</center>

	</form:form>
</div>