var javabot = angular.module('Javabot', []);
javabot.controller('MainControl', function ($scope, $http) {
    $scope.channels = [];
    $scope.listChannels = function () {
        $http.get('/api/channels')
                .success(function (data) {
                    $scope.channels = data;
                })
                .error(function (data) {
                    alert(data);
                });
    };
    $scope.showLogs = function(channel) {
      $scope.selected = channel;
    };
    $scope.today = function(channel) {
        return encodeURIComponent(channel.name);
    };

    $scope.listChannels();
});
Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1, //month
        "d+": this.getDate(), //day
        "h+": this.getHours(), //hour
        "m+": this.getMinutes(), //minute
        "s+": this.getSeconds() //second
    };
    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
};