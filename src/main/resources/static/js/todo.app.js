var app = angular.module('TodoApp', ['autoCompleteModule','ngProgress']);

app.config(function($httpProvider) {
    $httpProvider.interceptors.push('TodoAuthInterceptor');
});
