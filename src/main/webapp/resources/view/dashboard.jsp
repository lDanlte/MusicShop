<%-- 
    Document   : dashboard
    Created on : 06.11.2015, 0:51:04
    Author     : Dante
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
       <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>Личный кабинет</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="Music online store">
        <meta name="author" content="Denis Antonov">

        <link rel="stylesheet" type="text/css" href="<c:url value="/lib/bootstrap/css/bootstrap.min.css"/>" />
        <link rel='stylesheet' type="text/css" href='<c:url value="/lib/datepicker/css/bootstrap-datepicker3.min.css"/>' />
        <link rel='stylesheet' type="text/css" href='<c:url value="/lib/css/main.css"/>' />
        <link rel='stylesheet' type="text/css" href='<c:url value="/lib/css/dashboard.css"/>' />

</head>

<body style="background-color: white;">

    <sec:authorize access="hasAuthority('admin')" var="isAdmin"/>
    <sec:authorize access="hasAuthority('author')" var="isAuthor"/>

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
                <form class="navbar-form" role="search">
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
    
    
        <div class="conteiner-fluid" style="margin-top: 20px;">
        <div class="row" style="margin-right: 0px;">
            <div class="col-lg-1 col-md-2 col-sm-2">
                <ul class="nav nav-pills nav-stacked">
                    
                    <li class="active"><a href="#account" data-toggle="tab">Аккаунт</a></li>
                    <li><a href="#history" data-toggle="tab">История покупок</a></li>
                    <li><a href="#addMoney" data-toggle="tab">Пополнить счет</a></li>
                    
                    <c:if test="${isAdmin}">
                        <li><a href="#createAuthor" data-toggle="tab">Создать группу</a></li>
                    </c:if>
                        
                    <c:if test="${isAuthor}">
                        <li><a href="#groupControl" data-toggle="tab">Группа</a></li>
                        <li><a href="#addAlbum" data-toggle="tab">Добавить альбом</a></li>
                        <li><a href="#statistics" data-toggle="tab">Статистика продаж</a></li>
                        <li><a href="#discountMoney" data-toggle="tab">Вывод денег</a></li>
                    </c:if>
                    
                </ul>
            </div>
            <div class="col-lg-1 col-md-0 col-sm-0"></div>
            <div class="col-lg-6 col-md-7 col-sm-10  rowcolor" style="min-height: 300px;">
                <div class="tab-content">
                    <div id="account" class="tab-pane fade in active">
                      <h3>Настройка аккаунта</h3>
                      <form class="form-horizontal col-xs-5">
                            <fieldset>
                                <!-- Sign Up Form -->
                                <!-- Text input-->
                                <div class="control-group">
                                  <label class="control-label" for="Email">Поменять email:</label>
                                  <div class="controls">
                                    <input id="newEmail" name="Email" class="form-control input-large" type="text" placeholder="JoeSixpack@sixpacksrus.com" required="">
                                  </div>
                                </div>

                                <!-- Password input-->
                                
                                <div class="control-group">
                                  <label class="control-label" for="password">Новый пароль:</label>
                                  <div class="controls">
                                    <input id="newPassword" name="password" class="form-control input-large" type="password" placeholder="********" required="">
                                  </div>
                                </div>

                                <!-- Text input-->
                                <div class="control-group">
                                  <label class="control-label" for="reenterpassword">Повторите новый пароль:</label>
                                  <div class="controls">
                                    <input id="newReenterpassword" class="form-control input-large" name="reenterpassword" type="password" placeholder="********" required="">
                                  </div>
                                </div>
                                
                                </fieldset>
                          <button type="button" class="btn btn-info btn-sm" onclick="changeUserData();" style="margin-bottom: 20px; margin-top: 20px">Сохранить</button>
                            </form>
                      <div class="col-xs-7"></div>
                    </div>
                    
                    
                    
                    <div id="history" class="tab-pane fade">
                      <h3>История покупок</h3>
                      <div class="row" style="margin-top: 40px;">
                          <div class="col-xs-1 col-sm-2 col-md-2 col-lg-3"></div>
                          <div class="col-xs-11 col-sm-10 col-md-10 col-lg-9">
                              <div class="form-group form-inline form-group-sm" style="display: inline;"   >
                                    <div style="display: inline-block;">
                                        Показать за период c 
                                    </div>
                                  <div style="display: inline-block;">
                                      <input id="histotyDateFrom" type="text" class="form-control datepicker" size="7" />
                                    </div>
                                  <div  style="display: inline-block;">
                                      по
                                  </div>
                                  <div  style="display: inline-block;">
                                      <input id="histotyDateTo" type="text" class="form-control datepicker"  size="7" />
                                  </div>
                                  <div  style="display: inline-block;">
                                    <button type="button" class="btn btn-default btn-sm" onclick="showHistoryTable();">Показать</button>
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="row hide" id="historyTable" style="margin-top: 40px;">
                          <div class="col-lg-1"></div>
                          <div class="col-lg-10">
                              <table class="table">
                                  <thead>
                                      <tr>
                                          <td>Дата</td>
                                          <td>Действие</td>
                                          <td>Сумма, руб.</td>
                                      </tr>
                                  </thead>
                                  <tbody id="historyTableBody">
                                      <tr>
                                          <td>21.10.2015 15:00:00</td>
                                          <td>Пополнение счета</td>
                                          <td>200.00</td>
                                      </tr>
                                      <tr>
                                          <td>21.10.2015 15:04:12</td>
                                          <td>Покупка альбома <strong>AlbumName</strong></td>
                                          <td>49.00</td>
                                      </tr>
                                      <tr>
                                          <td>21.10.2015 15:09:45</td>
                                          <td>Покупка альбома <strong>AlbumName</strong></td>
                                          <td>109.00</td>
                                      </tr>
                                  </tbody>
                              </table>
                          </div>
                          <div class="col-lg-1"></div>
                      </div>
                    </div>
                    
                    
                    
                    <div id="addMoney" class="tab-pane fade">
                      <h3>Пополнение счета</h3>
                      <div class="row" style="margin-top: 40px;">
                          <div class="col-xs-1 col-sm-2 col-md-2 col-lg-3"></div>
                          <div class="col-xs-11 col-sm-10 col-md-10 col-lg-9">
                              <div class="form-group form-inline form-group-sm" style="display: inline;"   >
                                    <div style="display: inline-block;">
                                        Пополнить счет на сумму: 
                                    </div>
                                  <div style="display: inline-block; max-width: 125px;" >
                                      <div class="input-group">
                                          <input id="addCash" type="text" class="form-control" size="7" />
                                          <span class="input-group-addon" aria-hidden="true"><i class="glyphicon glyphicon-rub"></i></span>
                                      </div>
                                  </div>
                                  
                                  <div  style="display: inline-block; margin-left: 10px;">
                                    <button type="button" class="btn btn-info btn-sm" onclick="addCash();">Пополнить</button>
                                  </div>
                              </div>
                          </div>
                          
                      </div>
                    </div>
                    
                    <c:if test="${isAdmin}">
                    
                        <div id="createAuthor" class="tab-pane fade">
                          <h3>Создание новой группы</h3>
                          <form class="form-horizontal col-xs-5">
                                <fieldset>
                                    <!-- Sign Up Form -->
                                    <!-- Text input-->

                                    <div class="control-group" style="margin-top: 10px;">
                                      <label class="control-label" for="authorName">Название группы: </label>
                                      <div class="controls">
                                        <input id="authorName" name="authorName" class="form-control input-large" type="text" placeholder="Новая группа" required="">
                                      </div>
                                    </div>

                                    <div class="control-group" style="margin-top: 10px;">
                                      <label class="control-label" for="Email">Email:</label>
                                      <div class="controls">
                                        <input id="authorEmail" name="Email" class="form-control input-large" type="text" placeholder="JoeSixpack@sixpacksrus.com" required="">
                                      </div>
                                    </div>

                                    <div class="control-group" style="margin-top: 10px;">
                                      <label class="control-label" for="login">Логин: </label>
                                      <div class="controls">
                                        <input id="authorLogin" name="login" class="form-control input-large" type="text" placeholder="Логин" required="">
                                      </div>
                                    </div>

                                    <!-- Password input-->

                                    <div class="control-group" style="margin-top: 10px;">
                                      <label class="control-label" for="password">Пароль:</label>
                                      <div class="controls">
                                        <input id="authorPassword" name="password" class="form-control input-large" type="password" placeholder="********" required="">
                                      </div>
                                    </div>

                                    <!-- Text input-->
                                    <div class="control-group" style="margin-top: 10px;">
                                      <label class="control-label" for="reenterpassword">Повторите пароль:</label>
                                      <div class="controls">
                                        <input id="authorPasswordComf" class="form-control input-large" name="reenterpassword" type="password" placeholder="********" required="">
                                      </div>
                                    </div>

                                    <div class="control-group" style="margin-top: 10px;">
                                      <label class="control-label" for="authorDesc">Описание группы: </label>
                                      <div class="controls">
                                          <textarea id="authorDesc" name="authorDesc" class="form-control" rows="3" placeholder="Описание..." required=""></textarea>
                                      </div>
                                    </div>

                                    <div class="control-group" style="margin-top: 10px;">
                                        <label for="authorCover">Постер группы</label>
                                        <input type="file" id="authorCover" accept="image/jpeg">
                                    </div>

                                    </fieldset>
                              <button id="createAuthorButton" type="button" onclick="createAuthor();" class="btn btn-info btn-sm" style="margin-bottom: 20px; margin-top: 20px">Сохранить</button>
                          </form>
                        </div>
                    
                    </c:if>
                        
                    
                    <c:if test="${isAuthor}">
                    
                        <c:set value="${user.author}" var="author"/>
                        
                        <div id="groupControl" data-author="${author.name}" class="tab-pane fade">
                            <h3>Управление группой</h3>

                            <form class="form-horizontal col-xs-5">
                                <fieldset>

                                    <div class="control-group" style="margin-top: 10px;">
                                        <label for="updateCover">Новый постер группы</label>
                                        <input type="file" id="updateCover" accept="image/jpeg">
                                    </div>

                                    <div class="control-group" style="margin-top: 10px;">
                                      <label class="control-label" for="updateDesc">Новое описание группы: </label>
                                      <div class="controls">
                                          <textarea id="updateDesc" name="updateDesc" class="form-control" rows="3" placeholder="Описание..." required=""></textarea>
                                      </div>
                                    </div>

                                    </fieldset>
                                <button type="button" onclick="updateAuthor();" class="btn btn-info btn-sm" style="margin-bottom: 20px; margin-top: 20px">Сохранить</button>
                          </form>
                        </div>


                        <div id="addAlbum" class="tab-pane fade">
                            <h3>Добавить новый альбом</h3>
                            <div class="row">
                                <form class="form-horizontal col-xs-5">
                                    <fieldset>

                                        <div class="control-group" style="margin-top: 10px;">
                                          <label class="control-label" for="albumName">Название альбома: </label>
                                          <div class="controls">
                                            <input id="albumName" name="albumName" class="form-control input-large" type="text" placeholder="Новый альбом" required="">
                                          </div>
                                        </div>

                                        <div class="control-group" style="margin-top: 10px;">
                                          <label class="control-label" for="releaseDate">Дата релиза: </label>
                                          <div class="controls">
                                              <input id="releaseDate" name="releaseDate" type="text" class="form-control datepicker" style="max-width: 110px;" />
                                          </div>
                                        </div>
                                        
                                        <div class="control-group" style="margin-top: 10px;">
                                          <label class="control-label" for="albumGenres">Жанр(ы): </label>
                                          <div class="controls">
                                              <select id="albumGenres" class="input-sm form-control" title="Категории" multiple="true">
                                                    <c:forEach items="${genres}" var="genre" varStatus="status">
                                                        <option value="${genre.id}">${genre.name}</option>
                                                    </c:forEach>
                                                </select>
                                          </div>
                                        </div>
                                        
                                        <div class="control-group" style="margin-top: 10px;">
                                          <label class="control-label" for="albumPrice">Цена: </label>
                                          <div class="controls">
                                              <div class="input-group" style="max-width: 110px;">
                                                  <input id="albumPrice" type="text" class="form-control" size="7" />
                                                <span class="input-group-addon" aria-hidden="true"><i class="glyphicon glyphicon-rub"></i></span>
                                              </div>
                                          </div>
                                        </div>

                                        <div class="control-group" style="margin-top: 10px;">
                                          <label class="control-label" for="albumDesc">Описание альбома: </label>
                                          <div class="controls">
                                              <textarea id="albumDesc" name="albumDesc" class="form-control" rows="3" placeholder="Описание..." required=""></textarea>
                                          </div>
                                        </div>

                                        <div class="control-group" style="margin-top: 10px;">
                                            <label for="albumCover">Обложка альбома</label>
                                            <input type="file" id="albumCover" accept="image/jpeg">
                                        </div>

                                    </fieldset>
                              </form>
                            </div>

                            <div id="music" class="row form-inline" style="margin-top: 20px;">
                                <div class="form-group" style="display: inline; margin-top: 20px; margin-left: 15px;">
                                    <div style="display: inline-block; margin-top: 7px;">1.</div>
                                    <div style="display: inline-block;">
                                       <input id="musicName_1" type="text" class="form-control" placeholder="Название песни" style="max-width: 200px; margin-left: 10px; margin-right: 10px;" />
                                    </div>
                                    <div class="form-group" style="display: inline-block;">
                                    <input type="file" id="music_1" accept="audio/mpeg">
                                </div>
                                </div>
                            </div>
                            <div class="row" style="margin-top: 5px; margin-left: -10px;">
                                <button type="button" class="btn btn-default" onclick="addAudio();">
                                    <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                                </button>
                                <button type="button" class="btn btn-default" onclick="deleteAudio();">
                                    <span class="glyphicon glyphicon-minus" aria-hidden="true"></span>
                                </button>  
                            </div>
                            <div class="row" style="margin-top: 10px;  margin-left: 15px;">
                                <button id="createAlbum" type="button" onclick="createAlbum();" class="btn btn-info btn-sm" style="margin-bottom: 20px; margin-top: 20px;">Сохранить</button>
                             </div>
                        </div>


                        <div id="statistics" class="tab-pane fade">
                            <h3>Статистика продаж</h3>
                            <div class="row" id="statisticsTable" style="margin-top: 40px;">
                              <div class="col-lg-1"></div>
                              <div class="col-lg-10">
                                  <table class="table">
                                      <thead>
                                          <tr>
                                              <td>#</td>
                                              <td>Альбом</td>
                                              <td>Кол-во проданных копий</td>
                                          </tr>
                                      </thead>
                                      <tbody>
                                          
                                          <c:forEach items="${author.albums}" var="album" varStatus="status">
                                            <tr>
                                              <td>${status.index + 1}</td>
                                              <td><a href="#" style="color: #777;">${album.title}</a></td>
                                              <td>${album.qSold}</td>
                                            </tr>
                                          </c:forEach>
                                          
                                      </tbody>
                                  </table>
                              </div>
                              <div class="col-lg-1"></div>
                            </div>
                        </div>


                         <div id="discountMoney" class="tab-pane fade">
                            <h3>Вывод денег</h3>
                            <div class="row" style="margin-top: 40px;">
                              <div class="col-xs-1 col-sm-2 col-md-2 col-lg-3"></div>
                              <div class="col-xs-11 col-sm-10 col-md-10 col-lg-9">
                                  <div class="form-group form-inline form-group-sm" style="display: inline;"   >
                                        <div style="display: inline-block;">
                                            Снять со счета сумму: 
                                        </div>
                                      <div style="display: inline-block; max-width: 125px;" >
                                          <div class="input-group">
                                              <input id="discountCash" type="text" class="form-control" size="7" />
                                          <span class="input-group-addon" aria-hidden="true"><i class="glyphicon glyphicon-rub"></i></span>
                                          </div>
                                      </div>

                                      <div  style="display: inline-block; margin-left: 10px;">
                                          <button type="button" onclick="discountCash();" class="btn btn-info btn-sm">Снять</button>
                                      </div>
                                  </div>
                              </div>
                            </div>
                         </div>
                        
                    </c:if>    
                        
                        
                </div>
            </div>
            <div class="col-lg-4 col-md-3 col-sm-0"></div>
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
        <form id="logout" action="<c:url value="/logout"/>" method="POST">
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
    <script type="text/javascript" src="<c:url value="/lib/bootstrap/js/bootstrap.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/datepicker/js/bootstrap-datepicker.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/datepicker/js/bootstrap-datepicker.ru.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/js/navbar.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/js/dashboard.js"/>"></script>
    <c:if test="${isAdmin}">
        <script type="text/javascript" src="<c:url value="/lib/js/dashboardAdmin.js"/>"></script>
    </c:if>
    <c:if test="${isAuthor}">
        <script type="text/javascript" src="<c:url value="/lib/js/dashboardAuthor.js"/>"></script>
    </c:if>
    
</body>

</html>