
# Mevius
 Effective and easy Socket Server&Client library for java
 
# MeviusPacket
 We use our own Object-Oriented-Packet, It will helps you to control your packets more easily.
 Texts, Images, even Files! nothing matter!
  
# How to create your MeviusServer?
  ```java
  MeviusServer server = new MeviusServer(YOUR_PORT);
  server.start();
  ```
# How to create Listener?
  
  ```java
  public class TestConnectionListener implements ConnectionListener {
  
  @ConnectionHandler(ConnectionType)
  public void your_method_name(MeviusClient client) {
  // on ConnectionType event
  }
  }
  ```
  
  ```java
  public class TestPacketListener implements PacketListener {
  
  @PacketHandler(PacketEventType)
  public void your_method_name(PacketEvent event) {
  // on PacketEventType
  }
  ```
 # How to catch Events?
 ```java
 server.getHandler().registerConnectionListener(ConnectionListener);
 server.getHandler().registerPacketListener(PacketListener);
 ```
# Donation

My bitcoin addr : 1NDGV92UPmgH9wK6b9aHdz6BJPPJsmp6x5
