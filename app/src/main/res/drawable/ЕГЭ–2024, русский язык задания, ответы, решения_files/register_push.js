function register_push(){
        if (Notification.permission !== 'denied') {
           Notification.requestPermission(function(){});
       }
       if (Notification.permission === "granted") {
         navigator.serviceWorker.register('/service.js?29', {})
         navigator.serviceWorker.ready.then(function(req) {
           console.log(req.active);
           req.active.postMessage('reg');
         }).catch(function(error) {
           console.log('Registration failed with ' + error);
         });
       }
}
