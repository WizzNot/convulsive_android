var Cart = function(count, problems, div_class){
    var t = this;
    this.count = count;
    this.problems = problems;
    var wraper = $('<div class="DeskList"></div>');
    this.icon = $('<div class="DeskCard DeskCard_favourite" style=""></div>');
    this.fill_icon();
    this.icon.appendTo(wraper);
    wraper.appendTo('body');
    this.icon.click(function(){t.show_cart()});
    this.cart = $('<div style="margin:0"></div>');
};

Cart.prototype.fill_icon = function(){
    this.icon.empty();
    this.icon.append('<img class="DeskCard-Img" src="/img/briefcase.png">', ' ' ,'<span class="DeskCard-Caption">Заданий: ' + this.count+ '</span>');
    if (this.count){
        this.icon.fadeIn();
    }
    else {
        this.icon.hide()
    }
}

Cart.prototype.add_problem = function(id){
    var t = this;
    $.post('/cart?a=add&ajax=1', {'id': id}, function(resp){t.parse_resp(resp)}, 'json');
}
Cart.prototype.add_problems = function(ids){
    var t = this;
    $.post('/cart?a=addn&ajax=1', {'id': ids}, function(resp){t.parse_resp(resp)}, 'json');
}

Cart.prototype.show_cart = function(){
    this.cart.dialog({'title': 'Создание варианта', 'modal':true, 'width':550});
    this.fill_cart();
}

Cart.prototype.fill_cart = function(){
    var t = this;
    this.cart.empty().text('Не храните здесь задания постоянно, используйте для этого «Избранное».').append('<br><br>');
    for (var i = 0; i < this.problems.length; i++){
        this.cart.append(
            'Задание № ', '<a href="/problem?id='+this.problems[i]+'" target="_blank">'+this.problems[i]+'</a>',
            ' ',
            $('<img src="/img/cross.png" style="vertical-align:middle">').data('pid', this.problems[i]).click(function(){
                $.post('/cart?a=delete&ajax=1', {'id': $(this).data('pid')}, function(resp){t.parse_resp(resp)}, 'json');
            }),
            '<br>'
        );
    }
    this.cart.append('<br>');
    this.cart.append('<br>');
    var cb = $('<input type="checkbox">').change(function(){
        var ti = $(this);
        ti.parent().find('a').each(function(ind, el){
            $(el).attr('href', $(el).attr('data-href')+'&save=' + (ti.is(':checked')?'1':'0'));
        });
    });
    this.cart.append(cb, ' Сохранить этот список заданий здесь после составления варианта<br><br>');
    this.cart.append($('<a style="border-radius:3px; padding:5px 10px; background: linear-gradient(#D2E2FF,#9DCCFF);box-shadow: inset -1px -1px 2px rgba(77,77,77,.3);text-decoration: none;" target="_blank" href="/cart?a=test&pub=0" data-href="/cart?a=test&pub=0">Обычный вариант</a>').click(function(){t.cart.dialog('close'); $.post('/cart?ajax=1', {}, function(resp){t.parse_resp(resp)}, 'json');}), ' результаты выполнения будут в разделе «Моя статистика»');
    this.cart.append('<br><br>');
    this.cart.append($('<a style="border-radius:3px; padding:5px 10px; background: linear-gradient(#D2E2FF,#9DCCFF);box-shadow: inset -1px -1px 2px rgba(77,77,77,.3);text-decoration: none;" target="_blank" target="_blank" href="/cart?a=test&pub=2" data-href="/cart?a=test&pub=2">Домашняя работа</a>').click(function(){t.cart.dialog('close'); $.post('/cart?ajax=1', {}, function(resp){t.parse_resp(resp)}, 'json');}), ' вариант сохранится в разделе «Учителю»');
    this.cart.append('<br><br>');
    this.cart.append($('<a style="border-radius:3px; padding:5px 10px; background: linear-gradient(#D2E2FF,#9DCCFF);box-shadow: inset -1px -1px 2px rgba(77,77,77,.3);text-decoration: none;" target="_blank" target="_blank" href="/cart?a=test&pub=1" data-href="/cart?a=test&pub=1">Контрольная работа</a>').click(function(){t.cart.dialog('close'); $.post('/cart?ajax=1', {}, function(resp){t.parse_resp(resp)}, 'json');}), ' вариант сохранится в разделе «Учителю»');
}

Cart.prototype.parse_resp = function(resp){
    if (resp.hasOwnProperty('problems')){
        this.count = resp.problems.length;
        this.problems = resp.problems;
    }
    else {
        this.count = 0;
        this.problems = [];
    }
    this.fill_cart();
    this.fill_icon();

}
