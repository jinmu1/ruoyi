
function upload(url){
    var formId, width, height;
    var currentId = $.common.isEmpty(formId) ? 'importTpl' : formId;
    var _width = $.common.isEmpty(width) ? "400" : width;
    var _height = $.common.isEmpty(height) ? "230" : height;
    window.layer.open({
        type: 1,
        area: [_width + 'px', _height + 'px'],
        fix: false,
        //不固定
        maxmin: true,
        shade: 0.3,
        title: '导入'  + '数据',
        content: $('#' + currentId).html(),
        btn: ['<i class="fa fa-check"></i> 导入', '<i class="fa fa-remove"></i> 取消'],
        // 弹层外区域关闭
        shadeClose: true,
        btn1: function(index, layero){
            var file = layero.find('#file').val();
            if (file == '' || ( !$.common.endWith(file, '.xlsx') )){
                window.layer.msgWarning("请选择后缀为“xlsx”的文件。");
                return false;
            }
            var index = window.layer.load(2, {shade: false});
            var formData = new FormData(layero.find('form')[0]);
            $.ajax({
                url: ctx+ url, //加上ctx后，从百碟客户端可访问我们线上路径
                data: formData,
                cache: false,
                async: false,
                contentType: false,
                processData: false,
                type: 'POST',
                success: function (result) {
                    if (result.code == web_status.SUCCESS) {
                        window.layer.close(index);
                        window.layer.closeAll();
                        $.modal.alertSuccess(result.msg);
                        $.table.refresh();
                         return result;
                    } else if (result.code == web_status.WARNING) {
                        window.layer.close(index);
                        window.layer.enable();
                        $.modal.alertWarning(result.msg)//将失败信息返回，提示框为警告样式
                        return ;
                    } else {
                        window.layer.close(index);
                        window.layer.enable();
                        $.modal.alertWarning(result.msg)
                        return ;
                    }
                },
                complete: function () {
                    layero.find('#file').val('');
                }
            });
        }
    });
}
function isValueEmpty(value) {
    return value === undefined || value === null ||
        (typeof value === 'string' && value.trim() === '') ||
        (Array.isArray(value) && value.length === 0) ||
        (typeof value === 'object' && Object.keys(value).length === 0 && value.constructor === Object);
}