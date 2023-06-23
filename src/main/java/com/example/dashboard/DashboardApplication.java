//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.springframework.boot:spring-boot-starter-web:3.1.0
//DEPS org.springframework.boot:spring-boot-starter-test:3.1.0
//DEPS org.springframework.boot:spring-boot-starter-thymeleaf:3.1.0
//DEPS org.springframework.boot:spring-boot-devtools:3.1.0

//SOURCES ./MessageItem.java
//SOURCES ./Chat.java

//FILES ../../resources/templates/index.html=index.html
//FILES resources/templates/fragments.html=fragments.html
//FILES resources/templates/main.css=main.css

package com.example.dashboard;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Controller
@RequestMapping("/")
public class DashboardApplication {

  public static void main(String[] args) {
    SpringApplication.run(DashboardApplication.class, args);
  }

  public List<MessageItem> messages = new ArrayList<>();
  public void addMessages(){
    messages.add(new MessageItem("Hello, How are you?",false));
    messages.add(new MessageItem("Hey, I am fine dude. What's about you?",true));
    messages.add(new MessageItem("I am good. Tell me when are you comming?",false));
    messages.add(new MessageItem("I am waiting for you.",false));
  }
  @GetMapping
  public String mainPage(Model model){
    addChat();
    addAttributeForIndex(model);
    return "index";
  }

//  @GetMapping("{message}")
//  public String newMessage(@PathVariable("message") String message){
//    messages.add(new MessageItem(message,false));
//    return "index";
//  }

  @GetMapping("main.css")
  public String addCSS(){
    return "main.css";
  }

  @PostMapping
  public String sendMessage(@ModelAttribute("item") MessageItem messageItem, Model model){
    System.out.println(messageItem);
    messages.add(messageItem);
    System.out.println(messages);
    return "redirect:/";
  }

  @PostMapping(headers = "HX-Request")
  public String htmxSendMessage(MessageItem messageItem,
                                Model model,
                                HttpServletResponse response){
    System.out.println(messages + "from htmx");
    System.out.println(messageItem);
    messages.add(messageItem);
    model.addAttribute("item", messageItem);

    response.setHeader("HX-Trigger", "itemAdded");

    return "fragments :: meassageItem";
  }


  private void addAttributeForIndex(Model model){
    model.addAttribute("item", new MessageItem());
    model.addAttribute("messages",messages);
    model.addAttribute("chats",chats);
    model.addAttribute("chat",new Chat());
    model.addAttribute("currentId",1);
  }

  @GetMapping("/responce")
  public String reply(Model model){

    MessageItem messageItem = new MessageItem("Hello from AI", true);
    model.addAttribute("item", messageItem);
    messages.add(messageItem);

    return "fragments :: meassageItem";
  }

  public void addMes(){
    int id = 3;
    Chat currentChat = chats.get(id-1);
    List<MessageItem> ms = currentChat.chatHistory;

    ms.add(new MessageItem("Hello",false));

  }

  public void addChat(){
    chats.add(new Chat("Chat 1",new ArrayList<MessageItem>()));
    chats.add(new Chat("Chat 2",new ArrayList<MessageItem>()));
    chats.add(new Chat("Chat 3",new ArrayList<MessageItem>()));
    chats.add(new Chat("Chat 4",new ArrayList<MessageItem>()));
  }

  public List<Chat> chats = new ArrayList<>();
}
