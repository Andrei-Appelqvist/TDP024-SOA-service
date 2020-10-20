// package hello;
//
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.kafka.core.KafkaTemplate;
//
// @RestController
// public class KafkaController {
//   @Autowired
//   KafkaTemplate<String, String> kafkaTemplate;
//
//   private static final String TOPIC = "NewTopic";
//
//   @GetMapping("/publish/{message}")
//   public String publishMessage(@PathVariable("message") final String message){
//     kafkaTemplate.send(TOPIC, message);
//     return "Published succesfully";
//   }
// }
