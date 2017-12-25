package com.realtimebidding.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.realtimebidding.dtos.Response;
import com.realtimebidding.dtos.ResponseTypeEnum;
import com.realtimebidding.model.Auction;
import com.realtimebidding.services.AuctionService;

@RestController 
@RequestMapping("auction")
public class AuctionController {
	
	@Autowired
	AuctionService AuctionService;
	
	@RequestMapping(value="/{AuctionId}", method = RequestMethod.GET)
	public Response<?> getAuctionById(@PathVariable("AuctionId")String AuctionId) {
		try {
			return new Response<Auction>(ResponseTypeEnum.SUCCESS,AuctionService.getById(AuctionId));
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Response<String>(ResponseTypeEnum.ERROR,e.getMessage());
		}
	}
	
	@RequestMapping(value="/", method = RequestMethod.POST)
	public Response<?> addNewAuction(@RequestBody Auction Auction) {
		try {
			return new Response<Auction>(ResponseTypeEnum.SUCCESS,AuctionService.addNewAuction(Auction));
		}
		catch(Exception e) {
			return new Response<String>(ResponseTypeEnum.ERROR,e.getMessage());
		}
	}
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public Response<?> getAllAuctions() {
		try {
			return new Response<List<Auction>>(ResponseTypeEnum.SUCCESS,AuctionService.getAllActive());
		}
		catch(Exception e) {
			return new Response<String>(ResponseTypeEnum.ERROR,e.getMessage());
		}
	}
	
	
	
	@RequestMapping(value="/{AuctionId}", method = RequestMethod.PUT)
	public Response<?> updateAuction(@PathVariable("AuctionId")String AuctionId,@RequestBody Auction Auction) {
		try {
			return new Response<Auction>(ResponseTypeEnum.SUCCESS,AuctionService.updateAuction(AuctionId, Auction));
		}
		catch(Exception e) {
			return new Response<String>(ResponseTypeEnum.ERROR,e.getMessage());
		}
	}
	
	@RequestMapping(value="/{AuctionId}", method = RequestMethod.DELETE)
	public Response<?> deleteAuctionById(@PathVariable("AuctionId")String AuctionId) {
		try {
			return new Response<Auction>(ResponseTypeEnum.SUCCESS,AuctionService.deleteAuction(AuctionId));
		}
		catch(Exception e) {
			return new Response<String>(ResponseTypeEnum.ERROR,e.getMessage());
		}
	}

}
