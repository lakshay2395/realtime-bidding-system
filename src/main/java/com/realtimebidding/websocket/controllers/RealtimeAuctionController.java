package com.realtimebidding.websocket.controllers;


import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.realtimebidding.dtos.AuctionBidRequestDto;
import com.realtimebidding.dtos.AuctionDetailsRequestDto;
import com.realtimebidding.dtos.AuctionJoinRequestDto;
import com.realtimebidding.dtos.AuctionMessageDto;
import com.realtimebidding.dtos.AuctionMessageTypeEnum;
import com.realtimebidding.dtos.Response;
import com.realtimebidding.dtos.ResponseTypeEnum;
import com.realtimebidding.model.Auction;
import com.realtimebidding.model.BidInformation;
import com.realtimebidding.model.User;
import com.realtimebidding.services.RealtimeAuctionHandlerService;

@EnableScheduling
@Controller
public class RealtimeAuctionController {
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@Autowired
	RealtimeAuctionHandlerService auctionHandlerService;
	
	@MessageMapping("/rt-auction/join/{productId}")
	@SendTo("/rt-product/auction-updates/{productId}")
	@SuppressWarnings("unchecked")
	public AuctionMessageDto<?> joinAuction(@DestinationVariable("productId")String productId,AuctionJoinRequestDto auctionJoinRequest) {
		try {
			Response<User> response = (Response<User>)auctionHandlerService.joinAuction(productId, auctionJoinRequest);
			if(response.getStatus() == ResponseTypeEnum.SUCCESS)
				return new AuctionMessageDto<User>(response.getData(),AuctionMessageTypeEnum.NEW_USER_JOINED,auctionJoinRequest.getUserId(),new Timestamp(System.currentTimeMillis()));
			return new AuctionMessageDto<String>(response.getData().toString(),AuctionMessageTypeEnum.AUCTION_ERROR,auctionJoinRequest.getUserId(),new Timestamp(System.currentTimeMillis()));
		} catch (Exception e) {
			e.printStackTrace();
			return new AuctionMessageDto<String>(e.getMessage(),AuctionMessageTypeEnum.AUCTION_ERROR,auctionJoinRequest.getUserId(),new Timestamp(System.currentTimeMillis()));
		}
	}
	
	@MessageMapping("/rt-auction/bid/{productId}")
	@SendTo("/rt-product/auction-updates/{productId}")
	@SuppressWarnings("unchecked")
	public AuctionMessageDto<?> bid(@DestinationVariable("productId")String productId,AuctionBidRequestDto auctionBidRequest){
		try {
			Response<BidInformation> response = (Response<BidInformation>) auctionHandlerService.addBid(productId, auctionBidRequest);
			if(response.getStatus() == ResponseTypeEnum.SUCCESS)
				return new AuctionMessageDto<BidInformation>(response.getData(),AuctionMessageTypeEnum.BID_MADE,auctionBidRequest.getUserId(),new Timestamp(System.currentTimeMillis()));
			return new AuctionMessageDto<String>(response.getData().toString(),AuctionMessageTypeEnum.AUCTION_ERROR,auctionBidRequest.getUserId(),new Timestamp(System.currentTimeMillis()));
		} catch (Exception e) {
			e.printStackTrace();
			return new AuctionMessageDto<String>(e.getMessage(),AuctionMessageTypeEnum.AUCTION_ERROR,auctionBidRequest.getUserId(),new Timestamp(System.currentTimeMillis()));
		}
	}
	
	@MessageMapping("/rt-auction/getDetails/{productId}")
	@SendTo("/rt-product/auction-updates/{productId}")
	@SuppressWarnings("unchecked")
	public AuctionMessageDto<?> getDetails(@DestinationVariable("productId")String productId,AuctionDetailsRequestDto auctionDetailsRequest){
		try {
			Response<Auction> response = (Response<Auction>) auctionHandlerService.getAuctionDetails(productId);
			if(response.getStatus() == ResponseTypeEnum.SUCCESS)
				return new AuctionMessageDto<Auction>(response.getData(),AuctionMessageTypeEnum.DETAILS_SHARED,auctionDetailsRequest.getUserId(),new Timestamp(System.currentTimeMillis()));
			return new AuctionMessageDto<String>("Some error occurred",AuctionMessageTypeEnum.AUCTION_ERROR,auctionDetailsRequest.getUserId(),new Timestamp(System.currentTimeMillis()));
		} catch (Exception e) {
			e.printStackTrace();
			return new AuctionMessageDto<String>(e.getMessage(),AuctionMessageTypeEnum.AUCTION_ERROR,auctionDetailsRequest.getUserId(),new Timestamp(System.currentTimeMillis()));
		}
	}
	

	@Scheduled(fixedRate = 1000)
	public void sendCurrentTime() {
		try {
		 List<Auction> auctions = auctionHandlerService.getAllAuctions();
		 AuctionMessageDto<String> dto = new AuctionMessageDto<String>();
		 dto.setContent(null);
		 dto.setMessageTime(new Timestamp(System.currentTimeMillis()));
		 dto.setMessageType(AuctionMessageTypeEnum.TIME_UPDATES);
		 for(Auction auction : auctions)
			 this.template.convertAndSend("/rt-product/auction-updates/"+auction.getProduct().getId(),dto);
		}
		catch(Exception e) {
			 e.printStackTrace();
		}
	}
	
	@Scheduled(fixedRate = 1000)
	public void checkAndSendBidStartNotification() {
		try {
			 List<Auction> auctions = auctionHandlerService.getAllAuctions();
			 AuctionMessageDto<String> dto = new AuctionMessageDto<String>();
			 dto.setContent(null);
			 dto.setMessageTime(new Timestamp(System.currentTimeMillis()));
			 dto.setMessageType(AuctionMessageTypeEnum.AUCTION_STARTED);
			 for(Auction auction : auctions) {
				 if(auction.getStartTime().getTime() == System.currentTimeMillis())
					 this.template.convertAndSend("/rt-product/auction-updates/"+auction.getProduct().getId(),dto);
			 }
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Scheduled(fixedRate = 1000)
	public void checkAndSendBidEndNotification() {
		try {
			 List<Auction> auctions = auctionHandlerService.getAllAuctions();
			 AuctionMessageDto<String> dto = new AuctionMessageDto<String>();
			 dto.setContent(null);
			 dto.setMessageTime(new Timestamp(System.currentTimeMillis()));
			 dto.setMessageType(AuctionMessageTypeEnum.AUCTION_ENDED);
			 for(Auction auction : auctions) {
				 if(auction.getEndTime().getTime() == System.currentTimeMillis()) {
					auctionHandlerService.markAuctionAsCompleted(auction.getProduct().getId());
				 	this.template.convertAndSend("/rt-product/auction-updates/"+auction.getProduct().getId(),dto);
				 }
			 }
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
