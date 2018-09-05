var app = angular.module('TodoApp', ['autoCompleteModule']);

app.config(function($httpProvider) {
    $httpProvider.interceptors.push('TodoAuthInterceptor');
});
