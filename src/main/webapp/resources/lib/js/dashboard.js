function showHistoryTable() {

    var from = $("#histotyDateFrom").val(),
          to = $("#histotyDateTo").val();
  
    if (!checkStrs(from, to)) {
        showMessage("Внимание", "Не заполнено одно из полей.");
        return;
    }
  
    var div = $("#historyTable");
    div.addClass("hide");
    $.ajax({
        url: MAIN_URL + "user/tradehistory" + "?from=" + from + "&to=" + to ,
        method: "GET",
        dataType: "json",
        success: function (dataList) {
            
            if (dataList === undefined || dataList === null || dataList.tradeHistoryDtoList === undefined) {
                showMessage("Внимание", "Произошла неизвестная ошибка. Повторите позднее.");
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
            var respMessage = jqXHR.responseJSON;
            if (respMessage === undefined) {
                showMessage("Внимание", "Произошла неизвестная ошибка. Повторите позднее.");
            } else {
               showMessage("Внимание", respMessage.msg);
            }
        }

    });

}

function addCash() {
    var cash = $("#addCash").val();
    
     if (cash == "") {
        showMessage("Внимание", "Не введена сумма.");
        return;
    } 
    if (cash <= 0) {
        showMessage("Внимание", "Нверно введена сумма.");
        return;
    }
    
    $.ajax({
        url: MAIN_URL + "user/addMoney" + "?value=" + cash ,
        method: "PUT",
        dataType: "json",
        success: function (data) {
            popover.popover("hide");
            $("#wallet").html(data.userDto.wallet);
           popover.data('bs.popover').options.content = $('#popover-content').html();
            showMessage("Инфо", "Счет успешно пополнен.");
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

function changeUserData() {
    var email = $("#newEmail").val(),
        pass = $("#newPassword").val(),
        comfPass = $("#newReenterpassword").val();
    if (email == "" && pass == "" && comfPass == "") {
        showMessage("Внимание", "Все поля пустые.");
        return;
    }
    
    if (!EMAIL_REG_EXP.test(email)) {
        showMessage("Внимание", "Неверно записан email.");
        return;
    }
    
    if (pass != comfPass) {
        showMessage("Внимание", "Пароли не совпвдают.");
        return;
    }
    var newUser = {};
    if (email != "") {
        newUser.email = email;
    }
    if (pass != "") {
        newUser.pass = pass;
    }
    
     $.ajax({
        url: MAIN_URL + "user",
        method: "PUT",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(newUser),
        success: function (data, textStatus) {
            showMessage("Инфо", data.responseMessageDto.msg);
            if (newUser.pass !== undefined) {
                $("#modalInfo").on('hidden.bs.modal', function (e) { 
                    $("#logout").submit();
                });
            }
        },
        error: function (jqXHR, textStatus, errorThrown) { 
            var respMessage = jqXHR.responseJSON;
             if (respMessage === undefined) {
                showMessage("Внимание", "Произошла неизвестная ошибка. Повторите позднее.");
            } else {
               showMessage("Ошибка при обновлении данных.", respMessage.msg);
            }
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
            endDate: todayDate,
            orientation: "auto bottom"
        });
    });
});


function makeDateStr (date) {
    var dated   = date.getDate(),
        month   = date.getMonth() + 1,
        year    = date.getFullYear(),
        hours   = date.getHours(),
        minutes = date.getMinutes(),
        seconds = date.getSeconds();

    return ((dated < 10) ? "0" : "") + dated + "." + ((month < 10) ? "0" : "") + month + "."
            + year + " " + ((hours < 10) ? "0" : "") + hours + ":" + ((minutes < 10) ? "0" : "") + minutes
            + ":" + ((seconds < 10) ? "0" : "") + seconds;
}


