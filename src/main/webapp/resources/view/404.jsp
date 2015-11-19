<%-- 
    Document   : 404
    Created on : 18.11.2015, 23:06:54
    Author     : Dante
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>404</title>
    </head>
    <body style="background-color: #f9f9f9;">
        <h1 style="text-align: center; margin-top: 50px;">Ошибка 404. Страница не найдена.</h1>
        <img class="img" src="<c:url value="/image/404.jpeg"/>" style="display: block; margin: 0 auto; margin-top: 30px;"/>
        <h2 style="text-align: center; font-style: italic; margin-top: 30px; color: #919191;">Мы не знаем, как вы попали на эту страницу, но лучше вам вернуться на 
            <a href="<c:url value="/"/>" style="color: #303030;">главную страницу</a>
        </h2>
    </body>
</html>
