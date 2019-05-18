package ClientSide;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class API_Tester {
    static ClientAPI api;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        api = new ClientAPI();

        System.out.println("1 for login attempt");
        System.out.println("2 for login attempt with account creation enabled");
        System.out.println("3 for broadcase message");
        System.out.println("4 for get all user details");
        System.out.println("5 for get all usernames");
        System.out.println("6 for get online user details");
        System.out.println("7 for get online usernames");
        System.out.println("8 for get recent user details");
        System.out.println("9 for get recent usernames");
        System.out.println("10 for refresh personal inbox");
        System.out.println("11 for refresh all chat");
        System.out.println("12 for send message");
        System.out.println("13 for get user details");


        while (true) {
            System.out.print("Choice: ");
            int choice = Integer.parseInt(br.readLine().trim());

            switch (choice) {
                case 1:
                    System.out.println(api.attemptLogin(br.readLine(), br.readLine()));
                    break;
                case 2:
                    System.out.println(api.attemptLogin(br.readLine(), br.readLine(),
                            br.readLine().equals("1")));
                    break;
                case 3:
                    api.broadCastMessage(br.readLine());
                    break;
                case 4:
                    System.out.println(api.getAllUserDetails());
                    break;
                case 5:
                    System.out.println(api.getAllUsernames());
                    break;
                case 6:
                    System.out.println(api.getOnlineUserDetails());
                    break;
                case 7:
                    System.out.println(api.getOnlineUsernames());
                    break;
                case 8:
                    System.out.println(api.getRecentUserDetails());
                    break;
                case 9:
                    System.out.println(api.getRecentUsernames());
                    break;
                case 10:
                    System.out.println(api.getPersonalChatHistory(br.readLine()));
                    break;
                case 11:
                    System.out.println(api.getAllChatHistory());
                    break;
                case 12:
                    api.sendMessage(br.readLine(), br.readLine());
                    break;
                case 13:
                    System.out.println(api.getUserDetails(br.readLine()));
                    break;
                default:
                    System.out.println("Invalid choice !!!");
            }
        }
    }
}
