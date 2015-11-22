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
       
    $('#albumGenres :selected').each(function(i, selected){ 
        genres += $(selected).val() + ',';
    });
    genres = genres.substring(0, genres.length - 1);
    
    for(var i = 1; i <= audioCount; i++) {
        trackNames.push($("#musicName_" + i).val());
        trackFiles.push($("#music_" + i)[0].files[0]);
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

     var type = "POST";
     
    var data = new FormData();
    if (cover !== undefined) {
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
                            "<input type='file' id='music_" + audioCount + "' accept='audio/mpeg3'>" + 
                        "</div>" +
                    "</div>";
    elem.append(html);
}