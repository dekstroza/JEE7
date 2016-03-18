'use strict';

angular.module('myApp.login', ['ngRoute'])

    .controller('LoginController', LoginController);

LoginController.$inject = ['$location', '$rootScope', 'AuthenticationService'];
function LoginController($location, $rootScope, AuthenticationService) {
    var vm = this;
    vm.login = login;
    $rootScope.layout = "login";

    (function initController() {
        // reset login status
        AuthenticationService.ClearCredentials();
    })();


    function login() {
        vm.dataLoading = true;
        AuthenticationService.Login(vm.username, vm.password, function (response) {
            if (response['status'] == 'SUCCESS') {
                AuthenticationService.SetCredentials(vm.username, vm.password);
                $location.path('/view1');
            }
            else {
                Error(response['message']);
                vm.dataLoading = false;
            }
        });


    }

    function Error(message) {
        $rootScope.flash = {
            message: message,
            type: 'error',
            keepAfterLocationChange: false
        };
    }


}