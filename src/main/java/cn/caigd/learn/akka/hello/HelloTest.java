package cn.caigd.learn.akka.hello;

public class HelloTest {
    public static void main(String[] args) {
        akka.Main.main(new String[] { HelloWorld.class.getName() });
//        ActorSystem system = ActorSystem.create("Hello");
//        ActorRef a = system.actorOf(Props.create(HelloWorld.class), "helloWorld");
//        System.out.println(a.path());
    }
}
