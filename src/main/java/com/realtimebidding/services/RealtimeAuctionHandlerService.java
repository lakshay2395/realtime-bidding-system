package com.realtimebidding.services;

import java.util.List;

import com.realtimebidding.dtos.AuctionBidRequestDto;
import com.realtimebidding.dtos.AuctionJoinRequestDto;
import com.realtimebidding.dtos.Response;
import com.realtimebidding.model.Auction;
import com.realtimebidding.model.BidInformation;

public interface RealtimeAuctionHandlerService {

	Response<?> joinAuction(String productId,AuctionJoinRequestDto auctionJoinRequest)throws Exception;
	
	Response<?> addBid(String productId,AuctionBidRequestDto auctionBidRequest)throws Exception;
	
	List<Auction> getAllAuctions()throws Exception;
	
	BidInformation getAuctionWinner(String productId)throws Exception;
	
	Response<?> getAuctionDetails(String productId)throws Exception;
	
	void markAuctionAsCompleted(String productId)throws Exception;
	
}

