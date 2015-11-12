$("#createAlbum").on("click", function() {
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
    data.append("login", "test_group"); /////TMP DELETE AFTER ADDING SECURITY
    data.append("album", JSON.stringify(album));
    data.append("cover", cover);
    data.append("tracks", trackFiles)
    
    $.ajax({
        url: MAIN_URL + "/author/Test Group/album/create",
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
    
});


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