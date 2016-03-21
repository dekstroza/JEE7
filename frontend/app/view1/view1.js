'use strict';

angular.module('myApp.view1', ['ngRoute'])
    .controller('View1Ctrl', View1Ctrl);
View1Ctrl.$inject = ['$rootScope'];

function View1Ctrl($rootScope) {
    $rootScope.layout = "app";
}