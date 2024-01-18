package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Netsend;
import com.ruoyi.system.mapper.NetsendMapper;
import com.ruoyi.system.service.INetsendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-01-10
 */
@Service
public class NetsendServiceImpl implements INetsendService 
{
    @Autowired
    private NetsendMapper netsendMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param 订单编号 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public Netsend selectNetsendBy订单编号(String 订单编号)
    {
        return netsendMapper.selectNetsendBy订单编号(订单编号);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param netsend 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<Netsend> selectNetsendList(Netsend netsend)
    {
        return netsendMapper.selectNetsendList(netsend);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param netsend 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertNetsend(Netsend netsend)
    {
        return netsendMapper.insertNetsend(netsend);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param netsend 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateNetsend(Netsend netsend)
    {
        return netsendMapper.updateNetsend(netsend);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param 订单编号s 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteNetsendBy订单编号s(String 订单编号s)
    {
        return netsendMapper.deleteNetsendBy订单编号s(Convert.toStrArray(订单编号s));
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param 订单编号 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteNetsendBy订单编号(String 订单编号)
    {
        return netsendMapper.deleteNetsendBy订单编号(订单编号);
    }

    @Override
    public String importExcel(List<Netsend> deliveryList, boolean isUpdateSupport, Long userId) {
        if (StringUtils.isNull(deliveryList) || deliveryList.size() == 0) {

        }

        try {
            exec(deliveryList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "导入成功";

    }
    public  void exec(List<Netsend> list) throws InterruptedException{
        int count = 4000;                   //一个线程处理300条数据
        int listSize = list.size();        //数据集合大小
        int runSize = (listSize/count)+1;  //开启的线程数
        List<Netsend> newlist = null;       //存放每个线程的执行数据
        ExecutorService executor = Executors.newFixedThreadPool(runSize);      //创建一个线程池，数量和开启线程的数量一样
        //创建两个个计数器
        CountDownLatch begin = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(runSize);
        //循环创建线程
        for (int i = 0; i < runSize ; i++){
            //计算每个线程执行的数据
            if((i+1)==runSize){
                int startIndex = (i*count);
                int endIndex = list.size();
                newlist= list.subList(startIndex, endIndex);
            }else{
                int startIndex = (i*count);
                int endIndex = (i+1)*count;
                newlist= list.subList(startIndex, endIndex);
            }
            //线程类
            MyThread mythead = new MyThread(newlist,begin,end);
            //这里执行线程的方式是调用线程池里的executor.execute(mythead)方法。
            executor.execute(mythead);
        }
        begin.countDown();
        end.await();

        //执行完关闭线程池
        executor.shutdown();
    }

    class MyThread implements Runnable {
        private List<Netsend> list;
        private CountDownLatch begin;
        private CountDownLatch end;

        //创建个构造函数初始化 list,和其他用到的参数
        public MyThread(List<Netsend> list, CountDownLatch begin, CountDownLatch end) {
            this.list = list;
            this.begin = begin;
            this.end = end;
        }

        @Override
        public void run() {
            try {
                netsendMapper.insertNetsendList(list);
                begin.await();
            } catch (InterruptedException e) {

                e.printStackTrace();
            } finally {
                //这里要主要了，当一个线程执行完 了计数要减一不然这个线程会被一直挂起
// ，end.countDown()，这个方法就是直接把计数器减一的
                end.countDown();
            }
        }


    }
}
