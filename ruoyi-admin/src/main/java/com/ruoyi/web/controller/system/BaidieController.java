package com.ruoyi.web.controller.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.shiro.service.SysRegisterService;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.service.IBaidieScoreService;
import com.ruoyi.system.service.IGlcPublicService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.impl.BaidieScoreServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 出库单Controller
 * 
 * @author ruoyi
 * @date 2021-01-10
 */
@Controller
public class BaidieController extends BaseController
{
//    private static final String url1="http://121.41.20.3:7098/StartSimulator/CheckToken";//百碟
//    private static final String url2="http://121.41.20.3:7098/StartSimulator/GetTrainingDTO?trainingId=";
//    private static final String url1="http://172.18.227.201:9065/StartSimulator/CheckToken";//厦门学院
//    private static final String url2="http://172.18.227.201:9065/StartSimulator/GetTrainingDTO?trainingId=";
//    private static final String url1="http://192.168.1.58:9065/StartSimulator/CheckToken";//青海
//    private static final String url2="http://192.168.1.58:9065/StartSimulator/GetTrainingDTO?trainingId=";
/*    private static final String url1="http://192.168.120.100:9065/StartSimulator/CheckToken";//青海
      private static final String url2="http://192.168.120.100:9065/StartSimulator/GetTrainingDTO?trainingId=";*/
//    private static final String url1="http://10.18.15.188:9065/StartSimulator/CheckToken";//青海
//    private static final String url2="http://10.18.15.188:9065/StartSimulator/GetTrainingDTO?trainingId=";
    private static final String url1="http://10.8.48.200:9065/StartSimulator/CheckToken";//周口
    private static final String url2="http://10.8.48.200:9065/StartSimulator/GetTrainingDTO?trainingId=";
    @Autowired
    private IGlcPublicService glcPublicService;

    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IBaidieScoreService baidieScoreService;

    @GetMapping("/end")
    public String ass1(HttpServletRequest request)
    {
        String score = request.getParameter("score");
        if(score!=null){
            System.out.println("score获取成功！");
        }
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
        BaidieScore baidieScore = new BaidieScore();
        baidieScore.setUserCode(jsonObject.getString("Id"));
        baidieScore.setHeadId(jsonObject1.getString("DrillID"));
        baidieScore.setName(jsonObject.getString("Heading"));
        baidieScore.setUserId(jsonObject1.getString("IssueID"));

//        baidieScore.setRemarks( JSONArray.parseArray(jsonObject.getString("Grades")).getJSONObject(0).getString("Heading"));

        if (baidieScoreService.selectBaidieScoreList(baidieScore).size()==0){

            if (Double.parseDouble(score)>1) {
                baidieScore.setScore("100");
                baidieScoreService.insertBaidieScore(baidieScore);
            }else {
                baidieScore.setScore(String.valueOf(100 * Double.parseDouble(score)));
                baidieScoreService.insertBaidieScore(baidieScore);
            }

        }else {
            if(score!=null&&score!=""){
                if (Double.parseDouble(score)>1) {
                    baidieScore.setScore("100");
                }else {
                    baidieScore.setScore(String.valueOf(100 * Double.parseDouble(score)));
                }
            }else {
                baidieScore.setScore("100");
            }
            baidieScoreService.updateBaidieScore(baidieScore);
        }

        return   "end";
    }
    @GetMapping("/end1")
    public String assd(HttpServletRequest request)
    {

        return   "end";
    }
    @GetMapping("/33")
    public String net(HttpServletRequest request){
        String token = request.getParameter("Token");
        if(token!=null){
            System.out.println("token获取成功！");
        }
        String PatternID = request.getParameter("PatternID");
        if(PatternID!=null){
            System.out.println("PatternID获取成功！");
        }
        String PatternCode = request.getParameter("PatternCode");
        if(PatternCode!=null){
            System.out.println("PatternCode获取成功！");
        }
        String DrillID = request.getParameter("DrillID");
        if(DrillID!=null){
            System.out.println("DrillID获取成功！");
        }
        String PortIsIM = request.getParameter("PortIsIM");
        if(PortIsIM!=null){
            System.out.println("DrillID获取成功！");
        }
        GlcPublic glcPublic = new GlcPublic();
        glcPublic.setDrillID(DrillID);
        glcPublic.setPatternID(PatternID);
        glcPublic.setPatternCode(PatternCode);
        glcPublic.setToken(token);
        glcPublic.setPortIsIM(PortIsIM);
        GlcPublic glcPublic2 = new GlcPublic();
        glcPublic2.setDrillID(DrillID);
        glcPublic2.setPatternID(PatternID);
        glcPublic2.setPatternCode(PatternCode);
        glcPublic2.setPortIsIM(PortIsIM);
        List<GlcPublic> glcPublic1 = glcPublicService.selectGlcPublicList(glcPublic2);
        if (glcPublic1.size()==0){
            glcPublicService.insertGlcPublic(glcPublic);
        }
        return glcPublic1.get(0).getArr();
    }

    @GetMapping("/mainSim")
    public String mainSim(HttpServletRequest request, Model model){
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
        GlcPublic glcPublic = new GlcPublic();
        glcPublic.setDrillID(jsonObject1.getString("DrillID"));
        GlcPublic glcPublic1 = glcPublicService.selectGlcPublicList(glcPublic).get(0);

        model.addAttribute("trainingId", trainingId);
        model.addAttribute("token",token);
        return glcPublic1.getArr();
    }

    @GetMapping("/LogisticsAnalysis//ScoreRank/Index")
    public String LoginToken(HttpServletRequest request, Model model){
        String IssueId = request.getParameter("IssueId");
        if(IssueId!=null){
            System.out.println(IssueId);
        }
        String UserCode = request.getParameter("UserCode");
        if(UserCode!=null){
            System.out.println("type获取成功！");
        }

        model.addAttribute("issueId",IssueId);
        model.addAttribute("userCode", UserCode);
        return "system/score/score";
    }
    @PostMapping("/LogisticsAnalysis/ScoreSgGp/GetScore")
    @ResponseBody
    public SupplyScoreResultDataDto GetScore(HttpServletRequest request){
        String IssueId = request.getParameter("IssueId");
        if(IssueId!=null){
            System.out.println("IssueId获取成功！");
        }
        BaidieScore score = new BaidieScore();
        score.setUserId(IssueId);
        List<BaidieScore> baidieScores = baidieScoreService.selectBaidieScoreList(score);

        Map<String, List<BaidieScore>> groupMap = baidieScores.stream()
                .collect(Collectors.groupingBy(BaidieScore::getUserCode));
        SupplyScoreResultDataDto supplyScoreResultDataDto1 = new SupplyScoreResultDataDto();
        List<SumScoreDTO> sumScoreDTOS = new ArrayList<>();
        for(String userid :groupMap.keySet()){
            List<BaidieScore> list = groupMap.get(userid);
            List<DetailScoreDTO> detailScoreDTOS = new ArrayList<>();
            double sco = 0;
            for(BaidieScore baidieScore:list){
                DetailScoreDTO detailScoreDTO = new DetailScoreDTO();
                detailScoreDTO.setHeadId(baidieScore.getHeadId());
                detailScoreDTO.setScore(baidieScore.getScore());
                detailScoreDTO.setId(baidieScore.getId().intValue());
                detailScoreDTO.setName(baidieScore.getName());
                detailScoreDTOS.add(detailScoreDTO);
                sco = sco + Double.parseDouble(baidieScore.getScore());
            }
            SumScoreDTO sumScoreDTO = new SumScoreDTO();
//            sumScoreDTO.setId((int)(Math.random()*1000));
            sumScoreDTO.setId(null);
            sumScoreDTO.setDetailScore(detailScoreDTOS);
            sumScoreDTO.setUserId(userid);
            sumScoreDTO.setSumScore(String.valueOf(sco));
            sumScoreDTOS.add(sumScoreDTO);
        }
        supplyScoreResultDataDto1.setData(sumScoreDTOS);
        supplyScoreResultDataDto1.setSuccess(true);


        return supplyScoreResultDataDto1;
    }

    @GetMapping("/LogisticsAnalysis/Account/LoginToken")
    public String Monitor(HttpServletRequest request){
        return "system/public/public";
    }


    @GetMapping("/StartSimulator/TryStopProcess")
    public AjaxResult TryStopProcess(HttpServletRequest request){

        return success("成功");
    }
    @GetMapping("/StartSimulator/QueryDrillData")
    public AjaxResult QueryDrillData(HttpServletRequest request){


        return success("成功");
    }
}
