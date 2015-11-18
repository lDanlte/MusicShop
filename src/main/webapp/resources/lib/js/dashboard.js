function showHistoryTable() {

    var from = $("#histotyDateFrom").val(),
          to = $("#histotyDateTo").val();
    var div = $("#historyTable");
    div.addClass("hide");
  $.ajax({
        url: MAIN_URL + "/user/tradehistory" + "?from=" + from + "&to=" + to ,
        method: "GET",
        dataType: "json",
        success: function (dataList) {
            
            if (dataList === undefined || dataList === null || dataList.tradeHistoryDtoList === undefined) {
                alert("Data is undefined");
                return;
            }
            var data = dataList.tradeHistoryDtoList;
            var tableBody = $("#historyTableBody");
            tableBody.empty();
            
            for (var i = 0; i < data.length; i++) {
                var html = "<tr>" +
                                "<td>" +
                                makeDateStr(new Date(data[i].datetime)) +
                                "</td>" + 
                                "<td>" + 
                                data[i].action + " " + ((data[i].album !== null) ? ("<strong>" + data[i].album) + " </strong>" : "") + 
                                "</td>" + 
                                "<td>" +
                                data[i].price + 
                                "</td>" +
                            "</tr>"
                tableBody.append(html);
            }
            div.removeClass("hide");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("Упс... Что-то пошло не так." + errorThrown);
        }

    });

}

function addCash() {
    var cash = $("#addCash").val();
    
    $.ajax({
        url: MAIN_URL + "/user/addMoney" + "?value=" + cash ,
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

var todayDate = new Date();
todayDate.setDate(todayDate.getDate() + 1);

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


function makeDateStr (date) {
    var dated = date.getDate(),
        month = date.getMonth() + 1,
        year = date.getFullYear(),
        hours = date.getHours(),
        munutes = date.getMinutes(),
        seconds = date.getSeconds();

    return ((dated < 10) ? "0" : "") + dated + "." + ((month < 10) ? "0" : "") + month + "."
            + year + " " + ((hours < 10) ? "0" : "") + hours + ":" + ((munutes < 10) ? "0" : "") + munutes
            + ":" + ((seconds < 10) ? "0" : "") + seconds;
}


