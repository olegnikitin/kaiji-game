package com.softserveinc.ita.kaiji.web.controller;

import com.softserveinc.ita.kaiji.chat.ChatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ievgen_s
 * @since 13.03.14
 */

@Controller
public class StartPageController {

    @RequestMapping("/*")
    public String getStartPage() {
        return "start-page";
    }

    @RequestMapping("/statistic/user")
    public String getStatisticPage() {
        return "statistics";
    }

    @RequestMapping("/gamechat/{nickname}")
    public String getChatPage(@PathVariable("nickname")String nickname ,Model model) {
        ChatUtils.getUnReadMessages().put(nickname,false);
        model.addAttribute("messages",ChatUtils.getMessages());
        return "chat";
    }
}
