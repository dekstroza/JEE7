'use strict'
angular.module('myApp').factory('RegistrationService', RegistrationService);

RegistrationService.$inject = ['$http']
function RegistrationService($http) {
    var service = {}
    service.Register = Register;
    return service;

    function Register(firstname, lastname, email, password, callback) {

        var jsonString = '{"firstname":' + '"' + firstname + '",';
        jsonString += '"lastname":' + '"' + lastname + '",';
        jsonString += '"email":' + '"' + email + '",';
        jsonString += '"password":' + '"' + password + '"}';

        $http.post('http://172.17.0.3:8080/api/v1.0/security/user/', jQuery.parseJSON(jsonString))
            .success(function (response) {
                callback(response);
            });
    }
}