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

function albumPage(elem) {
    var e = $(elem);
    //some stuff doing here
    location = location + "#";
}
