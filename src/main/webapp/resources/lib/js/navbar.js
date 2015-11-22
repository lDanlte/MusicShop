var MAIN_URL = "http://localhost:8084/MusicStore/";

var popover = $("[data-toggle=popover]").popover({
    html: true, 
    content: $('#popover-content').html()
});

$(document).ready(function(){
   var modal = $("#modalInfo");
   var content = modal.find(".modal-body");
   if (content.html() != "") {
       modal.modal();
   }
});


$(window).on('resize', function () {
     $("[data-toggle=popover]").popover("hide");
});

$("#genreSelect").on("change", function() {
    
    location = MAIN_URL + "category?gid=" + $("#genreSelect :selected").val();
    
});

function registration() {
    var login = $("#newLogin").val(),
        email = $("#newEmail").val(),
        pass = $("#newPass").val(),
        passComf = $("#newPass2").val();

    if (pass != passComf) {
        showMessage("Внимание", "Пароли не совпвдают.");
        return;
    }
    
    var newUser = {
        login: login,
        email: email,
        pass: pass
    }
    
    $.ajax({
        url: MAIN_URL + "user",
        method: "POST",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(newUser),
        success: function (respData) {
            showMessage("Инфо", respData.responseMessageDto.msg);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            var respMessage = jqXHR.responseJSON;
            if (respMessage === undefined) {
                showMessage("Ошибка при регистрации", "Произошла неизвестная ошибка. Повторите позднее.");
            } else {
                showMessage("Ошибка при регистрации.", respMessage.msg);
            }
        }
    });

}

function auth() {
    var login = $(".popover #authLogin").val(),
        pass  = $(".popover #authPass").val();
    var data = new FormData();
        data.append("login", login);
        data.append("pass", pass);

    $.ajax({
        url: MAIN_URL + "login",
        method: "POST",
        cache: false,
        contentType: false,
        processData: false,
        data: data,
        success: function (respData, textStatus, jqXHR) {
            var user = respData.userDto;
            popover.popover("hide");
            var oldContent = $("#popover-content");
            var newContent = $("#popover-content-disable");
            oldContent.attr("id", "popover-content-disable");
            newContent.attr("id", "popover-content");
            $("#login").html(user.login);
            $("#wallet").html(user.wallet);
            popover.data('bs.popover').options.content = $('#popover-content').html();
        },
        error: function(jqXHR) {
            var respMessage = jqXHR.responseJSON;
            if (respMessage === undefined) {
                showMessage("Ошибка при авторизации.", "Произошла неизвестная ошибка. Повторите позднее.");
            } else {
                showMessage("Ошибка при авторизации.", respMessage.msg);
            }
        }
    });
}

function logout() {
    $.ajax({
        url: MAIN_URL + "logout",
        method: "POST",
        success: function (data, textStatus, jqXHR) {
            console.log("Логаут прошел успешно.");
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log("Ошибка при логауте " + jqXHR.responseText);
        }
    });
}

function buy() {
    var url = "http://" + location.host + location.pathname + "/buy";
    $.ajax({
        url: url,
        method: "PUT",
        dataType: "json",
        success: function (respData, textStatus, jqXHR) {
            showMessage("Инфо", respData.responseMessageDto.msg);
            $("#modalInfo").on('hidden.bs.modal', function (e) { 
                location.reload(true);
            });
        },
        error: function(jqXHR) {
            var respMessage = jqXHR.responseJSON;
            if (respMessage === undefined) {
                showMessage("Ошибка при регистрации", "Произошла неизвестная ошибка. Повторите позднее.");
            } else {
                showMessage("Ошибка при регистрации.", respMessage.msg);
            }
        }
    });
}

function showMessage(title, message) {
    var modal = $("#modalInfo");
    
    modal.find(".modal-body").html(message);
    modal.find(".modal-title").html(title);
    
    modal.modal();
}
