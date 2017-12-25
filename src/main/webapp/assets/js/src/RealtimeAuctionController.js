angular.module("AuctionApp",['ngStomp'])
.config(['$locationProvider',function($locationProvider) {
	
	$locationProvider.html5Mode({
		enabled : true,
		rewriteLinks : false
	});
					
}])
.service("RealtimeAuctionService",['$http','$q','$log',function($http,$q,$log){
	
	this.getAuctionByAuctionId = function(productId){
		var defer = $q.defer();
		$http.get('/auction/'+productId).then(function(data){
			if(data.data.status == "SUCCESS")
				defer.resolve(data.data.data);
			else
				defer.reject(data.data.data);
		})
		return defer.promise;
	}
	
	this.getUserById = function(userId){
		var defer = $q.defer();
		$http.get('/user/'+userId).then(function(data){
			if(data.data.status == "SUCCESS")
				defer.resolve(data.data.data);
			else
				defer.reject(data.data.data);
		})
		return defer.promise;
	}
	
	this.getUserByEmail = function(email){
		var defer = $q.defer();
		$http.post('/user/email',email).then(function(data){
			if(data.data.status == "SUCCESS")
				defer.resolve(data.data.data);
			else
				defer.reject(data.data.data);
		})
		return defer.promise;
	}
	
}])
.controller("RealtimeAuctionCtrl",['$scope','$stomp','$location','$log','$timeout','RealtimeAuctionService',function($scope,$stomp,$location,$log,$timeout,RealtimeAuctionService){
	
	$scope.auctionId = null;
	$scope.auction = null;
	$scope.subscription = null;
	$scope.currentTime = null;
	$scope.user = null;
	$scope.joined = false;
	$scope.temp = {
			"email" : null,
			"bid" : 0
	};

	(function(){
		$log.log("search -> ",$location.search());
		$scope.auctionId = $location.search().auctionId;
		if(localStorage.getItem("user") != null){
			$scope.user = JSON.parse(localStorage.getItem("user"));
			$scope.joined = true;
		}
		return RealtimeAuctionService.getAuctionByAuctionId($scope.auctionId);
	}())
	.then(function(data){
		$scope.auction = angular.copy(data);
		$log.log($scope.auction);
		return $stomp.connect("/rt-auction");
	})
	.then(function(frame){
		$scope.subscription = $stomp.subscribe('/rt-product/auction-updates/'+$scope.auction.product.id, function (payload, headers, res) {});
		$stomp.on('/rt-product/auction-updates/'+$scope.auction.product.id,function(data){
			$log.log("received payload -> ",data);
			switch(data.messageType){
				case "TIME_UPDATES" : return handleTimeUpdates(data);
				case "NEW_USER_JOINED" : return handleNewUserJoining(data);
				case "DETAILS_SHARED" : return handleAuctionDetails(data);
				case "BID_MADE" : return handleNewBidMade(data);
				case "AUCTION_STARTED" : return handleAuctionStarted(data);
				case "AUCTION_ENDED" : return handleAuctionEnded(data);
				case "AUCTION_ERROR" : return handleAuctionError(data);
			}
		})
		$stomp.send("/app/rt-auction/getDetails/"+$scope.auction.product.id,{
				"userId" : null,
				"requestTime" : $scope.currentTime
		})
	})
	
	var handleTimeUpdates = function(data){
		$timeout(function(){
			$scope.currentTime = new Date(data.messageTime);
		},0);
	}
	
	var handleNewUserJoining = function(data){
		RealtimeAuctionService.getUserById(data.userId)
		.then(function(user){
			if(user.id == $scope.user.id){
				$scope.joined = true;
				localStorage.setItem("user",JSON.stringify($scope.user));
			}
			$scope.auction.participants.push(user);
			notifyContestant(user.firstName+"["+user.email+"] joined auction !!!");
		})
	}
	
	var handleAuctionDetails = function(data){
		$scope.auction = angular.copy(data.content);
		notifyContestant("Auction details updated !!!");
	}
	
	var handleNewBidMade = function(data){
		$scope.auction.biddings.push(data.content);
		RealtimeAuctionService.getUserById(data.userId)
		.then(function(user){
			notifyContestant(user.firstName+"["+user.email+"] made new bid !!!");
		})
	}
	
	var handleAuctionStarted = function(data){
		notifyContestant("Auction has been started");
	}
	
	var handleAuctionEnded = function(data){
		notifyContestant("Auction ended successfully !!");
		$timeout(function(){
			var maxBidder = $scope.auction.biddings[0];
			for(var i = 1 ; i <  $scope.auction.biddings.length ; i++)
				maxBidder = maxBidder.bidAmount > $scope.auction.biddings[i].amount ? maxBidder : $scope.auction.biddings[i];
			$scope.winner = {
					"name" : maxBidder.user.firstName+" "+maxBidder.user.lastName,
					"bidAmount" : maxBidder.amount
			}
			$("#winnerModal").modal("show");
			localStorage.removeItem("user");
		},0);
		
	}
	
	var handleAuctionError = function(data){
		if($scope.user.id == data.userId)
			notifyError("Error : "+data.content);
	}
	
	var notifyContestant = function(message){
		$log.log("message -> ",message);
		alertify.success(message);
	}
	
	var notifyError = function(err){
		$log.log("error -> ",err);
		alertify.error(err);
	}
	
	$scope.addNewBid = function(){
		var maxBid = $scope.auction.startingAmount;
		for(var i = 0 ; i <  $scope.auction.biddings.length ; i++)
			maxBid = maxBid > $scope.auction.biddings[i].amount ? maxBid : $scope.auction.biddings[i].amount;
		if($scope.temp.bid > maxBid){
			$stomp.send("/app/rt-auction/bid/"+$scope.auction.product.id,{
				"userId" : $scope.user.id,
				"bidAmount" : angular.copy($scope.temp.bid)
			});
		}
		else{
			notifyError("Next Bid Amount Must Be Greater Than Max Till Now !!");
		}
	}
	
	$scope.joinAuction = function(){
		console.log($scope.temp.email);
		RealtimeAuctionService.getUserByEmail({"email" : $scope.temp.email})
		.then(function(data){
			$log.log("join Auction User Result -> ",data);
			$scope.userIds = $scope.auction.participants.map(function(user){
				return user.id;
			})
			for(var i = 0 ; i < $scope.userIds.length ; i++)
				if(data.id == $scope.userIds[i])
					return notifyError("User already joined");
			$scope.user = angular.copy(data);
			$stomp.send("/app/rt-auction/join/"+$scope.auction.product.id,{
				"userId" : $scope.user.id,
				"userName" : $scope.user.firstName+" "+$scope.user.lastName,
				"joiningTime" : $scope.currentTime
			});
		})
		.catch(function(err){
			notifyError(err);
		})
	}
	
	
	
}]);