package com.ruoyi.web.controller.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BaidieOp;
import com.ruoyi.system.service.IBaidieOpService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

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
//    private static final String url1="http://121.41.20.3:7098/StartSimulator/CheckToken";//百碟
//    private static final String url2="http://121.41.20.3:7098/StartSimulator/GetTrainingDTO?trainingId=";
//    private static final String url1="http://172.18.227.201:9065/StartSimulator/CheckToken";//厦门学院
//    private static final String url2="http://172.18.227.201:9065/StartSimulator/GetTrainingDTO?trainingId=";
//    private static final String url1="http://192.168.1.58:9065/StartSimulator/CheckToken";//大连学院
//    private static final String url2="http://192.168.1.58:9065/StartSimulator/GetTrainingDTO?trainingId=";
//    private static final String url1="http://192.168.120.100:9065/StartSimulator/CheckToken";//青海
//    private static final String url2="http://192.168.120.100:9065/StartSimulator/GetTrainingDTO?trainingId=";
//    private static final String url1="http://10.18.15.188:9065/StartSimulator/CheckToken";//青海
//    private static final String url2="http://10.18.15.188:9065/StartSimulator/GetTrainingDTO?trainingId=";
    private static final String url1="http://10.8.48.200:9065/StartSimulator/CheckToken";//周口
    private static final String url2="http://10.8.48.200:9065/StartSimulator/GetTrainingDTO?trainingId=";
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
        final String token = request.getParameter("token");
        final String trainingId = request.getParameter("trainingId");

        final String baidieUserId = getBaidieUserIdFromToken(token);
        final String baidieDrillId = getBaidieDrillId(token, baidieUserId, trainingId);

        if (isNull(baidieUserId) || baidieUserId.isEmpty()) {
            return new TableDataInfo(List.of(), 0);
        }

        if (isNull(baidieDrillId) || baidieDrillId.isEmpty()) {
            return new TableDataInfo(List.of(), 0);
        }

        // Now do the db lookup.
        BaidieOp baidieOp = new BaidieOp();
        baidieOp.setDrillID(baidieDrillId);
        baidieOp.setUserId(baidieUserId);
        final List<BaidieOp> list = baidieOpService.selectBaidieOpList(baidieOp);
        return getDataTable(list);
    }

    @PostMapping("/list2")
    @ResponseBody
    public TableDataInfo list2(HttpServletRequest request)
    {
        String token = request.getParameter("token");
        if(token!=null){
            logger.info("token获取成功！");
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
            logger.info(result);
        }else {
            logger.info("获取失败");
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
            logger.info("token获取成功！");
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
            logger.info(result);
        }else {
            logger.info("获取失败");
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
        final String token = request.getParameter("token");
        final String trainingId = request.getParameter("trainingId");
        final String opId = request.getParameter("opId");
        final String opValue = request.getParameter("opValue");

        final String baidieUserId = getBaidieUserIdFromToken(token);
        final String baidieDrillId = getBaidieDrillId(token, baidieUserId, trainingId);

        if (isNull(baidieUserId) || baidieUserId.isEmpty()) {
            return AjaxResult.error("user id not found");
        }

        if (isNull(baidieDrillId) || baidieDrillId.isEmpty()) {
            return AjaxResult.error("drill id not found");
        }

        // Now do the db lookup.
        BaidieOp baidieOp = new BaidieOp();
        baidieOp.setDrillID(baidieDrillId);
        baidieOp.setUserId(baidieUserId);
        baidieOp.setOpId(opId);
        final List<BaidieOp> list = baidieOpService.selectBaidieOpList(baidieOp);
        for (BaidieOp op : list) {
            baidieOpService.deleteBaidieOpById(op.getId());
        }
        baidieOp.setOpValue(opValue);
        baidieOpService.insertBaidieOp(baidieOp);

        return AjaxResult.success();
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

    private String getBaidieUserIdFromToken(String token) {
        if (token == null || token.isEmpty()) {
            logger.info("No token, skipping baidie user id lookup.");
            return null;
        }

        final String result = getResponseFromBaidie(token, url1);
        final JSONObject jsonObject= JSON.parseObject(result);
        return jsonObject.getString("Id");
    }

    private String getBaidieDrillId(String token, String userId, String trainingId) {
        if (isNull(token) || token.isEmpty()) {
            logger.info("No token, skipping baidie drill id lookup.");
            return "";
        }

        if (isNull(userId) || userId.isEmpty()) {
            logger.info("No userId, skipping baidie drill id lookup.");
            return "";
        }
        if (isNull(trainingId) || trainingId.isEmpty()) {
            logger.info("No trainingId, skipping baidie drill id lookup.");
            return "";
        }

        final String result = getResponseFromBaidie(
                token, url2+ trainingId +"&userId="+ userId);

        final JSONObject jsonObject = JSON.parseObject(result);
        return jsonObject.getString("DrillID");
    }

    private String getResponseFromBaidie(String token, String urlString) {
        String result = null;// 返回结果字符串

        HttpURLConnection connection = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;

        try {
            // 创建远程url连接对象
            URL url = new URL(urlString);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestProperty(HttpHeaders.AUTHORIZATION,"Bearer "+ token);
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：10秒
            connection.setReadTimeout(10000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                // 封装输入流is，并指定字符集
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp;
                while ((temp = bufferedReader.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            logger.error("MalformedURLException caught: " + e.getMessage());
        } catch (IOException e) {
            logger.error("IOException caught: " + e.getMessage());
        } finally {
            // 关闭资源
            if (nonNull(bufferedReader)) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    logger.error("IOException caught while closing buffered reader: " + e.getMessage());
                }
            }
            if (nonNull(inputStream)) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("IOException caught while closing input stream: " + e.getMessage());
                }
            }
            if (nonNull(connection)) {
                connection.disconnect();// 关闭远程连接
            }
        }

        if (isNull(result)) {
            logger.error("百蝶远程调用失败");
            return Strings.EMPTY;
        }
        logger.info("百蝶远程调用成功：" + result);
        return result;
    }
}
