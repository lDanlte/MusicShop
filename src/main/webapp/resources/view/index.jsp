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
                <form class="navbar-form" role="search" action="<c:url value="/search"/>" >
                    <div class="input-group col-sm-8 col-md-6">
                        <input type="text" class="form-control" placeholder="Поиск по группам, альбомам, музыке" name="q">
                        <div class="input-group-btn">
                            <button class="btn btn-default" type="submit"><i class="glyphicon glyphicon-search"></i></button>
                        </div>
                    </div>
                    
                    <select id="genreSelect" class="input-sm form-control" title="Категории">
                        <option value="-1">Категории</option>
                        <c:forEach items="${genres}" var="genre" varStatus="status">
                            <option value="${genre.id}" 
                                <c:if test="${selectedGenreId == genre.id}">
                                    selected="selected"
                                </c:if>
                                >${genre.name}
                            </option>
                        </c:forEach>
                    </select>
                    
                </form>
            </div>
            <ul class="nav navbar-nav navbar-right" style="margin-right: 100px;">
                <li><a href="#" data-toggle="modal" data-target="#myModal">Регистрация</a></li>
                <li><a data-placement="bottom" data-toggle="popover" data-container="body" data-html="true" href="#" id="login">Вход</a></li>
            </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>
    
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
                                        <p><small><a href="#" data-toggle="modal" data-target="#myModal">Зарегестрируйтесь</a> и получите возможноть прослушивать любымые треки в любое время.</small></p>
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
                                    <p><h4><em><small class="margintext">${author.desc}</small></em></h4>
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
                                                    <p class="text-right">${format.format(album.price)} руб</p>
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
        
    <footer style="padding-left: 15px;">
        <hr>
        <p>&copy; Денис Антонов 2015</p>
    </footer>
    
    <div id="popover-content-login" class="hide">
        <form action="" role="form">
            <div class="form-group">
              <label for="user">Логин</label>
              <input type="text" class="form-control" id="user" placeholder="Логин" />
              <label for="password">Пароль</label>
              <input type="password" class="form-control" id="password" placeholder="Пароль" />
            </div>
            <button type="submit" class="btn btn-default">Вход</button>
      </form>
    </div>
    
    <div id="popover-content-user" class="hide">
        <p style="margin-top: 10px;"><a href="#">Личный кабинет</a></p>
        
        <p><a href="#">Мои альбомы</a></p>
        <div style="display: inline;">
            <div style="display: inline-block;">
                Баланс: 
            </div>
            <div style="display: inline-block;">
                0.00 
            </div>
            <div style="display: inline-block;">
                р. 
            </div>
        </div>
        <p>
        <button type="button" class="btn btn-default" style="margin-top: 10px;">Выйти</button>
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
                        <input id="newEmail" id="Email" name="Email" class="form-control input-large" type="text" placeholder="JoeSixpack@sixpacksrus.com" required="">
                      </div>
                    </div>

                    <!-- Text input-->
                    <div class="control-group">
                      <label class="control-label" for="userid">Логин:</label>
                      <div class="controls">
                        <input id="newLogin" name="userid" class="form-control input-large" type="text" placeholder="JoeSixpack" required="">
                      </div>
                    </div>

                    <!-- Password input-->
                    <div class="control-group">
                      <label class="control-label" for="password">Пароль:</label>
                      <div class="controls">
                        <input id="newPass" name="password" class="form-control input-large" type="password" placeholder="********" required="">
                      </div>
                    </div>

                    <!-- Text input-->
                    <div class="control-group">
                      <label class="control-label" for="reenterpassword">Повторите пароль:</label>
                      <div class="controls">
                        <input id="newPass2" class="form-control input-large" name="reenterpassword" type="password" placeholder="********" required="">
                      </div>
                    </div>

                    </fieldset>
                </form>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">Отмена</button>
              <button type="button" class="btn btn-info" onclick="registration();">Регистрация</button>
            </div>
          </div>
        </div>
    </div>

    <!--/.fluid-container-->
    <!-- Placed at the end of the document so the pages load faster -->
    <script type="text/javascript" src="<c:url value="/lib/jquery/jquery-2.1.4.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/jcarousel/jquery.jcarousel.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/bootstrap/js/bootstrap.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/js/navbar.js"/>"></script>
    <script type="text/javascript" src='<c:url value="/lib/js/main.js"/>'></script>
</body>

</html>