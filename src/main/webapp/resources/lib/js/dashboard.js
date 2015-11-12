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



