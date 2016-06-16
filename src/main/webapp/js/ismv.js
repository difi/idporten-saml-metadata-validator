function showResult() {
    $("#result-panel").show();
}

function setFilename() {
    var split = $("#file-picker").val().split('\\');
    //In case of linux
    if (split.length == 1) {
        split = filename.split('/');
    }
    var filename = $("#filename");
    filename.html(split[split.length - 1]);
    filename.fadeTo(1000, 0.40);
    filename.fadeTo(1000, 1);
    $('#validate').removeAttr('disabled').removeClass('button-disabled');
}
