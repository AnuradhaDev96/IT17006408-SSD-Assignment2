$(document).ready(function () {
    $("#createFolderButton").click(function(){
        var folderName = prompt("Enter folder name");
        $.ajax({
            url: '/createFolder/' + folderName
        }).done(function (data) {
            console.dir(data)
        })
    });

    $("#uploadFileInFolder").click(function () {
        $.ajax({
            url: '/uploadInFolder'
        }).done(function (data) {
            alert(data.message);
        });
    });
});