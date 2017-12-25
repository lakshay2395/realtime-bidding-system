package com.realtimebidding.services;

import java.util.List;

import com.realtimebidding.model.Auction;
import com.realtimebidding.model.Product;

public interface AuctionService {
	
	Auction getById(String id)throws Exception;
	
	List<Auction> getAllActive() throws Exception;
	
	Auction addNewAuction(Auction auction)throws Exception;
	
	Auction updateAuction(String id,Auction auction)throws Exception;
	
	Auction deleteAuction(String id)throws Exception; 

	Auction getByProduct(Product product) throws Exception;
}
