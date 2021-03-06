package webstationapi.Service;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import webstationapi.Entity.Cart;
import webstationapi.Entity.User;
import webstationapi.Repository.CartRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserService userService;


    @Transactional
    public Cart findByUserId(int userId) {
        User user = this.userService.findById(userId);
        if (user == null)
            return null;
        return cartRepository.findByUser(user);
    }

    @Transactional
    public void addCartOneElement(int userid, int idflat) {
        User user = this.userService.findById(userid);
        if (user == null)
            return;
        Cart cart = this.findByUserId(userid);

        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.addFlatId(idflat);
        } else {
        	if (!cart.getFlatIds().contains(idflat)) {
        		cart.addFlatId(idflat);
        	}
        }
        cartRepository.save(cart);
    }

    @Transactional
    public void addCartElements(int userid, Collection<Integer> elements) {
        User user = this.userService.findById(userid);
        if (user == null) {
            return;
        }
        Cart cart = this.findByUserId(userid);

        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.setFlatIds(elements);
        } else {
            cart.setFlatIds(elements);
        }
        cartRepository.save(cart);
    }

    @Transactional
    public void removeElements(int userid, Collection<Integer> elements) {
    	 User user = this.userService.findById(userid);
         if (user == null) {
             return;
         }
         Cart cart = this.findByUserId(userid);

         if (cart == null) {
             return;
         } else {
             for (Integer flatId : elements) {
            	 cart.getFlatIds().remove(flatId);
             }
         }
         cartRepository.save(cart);
    }

    @Transactional
    public void delete(int iduser) {
        Cart cart = this.findByUserId(iduser);
        if (cart == null)
            return;
        cartRepository.delete(cart);
    }
    
    @Transactional
    public void createNewCart(User user) {
    	Cart cart = new Cart();
    	cart.setUser(user);
    	cart.setFlatIds(new ArrayList<Integer>());
    	cartRepository.save(cart);
    }
}
