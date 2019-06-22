# GOSSup

The GOSSup is a project that utilizes features only included in java 8, so please upgrade if you are on 1.7 or lower.

#### 1. Purpose
This code repository implements a secure instant message system between a client and server program.

#### 2. features:
##### 2.1 Confidentiality
Confidentiality is ensuring that information is secure during transfer.

###### 2.2 Encryption and Decryption
This feature is going to be available soon.

##### 2.3 Integrity
Integrity is ensuring data has not been tampered with during transfer.

##### 2.4 Authentication
Authentication is ensuring the person sending data is who they say they are.

### 3. Instructions
1. Navigate to: `<root-to-directory>/GOSSup`
2. Compile the program: `javac *.java`
3. Start the server: `java Server`
4. Start the client: `java Client localhost`
5. If IP for the server/client do not match connection will fail


##### Successful protocol validation:


![](gossup.png)

4. You can now send messages from client to server and vice versa, behind the scenes we are soon going to implement the security requirements above are used to ensure that the chosen settings are enforced. You will be prompted to four options there like press 1 to send message, press2 to refresh all chat history and press 3 to refresh personal chat history else press E to exit. To send4message put your pc IP address and voila! you are good to send messages to your dear ones.

5. To safely close the connection from the client's side, type `E` and send it to the server.


#### 6. Bug Report :  
If you find any bug, please report it at sarthakmannaofficial@gmail.com

That's all! thank you for your precious time!! :D
