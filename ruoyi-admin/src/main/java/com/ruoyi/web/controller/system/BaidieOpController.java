package com.ruoyi.web.controller.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.BaidieOp;
import com.ruoyi.system.service.IBaidieOpService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * 操作Controller
 * 
 * @author ruoyi
 * @date 2023-03-18
 */
@Controller
@RequestMapping("/system/op")
public class BaidieOpController extends BaseController
{
    private String prefix = "system/op";
    private static final String url1="http://121.41.20.3:7098/StartSimulator/CheckToken";//百碟
    private static final String url2="http://121.41.20.3:7098/StartSimulator/GetTrainingDTO?trainingId=";
//    private static final String url1="http://172.18.227.201:9065/StartSimulator/CheckToken";//厦门学院
//    private static final String url2="http://172.18.227.201:9065/StartSimulator/GetTrainingDTO?trainingId=";
//    private static final String url1="http://192.168.1.58:9065/StartSimulator/CheckToken";//大连学院
//    private static final String url2="http://192.168.1.58:9065/StartSimulator/GetTrainingDTO?trainingId=";
//    private static final String url1="http://192.168.120.100:9065/StartSimulator/CheckToken";//青海
//    private static final String url2="http://192.168.120.100:9065/StartSimulator/GetTrainingDTO?trainingId=";
//    private static final String url1="http://10.18.15.188:9065/StartSimulator/CheckToken";//青海
//    private static final String url2="http://10.18.15.188:9065/StartSimulator/GetTrainingDTO?trainingId=";
//    private static final String url1="http://10.8.48.200:9065/StartSimulator/CheckToken";//周口
//    private static final String url2="http://10.8.48.200:9065/StartSimulator/GetTrainingDTO?trainingId=";
    @Autowired
    private IBaidieOpService baidieOpService;

    @GetMapping()
    public String op()
    {
        return prefix + "/op";
    }

    /**
     * 查询操作列表
     */

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BaidieOp baidieOp)
    {
        startPage();
        List<BaidieOp> list = baidieOpService.selectBaidieOpList(baidieOp);
        return getDataTable(list);
    }
    @PostMapping("/list1")
    @ResponseBody
    public TableDataInfo list1(HttpServletRequest request)
    {
        String token = request.getParameter("token");
        if(token!=null){
            System.out.println("token获取成功！");
        }
        String trainingId = request.getParameter("trainingId");
        if(trainingId!=null){
            System.out.println("trainingId获取成功！");
        }
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;

        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(url1);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty(HttpHeaders.AUTHORIZATION,"Bearer "+token);
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            connection.disconnect();// 关闭远程连接
        }
        if(result!=null) {
            System.out.println(result);
        }else {
            System.out.println("获取失败");
        }
        JSONObject jsonObject= JSON.parseObject(result);
        String result1 = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(url2+trainingId+"&userId="+ jsonObject.getString("Id"));
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestProperty(HttpHeaders.AUTHORIZATION,"Bearer "+token);
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result1 = sbf.toString();


            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            connection.disconnect();// 关闭远程连接
        }
        if(result!=null) {
            System.out.println(result1);
        }else {
            System.out.println("获取失败");
        }
        JSONObject jsonObject1=JSON.parseObject(result1);
        BaidieOp baidieOp = new BaidieOp();
        baidieOp.setDrillID(jsonObject1.getString("DrillID"));
        baidieOp.setUserId(jsonObject.getString("Id"));
        List<BaidieOp> list = baidieOpService.selectBaidieOpList(baidieOp);
        return getDataTable(list);
    }
    @PostMapping("/list2")
    @ResponseBody
    public TableDataInfo list2(HttpServletRequest request)
    {
        String token = request.getParameter("token");
        if(token!=null){
            System.out.println("token获取成功！");
        }

        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;

        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(url1);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty(HttpHeaders.AUTHORIZATION,"Bearer "+token);
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            connection.disconnect();// 关闭远程连接
        }
        if(result!=null) {
            System.out.println(result);
        }else {
            System.out.println("获取失败");
        }
        JSONObject jsonObject= JSON.parseObject(result);
        BaidieOp baidieOp = new BaidieOp();
        baidieOp.setUserId(jsonObject.getString("Id"));
//        baidieOp.setDrillID(jsonObject.getString("DrillID"));
        List<BaidieOp> list = baidieOpService.selectBaidieOpList(baidieOp);
        return getDataTable(list);
    }

    @PostMapping("/list3")
    @ResponseBody
    public String list3(HttpServletRequest request)
    {
        String token = request.getParameter("token");
        if(token!=null){
            System.out.println("token获取成功！");
        }
        String value = request.getParameter("value");

        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;

        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(url1);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty(HttpHeaders.AUTHORIZATION,"Bearer "+token);
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            connection.disconnect();// 关闭远程连接
        }
        if(result!=null) {
            System.out.println(result);
        }else {
            System.out.println("获取失败");
        }
        JSONObject jsonObject= JSON.parseObject(result);
        BaidieOp baidieOp = new BaidieOp();
        baidieOp.setUserId(jsonObject.getString("Id"));
        baidieOp.setOpId(value);
        List<BaidieOp> list = baidieOpService.selectBaidieOpList(baidieOp);
        return list.get(0).getOpValue();
    }
    /**
     * 导出操作列表
     */

    @Log(title = "操作", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BaidieOp baidieOp)
    {
        List<BaidieOp> list = baidieOpService.selectBaidieOpList(baidieOp);
        ExcelUtil<BaidieOp> util = new ExcelUtil<BaidieOp>(BaidieOp.class);
        return util.exportExcel(list, "操作数据");
    }

    /**
     * 新增操作
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存操作
     */

    @Log(title = "操作", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HttpServletRequest request)
    {
        String trainingId = request.getParameter("trainingId");
        if(trainingId!=null){
            System.out.println("trainingId获取成功！");
        }
        String token = request.getParameter("token");
        if(token!=null){
            System.out.println("token获取成功！");
        }

        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;

        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(url1);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty(HttpHeaders.AUTHORIZATION,token);
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            connection.disconnect();// 关闭远程连接
        }
        if(result!=null) {
            System.out.println(result);
        }else {
            System.out.println("获取失败");
        }
        JSONObject jsonObject= JSON.parseObject(result);
        String result1 = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(url2+trainingId+"&userId="+ jsonObject.getString("Id"));
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestProperty(HttpHeaders.AUTHORIZATION,token);
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result1 = sbf.toString();


            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            connection.disconnect();// 关闭远程连接
        }
        if(result!=null) {
            System.out.println(result1);
        }else {
            System.out.println("获取失败");
        }
        String opId = request.getParameter("opId");
        if(opId!=null){
            System.out.println("opId获取成功！");
        }
        String opValue = request.getParameter("opValue");
        if(opValue!=null){
            System.out.println("opValue获取成功！");
        }
        JSONObject jsonObject1=JSON.parseObject(result1);
        BaidieOp baidieOp = new BaidieOp();
        baidieOp.setDrillID(jsonObject1.getString("DrillID"));
        baidieOp.setUserId(jsonObject.getString("Id"));
        baidieOp.setOpId(opId);
        List<BaidieOp> list = baidieOpService.selectBaidieOpList(baidieOp);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                baidieOpService.deleteBaidieOpById(list.get(i).getId());
            }
        }
        baidieOp.setOpValue(opValue);
        baidieOpService.insertBaidieOp(baidieOp);


        return toAjax(0);
    }

    /**
     * 修改操作
     */

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        BaidieOp baidieOp = baidieOpService.selectBaidieOpById(id);
        mmap.put("baidieOp", baidieOp);
        return prefix + "/edit";
    }

    /**
     * 修改保存操作
     */

    @Log(title = "操作", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BaidieOp baidieOp)
    {
        return toAjax(baidieOpService.updateBaidieOp(baidieOp));
    }

    /**
     * 删除操作
     */

    @Log(title = "操作", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(baidieOpService.deleteBaidieOpByIds(ids));
    }
}
