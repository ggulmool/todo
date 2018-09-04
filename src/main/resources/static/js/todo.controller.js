(function() {
    var app = angular.module('TodoApp');
    app.controller('TodoCtrl', TodoCtrl);

    function TodoCtrl(TodoDataSvc) {
        var self = this;
        self.currentPage = 1;
        self.currentSize = 5;

        // ============== auto-complete ==============
        self.parentId = null;
        self.parentIds = [];
        self.parentTodos = [];
        self.autoCompleteOptions = {
            minimumChars: 1,
            data: function (searchText) {
                return TodoDataSvc.getTodos(1, 5)
                    .then(function (response) {
                        searchText = searchText.toUpperCase();
                        return _.filter(response.todos, function (todo) {
                            return todo.contents.startsWith(searchText);
                        });
                    });
            },
            renderItem: function (todo) {
                return {
                    value: todo.contents,
                    label: "<p class='auto-complete' ng-bind-html='entry.item.contents'></p>"
                };
            },
            itemSelected: function (e) {
                var exists = _.filter(self.parentTodos, function(todo) {
                    return todo.id == e.item.id;
                });

                if (exists.length > 0) {
                    alert('이미 참조목록에 포함된 할일 입니다.');
                    self.parentId = '';
                    return;
                }

                self.parentTodos.push({
                    'id': e.item.id,
                    'contents': e.item.contents
                });

                self.parentIds.push(e.item.id);
            }
        };

        // ============== paging ==============
        getPagingTodos(self.currentPage, self.currentSize);

        self.addTodo = function() {
            if (self.newTodo == '' || self.newTodo == null) {
                alert('할일을 입력하세요.');
                return;
            }

            if (!confirm('등록하시겠습니까?')) {
                return;
            }

            var todo = {
                'contents': self.newTodo,
                'parentIds': self.parentIds
            };

            TodoDataSvc.addTodo(todo)
                .then(function() {
                        alert('등록 성공');
                        self.newTodo = '';
                        self.parentId = '';
                        self.parentIds = [];
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

        self.getParentTodos = function(todo) {
            TodoDataSvc.getParentTodos(todo.id)
                .then(function(data){
                    console.log(todo);
                    console.log(data);
                });
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