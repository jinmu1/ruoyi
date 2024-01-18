package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.BlogContent;
import com.ruoyi.system.domain.Content;
import com.ruoyi.system.service.IBlogContentService;
import com.ruoyi.system.service.IContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 用户信息
 * 
 * @author ruoyi
 */
@Controller
public class SysContentController extends BaseController
{
    @Autowired
    private IContentService contentService;

    @Autowired
    private IBlogContentService blogContentService;
    @GetMapping("/account/blog/open/post")
    @ResponseBody
    public HashMap<String,Object> open(HttpServletRequest req){
        BlogContent contentDO =blogContentService.selectBlogContentById(Long.parseLong(req.getParameter("id")));
        HashMap<String,Object> pm1 = new HashMap<>();
        pm1.put("content",contentDO.getContent());
        return pm1;
    }

    @GetMapping("/account/blog/open/list")
    @ResponseBody
    public List<BlogContent> getList(){
        BlogContent blogContent = new BlogContent();
        blogContent.setDel(1);
        return blogContentService.selectList(blogContent);
    }

    @PostMapping("/account/blog/eidt")
    @ResponseBody
    public AjaxResult saveBlog(HttpServletRequest request){
        BlogContent contentDO = new BlogContent();
        contentDO.setTitle(request.getParameter("title"));
        contentDO.setContent(request.getParameter("content"));
        contentDO.setType((long) 1);
        contentDO.setDel(1);
        blogContentService.insertBlogContent(contentDO);
        return AjaxResult.success("提交成功");
    }
    @GetMapping("/account/getContent")
    @ResponseBody
    public HashMap<String,Object> getContent(HttpServletRequest req){
        String page = req.getParameter("page");
        String type = req.getParameter("type");
        HashMap<String,Object> pm = new HashMap<>();
        pm.put("start",(Integer.parseInt(page)-1)*6);
        pm.put("type",type);
        List<Content> list = contentService.getContentList(pm);
        int num = contentService.getContentNum(pm);
        for (int i = 0;i<list.size();i++){
            sub(list.get(i));
        }
        HashMap<String,Object> pm1 = new HashMap<>();
        pm1.put("page",num);
        pm1.put("content",list);
        return pm1;
    }

    private void sub(Content content) {
        String title = content.getTitle();
        String[] s = title.split(".pdf");
        String th= s[0];
        content.setTitle(th);
    }



    @PostMapping("/account/imgUpload")
    @ResponseBody
    public AjaxResult imgUpload(@PathVariable(name = "image_data", required = false) MultipartFile file) throws IOException {
        String url=null;
        if (!file.isEmpty()) {
            try {
                Date date=new Date();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd-hh-mm-ss");
                String newCompanyImagepath = "C:/var/uploaded_files/" + sdf.format(date)+file.getName()+".png";
                File newFile = new File(newCompanyImagepath);
                if (!newFile.exists()) {
                    newFile.createNewFile();
                }
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(newFile));
                url =  "http://edu.glc360.com/file/"+sdf.format(date)+file.getName()+".png";
                out.write(file.getBytes());
                out.flush();
                out.close();
                return   AjaxResult.success(url);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return AjaxResult.success("图片上传失败！");
            } catch (IOException e) {
                e.printStackTrace();
                return AjaxResult.success("图片上传失败！");
            }
        }
        return AjaxResult.error("图片上传失败！");
    }

    @PostMapping("/account/uploadImage")
    @ResponseBody
    public Map<String, String> uploadaaa(MultipartFile file) throws IllegalStateException, IOException{
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式  HH:mm:ss
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        String path = "C:/var/uploaded_files/"+date+"/";
        String originalFilename = file.getOriginalFilename();
        String extendName = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = extendName;
        File dir = new File(path, fileName);
        File filepath = new File(path);
        if(!filepath.exists()){
            filepath.mkdirs();
        }
        file.transferTo(dir);
        Map<String, String> map = new HashMap<>();
        map.put("filePath", path);
        map.put("fileName", fileName);
        return map;

    }
    @ResponseBody
    @PostMapping("/downLoad")
    public void downloadFile(HttpServletRequest req, HttpServletResponse response) throws Exception {
    Content content = contentService.selectContentById(new Long(179));
    content.setUrl2(Integer.parseInt(content.getUrl2())+1+"");
    contentService.updateContent(content);
    String url1 = req.getParameter("url").toString();
    URL url = new URL(url1);
    URLConnection conn = url.openConnection();
    InputStream inputStream = conn.getInputStream();
    response.reset();
    response.setContentType("application/pdf");
    //纯下载方式 文件名应该编码成UTF-8
    response.setHeader("Content-Disposition",
            "attachment; filename=" + URLEncoder.encode(req.getParameter("name").toString()+".pdf", "UTF-8"));
    byte[] buffer = new byte[1024];
    int len;
    OutputStream outputStream = response.getOutputStream();
    while ((len = inputStream.read(buffer)) > 0) {
        outputStream.write(buffer, 0, len);
    }
    inputStream.close();


    }


}