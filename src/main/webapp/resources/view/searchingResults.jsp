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
    <nav class="navbar navbar-default navbar-fixed-top">
      <div class="container-fluid">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
            <a class="navbar-brand" href="<c:url value="/"/>">Music Store</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <div class="col-sm-6 col-md-6">
                <form class="navbar-form" role="search" action="<c:url value="/search"/>">
                    <div class="input-group col-sm-8 col-md-6">
                        <input type="text" class="form-control" placeholder="Поиск по группам, альбомам, музыке" name="q">
                        <div class="input-group-btn">
                            <button class="btn btn-default" type="submit"><i class="glyphicon glyphicon-search"></i></button>
                        </div>
                    </div>
                    
                    <select id="genreSelect" class="input-sm form-control" title="Категории">
                        <option value="-1">Категории</option>
                        <c:forEach items="${genres}" var="genre" varStatus="status">
                            <option value="${genre.id}">${genre.name}</option>
                        </c:forEach>
                    </select>
                    
                </form>
            </div>
            <ul class="nav navbar-nav navbar-right" style="margin-right: 100px;">
                <li><a href="#" data-toggle="modal" data-target="#myModal">Регистрация</a></li>
                <li><a data-placement="bottom" data-toggle="popover" data-container="body" data-html="true" href="#" id="login">
                    <c:choose>
                        <c:when test="${user != null}">${user.login}</c:when>
                        <c:otherwise>Вход</c:otherwise>
                    </c:choose>
                </a></li>
            </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>
    
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
                                                <img src="<c:url value="/resource/${author.name}/cover.jpg"/>" alt="${author.name}" style="height: 160px;">
                                                <h5 style="margin-top: 10px;">${author.name}</h5>
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
                                                    <img src="<c:url value="/resource/${track.album.author.name}/${track.album.title}/cover.jpg"/>" alt="${track.name}" width="160" height="160">
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
        
     <footer style="padding-left: 15px; padding-top: 30px;">
        <hr>
        <p>&copy; Денис Антонов 2015</p>
    </footer>

    <%@include file="fragment/loginPopup.jspf"%>

    <%@include file="fragment/userInfo.jspf"%>

    <%@include file="fragment/registrationModal.jspf"%>

    <%@include file="fragment/modalInfo.jspf"%>

    <div id="mainUrl" data-main-url="<c:url value="/"/>"></div>

    <!--/.fluid-container-->
    <!-- Placed at the end of the document so the pages load faster -->
    <script type="text/javascript" src="<c:url value="/lib/jquery/jquery-2.1.4.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/jcarousel/jquery.jcarousel.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/bootstrap/js/bootstrap.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/js/navbar.js"/>"></script>
    <script type="text/javascript" src='<c:url value="/lib/js/main.js"/>'></script>
</body>

</html>