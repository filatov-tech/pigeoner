$(document).ready( function () {
    dataTableApi = $('#pigeons-table').DataTable({
        ajax: {
            url: 'api/v1/pigeons',
            dataSrc: ''
        },
        columns: [
            { data: 'ringNumber' },
            { data: 'color' },
            { data: 'sex' },
            { data: 'birthday' },
            { data: 'age' },
            { data: 'mate' },
            { data: 'status' }
        ]
    });
    $.ajax({
        type: "GET",
        url: "api/v1/sections",
    }).done(addDovecoteNames);
} );

function updateTable() {
    $.ajax({
        type: "GET",
        url: "api/v1/pigeons/filter",
        data: $('#pigeon-filter').serialize()
    }).done(updateTableWithData)
}

function updateTableWithData(data) {
    dataTableApi.clear().rows.add(data).draw();
}

function addDovecoteNames(data) {
    $('#location').append
}