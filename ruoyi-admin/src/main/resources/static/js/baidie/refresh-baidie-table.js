function initBaidieTable(elementId, columns) {
    const options = {
        id: elementId,
        method: 'get',
        cache: false,
        striped: true,
        pagination: true,
        pageSize: 10,
        pageNumber: 1,
        pageList: [10, 20, 50, 100, 200, 500],
        sidePagination: 'client',
        search: true,
        showColumns: true,
        showRefresh: false,
        showExport: true,
        exportTypes: ['excel', 'txt', 'xml'],
        columns: columns
    };
    $.table.init(options);
}

function refreshBaidieTable(elementId, data) {
    $('#' + elementId).bootstrapTable('load', data);
}

function createBaidieTable(elementId, columns, data) {
    initBaidieTable(elementId, columns);
    $('#' + elementId).bootstrapTable('load', data);
}