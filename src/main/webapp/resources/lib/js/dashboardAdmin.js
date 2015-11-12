
function createAuthor() {
   var name =  $("#authorName").val(),
       email = $("#authorEmail").val(),
       login = $("#authorLogin").val(),
       pass = $("#authorPassword").val(),
       passComf = $("#authorPasswordComf").val(),
       desc = $("#authorDesc").val(),
       cover = $("#authorCover")[0].files[0];
       
    if (pass != passComf) {
        alert("Пароли не совпвдают.");
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
        url: MAIN_URL + "/author/create",
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        type: 'POST',
        success: function(){
            alert("Урааааа!!!");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("Упс...");
        }
    });
    
}


