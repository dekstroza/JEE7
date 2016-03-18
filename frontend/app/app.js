(function () {
    'use strict';

// Declare app level module which depends on views, and components
    angular.module('myApp', [
        'ui.bootstrap',
        'ngRoute',
        'ngCookies',
        'myApp.login',
        'myApp.view1',
        'myApp.view2'
    ]).config(config)
        .run(run);

    config.$inject = ['$routeProvider', '$locationProvider'];
    function config($routeProvider, $locationProvider) {
        $routeProvider
            .when('/login', {
                controller: 'LoginController',
                templateUrl: 'login/login.html',
                controllerAs: 'vm'
            })
            .when('/view1', {
                controller: 'View1Ctrl',
                templateUrl: 'view1/view1.html',
                controllerAs: 'vm'
            })

            .when('/view2', {
                controller: 'View2Ctrl',
                templateUrl: 'view/view2.html',
                controllerAs: 'vm'
            })
            .otherwise({redirectTo: '/login'});
    }

    run.$inject = [ '$rootScope', '$location', '$cookieStore', '$http'];
    function run($rootScope, $location, $cookieStore, $http) {
        $rootScope.layout = "app";
        // keep user logged in after page refresh
        $rootScope.globals = $cookieStore.get('globals') || {};
        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata; // jshint ignore:line
        }

        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // redirect to login page if not logged in and trying to access a restricted page
            var restrictedPage = $.inArray($location.path(), ['/login']) === -1;
            var loggedIn = $rootScope.globals.currentUser;
            if (restrictedPage && !loggedIn) {
                $location.path('/login');
            }
        });
    }
})();
