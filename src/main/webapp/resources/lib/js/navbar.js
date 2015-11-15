var MAIN_URL = "http://localhost:8084/MusicStore/";

$("[data-toggle=popover]").popover({
    html: true, 
    content: $('#popover-content').html()
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
        alert("Пароли не совпвдают.");
        return;
    }
    
    var newUser = {
        login: login,
        email: email,
        pass: pass
    }
    
    $.ajax({
        url: MAIN_URL + "user/registration",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(newUser),
        success: function (data, textStatus) {
            alert("Вы успешно зарегистрированы.");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("Упс... Что-то пошло не так.");
        }
    });

}

function auth() {
    var login = $(".popover #authLogin").val(),
        pass  = $(".popover #authPass").val(); //блядь, это какая-то магия. Если искать элемент по id, то val() возвращяет пустую строку.
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
        success: function (data, textStatus, jqXHR) {
            alert("Авторизация прошла успешно.");
        },
        error: function(jqXHR, textStatus, errorThrown) {
            alert("Auth faild.");
        }
    });
}

function logout() {
    $.ajax({
        url: MAIN_URL + "logout",
        method: "POST",
        success: function (data, textStatus, jqXHR) {
            alert("logout");
        },
        error: function(jqXHR, textStatus, errorThrown) {
            alert("logout faild");
        }
    });
}
