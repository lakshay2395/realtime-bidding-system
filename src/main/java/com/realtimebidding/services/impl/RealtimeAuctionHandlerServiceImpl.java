package com.realtimebidding.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realtimebidding.dtos.AuctionBidRequestDto;
import com.realtimebidding.dtos.AuctionJoinRequestDto;
import com.realtimebidding.dtos.Response;
import com.realtimebidding.dtos.ResponseTypeEnum;
import com.realtimebidding.model.Auction;
import com.realtimebidding.model.BidInformation;
import com.realtimebidding.model.Product;
import com.realtimebidding.model.User;
import com.realtimebidding.services.AuctionService;
import com.realtimebidding.services.ProductService;
import com.realtimebidding.services.RealtimeAuctionHandlerService;
import com.realtimebidding.services.UserService;

@Service
public class RealtimeAuctionHandlerServiceImpl implements RealtimeAuctionHandlerService {
	
	private HashMap<String,Auction> auctionsMap = new HashMap<String,Auction>();
	
	@Autowired
	AuctionService auctionService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	UserService userService;
  
	@Override
	public Response<?> joinAuction(String productId, AuctionJoinRequestDto auctionJoinRequest) throws Exception {
		checkForAuctionExistence(productId);
		User user = userService.getById(auctionJoinRequest.getUserId());
		if(user != null) {
			Auction auction = auctionsMap.get(productId);
			if(auction.getParticipants() == null)
				auction.setParticipants(new java.util.HashSet<User>());
			if(auction.getBiddings() == null)
				auction.setBiddings(new java.util.ArrayList<BidInformation>());
			if(auction.getParticipants().add(user))
				return new Response<User>(ResponseTypeEnum.SUCCESS,user);
			return new Response<String>(ResponseTypeEnum.ERROR,"User already added as auction participant");
		}
		return new Response<String>(ResponseTypeEnum.ERROR,"No such user exists");
	}

	@Override
	public Response<?> addBid(String productId, AuctionBidRequestDto auctionBidRequest) throws Exception {
		checkForAuctionExistence(productId);
		Auction auction = auctionsMap.get(productId);
		if(auction.getParticipants() == null)
			auction.setParticipants(new java.util.HashSet<User>());
		if(auction.getBiddings() == null)
			auction.setBiddings(new java.util.ArrayList<BidInformation>());
		User user = userService.getById(auctionBidRequest.getUserId());
		if(user == null)
			return new Response<String>(ResponseTypeEnum.ERROR,"No such user exists");
		if(auction.getParticipants().contains(user)){
			BidInformation bidInformation = new BidInformation();
			bidInformation.setAmount(auctionBidRequest.getBidAmount());
			bidInformation.setUser(user);
			if(!auction.getBiddings().contains(bidInformation)) {
				Double maxBidding = getMaxBidding(auction.getBiddings());
				if(maxBidding >= auctionBidRequest.getBidAmount())
					return new Response<String>(ResponseTypeEnum.ERROR,"Bid has to be greater than max bid");
				auction.getBiddings().add(bidInformation);
				return new Response<BidInformation>(ResponseTypeEnum.SUCCESS,bidInformation);
			}
			return new Response<String>(ResponseTypeEnum.ERROR,"User Bid Already Registered");
		}
		return new Response<String>(ResponseTypeEnum.ERROR,"No such user registered for auction");
	}

	@Override
	public List<Auction> getAllAuctions() throws Exception {
		List<Auction> auctions = new ArrayList<Auction>();
		if(auctionsMap.size() == 0) {
			auctions = auctionService.getAllActive();
			for(Auction auction : auctions)
				auctionsMap.put(auction.getProduct().getId(),auction);
		}
		for(Map.Entry<String, Auction> entry : auctionsMap.entrySet()) 
			auctions.add(entry.getValue());
		return auctions;
	}

	@Override
	public BidInformation getAuctionWinner(String productId) throws Exception {
		Product product = productService.getById(productId);
		if(product != null) {
			Auction auction = auctionService.getByProduct(product);
			if(auction.getParticipants() == null)
				auction.setParticipants(new java.util.HashSet<User>());
			if(auction.getBiddings() == null)
				auction.setBiddings(new java.util.ArrayList<BidInformation>());
			List<BidInformation> list = auction.getBiddings();
			BidInformation maxBid = list.get(0);
			for(BidInformation bid : list){
				if(bid.getAmount() > maxBid.getAmount())
					maxBid = bid;
			}
			return maxBid;
		}
		return null;
	}
	
	@Override
	public void markAuctionAsCompleted(String productId)throws Exception{
		checkForAuctionExistence(productId);
		Auction auction = auctionsMap.get(productId);
		if(auction.getParticipants() == null)
			auction.setParticipants(new java.util.HashSet<User>());
		if(auction.getBiddings() == null)
			auction.setBiddings(new java.util.ArrayList<BidInformation>());
		auctionService.updateAuction(auction.getId(), auction);
		auctionsMap.remove(productId);
	}
	
	private void checkForAuctionExistence(String productId)throws Exception{
		if(auctionsMap.containsKey(productId))
			return;
		Product product = productService.getById(productId);
		if(product != null)
			auctionsMap.put(productId,auctionService.getByProduct(product));
	}
	
	@Override
	public Response<Auction> getAuctionDetails(String productId)throws Exception{
		checkForAuctionExistence(productId);
		return new Response<Auction>(ResponseTypeEnum.SUCCESS,auctionsMap.get(productId));
	}
	
	private Double getMaxBidding(List<BidInformation> bidding) {
		Double max = Double.MIN_VALUE;
		for(BidInformation bid : bidding) 
			max = Double.max(bid.getAmount(),max);
		return max;
	}

}
