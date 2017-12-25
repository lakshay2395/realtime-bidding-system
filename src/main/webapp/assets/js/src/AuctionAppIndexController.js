angular.module("AuctionApp",[])
.service("IndexService",['$http','$q','$log',function($http,$q,$log){
	
	this.addUser = function(user){
		var defer = $q.defer();
		$http.post("/user/").then(function(data){
			if(data.data.status == "SUCCESS")
				defer.resolve(data.data.data);
			else
				defer.reject(data.data.data);
		});
		return defer.promise;
	}
	
	this.getAllAuctions = function(){
		var defer = $q.defer();
		$http.get('/auction/').then(function(data){
			if(data.data.status == "SUCCESS")
				defer.resolve(data.data.data);
			else
				defer.reject(data.data.data);
		})
		return defer.promise;
	}
	
}])
.controller("AuctionAppIndexCtrl",['$scope','IndexService',function($scope,IndexService){
	
	$scope.auctions = [];
	$scope.tempUser = {
			"firstName" : null,
			"lastName" : null,
			"email" : null,
			"dateOfJoining" : null
	};
	$scope.user = {
			"firstName" : null,
			"lastName" : null,
			"email" : null,
			"dateOfJoining" : null
	}
	
	$scope.errorHandler = function(err){
		window.alert(err.toString());
	}
	
	IndexService.getAllAuctions().then(function(data){
		$scope.auctions = angular.copy(data);
	})
	.catch(function(err){
		$scope.errorHandler(err);
	})
	
	$scope.addUser = function(){
		$scope.user.dateOfJoining = new Date();
		IndexService.addUser($scope.user)
		.then(function(data){
			$scope.user = angular.copy($scope.tempUser);
		})
		.catch(function(err){
			$scope.errorHandler(err);
		})
	}
	
	
}]);