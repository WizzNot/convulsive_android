function login_popup(message, href) {

    var isUAExam = window.location.hostname.includes('skladu');

    var div = $('<div class="login_popup"><div>');
    var mess_div = $('<div style="text-align: center;margin-bottom: 10px;"></div>');
    if (message !== undefined){
        mess_div.text(message);
    } else {
        if (isUAExam) {
            mess_div.text('Для використання цієї функції необхідно увійти до системи.');
        } else {
            mess_div.text('Для использования этой функции необходимо войти в систему.');
        }

    }
    if (isUAExam) {
        var un_inp = $('<input class="lpinp" style="width: 98%;margin-bottom: 5px;" name="user"      type="text"     placeholder="Електронна пошта">');
        var pw_inp = $('<input class="lpinp" style="width: 98%;" name="password"  type="password" placeholder="Пароль">');
        var err_div = $('<div style="color: #a00" class="error">&nbsp;</div>');
        var cnl_btn = $('<input class="lpinp" type="button" style="float:left" value="Відміна">');
        var lgn_btn = $('<input class="lpinp" type="button" style="float:right" value="Увійти">');
        var reg_btn = $('<a href="/register">Зареєструватись</a>');
        var chp_btn = $('<a href="/pass_change">Відновлення паролю</a>')
        var vk_btn = $('<a href="/vkauth">Вхід через Вконтакте</a>')
    } else {
        var un_inp = $('<input class="lpinp" style="width: 98%;margin-bottom: 5px;" name="user"      type="text"     placeholder="Электронная почта">');
        var pw_inp = $('<input class="lpinp" style="width: 98%;" name="password"  type="password" placeholder="Пароль">');
        var err_div = $('<div style="color: #a00" class="error">&nbsp;</div>');
        var cnl_btn = $('<input class="lpinp" type="button" style="float:left" value="Отмена">');
        var lgn_btn = $('<input class="lpinp" type="button" style="float:right" value="Войти">');
        var reg_btn = $('<a href="/register">Зарегистрироваться</a>');
        var chp_btn = $('<a href="/pass_change">Восстановить пароль</a>')
        var vk_btn = $('<a href="/vkauth">Войти через ВКонтакте</a>')
    }
    reg_btn.click(function(event){
        event.preventDefault();
        div.empty();
        div.dialog({ width: 670});
        $.getScript('/react/dist/register.js?v=2', function(){
            register_form(div[0], function(){if (href !== undefined){window.location.href = href;} else {window.location.href += '';}});
            div.dialog({position: { my: "center", at: "center", of: window }});
        });
        return false;
    })
    cnl_btn.click(function(){
        div.dialog('close');
        div.remove()}
    );
    lgn_btn.click(function(){
        $.post('/newapi/login', JSON.stringify({user: un_inp.val(), password: pw_inp.val()}), function(resp){
            if (resp.status) {
                if (href !== undefined){
                    window.location.href = href;
                } else {
                    window.location.href += '';
                }
            } else {
                if (isUAExam) {
                    err_div.text('Введено неправильний логін або пароль.')
                } else {
                    err_div.text('Введен неправильный логин или пароль.')
                }
            }
        }, 'json');
    })
    div.append(mess_div, un_inp, pw_inp, err_div, reg_btn, '<br>', chp_btn, '<br>', vk_btn, '<br>', cnl_btn, lgn_btn);
    if (isUAExam) {
        div.dialog({'title': 'Необхідна авторизація'});
    } else {
        div.dialog({'title': 'Необходима авторизация'});
    }
    div.dialog({ width: 340})
    div.dialog({
      dialogClass: "jq-ui-dialog-max-width"
    });
}
$('<style>.login_popup a{font-size: 11px;} .login_popup input.lpinp {display:block} </style>').appendTo(document.head);
