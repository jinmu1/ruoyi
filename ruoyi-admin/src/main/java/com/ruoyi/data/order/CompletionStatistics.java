package com.ruoyi.data.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CompletionStatistics {
    @NotNull
    @JsonProperty("订单完成日期")
    private String completionDate;
    @NotNull
    @JsonProperty("周期内每日完成订单数量")
    private Integer dailyCompletionCount;
    @NotNull
    @JsonProperty("平均数")
    private Integer averageCount;
    @NotNull
    @JsonProperty("中位数")
    private Integer medianCount;
}
