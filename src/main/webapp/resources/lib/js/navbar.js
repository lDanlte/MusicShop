var MAIN_URL = "http://localhost:8084/MusicStore/";

$("[data-toggle=popover]").popover({
    html: true, 
    content: function() {
        if ($("#login").html() == "Вход") {
            return $('#popover-content-login').html();
        } else {
            return $("#popover-content-user").html();
        }
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