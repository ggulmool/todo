(function() {
    var app = angular.module('TodoApp');
    app.factory('TodoAuthInterceptor', function () {
        return {
            // Sent the token (if present) with each request
            'request': function(config) {
                config.headers = config.headers || {};
                var encodedString = btoa("ggulmool:test1234");
                config.headers.Authorization = 'Basic '+encodedString;
                return config;
            }
        };
    });
})();
