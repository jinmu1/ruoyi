function refreshBaidieChart(chartElementId,
                            data,
                            xAttrName,
                            yAttrName) {
    var chart = echarts.init(document.getElementById(chartElementId), 'macarons');

    const xAxisArray = data.map(entry => entry[xAttrName]);
    const yAxisArray = data.map(entry => parseFloat(entry[yAttrName].replace("%", "")));

    var option = {
        tooltip: {
            trigger: 'axis',
        },
        toolbox: {
            show: true,
            feature: {
                mark: {show: true},
                dataView: {show: true, readOnly: false},
                magicType: {show: true, type: ['line', 'bar']},
                restore: {show: true},
                saveAsImage: {show: true}
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
                lineStyle: {
                    color: '#BDB9B9'
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
                lineStyle: {
                    color: '#BDB9B9'
                }
            },
            axisTick: {
                show: true
            },
            axisLabel: {
                formatter: '{value}%',
                textStyle: {
                    color: "#595959"
                }
            },
            splitLine: {
                show: true
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
                textStyle: {
                    color: "#595959"
                }
            },
            itemStyle: {
                emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                },
                normal: {
                    lineStyle: {
                        width: 1
                    },
                    color: '#51D0E5',
                }
            }
        }
        ]
    };
    chart.setOption(option);
}

function refreshChartWithMaxMin(chartElementId,
                                data,
                                xAttrName,
                                yAttrName) {
    var chart = echarts.init(document.getElementById(chartElementId), 'macarons');

    const xAxisArray = data.map(entry => entry[xAttrName]);
    const yAxisArray = data.map(entry => parseFloat(entry[yAttrName]));
    const option = {
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            show: true
        },
        dataZoom: {
            show: true,
            realtime: true
        },
        toolbox: {
            show: true,
            feature: {
                mark: {show: true},
                dataView: {show: true, readOnly: false},
                magicType: {show: true, type: ['line', 'bar']},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: xAxisArray
        },
        yAxis: {
            type: 'value',
            axisLabel: {
                formatter: '{value}'
            },
            splitLine: {
                show: false
            }
        },
        series: [
            {
                name: yAttrName,
                type: 'line',
                data: yAxisArray,
                itemStyle: {
                    normal: {
                        color: '#39c7aa'
                    }
                },
                markPoint: {
                    data: [
                        {type: 'max', name: '最大值'},
                        {type: 'min', name: '最小值'}
                    ]
                },
                markLine: {
                    data: [
                        {type: 'average', name: '平均值'}
                    ]
                }

            }
        ]
    };
    chart.setOption(option);
}

function refreshBaidieBarChart(chartElementId,
                               data,
                               xAttrName,
                               yAttrName) {
    var chart = echarts.init(document.getElementById(chartElementId), 'macarons');

    const xAxisArray = data.map(entry => entry[xAttrName]);
    const yAxisArray = data.map(entry => parseFloat(entry[yAttrName]));
    var option = {
        backgroundColor: 'rgba(255, 255, 255, 0)',
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            show: true
        },
        dataZoom: {
            show: true,
            realtime: true
        },
        xAxis: {
            type: "category",
            data: xAxisArray
        },
        yAxis: {
            type: "value",
            axisLabel: {
                formatter: '{value}%'
            },
        },
        series: [
            {
                name: yAttrName,
                type: "bar",
                data: yAxisArray,
                itemStyle: {
                    normal: {
                        color: '#39c7aa'
                    }
                },
            }
        ]
    };
    chart.setOption(option);
}

function refreshBaidieChartWithMid(chartElementId,
                                   data,
                                   xAttrName,
                                   yAttrName,
                                   yAxisMidName,
                                   yAxisAVGName
) {
    var chart = echarts.init(document.getElementById(chartElementId), 'macarons');

    const xAxisArray = data.map(entry => entry[xAttrName]);
    const yAxisArray = data.map(entry => parseFloat(entry[yAttrName]));
    const yAxisMidArray = data.map(entry => parseFloat(entry[yAxisMidName]));
    const yAxisAvgArray = data.map(entry => parseFloat(entry[yAxisAVGName]));
    const option = {
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            show: true
        },
        dataZoom: {
            show: true,
            realtime: true
        },
        toolbox: {
            show: true,
            feature: {
                mark: {show: true},
                dataView: {show: true, readOnly: false},
                magicType: {show: true, type: ['line', 'bar']},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: xAxisArray
        },
        yAxis: {
            type: 'value',
            axisLabel: {
                formatter: '{value}'
            },
            splitLine: {
                show: false
            }
        },
        series: [
            {
                name: yAttrName,
                type: 'line',
                data: yAxisArray,
                itemStyle: {
                    normal: {
                        color: '#39c7aa'
                    }
                },

            }, {
                name: yAxisMidName,
                type: 'line',
                data: yAxisMidArray,

                itemStyle: {
                    normal: {
                        color: '#39c7aa'
                    }
                },


            }, {
                name: yAxisAVGName,
                type: 'line',
                data: yAxisAvgArray,

                itemStyle: {
                    normal: {
                        color: '#39c7aa'
                    }
                },


            }
        ]
    };
    chart.setOption(option);
}

function refreshBaidiePieChart(chartElementId,
                               data,
                               xAttrName,
                               yAttrName) {
    var chart = echarts.init(document.getElementById(chartElementId), 'macarons');

    const xAxisArray = data.map(entry => entry[xAttrName]);
    const yAxisArray = data.map(entry => parseFloat(entry[yAttrName]));
    var option = {
        backgroundColor: 'rgba(255, 255, 255, 0)',
        tooltip: {
            trigger: 'axis'
        },
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        color: ['#8fc31f', '#f35833', '#00ccff', '#ffcc00'],
        legend: {
            orient: 'vertical',
            x: 'right',
            data: xAxisArray,
        },
        series: [
            {
                name: yAttrName,
                type: 'pie',
                radius: '55%',
                center: ['40%', '50%'],
                data: yAxisArray,
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                },
                itemStyle: {
                    normal: {
                        label: {
                            show: true,
//	                            position:'inside',
                            formatter: '{a} : ({d}%)'
                        }
                    },
                    labelLine: {show: true}
                }
            }
        ]
    };
    chart.setOption(option);
}