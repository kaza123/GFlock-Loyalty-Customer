(function () {
    //index module
    angular.module("AppModule", [
        "ngRoute",
        "ngAnimate",
        "ngCookies",
        "ui.bootstrap",
        "ui-notification"
    ]);
    //constants
    angular.module("AppModule")
            .constant("systemConfig", {
                apiUrl:
                        location.hostname === 'localhost'
                        ? "http://localhost:8084"
                        : location.protocol + "//" + location.hostname + (location.port ? ':' + location.port : '')
            });
           

    //route config
    angular.module("AppModule")
            .config(function ($routeProvider) {
                $routeProvider
                        
                        .when("/", {
                            templateUrl: "app/home/home.html"
                        })
                        .when("/loyalty-customer", {
                            templateUrl: "app/master/loyalty-customer/loyalty-customer.html",
                            controller: "LoyaltyController"
                        })
                        .otherwise({
                            redirectTo: "/"
                        });
            });
}());