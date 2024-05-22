function razbor(){
    jQuery('div[data-razbor]').each(function(i,e){
        var el = jQuery(e);
        if (!el.parent().hasClass(el.attr('data-razbor'))){
        el.parent().data('razbor', 0);
        console.log('raz', el[0]);
        el.remove();
        }
    });
    jQuery('.base:visible').css({'position':'relative', 'padding-left':'1px', 'padding-right':'1px'}).each(function(i,e){
        var el = jQuery(e);
        if (el.data('razbor')) return;
        el.data('razbor', 1);
        el.append($('<div data-razbor="base" style="position:absolute; bottom:0px; left:2px; right:2px; height:2px; border-bottom:1px solid green; border-left:1px solid green; border-right:1px solid green"></div>').data('el', el[0]));
    });
    jQuery('.ending:visible').css({'position':'relative', 'padding-left':'1px', 'padding-right':'1px', 'margin-top': '5px'}).each(function(i,e){
        var el = jQuery(e);
        if (el.data('razbor')) return;
        el.data('razbor', 1);
        el.append($('<div data-razbor="ending" style="position:absolute; top:-6px; left:0px; right:0px; bottom:0; border:1px solid green"></div>').data('el', el[0]));
    });
    jQuery('.prefix:visible').css({'position':'relative', 'padding-left':'1px', 'padding-right':'1px', 'margin-top': '5px'}).each(function(i,e){
        var el = jQuery(e);
        if (el.data('razbor')) return;
        el.data('razbor', 1);
        el.append($('<div data-razbor="prefix" style="position:absolute; top:-8px; left:0px; right:0px; height:6px; border-top:1px solid green; border-right:1px solid green"></div>')).data('el', el[0]);
    });
    jQuery('.root:visible').css({'position':'relative', 'padding-left':'1px', 'padding-right':'1px', 'margin-top': '5px'}).each(function(i,e){
        var el = jQuery(e);
        if (el.data('razbor')) return;
        el.data('razbor', 1);
        el.append($('<div data-razbor="root" style="position:absolute; top:-8px; left:1px; right:1px; height:12px; border-radius: ' + el.width()/2 + 'px/6px; border-top:1px solid green"></div>').data('el', el[0]));
    });
    jQuery('.suffix:visible').css({'position':'relative', 'padding-left':'1px', 'padding-right':'1px', 'margin-top': '5px'}).each(function(i,e){
        var el = jQuery(e);
        if (el.data('razbor')) return;
        el.data('razbor', 1);
        el.append($('<div data-razbor="suffix" style="position:absolute; top:-9px; left:'+el.width()/2+'px; height:10px; width:10px; border-left:1px solid green; border-top:1px solid green; transform:scale(' + el.width()/10/1.4 + ',0.8) rotate(45deg); transform-origin: left top; -webkit-transform:scale(' + el.width()/10/1.4 + ',0.8) rotate(45deg); -webkit-transform-origin: left top;"></div>').data('el', el[0]));
    });
}


function wrap_table() {
    $('.pbody table').each(function(){
        if (!$(this).data('wrap_t')) {
		    $(this).wrap('<div class="wrap_scroll_table"></div>');
		    $(this).data('wrap_t', true)
	    }
    });
}

jQuery(document).ready(function(){
    if (window.MutationObserver){
        var robserver = new MutationObserver(function(mut) {
            razbor();
        });
        robserver.observe(jQuery('.sgia-main-content')[0]||jQuery('body')[0], {childList:true, subtree:true});
        var sols = document.getElementsByClassName('solution');
        for (var i=0; i<sols.length; i++){
            robserver.observe(sols[i], {attributes:true});
        }
        var wrap_table_obs = new MutationObserver(function(mut) {wrap_table();});
        wrap_table_obs.observe(jQuery('.sgia-main-content')[0]||jQuery('body')[0], {childList:true, subtree:true})
    }
    razbor();
    wrap_table();

});


function toDataURL(url, callback) {
    var xhr = new XMLHttpRequest();
    xhr.onload = function() {
        var reader = new FileReader();
        reader.onloadend = function() {
        callback(reader.result);
        }
        reader.readAsDataURL(xhr.response);
    };
    xhr.open('GET', url);
    xhr.responseType = 'blob';
    xhr.send();
}
/*
if (window.location.href.indexOf('print=true') !== -1 && window.location.href.indexOf('svg=0') !== -1){
    $(document).ready(function(){
        $("img").one("load", function() {
            var el = $(this);
            if (el.attr('src').indexOf('data:') === -1) {
                this.style.width = el.width();
                this.style.height = el.height();
                toDataURL(el.attr('src'), function(el){return function(res){el.attr('src', res)}}(el));
            }
          }).each(function() {
            if(this.complete) {
                $(this).load(); // For jQuery < 3.0 
                // $(this).trigger('load'); // For jQuery >= 3.0 
            }
          });


    });
}*/
//document.removeEventListener('copy', copy_function);
var copy_function = function(e){
    var selection = window.getSelection();
    if (selection.rangeCount > 0 && !selection.getRangeAt(0).collapsed) {
      range = selection.getRangeAt(0);
      var clonedSelection = range.cloneContents();
      var div = document.createElement('div');
      div.style.position = 'absolute';
      div.style.left = '-1000px';
      div.style.width = '800px';
      div.appendChild(clonedSelection);
      document.body.append(div);
      $(div).find('.nocopy').each(function(i,e){$(e).remove()});
      $(div).find(':hidden').each(function(i,e){$(e).remove()});
      $(div).find('img').each(function(i,e){
        var el = $(e);
        if (el.attr('src').indexOf('http') === -1 && el.attr('src').indexOf('data:') === -1) 
            el.attr('src', 'https://' + window.location.hostname + el.attr('src'))
      });
      $(div).find('a').each(function(i,e){var el = $(e); if (el.attr('href').indexOf('http') === -1 && el.attr('href')[0] == '/') el.attr('href', 'https://' + window.location.hostname + el.attr('href'))});
      var text = '<!doctype html><html class="Html" lang="ru"><head></head><body>'+div.innerHTML+'</body></html>';
      $(div).remove();
      var ptext = window.getSelection().toString().replace(/­/g, '');
    }
    else if (document.activeElement && document.activeElement.selectionStart !== undefined ){
        var text = document.activeElement.value.substring(document.activeElement.selectionStart,document.activeElement.selectionEnd);
        var ptext = text;
    } else {
        var text = '';
        var ptext = window.getSelection().toString().replace(/­/g, '');
    }
  text = text.replace(/­/g, '');
  e.clipboardData.setData('text/html', text);
  ptext = ptext.replace(/­/g, '');
  e.clipboardData.setData('text/plain', ptext);
  e.preventDefault();
};
document.addEventListener('copy', copy_function);
