<%-- 
    Document   : index
    Created on : 05.10.2015, 15:27:42
    Author     : Dante
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Music Store</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Music online store">
    <meta name="author" content="Denis Antonov">

    <link rel="stylesheet" type="text/css"   href="<c:url value="/lib/bootstrap/css/bootstrap.min.css"/>" />
    <link rel='stylesheet' type="text/css"   href='<c:url value="/lib/css/main.css"/>'>
    
</head>

<body style="background-color: white;">
    <!-- Fixed navbar -->
    <%@include file="fragment/navBar.jspf"%>
    
    <div class="container-fluid">
        
        <c:choose>
            <c:when test="${pageContextStr == 'index'}">
                
                <div class="row">
                    <div class="col-lg-2 col-md-2 col-sm-1"></div>
                    <div class="col-lg-7 col-md-7 col-sm-9">
                        <div class="jumbotron" class="carouset-content" style="padding: 5px;">
                            <div class="conteiner">
                                <div class="row">
                                    <div class="col-lg-5 col-md-6 col-sm-7">
                                        <img class="img-thumbnail" src="<c:url value="/image/music.jpg"/>" style="height: 280px;">
                                    </div>
                                    <div class="col-lg-7 col-md-6 col-sm-5">
                                        <h2>Добро пожаловать!</h2>
                                        <p style="margin-bottom: 0px;"><em>Данный сервис является большой библиотека музыки.<small> В будущем, может быть :)</small></em></p>
                                        <p style="margin-top: 0px;"><em>Слушайте любимые песни в одном месте.</em></p>
                                        <p><small><a href="#" data-toggle="modal" data-target="#myModal">Зарегестрируйтесь</a> и получите возможноть прослушивать любимые треки в любое время.</small></p>
                                <!--/row-->
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!--/span-->
                    </div>
                    <div class="col-lg-3 col-md-3 col-sm-2"></div>
                </div>
                                    
            </c:when>
                
            <c:when test="${pageContextStr == 'author'}">
                <div class="row">
                    <div class="col-lg-2 col-md-2 col-sm-1"></div>
                    <div class="col-lg-7 col-md-7 col-sm-9">
                        <div class="jumbotron carouset-content" style="padding: 5px;">
                            <div class="conteiner">
                                <div class="row">
                                    <img class="img-rounded" src="<c:url value="/resource/${author.name}/cover.jpg"/>" style="height: 280px; float: left; margin-left: 15px; margin-right: 15px;">

                                    <h4><strong class="margintext">${author.name}</strong></h4>
                                    <p><h4 style="padding-right: 25px;"><em><small class="margintext">${author.desc}</small></em></h4>
                            <!--/row-->

                                </div>
                            </div>
                        </div>
                        <!--/span-->
                    </div>
                    <div class="col-lg-3 col-md-3 col-sm-2"></div>
                </div>
            </c:when>
        </c:choose>
        
        <c:forEach var="entry" items="${dataMap}">
            
            <div class="row">
                <div class="col-lg-2 col-md-2 col-sm-1"></div>
                <div class="col-lg-8 col-md-8 col-sm-10 content-category"><h3><strong>${entry.key}</strong></h3></div>
                <div class="col-lg-2 col-md-2 col-sm-1"></div>
            </div>

            <div class="row">
                <div class="col-lg-2 col-md-2 col-sm-1"></div>
                <div class="col-lg-8 col-md-8 col-sm-10 carouset-content">
                    
                    <c:choose>
                        
                        <c:when test="${not empty entry.value}">
                    
                            <div class='jcarousel-wrapper'>
                                <div  class="jcarousel">
                                    <ul>

                                        <c:forEach items="${entry.value}" var="album">
                                            <li>
                                                <div class="album" onclick="albumPage('${album.author.name}','${album.title}');">
                                                    <img src="<c:url value="/resource/${album.author.name}/${album.title}/cover.jpg"/>" alt="${album.title}" width="160" height="160">
                                                    <h5 style="margin-top: 10px;">${album.title}</h5>
                                                    <h5 ><small><a href="<c:url value="/author/${album.author.name}"/>" style="color: #777;">${album.author.name}</a></small></h5>
                                                    <p class="text-right"><c:choose><c:when test="${album.isBought}">Куплено</c:when><c:otherwise>${format.format(album.price)} руб</c:otherwise></c:choose></p>
                                                </div>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                    <button class="jcarousel-prev"><</button>
                                    <button class="jcarousel-next">></button>
                                </div>
                            </div>
                            
                        </c:when>
                        
                        <c:otherwise>Не найдено</c:otherwise>
                    
                    </c:choose>
                    
                </div>
                <div class="col-lg-2 col-md-2 col-sm-1"></div>
            </div>
        
        </c:forEach>
         
        
    </div>
        
    <footer style="padding-left: 15px; padding-top: 30px;">
        <hr>
        <p>&copy; Денис Антонов 2015</p>
    </footer>

    <%@include file="fragment/loginPopup.jspf"%>

    <%@include file="fragment/userInfo.jspf"%>

    <%@include file="fragment/registrationModal.jspf"%>
            
    <%@include file="fragment/modalInfo.jspf"%>

    <div id="mainUrl" data-main-url="<c:url value="/"/>"></div>
    <div id="csrfheader" data-csrf-header="${_csrf.headerName}"></div>
    <div id="csrfvalue" data-csrf-value="${_csrf.token}"></div>

    <!--/.fluid-container-->
    <!-- Placed at the end of the document so the pages load faster -->
    <script type="text/javascript" src="<c:url value="/lib/jquery/jquery-2.1.4.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/jcarousel/jquery.jcarousel.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/bootstrap/js/bootstrap.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/js/navbar.js"/>"></script>
    <script type="text/javascript" src='<c:url value="/lib/js/main.js"/>'></script>
</body>

</html>