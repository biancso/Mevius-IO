
 # Mevius
 Effective and easy Socket Server&Client library for java
 
 # MeviusPacket
 We use our own Object-Oriented-Packet, It will helps you to control your packets more easily.
 Texts, Images, even Files! nothing matter!

 # 2017-07-28 08:00 KST Updated!
  From now, you can controll the packet more effectivly!
  ```java
  @EventMethod
  public void yourmethodname(TextPacketEvent e) {
  System.out.println("New TextPacket has arrive!");
  System.out.println("data : " + e.getPacket().toString());
  }
  ```
  Just like this,
  MeviusInputStream and MeviusOutputStream has been deprecated!
  
  >> To use PacketHandler in MeviusClient
     Attath PacketHandler to your client
     
     ```java
     PacketHandler ph = new PacketHandler();
     MeviusClient client = new MeviusClient(InetAddress, port, PacketHandler);
     client.start();
     ph.registerListener(PacketListener);
     ```
     
     
 # MeviusCipher
 > Examples
 >> For AES256
   ```java
   MeviusCipherKey key = MeviusCipherKey.randomAES256Key();
   MeviusCipher cipher = new MeviusCipher(key, MeviusCipherAction.DECRYPT, STRING_TO_ENCRYPT);
   cipher.toString(); // cipher.toByte() also works for byte[]
   ```
 >> For RSA
   ```java
   MeviusCipherKey key = MeviusCipherKey.randomRSAKeyPair(512); // It will return KeyPair
   MeviusCipher cipher = new MeviusCipher(key, MeviusCipherAction.ENCRYPT, "TEST");
   cipher.toString(); // cipher.toByte() also works for byte[]
   ```
 >> You can also store your RSAKeyPair with my Configuration or StringKeyPair
# Donation

My bitcoin addr : 1NDGV92UPmgH9wK6b9aHdz6BJPPJsmp6x5
