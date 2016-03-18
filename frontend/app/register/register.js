'use strict';

angular.module('myApp.register', ['ngRoute'])

    .controller('RegisterController', RegisterController);

RegisterController.$inject = ['$location', '$rootScope', 'RegistrationService'];
function RegisterController($location, $rootScope, RegistrationService) {
    var vm = this;
    vm.register = register;
    $rootScope.layout = "login";


    function register() {
        vm.dataLoading = true;
        RegistrationService.Register(vm.firstname, vm.lastname, vm.username, vm.password, function (response) {
            if (response['status'] == 'SUCCESS') {
                $location.path('/login');
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