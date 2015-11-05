$("[data-toggle=popover]").popover({
    html: true, 
    content: function() {
        if ($("#login").html() == "Вход") {
            return $('#popover-content-login').html();
        } else {
            return $("#popover-content-user").html();
        }
    }
});


$(window).on('resize', function () {
     $("[data-toggle=popover]").popover("hide");
});

$("#genreSelect").on("change", function() {
    location = location + "#" + $("#genreSelect :selected").val();
});



