// 第一组下拉菜单的id和对应的正确选项值。
const ABC_SELECT_GROUP_ONE_ANSWERS = new Map([
    ["s1", 1],
    ["s2", 2],
    ["s3", 4],
    ["s4", 4],
    ["s5", 2],
    ["s6", 1]
]);

/**
 * 通用函数
 * 用于检查一组下拉菜单的选项值，如果跟答案一致就调用传进来的callback函数
 * @param selectGroupAnswerMap 传进来的<select> element id: 对应的答案。
 * @param numSubmitCounter 总共提交按钮的次数
 * @param numDropdownCorrectOp  总共下拉菜单正确选择的次数。
 * @param refreshCallback  如果所有选择都符合对应的答案，调用这个函数。
 *
 * 返回更新后的 【提交次数， 选择正确次数】
 */
function submitSelectGroupAndRefresh(
    selectGroupAnswerMap,
    numSubmitCounter,
    numDropdownCorrectOp,
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
            addResutl(key, value);
        })
    }

    // 返回新的总提交数 和 正确选择总数。
    return [numSubmitCounter + 1, numDropdownCorrectOp + correctCount];
}

function getSelectedValue(elementId) {
    return parseInt(document.getElementById(elementId).value);
}

/**
 * 更新百蝶用户行为数据
 * @param opId
 * @param opValue
 */
function addResutl(opId, opValue) {
    $.post(ctx +
        "system/op/add?trainingId="
        + $('#trainingId').text()
        + "&token="+$('#token').text()
        + "&opId=" + opId
        + "&opValue=" + opValue,
        function (result) {});
}
