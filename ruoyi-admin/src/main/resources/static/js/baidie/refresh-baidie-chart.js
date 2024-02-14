function refreshBaidieChart(chartElementId,
                            data,
                            xAttrName,
                            yAttrName) {
    var chart = echarts.init(document.getElementById(chartElementId), 'macarons');

    const xAxisArray = data.map(entry => entry[xAttrName]);
    const yAxisArray = data.map(entry => parseFloat(entry[yAttrName].replace("%", "")));

    var option = {
        title: {
            show: false
        },
        tooltip: {
            trigger: 'axis',
        },
        toolbox: {
            show: true,
            feature : {
                mark : {show: true},
                dataView : {show: true, readOnly: false},
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        grid: {
            top: 30,
            left: 20,
            right: 20,
            bottom: 20,
            containLabel: true
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: xAxisArray,
            axisLabel: {
                interval: 'auto',
                fontSize: 12,
                formatter: '{value}%'
            },
            axisLine: {
                show: true,
                lineStyle:{
                    color:'#BDB9B9'
                }
            },
            axisTick: {
                show: true
            },
        },
        yAxis: {
            type: 'value',
            axisLine: {
                show: true,
                lineStyle:{
                    color:'#BDB9B9'
                }
            },
            axisTick: {
                show: true
            },
            axisLabel: {
                formatter: '{value}%',
                textStyle:{
                    color:"#595959"
                }
            },
            splitLine:{
                show:true
            },
        },
        series: [{
            name: yAttrName,
            type: 'line',
            showSymbol: false,
            hoverAnimation: false,
            data: yAxisArray,
            axisLabel: {
                formatter: '{value}%',
                textStyle:{
                    color:"#595959"
                }
            },
            itemStyle: {
                emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                },
                normal: {
                    lineStyle:{
                        width:1
                    },
                    color:'#51D0E5',
                }
            }
        }
        ]
    };
    chart.setOption(option);
}