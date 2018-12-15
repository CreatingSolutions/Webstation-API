package webstationapi.Controller;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import webstationapi.DTO.CartDTO;
import webstationapi.Entity.Cart;
import webstationapi.Entity.Flat;
import webstationapi.Service.CartService;

@RestController
@RequestMapping(path = "/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@GetMapping
    public CartDTO retrieveCart(int userId) {
    	CartDTO result = new CartDTO();

    	Collection<Flat> flats = new ArrayList<Flat>();
    	Cart cart = cartService.findByUserId(userId);

    	if (cart == null)
    		return new CartDTO();

    	for (int flatId : cart.getFlatIds()) {
    		RestTemplate restTemplate = new RestTemplate();
			Flat flt = restTemplate.getForObject("http://51.75.140.39:8083/flat/{id}", Flat.class, flatId);
    		flats.add(flt);
    	}

		result.setUserid(cart.getUser().getId());
    	result.setFlats(flats);
    	return result;
    }

    @PostMapping(path = "/addOne")
    public void addCart(@RequestParam int userId, @RequestParam int flatId){
		cartService.addCartOneElement(userId, flatId);
	}

    @PostMapping(path = "/removeElements")
    public void removeElement(@RequestParam int userId, @RequestParam Collection<Integer> flatIds) {
    	cartService.removeElements(userId, flatIds);
    }

	@PostMapping(path = "/addElements")
	public void addCartM(@RequestParam int userId, @RequestParam Collection<Integer> flatId){
		cartService.addCartElements(userId, flatId);
	}

	@PostMapping(path = "/validate")
	public void validatePanier(@RequestParam int userId){
		/* Ici, il faut :
		 * - Valider le paiement,
		 * - Enregistrer les réservations en base,
		 * - Supprimer le panier pour actualiser le front si et seulement si tout s'est bien passé. */
		cartService.delete(userId);
	}

	@PostMapping(path = "/delete")
	public void deletePanier(@RequestParam int userId){
		cartService.delete(userId);
	}


}
