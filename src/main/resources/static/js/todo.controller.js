(function() {
    var app = angular.module('TodoApp');
    app.controller('TodoCtrl', TodoCtrl);

    function TodoCtrl(TodoDataSvc) {
        var self = this;
        self.page = 1;
        self.pageSize = 10;

        // ============== auto-complete ==============
        self.todoId = '';
        self.parentId = '';
        self.parentIds = [];
        self.parentTodos = [];

        self.autoCompleteOptions = {
            minimumChars: 1,
            dropdownWidth: '500px',
            dropdownHeight: '200px',
            pagingEnabled: true,
            pageSize: 5,
            data: function (searchText, pagingParams) {
                return TodoDataSvc.getTodos(1, 100)
                    .then(function (response) {
                        searchText = searchText.toUpperCase();
                        var todos = _.filter(response.todos, function (todo) {
                            return todo.contents.startsWith(searchText);
                        });

                        return getPage(todos, pagingParams.pageIndex, pagingParams.pageSize);
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

                self.parentId = '';
                self.parentIds.push(e.item.id);
            }
        };

        function getPage(todos, pageIndex, pageSize) {
            var startIndex = pageIndex * pageSize;
            var endIndex = startIndex + pageSize;
            return todos.slice(startIndex, endIndex);
        }

        // ============== paging ==============
        getPagingTodos(self.page, self.pageSize);

        self.addTodo = function() {
            if (self.contents == '' || self.contents == null) {
                alert('할일을 입력하세요.');
                return;
            }

            if (!confirm('등록하시겠습니까?')) {
                return;
            }

            var todo = {
                'contents': self.contents,
                'parentIds': self.parentIds
            };

            TodoDataSvc.addTodo(todo)
                .then(function() {
                        alert('등록 성공');
                        self.todoId = '';
                        self.contents = '';
                        viewClear();
                        getPagingTodos(1, self.pageSize)
                    },
                    function () {
                        alert('등록 실패')
                    }
                );
        };

        self.updateTodo = function() {
            if (self.contents == '' || self.contents == null) {
                alert('할일을 입력하세요.');
                return;
            }

            if (!confirm('수정하시겠습니까?')) {
                return;
            }

            var todo = {
                'contents': self.contents,
                'parentIds': self.parentIds
            };

            TodoDataSvc.updateTodo(self.todoId, todo)
                .then(function() {
                        alert('수정 성공');
                        self.todoId = '';
                        self.contents = '';
                        viewClear();
                        getPagingTodos(1, self.pageSize)
                    },
                    function () {
                        alert('수정 실패')
                    }
                );
        };

        self.delParentTodos = function() {
            if (!confirm('참조목록을 삭제 하시겠습니까?')) {
                return;
            }
            viewClear();
        };

        self.getParentTodos = function(todo) {
            TodoDataSvc.getParentTodos(todo.id)
                .then(function(data) {
                    viewClear();
                    self.todoId = todo.id;
                    self.contents = todo.contents;

                    _.each(data, function(t) {
                        self.parentIds.push(t.id);
                        self.parentTodos.push({
                            id: t.id,
                            contents: t.contents
                        });
                    });
                });
        };

        self.done = function(todoId) {
            if (!confirm('완료처리 하시겠습니까?')) {
                return;
            }

            TodoDataSvc.done(todoId)
                .then(function(){
                    alert('완료처리 되었습니다.');
                    viewClear();
                    getPagingTodos(1, self.pageSize)
                }, function() {
                    alert('완료처리실패');
                });
        };

        self.prevPage = function () {
            getPagingTodos(self.pagingInfo.start_page - 1, self.pageSize);
        };

        self.nextPage = function () {
            getPagingTodos(self.pagingInfo.end_page + 1, self.pageSize);
        };

        self.page = function (page) {
            getPagingTodos(page, self.pageSize);
        };

        function getPagingTodos(page, size) {
            TodoDataSvc.getTodos(page, size)
                .then(function (data) {
                    self.pagingInfo = data;
                });
        }

        function viewClear() {
            self.parentId = '';
            self.parentIds = [];
            self.parentTodos = [];
        }
    }
})();