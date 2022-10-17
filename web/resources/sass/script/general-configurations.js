function overrideStyle() {
    $(document).ready(function () {
        $('*.button-prime').removeClass('ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-left');
        $('*.button-prime>span').removeClass('ui-button-icon-left ui-icon ui-c');
        $('*.ui-accordion>h3').removeClass('ui-state-default');
        $('*.ui-tabs-nav>li').removeClass('ui-state-default');

    });
}

/*function handleAjax(data) {
    var status = data.status;

    switch(status) {
        case "begin":
            // This is invoked right before ajax request is sent.
            break;

        case "complete":
        	$('*.button-prime').removeClass('ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-left');
        	$('*.button-prime>span').removeClass('ui-button-icon-left ui-icon ui-c');
            break;

        case "success":
            // This is invoked right after successful processing of ajax response and update of HTML DOM.
            someFunction();
            break;
    }
}*/