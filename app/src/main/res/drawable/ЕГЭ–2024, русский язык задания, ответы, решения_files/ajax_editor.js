var enable_editor = function(){
    var show_edit_window = function(item, source){
        item.empty();
        var ta = $('<textarea style="width:100%; height:30px;">');
        if (item.attr('data-field_type') == 'mtext') ta.addClass('math_editor');
        ta.val(source);
        var pw = $('<div class="probtext"></div>');
        ta.keyup(function(){
            var t=$(this);
            if (t.data('oldto')) {
                clearTimeout(t.data('oldto'));
            }
            t.data('oldto', 
                setTimeout(function(){
                    $.post('/?c=preview', {body:t.val()}, function(resp){
                        t.next().html(resp)
                    }, 'html');
                }, 3000)
            );
        });
        item.append(
            ta, pw,
            $('<div style="position:absolute; right:2px; top:2px; box-shadow: 0px 0px 20px 10px #f0f0f0; margin:0">').append(
                $('<img src="/img/disk.png" class="ajax_editor_controls" style="cursor:pointer;" title="Сохранить">').click(function(){
                    $.post('/ajax_editor?a=save&ajax=1', {'field': item.attr('data-field'), 'model': item.attr('data-model'), 'id': item.attr('data-id'), 'body': $(this).parent().parent().find('textarea').val()}, function(resp){
                        item.data('view', resp).html(resp)
                        append_edit_btn(item);
                    });
                }),
                $('<img src="/img/cross.png" class="ajax_editor_controls" style="cursor:pointer;" title="Отмена">').click(function(){
                    item.html(item.data('view'));
                    append_edit_btn(item);
                })
            )
        )
        ta.autoResize();
    }
    var append_edit_btn = function(item){
        item.append(
            $('<img src="/img/edit.png" class="ajax_editor_controls" style="cursor:pointer; position:absolute; right:2px; top:2px; box-shadow: 0px 0px 20px 10px #f0f0f0; display:none" title="Редактировать">').click(function(){
                $.post('/ajax_editor?a=src&ajax=1', {'field': item.attr('data-field'), 'model': item.attr('data-model'), 'id': item.attr('data-id')}, function(resp){show_edit_window(item, resp.src)}, 'json');
            })
        );
    }
    var hover_in = function(){$(this).find('.ajax_editor_controls').fadeIn('fast')};
    var hover_out = function(){$(this).find('.ajax_editor_controls').fadeOut('fast')};
    $('.ajax_editor')
        .each(function(ind, el){
            var e = $(el);
            if (e.data('inited') === undefined) {
                e
                .data('inited', true)
                .css({'position': 'relative', 'min-height':20})
                .hover(hover_in, hover_out)
                .data('view', e.html());
                append_edit_btn(e);
            }
        });
        
};

var extra_select = function(){
    var append_edit_btn = function(item){
        item.after(
            $('<img src="/img/edit.png" style="float:right; margin:7px 2px">').click(function(){
                var div = $('<div style="width:600px; height:600px; overflow:scroll"></div>');
                console.log(item.val());
                if (item.attr('multiple')){
                    item.find('option').each(function(ind,el){
                        div.append('<label><input type="checkbox" value="'+$(el).val()+'" '+(item.val() && item.val().indexOf($(el).val()) != -1? 'checked':'')+'> '+$(el).text()+'</label><br>')
                    });
                } else {
                    item.find('option').each(function(ind,el){
                        div.append('<label><input type="radio" value="'+$(el).val()+'" '+(item.val() && item.val() == $(el).val()? 'selected':'')+'> '+$(el).text()+'</label><br>')
                    });
                }
                div.append($('<input type="button" value="OK">').click(function(){
                    var sets = [];
                    div.find('input:checked').each(function(ind, el){sets.push($(el).val())});
                    item.val(sets);
                    div.dialog('close');
                    item.change();
                }));
                div.dialog({'width':600, 'height':600});
            })
        );
    }
    $('.prob_extra_select').each(function(ind,el){
        var e = $(el);
        if (e.data('inited') === undefined) {
            e.data('inited', true);
            append_edit_btn(e);
        }    
    });
}
var svg_enable = function(){
    if ($('.test_inp[type=hidden]').length){
        return
    }
    if (window.new_svg_draw === undefined) {
        return
    }
    
    $('.pbody img, .solution img').each(function(i,e){
        var el = $(e);
        if (el.hasClass('tex')){return;}
        if (el.hasClass('nodraw')){return;}
        if (el.data('inited') === undefined) {
            el.data('inited', true);
            el.click(function(){
                var div=$('<div></div>');
                new_svg_draw(div[0], undefined, {background: el.attr('src'), width: Math.min(600, screen.width-40), height: Math.min(600, screen.height-200)});
                div.dialog({width:Math.min(650, screen.width-5), height:Math.min(700, screen.height-100), resize: function(event, ui){div[0].onresize && div[0].onresize({width:ui.size.width-24, height:ui.size.height-94})}});
            })
        }    
    });
}






$(document).ready(enable_editor);
$(document).ready(extra_select);
$(document).ready(svg_enable);

MutationObserver = window.MutationObserver || window.WebKitMutationObserver;
if (MutationObserver){
    var observer = new MutationObserver(function(mutations) {
        enable_editor();
        extra_select();
        svg_enable();
    });
    observer.observe(document, {childList:true, subtree:true});
}


