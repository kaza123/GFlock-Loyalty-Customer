(function () {
    angular.module("AppModule")
            .controller("LoyaltyController", function ($scope, $filter, $uibModalStack, $uibModal, $location, Factory, Notification) {
                $scope.model = {};
                $scope.ui = {};
                $scope.model.loyalty = {};
                $scope.ui.otp = "";
                $scope.ui.otpValidateCount = 0;
                $scope.ui.validateMobile = "";
                $scope.ui.otpType = "mobile";//save mobile
                $scope.ui.selectedLoyalty = {};

                var saveUrl = "/api/sv/master/loyalty/save";
                var checkMobileUrl = "/api/sv/master/loyalty/find-by-mobile/";
                var getOpt = "/api/sv/master/loyalty/sent-otp/";
                var resentOpt = "/api/sv/master/sms/resent-otp/";

                $scope.ui.reset = function () {
                    $scope.model.loyalty = {};
                    $scope.ui.otp = "";
                    $scope.ui.otpValidateCount = 0;
                    $scope.ui.validateMobile = "";
                    $scope.ui.otpType = "mobile";
                };

                $scope.ui.modelCancel = function () {
                    $uibModalStack.dismissAll();
                };

                $scope.ui.viewOTPValidateionModel = function () {
                    $uibModal.open({
                        animation: true,
                        ariaLabelledBy: 'modal-title',
                        ariaDescribedBy: 'modal-body',
                        templateUrl: 'otpValidation.html',
                        scope: $scope,
                        size: 'md'
                    });
                };

                $scope.ui.checkMobile = function () {
                    // check mobile no is here !
                    var check = true;
                    if ($scope.model.loyalty.mobileNo.length !== 10) {
                        Notification.error("Invalied mobile no !");
                        check = false;
                    }
                    if ($scope.model.loyalty.mobileNo.substring(0, 1) !== "0") {
                        Notification.error("Invalied mobile no. First letter must be zero");
                        check = false;
                    }
                    if (check) {
                        $scope.ui.validateMobile = "94" + $scope.model.loyalty.mobileNo.substring(1, 10);
                        Factory.findOne(checkMobileUrl + $scope.ui.validateMobile,
                                function (data) {
                                    if (data) {
                                        $scope.ui.selectedLoyalty = data;
                                        var yearMonth = data.bYear + "-" + data.bMonth + "-" + data.bDate;
                                        $scope.ui.selectedLoyalty.yearMonth = new Date(yearMonth);
                                        $scope.ui.selectedLoyalty.dYear = data.bYear + "";
                                        $scope.ui.selectedLoyalty.dMonth = data.bMonth + "";
                                        $scope.ui.selectedLoyalty.dDate = data.bDate + "";
                                        $scope.ui.otpType = "mobile";
//                                    send sms
                                        $scope.ui.getOtp();
                                    }

                                },
                                function (data) {
                                }
                        );
                    }
                };

                $scope.ui.getOtp = function () {
                    if (!$scope.ui.otp) {
                        Factory.findOne(getOpt + $scope.ui.validateMobile,
                                function (data) {
                                    if (data) {
                                        $scope.ui.otp = data['otpNo'];
                                        $scope.ui.viewOTPValidateionModel();

                                    }
                                },
                                function (data) {
                                }
                        );
                    } else if ($scope.ui.validateMobile === $scope.model.loyalty.mobileNo) {
                        if ($scope.ui.otp + "" === $scope.model.loyalty.userOtp + "") {

                            var detail = $scope.model.loyalty;
                            var detailJSON = JSON.stringify(detail);
                            Factory.save(saveUrl, detailJSON,
                                    function (data) {
                                        Notification.success(data.name + " - Congratulations ! You are a Loyal Customer now. Your Loyalty no is 0" + data.mobileNo.substring(2, 11));
                                        $scope.ui.reset();
                                        $location.path('/');
                                    },
                                    function (data) {
                                        Notification.error(data.message);
                                    }
                            );
                        } else {
                            Notification.error("Enter correct OTP code");
                            $scope.ui.viewOTPValidateionModel;
                        }
                    } else {
                        Factory.findOne(getOpt + $scope.ui.validateMobile,
                                function (data) {
                                    if (data) {
                                        $scope.ui.otp = data['otpNo'];
                                        console.log($scope.ui.otp);
                                        $scope.ui.viewOTPValidateionModel();
                                    }
                                },
                                function (data) {
                                }
                        );
                    }
                };
                $scope.ui.mobileChange = function () {
                    $scope.ui.otp = null;
                };
                $scope.ui.resentOtp = function () {
                    Factory.findOne(resentOpt + $scope.ui.validateMobile + "/" + $scope.ui.otp,
                            function (data) {
                                if (data) {
                                    Notification.success("Sms sent successfully to " + $scope.ui.validateMobile);

                                }
                            },
                            function (data) {
                            }
                    );
                };

                $scope.ui.checkOtpCode = function () {
                    if ($scope.ui.otpValidateCount >= 3) {
//                        $scope.ui.modelCancel();
                        $scope.ui.reset();
                    }
                    if ($scope.ui.otp + "" === $scope.model.loyalty.userOtp + "") {
//                        $scope.ui.save();
                        $scope.model.loyalty = {};
                        Notification.success("Already Entered !");
                        $scope.model.loyalty = $scope.ui.selectedLoyalty;

                        $scope.model.loyalty.mobileNo = "0" + $scope.ui.selectedLoyalty.mobileNo.substring(2, 11);
                        $scope.ui.modelCancel();
                    } else {
                        Notification.error("Enter correct OTP code");
                    }
                };

                $scope.ui.checkValidation = function () {
                    var check = true;
                    $scope.model.loyalty.bYear = $filter('date')($scope.model.loyalty.yearMonth, "yyyy");
                    $scope.model.loyalty.bMonth = $filter('date')($scope.model.loyalty.yearMonth, "MM");

                    if (!$scope.model.loyalty.recidance) {
                        Notification.error("Select Mr Miss Mrs ");
                        check = false;
                    }
                    if (!$scope.model.loyalty.name) {
                        Notification.error("Please Enter your name");
                        check = false;
                    }
                    if (!$scope.model.loyalty.mobileNo) {
                        Notification.error("Please Enter your mobile number");
                        check = false;
                    }
                    if (!$scope.model.loyalty.mobileNo.length === 10) {
                        Notification.error("Please Enter your valid mobile number with 10 numbers");
                        check = false;
                    }
                    if ($scope.model.loyalty.bYear && ($scope.model.loyalty.bYear > 9999 || $scope.model.loyalty.bYear <= 0)) {
                        Notification.error("Please Enter the year");
                        check = false;
                    }
                    if ($scope.model.loyalty.bMonth && ($scope.model.loyalty.bMonth > 12 || $scope.model.loyalty.bMonth <= 0)) {
                        Notification.error("Please Enter the month");
                        check = false;
                    }
                    if ($scope.model.loyalty.bDate && ($scope.model.loyalty.bDate > 31 || $scope.model.loyalty.bDate <= 0)) {
                        Notification.error("Please Enter the day");
                        check = false;
                    }
                    if ($scope.model.loyalty.bDate && (!$scope.model.loyalty.bYear || $scope.model.loyalty.bMonth)) {
                        Notification.error("Please Enter the year and month");
                        check = false;
                    }
                    if (!$scope.model.loyalty.bDate && ($scope.model.loyalty.bYear || $scope.model.loyalty.bMonth)) {
                        Notification.error("Please Enter the day");
                        check = false;
                    }
                    if ($scope.model.loyalty.email && !$scope.ui.checkEmail()) {
                        Notification.error("Please Enter valid email address");
                        check = false;
                    }
                    if (check) {
                        $scope.model.loyalty.mobileNo = $scope.ui.validateMobile;
                        $scope.ui.otpType = "save";
                        $scope.saveWithOtp();
                    }
                };

                $scope.ui.checkEmail = function () {
                    if ($scope.model.loyalty.email) {

                        var email = $scope.model.loyalty.email;
                        var emailSplit = email.split('');
                        var check1 = false;
                        var check2 = false;
                        angular.forEach(emailSplit, function (char) {
                            if (!check1 && char === '.') {
                                check1 = true;
                            }
                            if (!check2 && char === '@') {
                                check2 = true;
                            }
                        });
                        if (check1 && check2) {
                            return true;
                        }
                        return false;
                    } else {
                        return true;
                    }
                };
                $scope.saveWithOtp = function () {
                    $scope.ui.getOtp();
                };

                $scope.ui.save = function () {
                    if ($scope.ui.otp + "" === $scope.model.loyalty.userOtp + "") {
                        var detail = $scope.model.loyalty;
                        var detailJSON = JSON.stringify(detail);

                        Factory.save(saveUrl, detailJSON,
                                function (data) {
                                    Notification.success(data.name + " - Congratulations ! You are a Loyal Customer now. Your Loyalty no is 0" + data.mobileNo.substring(2, 11));
                                    $scope.ui.reset();
                                    $location.path('/');
                                },
                                function (data) {
                                    console.log(data);
                                    Notification.error(data.message);
                                }
                        );
                        $scope.ui.modelCancel();
                    } else {
                        Notification.error("Enter correct OTP code");
                    }
                };

                $scope.ui.init = function () {
//                    $location.path('/home');
                    $scope.ui.selectedLoyalty.yearMonth = new Date('2000-12-01');
                    $scope.ui.selectedLoyalty.dYear = "2000";
                    $scope.ui.selectedLoyalty.dMonth = "12";
                    $scope.ui.selectedLoyalty.dDate = "12";
                };
                $scope.ui.init();
            });
}());