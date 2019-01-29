package mock.powermock;

public class UserService {
    public String getUserName(int id) {
        System.out.println("calling UserService.getUserName(id)");
        return "hello";
    }

    public void handle(String s) {
        System.out.println("handle(): ..." + s);
    }
}
