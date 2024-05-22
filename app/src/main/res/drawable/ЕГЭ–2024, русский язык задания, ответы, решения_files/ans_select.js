function create_selectors(){
    $('[data-select-num]').each(function(i, e){
        var t = $(e);
        var opts=['—'];
        var md = t.closest('.prob_maindiv');
        md.find('[data-option=all]').each(function(ii,ee){opts.push($(ee).text())});
        md.find('[data-option=' + t.attr('data-select-num') + ']').each(function(ii,ee){opts.push($(ee).text())});
        var sel = $('<select class="test_inp" name="answer_' + md.attr('data-num') + '_' + md.attr('data-id') + '_' + t.attr('data-select-num') + '"></select>');
        for (var i=0; i< opts.length; i++){
            sel.append($('<option></option>').val(i).text(opts[i]));
        }
        t.append(sel)
        t.show();
    });
    $('.not_in_test').remove();
}
create_selectors();
