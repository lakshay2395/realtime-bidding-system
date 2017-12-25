package com.realtimebidding.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.realtimebidding.dtos.Response;
import com.realtimebidding.dtos.ResponseTypeEnum;
import com.realtimebidding.model.Product;
import com.realtimebidding.services.ProductService;

@RestController
@RequestMapping("product")
public class ProductController {
	
	@Autowired
	ProductService ProductService;
	
	@RequestMapping(value="/{ProductId}", method = RequestMethod.GET)
	public Response<?> getProductById(@PathVariable("ProductId")String ProductId) {
		try {
			return new Response<Product>(ResponseTypeEnum.SUCCESS,ProductService.getById(ProductId));
		}
		catch(Exception e) {
			return new Response<String>(ResponseTypeEnum.ERROR,e.getMessage());
		}
	}
	
	@RequestMapping(value="/", method = RequestMethod.POST)
	public Response<?> addNewProduct(@RequestBody Product Product) {
		try {
			return new Response<Product>(ResponseTypeEnum.SUCCESS,ProductService.addNewProduct(Product));
		}
		catch(Exception e) {
			return new Response<String>(ResponseTypeEnum.ERROR,e.getMessage()); 
		}
	}
	
	@RequestMapping(value="/{ProductId}", method = RequestMethod.PUT)
	public Response<?> updateProduct(@PathVariable("ProductId")String ProductId,@RequestBody Product Product) {
		try {
			return new Response<Product>(ResponseTypeEnum.SUCCESS,ProductService.updateProduct(ProductId, Product));
		}
		catch(Exception e) {
			return new Response<String>(ResponseTypeEnum.ERROR,e.getMessage());
		}
	}
	
	@RequestMapping(value="/{ProductId}", method = RequestMethod.DELETE)
	public Response<?> deleteProductById(@PathVariable("ProductId")String ProductId) {
		try {
			return new Response<Product>(ResponseTypeEnum.SUCCESS,ProductService.deleteProduct(ProductId));
		}
		catch(Exception e) {
			return new Response<String>(ResponseTypeEnum.ERROR,e.getMessage());
		}
	}

}
