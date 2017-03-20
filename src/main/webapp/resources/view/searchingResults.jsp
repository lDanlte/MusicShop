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
    
    <div <c:choose> <c:when test="${user == null}"> id="popover-content" </c:when> <c:otherwise> id="popover-content-disable" </c:otherwise> </c:choose> class="hide">
        <form action="<c:url value="/login"/>" role="form" method="POST">
            <div class="form-group">
                <label for="user">Логин</label>
                <input type="text" class="form-control" id="user" name="login" placeholder="Логин" />
                <label for="password">Пароль</label>
                <input type="password" class="form-control" id="password" name="pass" placeholder="Пароль" />
                <label for="rememberMe">Запомнить</label>
                <input type="checkbox" id="rememberMe" name="remember-me"/>
            </div>
            <button type="submit" class="btn btn-default">Вход</button>
        </form>
    </div>
    
    <div <c:choose> <c:when test="${user != null}"> id="popover-content" </c:when> <c:otherwise> id="popover-content-disable" </c:otherwise> </c:choose> class="hide">
        <p style="margin-top: 10px;"><a href="<c:url value="/user"/>">Личный кабинет</a></p>
        
        <p><a href="<c:url value="/user/boughtAlbums"/>">Мои альбомы</a></p>
        <div style="display: inline;">
            <div style="display: inline-block;">
                Баланс: 
            </div>
            <div id="wallet" style="display: inline-block;">
                <c:if test="${user != null}">${format.format(user.wallet)}</c:if>
            </div>
            <div style="display: inline-block;">
                р. 
            </div>
        </div>
        <p>
        <form action="<c:url value="/logout"/>" method="POST">
            <button type="submit" class="btn btn-default" style="margin-top: 10px;">Выйти</button>
        </form>
    </div>
    
    
    <div class="modal fade bs-example-modal-sm" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog modal-sm" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
              <h4 class="modal-title" id="myModalLabel">Регистрация</h4>
            </div>
            <div class="modal-body">
              <form class="form-horizontal">
                <fieldset>
                    <!-- Sign Up Form -->
                    <!-- Text input-->
                    <div class="control-group">
                      <label class="control-label" for="Email">Email:</label>
                      <div class="controls">
                        <input id="Email" name="Email" class="form-control input-large" type="text" placeholder="JoeSixpack@sixpacksrus.com" required="">
                      </div>
                    </div>

                    <!-- Text input-->
                    <div class="control-group">
                      <label class="control-label" for="userid">Логин:</label>
                      <div class="controls">
                        <input id="userid" name="userid" class="form-control input-large" type="text" placeholder="JoeSixpack" required="">
                      </div>
                    </div>

                    <!-- Password input-->
                    <div class="control-group">
                      <label class="control-label" for="password">Пароль:</label>
                      <div class="controls">
                        <input id="password" name="password" class="form-control input-large" type="password" placeholder="********" required="">
                      </div>
                    </div>

                    <!-- Text input-->
                    <div class="control-group">
                      <label class="control-label" for="reenterpassword">Повторите пароль:</label>
                      <div class="controls">
                        <input id="reenterpassword" class="form-control input-large" name="reenterpassword" type="password" placeholder="********" required="">
                      </div>
                    </div>

                    </fieldset>
                </form>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">Отмена</button>
              <button type="button" class="btn btn-info">Регистрация</button>
            </div>
          </div>
        </div>
    </div>
            
    <div class="modal fade" id="modalInfo">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
              <h4 class="modal-title"></h4>
            </div>
            <div class="modal-body"></div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>
            </div>
          </div>
        </div>
    </div>

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