package com.example.dashboard;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Controller
@RequestMapping("/")
public class Controller {

  private StringBuilder currentContent;

  @Autowired
  private WebClient webClient;
  public List<MessageItem> messages = new ArrayList<>();

  private String Url = "http://localhost:8003/v1/sse/wiki/summary?query=";
  @GetMapping
  public String mainPage(Model model){
    addChat();
    addAttributeForIndex(model);
    return "index";
  }

  @GetMapping("main.css")
  public String addCSS(){

    System.out.println("css");
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
    System.out.println("Add Attribute For Index");
    model.addAttribute("item", new MessageItem());
    model.addAttribute("messages",messages);
    model.addAttribute("chats",chats);
    model.addAttribute("chat",new Chat());
    model.addAttribute("currentId",1);
  }

  @GetMapping("/responce")
  public String reply(Model model){
    System.out.println("reply");
    currentContent = new StringBuilder("");
    MessageItem messageItem = new MessageItem("", true);
    model.addAttribute("item", messageItem);
//    messages.add(messageItem);

    return "fragments :: meassageItem";
  }

  @GetMapping(value = "/sse")
  public Flux sseMethod(){

//    Flux<ServerSentEvent<WikiResponse>> eventStream = webClient.get()
//            .uri(Url + messages.get(messages.size()-1).getMessage())
//            .retrieve()
//            .bodyToFlux(type);

    return webClient.get()
            .uri(Url + messages.get(messages.size()-1).getMessage())
            .retrieve()
            .bodyToFlux(WikiResponse.class)
            .delayElements(Duration.ofMillis(100))
            .filter(it -> !it.getResponse().equals("OpenAI Chat Completion Finished"))
            .map(it -> currentContent.append(it.getResponse()));

//      eventStream.subscribe(
//            content -> {
//
//            },
//            error -> {},
//            () -> {}
//    );
  }


  public void addChat(){
    chats.add(new Chat("Chat 1",new ArrayList<MessageItem>()));
    chats.add(new Chat("Chat 2",new ArrayList<MessageItem>()));
    chats.add(new Chat("Chat 3",new ArrayList<MessageItem>()));
    chats.add(new Chat("Chat 4",new ArrayList<MessageItem>()));
  }

  public List<Chat> chats = new ArrayList<>();


  //  public void addMes(){
//    int id = 3;
//    Chat currentChat = chats.get(id-1);
//    List<MessageItem> ms = currentChat.chatHistory;
//
//    ms.add(new MessageItem("Hello",false));
//
//}

}
