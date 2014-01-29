/*
 Copyright (C) 2014 VoltDB Inc.
*/

var con;
var intervalId;

function formatDecimal2(n) {
    return (Math.round(parseFloat(n) * 100) / 100).toFixed(2);
}

function DrawTable(response, tableName, selectedRow) {
    try {
        var tables = response.results;
        var t0 = tables[0];
        var colcount = t0.schema.length;
        
        // the first time, initialize the table head
        if ($(tableName+' thead tr').length == 0) {
            var theadhtml = '<tr>';
            for (var i=0; i<colcount; i++) {
                theadhtml += '<th>' + t0.schema[i].name + '</th>';
            }
            $(tableName).append('<thead></thead>');
            $(tableName).append('<tbody></tbody>');
            $(tableName).children('thead').html(theadhtml);
        }
        
        var tbodyhtml;
        for(var r=0;r<t0.data.length;r++){ // for each row
            if (r==selectedRow) {
                tbodyhtml += '<tr class="success">';
            } else {
                tbodyhtml += '<tr>';
            }
            for (var c=0;c<colcount;c++) { // for each column
                var f = t0.data[r][c];

                // if type is DECIMAL
                if (t0.schema[c].type == 22) {
                    f = formatDecimal2(f);
                }
                tbodyhtml += '<td>' + f + '</td>';
            }
            tbodyhtml += '</tr>';
        }
        $(tableName).children('tbody').html(tbodyhtml);

    } catch(x) {}
}

function SetRefreshInterval(interval) {
    if (intervalId != null) {
        clearInterval(intervalId);
        intervalId = null;
    }
    if(interval > 0)
        intervalId = setInterval(RefreshData, interval*1000);
}

$(document).ready(function(){
    con = VoltDB.AddConnection('localhost', 8080, false, null, null, false, (function(connection, success){}));
    SetRefreshInterval(1);

    // $('#table_ad_sum > tbody > tr').click(function() {
    //     // row was clicked
    //     console.log("you clicked a row!");
    // });

});

// Refresh drop-down actions
$('#refresh-1').click(function(e) {
    e.preventDefault();// prevent the default anchor functionality
    SetRefreshInterval(1);
});
$('#refresh-5').click(function(e) {
    e.preventDefault();// prevent the default anchor functionality
    SetRefreshInterval(5);
});
$('#refresh-10').click(function(e) {
    e.preventDefault();// prevent the default anchor functionality
    SetRefreshInterval(10);
});
$('#refresh-pause').click(function(e) {
    e.preventDefault();// prevent the default anchor functionality
    SetRefreshInterval(-1);
});

