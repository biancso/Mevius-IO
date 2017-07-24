
 # Mevius
 Effective and easy Socket server&amp;client api for java
 
 # MeviusPacket
 We use our own Object-Oriented-Packet, It will helps you to control your packets more easily.
 Texts, Images, even Files! nothing matter!

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

# Donation

My bitcoin addr : 1NDGV92UPmgH9wK6b9aHdz6BJPPJsmp6x5
