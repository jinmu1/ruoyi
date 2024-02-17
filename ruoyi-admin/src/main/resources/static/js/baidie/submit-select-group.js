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
    if (baidieScoreKeeper) {
        baidieScoreKeeper.incNumSubmit();
        baidieScoreKeeper.incNumDropDownCorrectOpBy(correctCount);
    }
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
