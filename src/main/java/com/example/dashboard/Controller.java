package com.example.dashboard;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Controller
@RequestMapping("/")
public class Controller {

  @Autowired
  private WebClient webClient;
  public List<MessageItem> messages = new ArrayList<>();

  private String Url = "http://localhost:8003/v1/wiki/summary?query=";
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

      WikiResponse wikiResponse = webClient.get()
              .uri(Url + messages.get(messages.size()-1).getMessage())
              .retrieve()
              .bodyToMono(WikiResponse.class)
              .block();

    MessageItem messageItem = new MessageItem(wikiResponse.getResponse(), true);
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
