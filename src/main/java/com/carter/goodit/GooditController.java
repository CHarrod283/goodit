package com.carter.goodit;

import com.carter.goodit.group.Group;
import com.carter.goodit.group.GroupService;
import com.carter.goodit.post.Post;
import com.carter.goodit.post.PostService;
import com.carter.goodit.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Controller
public class GooditController {

    @Autowired
    DataSource dataSource;

    UserService userService;
    GroupService groupService;
    PostService postService;

    @PostConstruct
    void initialize() {
        userService = new UserService(dataSource);
        groupService = new GroupService(dataSource);
        postService = new PostService(dataSource);
    }


    @GetMapping("/")
    public ModelAndView home(Map<String, Object> model, HttpSession httpSession) {
        model.put("login_error", "");
        httpSession.setAttribute("signing_up", "yes");
        return new ModelAndView("login", model);
    }

    @PostMapping("/login")
    public ModelAndView saveMessage(@RequestParam("name") String username, Map<String, Object> model, HttpServletRequest httpServletRequest) {
        if(userService.login(username)) {
            httpServletRequest.getSession().setAttribute("username", username);
            httpServletRequest.getSession().setAttribute("home_text", "Welcome "+ username);
            return new ModelAndView("redirect:/home", model);
        }

        model.put("login_error", "Invalid Username");
        return new ModelAndView("login", model);
    }

    @GetMapping("/signup")
    public ModelAndView signup(Map<String, Object> model, HttpSession session) {
        model.put("signup_error", session.getAttribute("signup_error"));
        return new ModelAndView("create/user", model);
    }

    @PostMapping("/signup")
    public ModelAndView createUser(Map<String, Object> model, HttpServletRequest httpServletRequest, @RequestParam String username) {
        if(username==""){
            model.put("signup_error", "");
            return new ModelAndView("create/user", model);
        }
        if (!userService.signup(username)) {
            model.put("signup_error", "Invalid name, already taken");
            return new ModelAndView("create/user", model);
        }
        httpServletRequest.getSession().setAttribute("login_error", "User created");
        return new ModelAndView("redirect:/", model);
    }

    @PostMapping("/gohome")
    public ModelAndView goHome(Map<String, Object> model, HttpServletRequest httpServletRequest) {
        model.put("home_text", "Welcome home");
        return new ModelAndView("redirect:/home", model);
    }

    @GetMapping("/home")
    public ModelAndView showHomePage(Map<String, Object> model, HttpSession session) {
        model.put("home_text", session.getAttribute("home_text"));
        model.put("group",  groupService.getGroupsByUser( (String) session.getAttribute("username")));
        model.put("post", postService.getPosts((String) session.getAttribute("username")));
        return new ModelAndView("home", model);
    }

    @PostMapping("/delete")
    public String deleteSession(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().invalidate();
        return "redirect:/";
    }

    @GetMapping("/create/group")
    public ModelAndView createGroup(Map<String, Object> model, HttpSession session) {
        model.put("create_group_error", "");
        return new ModelAndView("create/group", model);
    }

    @PostMapping("/create/group")
    public ModelAndView createGroup(Map<String, Object> model, HttpServletRequest httpServletRequest,  @RequestParam String name) {
        if(groupService.getGroupByName(name) != null) {
            model.put("create_group_error", "group already exists!");
            return new ModelAndView("create/group", model);
        }
        groupService.createGroup(name, (String) httpServletRequest.getSession().getAttribute("username"));
        httpServletRequest.getSession().setAttribute("home_text",
                (String) httpServletRequest.getSession().getAttribute("username") +
                        " created group "
                + name
                );
        return new ModelAndView("redirect:/home", model);
    }

    @GetMapping("/create/post")
    public ModelAndView createPost(Map<String, Object> model, HttpSession session) {
        model.put("create_post_error", "");
        return new ModelAndView("create/post", model);
    }

    @PostMapping("/create/post")
    public ModelAndView createPost(Map<String, Object> model, HttpServletRequest httpServletRequest,  @RequestParam String gname, @RequestParam String title, @RequestParam String text) {
        if(groupService.getGroupByName(gname) == null) {
            model.put("create_group_error", "group doesnt exist!");
            return new ModelAndView("create/post", model);
        }
        httpServletRequest.getSession().setAttribute("home_text",
                httpServletRequest.getSession().getAttribute("username") +
                        " created post "

                );
        postService.createPost(
                gname,
                (String) httpServletRequest.getSession().getAttribute("username"),
                title,
                text
        );
        return new ModelAndView("redirect:/home", model);
    }


    @GetMapping("/search")
    public ModelAndView searchGroup(Map<String, Object> model, HttpSession session, @RequestParam String query) {
        List<Group> groups = groupService.searchGroups(query);
        model.put("group", groups);
        return new ModelAndView("search", model);
    }

    @PostMapping("/join")
    public ModelAndView joinGroup(Map<String, Object> model, HttpServletRequest httpServletRequest, @RequestParam String name) {
        groupService.joinGroup(
                name,
                (String) httpServletRequest.getSession().getAttribute("username")
        );
        httpServletRequest.getSession().setAttribute("home_text",
                httpServletRequest.getSession().getAttribute("username") + " joined " + name);
        return new ModelAndView("redirect:/home", model);
    }

    @GetMapping("/view")
    public ModelAndView viewGroup(Map<String, Object> model, HttpSession session, @RequestParam String name){
        List<Post> posts = postService.getPostsByGroup(name);
        Group group = groupService.getGroupByName(name);
        model.put("group", group);
        model.put("post", posts);
        session.setAttribute("current_group", name);
        return new ModelAndView("group", model);
    }

    @PostMapping("/home/like")
    public ModelAndView likePost(Map<String, Object> model, HttpServletRequest httpServletRequest, @RequestParam int id) {
        String username = (String) httpServletRequest.getSession().getAttribute("username");
        postService.likePost(id, username);
        httpServletRequest.getSession().setAttribute("home_text", "liked post");
        return new ModelAndView("redirect:/home", model);
    }

    @PostMapping("/group/like")
    public ModelAndView likeGroupPost(Map<String, Object> model, HttpServletRequest httpServletRequest, @RequestParam int id) {
        String username = (String) httpServletRequest.getSession().getAttribute("username");
        postService.likePost(id, username);
        httpServletRequest.getSession().setAttribute("home_text", "liked post");
        String group = (String) httpServletRequest.getSession().getAttribute("current_group");
        return new ModelAndView("redirect:/view?name=" + group + "&view=", model);
    }

    @PostMapping("/home/dislike")
    public ModelAndView dislikePost(Map<String, Object> model, HttpServletRequest httpServletRequest, @RequestParam int id) {
        String username = (String) httpServletRequest.getSession().getAttribute("username");
        postService.dislikePost(id, username);
        httpServletRequest.getSession().setAttribute("home_text", "disliked post");
        return new ModelAndView("redirect:/home", model);
    }
    @PostMapping("/group/dislike")
    public ModelAndView dislikeGroupPost(Map<String, Object> model, HttpServletRequest httpServletRequest, @RequestParam int id) {
        String username = (String) httpServletRequest.getSession().getAttribute("username");
        postService.dislikePost(id, username);
        httpServletRequest.getSession().setAttribute("home_text", "disliked post");
        String group = (String) httpServletRequest.getSession().getAttribute("current_group");
        return new ModelAndView("redirect:/view?name=" + group + "&view=", model);
    }

}
