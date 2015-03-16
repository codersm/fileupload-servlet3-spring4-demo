$(function () {
    var listBody = $("#listBody");
    var basePath = listBody.attr("basePath");
    var form = $("#form");
    var msg = $("#msg");
    msg.hide();
    
    function generateStoreTargetsList(storeTargets) {
        listBody.empty();
        var len = storeTargets.length;

        if (len >= 10) {
            form.hide();
            msg.show();
        } else {
            msg.hide();
            form.show();
        }

        for (var i = 0; i < len; ++i) {
            var storeTarget = storeTargets[i];
            var downloadUrl = basePath + "file/pull/" + storeTarget.id;
            var deleteUrl = basePath + "file/delete/" + storeTarget.id;
            var tr = $(document.createElement("tr"));

            var tdId = $(document.createElement("td"));
            tdId.html(storeTarget.id);

            var tdPath = $(document.createElement("td"));
            tdPath.html(storeTarget.path);

            var tdName = $(document.createElement("td"));
            tdName.html(storeTarget.name);

            var thOperate = $(document.createElement("td"));
            thOperate.append("<span><a href=" + downloadUrl + ">下载</a></span><span><a href=" + deleteUrl + ">删除</a></span>");

            var thImg = $(document.createElement("td"));
            thImg.append("<img src='" + downloadUrl + "' />");
            thImg.children("img").attr("onerror", "javascript:this.src='" + basePath + "resources/images/notImage.png'");

            tr.append(tdId);
            tr.append(tdPath);
            tr.append(tdName);
            tr.append(thImg);
            tr.append(thOperate);

            listBody.append(tr);
        }
    }

    function findAllFileStorage() {
        var url = basePath + "file/findAllFileStorage";
        $.get(url, function (storeTargets) {

            generateStoreTargetsList(storeTargets);
        });
    }

    findAllFileStorage();

});