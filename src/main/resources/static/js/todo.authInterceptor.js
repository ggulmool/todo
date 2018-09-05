(function() {
    var app = angular.module('TodoApp');
    app.factory('TodoAuthInterceptor', function () {
        return {
            'request': function(config) {
                config.headers = config.headers || {};
                var encodedString = btoa("user1:test1234");
                //var encodedString = btoa("user2:test1234");
                config.headers.Authorization = 'Basic '+encodedString;
                return config;
            }
        };
    });
})();
