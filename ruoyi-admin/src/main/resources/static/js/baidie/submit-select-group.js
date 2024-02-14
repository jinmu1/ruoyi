// 第一组下拉菜单的id和对应的正确选项值。
const ABC_SELECT_GROUP_ONE_ANSWERS = new Map([
    ["s1", "1"],
    ["s2", "2"],
    ["s3", "4"],
    ["s4", "4"],
    ["s5", "2"],
    ["s6", "1"]
]);

const ABC_SELECT_GROUP_TWO_ANSWERS = new Map([
    ["s11", "4"],
    ["s12", "3"],
    ["s13", "3"],
    ["s14", "2"],
    ["s15", "4"],
    ["s16", "3"]
]);

const ABC_SELECT_GROUP_THREE_ANSWERS = new Map([
    ["s21", "3"],
    ["s22", "2"],
    ["s23", "1"],
    ["s24", "2"],
    ["s25", "3"],
    ["s26", "3"]
]);

const ABC_GROUP_ONE_CHART_ANSWERS = new Map([
    ["x", "物料累计品目数百分比"],
    ["y", "平均资金占用额累计百分比"]
]);

const ABC_GROUP_TWO_CHART_ANSWERS = new Map([
    ["x1", "物料累计品目数百分比"],
    ["y1", "累计出库频次百分比"]
])

const ABC_GROUP_THREE_CHART_ANSWERS = new Map([
    ["x2", "物料累计品目数百分比"],
    ["y2", "累计出库量百分比"]
])
/**
 * 此类用来计算学生在页面上操作的分数。
 * numSubmit - 提交次数
 * numDropDownCorrectOp - 正确选择的次数。
 *
 * 最后的得分为 numDropDownCorrectOp/numSubmit
 */
class BaidieScore {
    constructor() {
        this.numSubmit = 0;
        this.numDropDownCorrectOp = 0;
    }

    incNumSubmit() {
        this.numSubmit++;
    }

    incNumDropDownCorrectOpBy(count) {
        this.numDropDownCorrectOp += count;
    }

    getScore() {
        return this.numDropDownCorrectOp / this.numSubmit;
    }
}

/**
 * 通用函数
 * 用于检查一组下拉菜单的选项值，如果跟答案一致就调用传进来的callback函数
 * @param selectGroupAnswerMap 传进来的<select> element id: 对应的答案。
 * @param baidieScoreKeeper 计算分数的类。
 * @param refreshCallback  如果所有选择都符合对应的答案，调用这个函数。
 *
 */
function checkSelectionResultAndRefresh(
    selectGroupAnswerMap,
    baidieScoreKeeper,
    refreshCallback) {
    // 首先统计选择正确的菜单数目。
    var correctCount = 0;
    for (const elementId of selectGroupAnswerMap.keys()) {
        if (getSelectedValue(elementId) === selectGroupAnswerMap.get(elementId)) {
            correctCount++;
        } else {
            // 没有选对，高亮为红色, 3秒后重新变白
            var currSelect = document.getElementById(elementId);
            currSelect.style.backgroundColor = "red";
            setTimeout(() => {
                document.getElementById(elementId).style.backgroundColor = "white";
            }, 3000);
        }
    }

    // 如果全对，调用callback函数
    if (correctCount === selectGroupAnswerMap.size) {
        refreshCallback();
        // 调用百蝶的端口记录用户行为
        selectGroupAnswerMap.forEach((value, key) => {
            updateBaidieOps(key, value);
        })
    }

    // Update score.
    baidieScoreKeeper.incNumSubmit();
    baidieScoreKeeper.incNumDropDownCorrectOpBy(correctCount);
}

function getSelectedValue(elementId) {
    return document.getElementById(elementId).value;
}

/**
 * 更新百蝶用户行为数据
 * @param opId
 * @param opValue
 */
function updateBaidieOps(opId, opValue) {
    $.post(ctx +
        "system/op/add?trainingId="
        + $('#trainingId').text()
        + "&token="+$('#token').text()
        + "&opId=" + opId
        + "&opValue=" + opValue,
        function (result) {});
}
