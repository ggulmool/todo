(function() {
    var app = angular.module('TodoApp');
    app.service('TodoDataSvc', function ($http) {
        var self = this;

        self.getTodos = function(page, size) {
            var url = '/api/todos?page=' + page + '&size=' + size;
            return $http.get(url).then(function (response) {
                console.log(response.data);
                return response.data;
            });
        };

        self.getParentTodoById = function(todoId) {
            var url = '/api/todos/' + todoId + '/parents';
            return $http.get(url).then(function (response) {
                console.log(response.data);
                return response.data;
            });
        };

        self.addTodo = function(todo) {
            var url = '/api/todos'
            return $http.post(url, todo)
                .then(function (response) {
                    console.log(response);
                });
        }

        self.updateTodo = function(todoId, todo) {
            var url = '/api/todos/' + todoId;
            return $http.put(url, todo)
                .then(function (response) {
                    console.log(response);
                });
        }
    });
})();