tools_loader = new Object;
tools_loader.calc = false;
tools_loader.inited = false;
tools_loader.init_funcs = [];
tools_loader.tools = {r3d:[], fcalc:[], graph:[]};
tools_loader.loaded = [];
tools_loader.to = 0;
tools_loader.busy = false;
tools_loader.do_init = function(){
    if (tools_loader.busy){
        if (tools_loader.to) {
            clearTimeout(tools_loader.to);
        }
        tools_loader.to = setTimeout(function(){tools_loader.do_init()}, 500);
        return
    }
    tools_loader.busy = true;
    var async=false
    if (tools_loader.calc && tools_loader.loaded.indexOf('calc') == -1){
        tools_loader.loaded.push('calc');
        $.getScript("/js/calc.js", function(){Calc()});
    }
        
    if (tools_loader.tools['r3d'].length){
        if (tools_loader.loaded.indexOf('r3d') == -1){
            tools_loader.loaded.push('r3d');
            async=true;
            $.getScript("/js/3drender.js", function(){for (var i=0; i<tools_loader.tools['r3d'].length; i++) tools_loader.tools['r3d'][i](); tools_loader.tools['r3d']=[]; tools_loader.busy = false});
        } else {
            for (var i=0; i<tools_loader.tools['r3d'].length; i++) tools_loader.tools['r3d'][i]();
            tools_loader.tools['r3d']=[];
        }
    }
        
    if (tools_loader.tools['fcalc'].length){
        if (tools_loader.loaded.indexOf('fcalc') == -1){
            tools_loader.loaded.push('fcalc');
            async=true;
            $.getScript("/js/fcalc.js", function(){for (var i=0; i<tools_loader.tools['fcalc'].length; i++) tools_loader.tools['fcalc'][i](); tools_loader.tools['fcalc']=[]; tools_loader.busy = false});
        } else {
            for (var i=0; i<tools_loader.tools['fcalc'].length; i++) tools_loader.tools['fcalc'][i]();
            tools_loader.tools['fcalc']=[];
        }
    }

    if (tools_loader.tools['graph'].length){
        if (tools_loader.loaded.indexOf('graph') == -1){
            tools_loader.loaded.push('graph');
            async=true;
            $.getScript("/js/graph.js", function(){for (var i=0; i<tools_loader.tools['graph'].length; i++) tools_loader.tools['graph'][i](); tools_loader.tools['graph']=[]; tools_loader.busy = false});
        } else {
            for (var i=0; i<tools_loader.tools['graph'].length; i++) tools_loader.tools['graph'][i]();
            tools_loader.tools['graph']=[];
        }
    }
    
    for (var fnum=0; fnum < tools_loader.init_funcs.length; fnum++){
        tools_loader.init_funcs[fnum]();
        }
    tools_loader.init_funcs = [];
    if (!async){
        tools_loader.busy = false
    }
    
}
tools_loader.init = function(tool, func){
    tools_loader.tools[tool].push(func);
    if (tools_loader.inited) tools_loader.do_init();
};
$(document).ready(function(){
    tools_loader.inited = true;
    tools_loader.do_init();
});


flyout = new Object;
flyout.func = function(){};
flyout.overlay = null;
flyout.div = null;
flyout.show = function(query, func){
    flyout.func = func;
    flyout.overlay = $('<div style="background-color:rgba(0,0,0,0.5);position:fixed;top:0px;bottom:0px;left:0px;right:0px;z-index:100;opacity:0">');
    $('body').prepend(flyout.overlay);
    flyout.div = $('<div style="z-index:101;position:fixed;background:white;border:1px solid black;border-radius:10px;padding:10px;opacity:0">')
    flyout.overlay.animate({opacity:1}, 'fast');
    $.ajax({url:query, success:function(data){
        flyout.div.html(data);
        flyout.overlay.after(flyout.div);
        flyout.div.css('left', Math.round((flyout.overlay.width()-flyout.div.width())/2)+'px');
        flyout.div.css('top', Math.round((flyout.overlay.height()-flyout.div.height())/2)+'px');
        flyout.div.animate({opacity:1}, 'fast');
    }});
}
flyout.close = function(callback){
    if (callback!==undefined)
        flyout.func(callback);
    flyout.overlay.animate({opacity:0}, 'fast', function(){flyout.overlay.remove()});
    flyout.div.animate({opacity:0}, 'fast', function(){flyout.div.remove()});
    }
