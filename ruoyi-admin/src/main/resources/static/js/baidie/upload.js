

/***
 * Excel导入方法
 * @param url：导入的后端请求路径
 */
function importData(url,updateGroup) {
    var _width =  "400";
    var _height = "230";
    //这里调用的是layer.js : https://layui.itze.cn/doc/modules/layer.html
    window.layer.open({
        type: 1, // 弹出层类型，这里是普通的页面层
        area: [_width + 'px', _height + 'px'], // 弹出层的尺寸，使用_width和_height变量来指定宽度和高度
        fix: false, // 是否固定在可视区域
        maxmin: true, // 是否显示最大化和最小化按钮
        shade: 0.3, // 遮罩层的透明度
        title: '导入数据', // 弹出层的标题
        content: generateImportFormTemplate(), // 弹出层的内容，这里使用jQuery获取指定ID的元素的HTML内容
        btn: ['<i class="fa fa-check"></i> 导入', '<i class="fa fa-remove"></i> 取消'], // 弹出层的按钮组
        shadeClose: true, // 是否点击遮罩层关闭弹出层
        btn1: function(index, layero) {
            uploadFileToBackend(index, layero, url, updateGroup);//调用ajax方法和处理返回的数据
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

/***
 * 将导入的html模版作为一个对象
 * @returns {HTMLScriptElement}
 */
function generateImportFormTemplate() {
    var htmlContent = '\
        <form enctype="multipart/form-data" class="mt20 mb10">\
            <div class="col-xs-offset-1">\
                <input type="file" id="file" name="file"/>\
                <div class="mt10 pt5">\
                </div>\
                <font color="red" class="pull-left mt10">\
                    提示：仅允许导入“xlsx”格式文件！\
                </font>\
            </div>\
        </form>';
    return htmlContent;
}

/***
 * 导入数据和处理导出的数据
 * @param index
 * @param layero
 * @param updateGroup
 * @returns {boolean}
 */
function uploadFileToBackend(index, layero, url, updateGroup) {
    var file = layero.find('#file').val();
    if (file == '' || (!file.endsWith('.xlsx'))) {
        window.layer.msgWarning("请选择后缀为“xlsx”的文件。");
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
                updateGroup(result);//页面的对返回数据的处理方法
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