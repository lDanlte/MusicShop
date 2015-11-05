function showHistoryTable() {

    var div = $("#historyTable");
    if (div.hasClass("hide")) {
        div.removeClass("hide");
    } else {
        div.addClass("hide");
    }

}

var todayDate = new Date(),
    tomorrowDate = todayDate;
tomorrowDate.setDate(tomorrowDate.getDate() + 1);

$(document).ready(function () {
    $('.datepicker').each(function () {
        $(this).datepicker({
            format: "dd.mm.yyyy",
            todayHighlight: true,
            language: "ru",
            endDate: todayDate
        });
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


