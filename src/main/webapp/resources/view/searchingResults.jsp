<%-- 
    Document   : searchingResults
    Created on : 06.11.2015, 2:06:06
    Author     : Dante
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Результаты поиска</title>
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
        

            <div class="row">
                <div class="col-lg-2 col-md-2 col-sm-1"></div>
                <div class="col-lg-8 col-md-8 col-sm-10 content-category"><h3><strong>Группы</strong></h3></div>
                <div class="col-lg-2 col-md-2 col-sm-1"></div>
            </div>

            <div class="row">
                <div class="col-lg-2 col-md-2 col-sm-1"></div>
                <div class="col-lg-8 col-md-8 col-sm-10 carouset-content">
                    
                    <c:choose>
                        
                     <c:when test="${not empty authors}">
                         
                        <div class='jcarousel-wrapper'>
                            <div  class="jcarousel">
                                <ul>

                                    <c:forEach  items="${authors}" var="author">
                                        <li>
                                            <div class="album" onclick="authorPage('${author.name}');" style="height: 200px;">
                                                <img src="<c:url value="/author/${author.name}/cover.jpg"/>" alt="${author.name}" style="height: 160px;">
                                                <h5 style="margin-top: 10px; width: auto;">${author.name}</h5>
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
        
        
            
            <div class="row">
                <div class="col-lg-2 col-md-2 col-sm-1"></div>
                <div class="col-lg-8 col-md-8 col-sm-10 content-category"><h3><strong>Альбомы</strong></h3></div>
                <div class="col-lg-2 col-md-2 col-sm-1"></div>
            </div>

            <div class="row">
                <div class="col-lg-2 col-md-2 col-sm-1"></div>
                <div class="col-lg-8 col-md-8 col-sm-10 carouset-content">
                    
                    <c:choose>
                        
                        <c:when test="${not empty albums}">
                            
                            <div class='jcarousel-wrapper'>
                                <div  class="jcarousel">
                                    <ul>

                                        <c:forEach items="${albums}" var="album">
                                            <li>
                                                <div class="album" onclick="albumPage('${album.author.name}','${album.title}');">
                                                    <img src="<c:url value="/author/${album.author.name}/album/${album.title}/cover.jpg"/>" alt="${album.title}" width="160" height="160">
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
        
        

            <div class="row">
                <div class="col-lg-2 col-md-2 col-sm-1"></div>
                <div class="col-lg-8 col-md-8 col-sm-10 content-category"><h3><strong>Треки</strong></h3></div>
                <div class="col-lg-2 col-md-2 col-sm-1"></div>
            </div>

            <div class="row">
                <div class="col-lg-2 col-md-2 col-sm-1"></div>
                <div class="col-lg-8 col-md-8 col-sm-10 carouset-content">
                    
                    <c:choose>
                        
                        <c:when test="${not empty tracks}">
                    
                            <div class='jcarousel-wrapper'>
                                <div  class="jcarousel">
                                    <ul>

                                        <c:forEach items="${tracks}" var="track">
                                            <li>
                                                <div class="album" onclick="albumPage('${track.album.author.name}' ,'${track.album.title}');">
                                                    <img src="<c:url value="/author/${track.album.author.name}/album/${track.album.title}/cover.jpg"/>" alt="${track.name}" width="160" height="160">
                                                    <h5 style="margin-top: 10px;">${track.name}</h5>
                                                    <h5 ><small><a href="<c:url value="/author/${track.album.author.name}"/>" style="color: #777;">${track.album.author.name}</a></small></h5>
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
        

    </div>

    <%@include file="fragment/footer.jspf"%>

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