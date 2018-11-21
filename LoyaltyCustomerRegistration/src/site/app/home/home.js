(function () {
    angular.module("AppModule")
            .controller("HomeController", function ($scope,$window) {

                $scope.ui.init = function () {
//                    $location.path('/home');
                    $window.location.reload();
                };
                $scope.ui.init();
            });
}());