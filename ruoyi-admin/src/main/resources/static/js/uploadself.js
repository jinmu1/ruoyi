(function($) {
    $.fn.filesUpload = function(opts) {
        var defaults = {
            url: '',
            multiple: true,
            accept: '',
            fileTypes: '',
            buttonText: '选择文件',
            irmeprocess: '<li id="${fileID}file"><div class="progress"><div class="progressbar"></div></div><span class="filename">${fileName}</span><span class="progressnum">0/${fileSize}</span><a class="uploadbtn">上传</a><a class="delfilebtn">删除</a></li>',
            filehtml: '<span class="selfbutton"><span class="selftext"></span><input type="file" id="FileUploadSelf" class="selfinput" /></span>',
            onUploadStart: function() {},
            onUploadSuccess: function() {},
            onUploadComplete: function() {},
            onUploadError: function() {},
            onInit: function() {},
        }
        var option = $.extend(defaults, opts);
        var _self = this
        var obj = {
            init: function() {
                _self.append(option.filehtml)
                _self.find(".selftext").text(option.buttonText)
                this.fileInput = _self.find(".selfinput")[0]
                if(option.accept && option.accept != '') {
                    _self.find(".selfinput").attr("accept", option.accept)
                }
                if(option.multiple) {
                    _self.find(".selfinput").attr("multiple", "multiple")
                }
                this.onChange()
            },
            onChange: function() {
                var that = this
                this.fileInput.addEventListener("change", function(e) {
                    var files = e.target.files || e.dataTransfer.files;
                    var filterfile = that.filter(files)
                    that.upload(filterfile)
                }, false);
            },
            upload: function(files) {
                var html = '',
                    that = this,
                    timing=new Date().getTime()
                for(var i = 0; i < files.length; i++) {
                    html += '<div class="fileprocessbox" name="'+timing+i+'"><div class="sizetext"><span>' + files[i].name + '\&nbsp;&nbsp;</span><span class="currentsize">0</span>/<span class="size">' + this.getSize(files[i].size) + '</span></div>'
                    html += '<div class="fileprocess"><div></div></div>'
                    html += '</div>'
                }
                _self.append(html)
                var data
                for(var j = 0; j < files.length; j++) {
                    var formData = new FormData();
                    formData.append("file", files[j]); //加入文件对象
                    data = formData;
                    that.send(timing,j,data)
                }
                //					$.ajax({
                //						url: "http://up.qiniu.com/",
                //						data: data,
                //						type: "Post",
                //						dataType: "json",
                //						cache: false, //上传文件无需缓存
                //						processData: false, //用于对data参数进行序列化处理 这里必须false
                //						contentType: false, //必须
                //						xhr: that.xhrOnProgress(function(e) {
                //							var percent = e.loaded / e.total*100;
                ////							$(".fileprocessbox").eq(j).find(".fileprocess div").width(percent+"%")
                ////							console.log(e);
                //						}),
                //						success: function(result) {
                //							console.log(result);
                //						},
                //					})
                //				}
            },
            send:function(timing,index,file){
                var xhr = new XMLHttpRequest();
                if(xhr.upload) {
                    xhr.upload.addEventListener("progress", function(e) {
                        var percent = (e.loaded / e.total*100).toFixed(2);
                        $('.fileprocessbox[name='+(timing+''+index)+']').find(".fileprocess div").width(percent+"%")
                        var size=(parseFloat($('.fileprocessbox[name='+(timing+''+index)+']').find(".size").text())*percent/100).toFixed(2)
                        var texts=$('.fileprocessbox[name='+(timing+''+index)+']').find(".size").text()
                        var unit=texts.replace(/\d+(\.\d+)/g,'')
                        $('.fileprocessbox[name='+(timing+''+index)+']').find(".currentsize").text(size+unit)
                    }, false);
                    xhr.onreadystatechange = function(e) {
                        if(xhr.readyState == 4) {
                            if(xhr.status == 200) {
                                option.onUploadSuccess(xhr, xhr.responseText);
                                option.onUploadComplete();
                            } else {
                                option.onUploadError(xhr, xhr.responseText);
                            }
                            var FileUploadSelf = document.getElementById('FileUploadSelf');
                            FileUploadSelf.value = ''
                        }
                    };
                    option.onUploadStart();
                    xhr.open("POST", option.url, true);
                    xhr.send(file);
                }
            },
            getSize: function(size) {
                var s
                if(size < 1024) {
                    s = size + "B"
                } else {
                    s = size / 1024 >= 1024 ? (size / 1048576).toFixed(2) + 'M' : (size / 1024).toFixed(2) + "KB"
                }
                return s
            },
            filter: function(files) {
                var fileType = option.fileTypes ? option.fileTypes.split(",") : '',
                    result = []
                if(fileType) {
                    for(var i = 0; i < files.length; i++) {
                        if(fileType.indexOf(files[i].type.split("/")[1]) > -1) {
                            result.push(files[i])
                        } else {
                            result = []
                            alert("上传类型不对！")
                            break;
                            return false;
                        }
                    }
                } else {
                    result = files
                }
                return result
            }
        }
        obj.init()
    }
})(jQuery)