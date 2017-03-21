<%-- 
    Document   : album
    Created on : 05.11.2015, 22:33:14
    Author     : Dante
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <c:set value="${album.author.name}" var="authorName"/>
    <c:set value="${album.title}" var="albumName"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>${albumName}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Music online store">
    <meta name="author" content="Denis Antonov">

    <link rel="stylesheet" type="text/css"   href="<c:url value="/lib/bootstrap/css/bootstrap.min.css"/>" />
    <link rel='stylesheet' type="text/css"   href='<c:url value="/lib/css/main.css"/>'>
    
    <c:if test="${isBought == true}">
        <link rel="stylesheet" type="text/css" href="<c:url value="/lib/jplayer/css/jplayer.blue.monday.min.css"/>" />
    </c:if>
    
</head>

<body style="background-color: white;">
    <!-- Fixed navbar -->
    <%@include file="fragment/navBar.jspf"%>
    
    <div class="container-fluid">
        <div class="row">
            <div class="col-lg-2 col-md-2 col-sm-1"></div>
            <div class="col-lg-6 col-md-8 col-sm-9">
                <div class="jumbotron carouset-content" style="padding: 5px;">
                    <div class="conteiner">
                        <div class="row">
                            <img class="img-rounded" src="<c:url value="/resource/${authorName}/${albumName}/cover.jpg"/>" style="height: 280px; float: left; margin-left: 15px; margin-right: 15px;">

                            <h4><strong class="margintext" style="margin-top: 10px;">${albumName}</strong></h4>

                            <p>
                            <div class="margintext" style="display: inline;">
                                <div style="display: inline-block;">
                                    <h4><small><a href="<c:url value="/author/${authorName}"/>" class="acolor" style="color: #777;">${authorName}</a></small></h4>
                                </div>
                                <div style="display: inline-block; margin-left: 15px;">
                                    <h5>${dateFormat.format(album.releaseDate)}</h5>
                                </div>
                            </div>
                            <h5><small class="margintext">
                                    <c:set var="size" value="${fn:length(album.genres)}"/>
                                    <c:forEach items="${album.genres}" var="genre" varStatus="status">
                                        
                                        ${genre.name}<c:if test="${status.index != size - 1}">, </c:if>
                                    </c:forEach>
                            </small></h5>
                            <p style="margin-top: 40px;">
                            <h4><small class="margintext" style="color: #000; padding-right: 20px;">${album.desc}</small></h4>
                            <button type = "button" class="btn btn-info" onclick="buy();" style="position: absolute; bottom: 45px; right: 40px; 
                                    <c:if test="${isBought == true}">cursor: default;</c:if>" <c:if test="${isBought == true}"> disabled="disabled"</c:if> >
                                
                                <c:choose>
                                    <c:when test="${isBought == true}">
                                        Куплено
                                    </c:when>
                                    <c:otherwise>
                                        Купить за ${format.format(album.price)}
                                        <span class="glyphicon glyphicon-rub" aria-hidden="true"></span>
                                    </c:otherwise>
                                </c:choose>
                                        
                            </button>
                    <!--/row-->

                        </div>
                    </div>
                </div>
                <!--/span-->
            </div>
            <div class="col-lg-4 col-md-2 col-sm-2"></div>
        </div>
                            
        <div class="row">
            <div class="col-lg-2 col-md-2 col-sm-1"></div>
            <div class="col-lg-8 col-md-8 col-sm-10 content-category"><h3><strong>Треки</strong></h3></div>
            <div class="col-lg-2 col-md-2 col-sm-1"></div>
        </div>
                            
        <div class="row">
            <div class="col-lg-2 col-md-2 col-sm-1"></div>
            <div class="col-lg-6 col-md-7 col-sm-10  carouset-content" style="padding: 5px; margin-left: 15px;">

                <c:choose>
                    
                    <c:when test="${isBought == false}">
                        
                        <table class="table table-hover">
                            <thead>
                            <tr>
                                <td>#</td>
                                <td>Название</td>
                                <td>Длительность</td>
                            </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${album.tracks}" var="track">
                                <tr>
                                    <td>${track.position}</td>
                                    <td>${track.name}</td>
                                    
                                    <td>
                                        <fmt:formatNumber var="min" value="${(track.duration - track.duration % 60) / 60}" maxFractionDigits="0" />
                                        <fmt:formatNumber var="sec" value="${track.duration % 60}" maxFractionDigits="0" pattern="00" />
                                        ${min}:${sec}
                                    </td>
                                </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        
                    </c:when>
                    
                    <c:otherwise>
                        
                        <div id="jquery_jplayer_1" class="jp-jplayer"></div>
                        <div id="jp_container_1" class="jp-audio" role="application" aria-label="media player" style="margin-bottom: 15px;">
                                    <div class="jp-type-playlist">
                                            <div class="jp-gui jp-interface">
                                                    <div class="jp-controls">
                                                            <button class="jp-previous" role="button" tabindex="0">Предыдущий</button>
                                                            <button class="jp-play" role="button" tabindex="0">Пуск</button>
                                                            <button class="jp-next" role="button" tabindex="0">Слудующий</button>
                                                            <button class="jp-stop" role="button" tabindex="0">Стоп</button>
                                                    </div>
                                                    <div class="jp-progress">
                                                            <div class="jp-seek-bar">
                                                                    <div class="jp-play-bar"></div>
                                                            </div>
                                                    </div>
                                                    <div class="jp-volume-controls">
                                                            <button class="jp-mute" role="button" tabindex="0">Звук выкл.</button>
                                                            <button class="jp-volume-max" role="button" tabindex="0">Громкость</button>
                                                            <div class="jp-volume-bar">
                                                                    <div class="jp-volume-bar-value"></div>
                                                            </div>
                                                    </div>
                                                    <div class="jp-time-holder">
                                                            <div class="jp-current-time" role="timer" aria-label="time">&nbsp;</div>
                                                            <div class="jp-duration" role="timer" aria-label="duration">&nbsp;</div>
                                                    </div>
                                                    <div class="jp-toggles">
                                                            <button class="jp-repeat" role="button" tabindex="0">Повтор</button>
                                                            <button class="jp-shuffle" role="button" tabindex="0">Случайный порядок</button>
                                                    </div>
                                            </div>
                                            <div class="jp-playlist">
                                                    <ul>
                                                            <li>&nbsp;</li>
                                                    </ul>
                                            </div>
                                            <div class="jp-no-solution">
                                                    <span>Необходимо обновление</span>
                                                    Для того, чтобы была возможеость проигрывания музыки вам нужна свежая версия <a href="http://get.adobe.com/flashplayer/" target="_blank">Flash plugin</a>.
                                            </div>
                                    </div>
                            </div>
                        
                    </c:otherwise>
                    
                </c:choose>
            </div>
            <div class="col-lg-4 col-md-3 col-sm-1"></div>
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
    <script type="text/javascript" src="<c:url value="/lib/bootstrap/js/bootstrap.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/lib/js/navbar.js"/>"></script>
    <c:if test="${isBought == true}">
        <script type="text/javascript" src="<c:url value="/lib/jplayer/js/jquery.jplayer.min.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/lib/jplayer/js/jplayer.playlist.min.js"/>"></script>
        
        <script>
            $(document).ready(function(){

                new jPlayerPlaylist({
                        jPlayer: "#jquery_jplayer_1",
                        cssSelectorAncestor: "#jp_container_1"
                }, [
                        <c:forEach items="${album.tracks}" var="track">
                            {
                                    title: "${track.name}",
                                    mp3: "<c:url value="/author/${authorName}/album/${albumName}/track/${track.position}.mp3"/>"
                            },
                        </c:forEach>        
                ], {
                        supplied: "mp3",
                        wmode: "window",
                        useStateClassSkin: true,
                        autoBlur: false,
                        smoothPlayBar: true,
                        keyEnabled: true,
                        swfPath:"<c:url value="/lib/jplayer/js/jquery.jplayer.swf"/>",
                        solution:"flash, html"
                });
            
            });
        </script>
    </c:if>
</body>

</html>
