//app.filter('unique', function () {
//
//    return function (arr, field) {
//        var o = {}, i, l = arr.length, r = [];
//        for (i = 0; i < l; i += 1) {
//            o[arr[i][field]] = arr[i];
//        }
//        for (i in o) {
//            r.push(o[i]);
//        }
//        return r;
//    };
//})

(function () {
    angular.module("AppModule").directive('unique', function () {
        return function (arr, field) {
            var o = {}, i, l = arr.length, r = [];
            for (i = 0; i < l; i += 1) {
                o[arr[i][field]] = arr[i];
            }
            for (i in o) {
                r.push(o[i]);
            }
            return r;
        };
    });
}());
