(function() {
    var app = angular.module('TodoApp');
    app.controller('TodoCtrl', TodoCtrl);

    function TodoCtrl(TodoDataSvc) {
        var self = this;
        self.currentPage = 1;
        self.currentSize = 3;

        self.modal = function() {
            var element = angular.element('#my_modal_popup');
            element.modal('show');
        };

        getPagingTodos(self.currentPage, self.currentSize);

        self.addTodo = function() {
            var todo = {
                'contents':'테스트1'
            };
            TodoDataSvc.addTodo(todo)
                .then(function() {
                        alert('등록 성공');
                        getPagingTodos(1, self.currentSize)
                    },
                    function () {
                        alert('등록 실패')
                    }
                );
        };

        self.updateTodo = function(todoId) {
            var todo = {
                'contents':'테스트2',
                'parentIds':[2, 5]
            };
            TodoDataSvc.updateTodo(todoId, todo)
                .then(function() {
                        alert('수정 성공');
                        getPagingTodos(1, self.currentSize)
                    },
                    function () {
                        alert('수정 실패')
                    }
                );
        };

        self.prevPage = function () {
            getPagingTodos(self.pagingInfo.start_page - 1, self.currentSize);
        };

        self.nextPage = function () {
            getPagingTodos(self.pagingInfo.end_page + 1, self.currentSize);
        };

        self.page = function (page) {
            getPagingTodos(page, self.currentSize);
        };

        function getPagingTodos(page, size) {
            self.currentPage = page;
            self.currentSize = size;
            TodoDataSvc.getTodos(page, size)
                .then(function (data) {
                    self.todos = data.todos;
                    self.pagingInfo = data;
                });
        }
    }
})();