var author = $("#groupControl").data("author");

function createAlbum() {
   var name =  $("#albumName").val(),
       releaseDate = $("#releaseDate").val(),
       price = $("#albumPrice").val(),
       genres = "",
       albumDesc = $("#albumDesc").val(),
       trackNames = [],
       trackFiles = [],
       cover = $("#albumCover")[0].files[0];
       
    if (audioCount == 0) {
        showMessage("Внимание", "Нужно добавить хотя бы одну песню.");
        return;
    }   
       
    if (!checkStrs(name, releaseDate, price, albumDesc)) {
        showMessage("Внимание", "Не заполнено одно из полей.");
        return;
    }
    
    if (cover === undefined) {
        showMessage("Внимание", "Не Не выбрана обложка.");
        return;
    }
    
    if (cover.type != "image/jpeg") {
        showMessage("Внимание", "Тип файла обложки должен быть .jpg.");
        return;
    }
       
    $('#albumGenres :selected').each(function(i, selected){ 
        genres += $(selected).val() + ',';
    });
    
    if (genres == "") {
        showMessage("Внимание", "Не выбран жанр.");
        return;
    }
    
    genres = genres.substring(0, genres.length - 1);
    
    
    
    for(var i = 1; i <= audioCount; i++) {
        var musicName = $("#musicName_" + i).val();
        var music = $("#music_" + i)[0].files[0];
        if (musicName == "" || music === undefined) {
            break;
        }
        if (music.type != "audio/mp3") {
            showMessage("Внимание", music.name + " не является mp3 файлом.");
            return;
        }
        trackNames.push(musicName);
        trackFiles.push(music);
    }
    if (trackNames.length == 0) {
        showMessage("Внимание", "Не добавлены песни.");
        return;
    }
    
    var album = {
        title: name,
        desc: albumDesc,
        price: price,
        releaseDate: releaseDate,
        genresIds: genres,
        songsTitles: trackNames
    }
    var data = new FormData();
    data.append("album", JSON.stringify(album));
    data.append("cover", cover);
    for(var i = 1; i <= audioCount; i++) {
      data.append("tracks[]", trackFiles[i - 1]);
    }
    
    $.ajax({
        url: MAIN_URL + "author/" + author + "/album",
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


function discountCash() {
    var cash = $("#discountCash").val();
    if (cash == "") {
        showMessage("Внимание", "Не введена сумма.");
        return;
    } 
    if (cash <= 0) {
        showMessage("Внимание", "Нверно введена сумма.");
        return;
    }
    
    $.ajax({
        url: MAIN_URL + "user/discountMoney" + "?value=" + cash ,
        method: "PUT",
        dataType: "json",
        success: function (data) {
            $("#wallet").html(data.userDto.wallet);
            showMessage("Инфо", "Со счета успешно переведены деньги.");
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

function updateAuthor() {
    var cover     = $("#updateCover")[0].files[0],
        albumDesc = $("#updateDesc").val();
        
    if (cover === undefined && albumDesc == "") {
        showMessage("Внимание", "Не введены данные для обновления.");
        return;
    }
    var type = "POST";
     
    var data = new FormData();
    if (cover !== undefined) {
        if (cover.type != "image/jpeg") {
            showMessage("Внимание", "Тип файла обложки должен быть .jpg.");
            return;
        }
        data.append("cover", cover);
    } else {
        type = "PUT";
    }

    $.ajax({
        url: MAIN_URL + "author/" + author + ((albumDesc != "") ? "?desc=" + albumDesc : ""),
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        dataType: "json",
        type: type,
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

var audioCount = 1;

function addAudio() {
    audioCount++;
    var elem = $("#music");
    var html = "<p><div class='control-group' style='display: inline; margin-top: 20px; margin-left: 15px;'>" + 
                        "<div style='display: inline-block; margin-top: 7px;'>" + audioCount + ".</div>" + 
                        "<div style='display: inline-block; margin-left: 4px;margin-right: 4px;'>" + 
                           "<input id='musicName_" + audioCount + "' type='text' class='form-control' placeholder='Название песни' style='max-width: 200px; margin-left: 10px; margin-right: 10px;' />" +
                        "</div>" +
                        "<div class='control-group' style='display: inline-block;'>" +
                            "<input type='file' id='music_" + audioCount + "' accept='audio/mpeg'>" + 
                        "</div>" +
                    "</div>";
    elem.append(html);
}

function deleteAudio() {
    if (audioCount == 0) {
        return;
    }
    $($($("#musicName_" + audioCount).parent().get(0)).parent().get(0)).remove();
    audioCount--; 
}