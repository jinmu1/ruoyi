package com.ruoyi.baidie;


import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.data.abc.MaterialBasicInfo;
import com.ruoyi.data.abc.MaterialValueInfo;
import com.ruoyi.data.common.ObjectMap;
import com.ruoyi.data.eiq.*;
import com.ruoyi.data.pcb.MaterialOutboundTypeInfo;
import com.ruoyi.data.pcb.MaterialPackagingInfo;
import com.ruoyi.data.pcb.OutboundBasicInfo;
import com.ruoyi.web.controller.utils.BaidieUtils;

import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/****
 * PCB 处理类：将PCB分析的各项统计指标进行转换
 */
public class PCBClassifier {

    /**
     * 统计出库物料的基本储运单位类型（P表示托盘单位、C表示件单位、B表示单品）
     *
     * @param outboundBasicInfos 基本的出库数据
     * @return 返回一个带有每个物料的储运单位的计算数据
     */
    public static List<MaterialPackagingInfo> transformPackaging(
            List<OutboundBasicInfo> outboundBasicInfos) {
        // step1：首先计算总销售金额
        final List<MaterialPackagingInfo> entryWithTotalValue =
                outboundBasicInfos
                        .stream()
                        .map(PCBClassifier::calculateTotalValue)
                        .collect(Collectors.toList());
        return entryWithTotalValue;
    }

    private static MaterialPackagingInfo calculateTotalValue(OutboundBasicInfo outboundBasicInfo) {
        MaterialPackagingInfo info = new MaterialPackagingInfo();
        info.setMaterialCode(outboundBasicInfo.getMaterialCode());
        info.setOutQuantityN(outboundBasicInfo.getOutQuantity());
        info.setCorrespondenceA(
                BaidieUtils.extractNumbers(
                        outboundBasicInfo.getOutUnit()
                ).get(0)
        );
        info.setCorrespondenceB(
                BaidieUtils.extractNumbers(
                        outboundBasicInfo.getOutPalletUnit()
                ).get(0)
        );
        info.setProductBoxRelation(info.getCorrespondenceA() * info.getCorrespondenceB());
        DecimalFormat df = new DecimalFormat("#.##");
        info.setOutDivision(
                Double.parseDouble(
                        df.format(
                                info.getOutQuantityN() / info.getProductBoxRelation()
                        )
                )
        );
        info.setDemandPalletQuantity(Math.floor(info.getOutDivision() * 100) / 100);
        info.setRemainder(info.getOutQuantityN() - info.getDemandPalletQuantity() * info.getProductBoxRelation());
        info.setDivisionByA(Double.parseDouble(
                df.format(
                        (info.getOutQuantityN() - info.getRemainder()) / info.getCorrespondenceA()
                ))
        );
        info.setDemandBoxQuantity(Math.floor(info.getDivisionByA() * 100) / 100);
        info.setDemandProductQuantity(
                info.getOutQuantityN() -
                        info.getDemandPalletQuantity() *
                                info.getProductBoxRelation()-
                        info.getDivisionByA()*info.getDemandBoxQuantity()
        );
        return info;
    }


}
