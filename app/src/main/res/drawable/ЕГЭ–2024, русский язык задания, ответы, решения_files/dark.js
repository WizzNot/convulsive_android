var DarkSwitch = function(){
    var t = this;
    $('div[style*=fixed]').appendTo('body');
    this.div = $('<div></div>').css({position: 'absolute', right:'45px', top:'10px', cursor: 'pointer', border: '1px solid silver',padding: '0', 'border-radius': '5px', height:'30px'}).appendTo('body');
    t.div.click(function(){t.click_hdlr()});
    this.theme = localStorage.getItem('theme');
    if (this.theme == undefined) this.theme = 'light';
    this.render();
}
DarkSwitch.prototype.render = function(){
    if (this.theme == 'dark'){
        var text = 'Тема: темная';
        var img = "dark.svg";
    } else if (this.theme == 'light') {
        var text = 'Тема: светлая';
        var img = "light.svg";
    } else {
        var text = 'Тема: авто';
        var img = "auto.svg";
    }
    this.div.html('<img style="height:30px" src="/img/'+img+'" title="'+text+'">');
    $('body').attr('data-theme', this.theme);
}

DarkSwitch.prototype.click_hdlr = function(){
    if (this.theme == 'dark'){
        this.theme = 'light';
    } else if (this.theme == 'light') {
        this.theme = 'auto';
    } else {
        this.theme = 'dark';
    }
    try{
        localStorage.setItem('theme', this.theme);
    } catch (e) {}
    console.log(this);
    this.render();
}

$(document).ready(function(){
    ds = new DarkSwitch();
});
