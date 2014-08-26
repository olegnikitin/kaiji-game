<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" pageEncoding="UTF-8" %>

<spring:message code="start-page.head_title" var="head_title"/>
<spring:message code="start-page.body_title" var="body_title"/>
<spring:message code="start-page.description" var="description"/>
<spring:message code="start-page.rules_title" var="rules_title"/>
<spring:message code="start-page.rule1" var="rule1"/>
<spring:message code="start-page.rule2" var="rule2"/>
<spring:message code="start-page.rule3" var="rule3"/>
<spring:message code="start-page.rule4" var="rule4"/>
<spring:message code="start-page.rule5" var="rule5"/>
<spring:message code="start-page.rule6" var="rule6"/>
<spring:message code="start-page.rule7" var="rule7"/>
<spring:message code="start-page.game_process_title"
                var="game_process_title"/>
<spring:message code="start-page.step1" var="step1"/>
<spring:message code="start-page.step2" var="step2"/>
<spring:message code="start-page.step3" var="step3"/>
<spring:message code="start-page.condition1" var="condition1"/>
<spring:message code="start-page.condition2" var="condition2"/>
<spring:message code="start-page.create" var="create"/>
<spring:message code="start-page.join" var="join"/>


<html>
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>${head_title}</title>

</head>
<body>

<div class="container">

    <div class="row">

        <div class="col-md-12">

            <br> <br> <br>

            <center>
                <a href='<spring:url value="/game/new" htmlEscape="true"/>'>
                    <H1>${create}</H1>
                </a>

                </br>

                <a href='<spring:url value="/game/join" htmlEscape="true"/>'>
                    <H1>${join}</H1>
                </a>
            </center>

            <blockquote>

                <h1>${body_title}</h1>

                <h3>${description}</h3>

            </blockquote>

            <blockquote>

                <h3>${rules_title}</h3>

                <ol>
                    <li>${rule1}</li>

                    <li>${rule2}</li>

                    <li>${rule3}</li>

                    <li>${rule4}</li>

                    <li>${rule5}</li>

                    <li>${rule6}</li>

                    <li>${rule7}</li>

                </ol>

            </blockquote>

            <blockquote>

                <h3>${game_process_title}</h3>

                <ol>

                    <li>${step1}</li>

                    <li>${step2}</li>

                    <li>${step3}</li>

                </ol>

            </blockquote>

            <blockquote>

                <p>${condition1}</p>

                <p>${condition2}</p>

            </blockquote>

        </div>

    </div>

</div>

</body>
</html>