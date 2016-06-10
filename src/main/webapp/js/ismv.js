function showResult() {
    $("#result-panel").show();
}

function setFilename() {
    var split = $("#file-picker").val().split('\\');
    //In case of linux
    if (split.length == 1) {
        split = filename.split('/');
    }
    $('#filename').html(split[split.length-1]);
    $('#validate').removeAttr('disabled');
}
