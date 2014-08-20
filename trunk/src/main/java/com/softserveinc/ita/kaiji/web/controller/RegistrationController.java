package com.softserveinc.ita.kaiji.web.controller;

import com.softserveinc.ita.kaiji.dao.UserDAO;
import com.softserveinc.ita.kaiji.dto.UserRegistrationDto;
import com.softserveinc.ita.kaiji.model.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * @author Vladyslav Shelest
 * @version 1.0
 * @since 22.04.14.
 */
@Controller
public class RegistrationController {

    private static final Logger LOG = Logger.getLogger(RegistrationController.class);

    @Autowired
    private UserRegistrationDto userDto;
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping(value = "/registration" , method = RequestMethod.GET)
    public String getRegistrationForm(Model model) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("getRegistrationForm");
        }
        model.addAttribute("userDto", userDto);
        return "registration-form";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String receiveFormModel(@ModelAttribute("userDto") @Valid UserRegistrationDto userDto,
                                   BindingResult br,
                                   Model model) {

        if (LOG.isTraceEnabled()) {
            LOG.trace("receiveFormModel");
        }

        if (br.hasErrors()) {
            if (LOG.isInfoEnabled()) {
                LOG.info("errors in the dto object");
            }
            return "registration-form";
        }

        if (userDAO.findByEmail(userDto.getEmail()) == null &&
                userDAO.findByNickname(userDto.getNickname()) == null) {

            User user = new User();
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setNickname(userDto.getNickname());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));

            if (LOG.isInfoEnabled()) {
                LOG.info("saving user to the database");
            }

            userDAO.save(user);

            model.addAttribute("notification", "You have successfully registered, now log in");

            return "login";

        } else {

            if (LOG.isInfoEnabled()) {
                LOG.info("User with such nickname or email already exists. Returning back to 'registration-form'");
            }

            model.addAttribute("notification", "User with such nickname or email already exists.");

            return "registration-form";
        }


    }
}
