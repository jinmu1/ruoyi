/***
 * Excel导入方法
 * @param url：导入的后端请求路径
 */
function importData(url) {
    var formId, width, height;
    var currentId = $.common.isEmpty(formId) ? 'importTpl' : formId;
    var _width = $.common.isEmpty(width) ? "400" : width;
    var _height = $.common.isEmpty(height) ? "230" : height;

    window.layer.open({
        type: 1,
        area: [_width + 'px', _height + 'px'],
        fix: false,
        maxmin: true,
        shade: 0.3,
        title: '导入数据',
        content: $('#' + currentId).html(),
        btn: ['<i class="fa fa-check"></i> 导入', '<i class="fa fa-remove"></i> 取消'],
        shadeClose: true,
        btn1: function(index, layero) {
            var file = layero.find('#file').val();
            if (file == '' || (!file.endsWith('.xls') && !file.endsWith('.xlsx') && !file.endsWith('.csv'))) {
                window.layer.msgWarning("请选择后缀为 “xls”、“csv”或“xlsx”的文件。");
                return false;
            }

            var formData = new FormData(layero.find('form')[0]);
            var loadingIndex = window.layer.load(2, {shade: false});
            $.ajax({
                url: ctx+url,
                data: formData,
                cache: false,
                contentType: false,
                processData: false,
                type: 'POST',
                success: function(result) {

                    if (result.code == web_status.SUCCESS) {
                        popUpWindow($.modal.icon("success"), result.msg);
                        $.table.refresh();
                        handleImportResult(url,result);//页面的对返回数据的处理方法
                    } else {
                        popUpWindow($.modal.icon("warning"), result.msg);
                    }
                },
                complete: function() {
                    layero.find('#file').val('');
                    window.layer.close(loadingIndex);
                }
            });
        }
    });
}

/***
 * 弹出返回的结果提示信息
 * @param icon
 * @param msg
 */
function popUpWindow(icon, msg) {
    window.layer.closeAll();
    window.layer.alert(msg, {
        icon: icon,
        title: "系统提示",
        btn: ['确认'],
        btnclass: ['btn btn-primary'],
    });
}