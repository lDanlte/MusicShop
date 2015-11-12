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
    var author = "Tmp Group 6";
    var data = new FormData();
    data.append("login", "tmp_group_6"); /////TMP DELETE AFTER ADDING SECURITY
    data.append("album", JSON.stringify(album));
    data.append("cover", cover);
    for(var i = 1; i <= audioCount; i++) {
      data.append("tracks[]", trackFiles[i - 1]);
    }
    
    $.ajax({
        url: MAIN_URL + "/author/" + author + "/album/create",
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


function discountCash() {
    var cash = $("#discountCash").val();
    
    var login = "tmp_group_6";
    
    $.ajax({
        url: MAIN_URL + "/user/" + login + "/discountMoney" + "?value=" + cash ,
        method: "PUT",
        dataType: "json",
        success: function (data) {
            alert("success");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("error" + errorThrown);
        }
    });
}

function updateAuthor() {
    var cover     = $("#updateCover")[0].files[0],
        albumDesc = $("#updateDesc").val();

     var type = "PUT";
     
    var data = new FormData();
    if (cover !== undefined) {
        data.append("cover", cover);
    } else {
        type = "POST";
    }
    if (albumDesc !== undefined) {
        data.append("desc", albumDesc);
    }

    var author = "Tmp Group 6";
    $.ajax({
        url: MAIN_URL + "/author/" + author + "/update",
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        type: type,
        success: function(){
            alert("Урааааа!!!");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("Упс..." + errorThrown);
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