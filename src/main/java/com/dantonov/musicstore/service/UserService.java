
package com.dantonov.musicstore.service;

import com.dantonov.musicstore.entity.TradeHistory;
import com.dantonov.musicstore.entity.User;
import com.dantonov.musicstore.exception.EmailAlreadyExistsException;
import com.dantonov.musicstore.exception.LoginAlreadyExistsException;
import com.dantonov.musicstore.exception.NotEnoughMoneyException;
import com.dantonov.musicstore.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */

@Service
public class UserService {
    
    private static final Integer ADD_CASH_ACTION = 2;
    private static final Integer DISCOUNT_CASH_ACTION = 3;
    
    
    @Autowired
    protected UserRepository userRepository;
    
    @Autowired
    protected ActionService actionService;
    
    @Autowired
    protected TradeHistoryService historyService;
    
    
    
    public User findById(UUID id) {
        return userRepository.findOne(id);
    }
    
    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Transactional
    public void addCash(User user, BigDecimal cash) {
        
        user.setWallet(user.getWallet().add(cash));
        userRepository.save(user);
        
        TradeHistory history = new TradeHistory();
        history.setUser(user);
        history.setPrice(cash);
        history.setAction(actionService.findById(ADD_CASH_ACTION));
        history.setDatetime(new Date());
        historyService.save(history);
    }
    
    @Transactional
    public void discountCash(User user, BigDecimal cash) throws NotEnoughMoneyException {
        if (user.getWallet().compareTo(cash) < 0) {
            throw new NotEnoughMoneyException("Не достаточно денег.");
        }
        user.setWallet(user.getWallet().subtract(cash));
        userRepository.save(user);
        
        TradeHistory history = new TradeHistory();
        history.setUser(user);
        history.setPrice(cash);
        history.setAction(actionService.findById(DISCOUNT_CASH_ACTION));
        history.setDatetime(new Date());
        historyService.save(history);
        
    }
    
    @Transactional
    public User save(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new EmailAlreadyExistsException("Пользователь с email " + user.getEmail() + " уже существует.");
        }
        if (userRepository.findByLogin(user.getLogin()) != null) {
            throw new LoginAlreadyExistsException("Пользователь с логином " + user.getLogin() + " уже существует.");
        }
        user.setWallet(BigDecimal.ZERO);
        return userRepository.save(user);
    }
    
    
    @Transactional
    public User update(User user) {
        return userRepository.save(user);
    }
    
}
