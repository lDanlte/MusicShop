$('.jcarousel').each(function() {
    var elem = $(this);
    var carousel = elem.jcarousel({

    });
    elem.find(".jcarousel-prev").jcarouselControl({
        target: '-=2',
        carousel: carousel
    });
    elem.find('.jcarousel-next').jcarouselControl({
        target: '+=2',
        carousel: carousel
    });
});

$(".album").each(function(){
    var widthStr = $(this).find("img").css("width");
    if (widthStr == "") {
        return;
    }
    var width = (parseInt(widthStr, 10) - 5) + "px";
    $(this).find("h5").each(function() {
        $(this).css("width", width);
    })
});

function albumPage(author, album) {
    location = MAIN_URL + "author/" + author + "/album/" + album;
}

function authorPage(author) {
     location = MAIN_URL + "author/" + author ;
}
