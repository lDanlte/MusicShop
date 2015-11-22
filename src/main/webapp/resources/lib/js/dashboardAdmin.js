
function createAuthor() {
   var name     = $("#authorName").val(),
       email    = $("#authorEmail").val(),
       login    = $("#authorLogin").val(),
       pass     = $("#authorPassword").val(),
       passComf = $("#authorPasswordComf").val(),
       desc     = $("#authorDesc").val(),
       cover    = $("#authorCover")[0].files[0];
       
    if (pass != passComf) {
        showMessage("Внимание", "Пароли не совпвдают.");
        return;
    }
    
    var author = {
        name: name,
        desc: desc,
        user : {
            login: login,
            email: email,
            pass: pass
        }
    }
    
    var data = new FormData();
    data.append("author", JSON.stringify(author));
    data.append("image", cover);
    
    $.ajax({
        url: MAIN_URL + "author",
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        dataType: "json",
        type: 'POST',
        success: function(respData){
            showMessage("Инфо", respData.responseMessageDto.msg);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            var respMessage = jqXHR.responseJSON;
             if (respMessage === undefined) {
                showMessage("Внимание", "Произошла неизвестная ошибка. Повторите позднее.");
            } else {
                showMessage("Внимание", respMessage.msg);
            }
            
        }
    });
    
}


