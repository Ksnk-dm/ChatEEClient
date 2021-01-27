package ua.kiev.prog;

public class ActiveUser {
    private static String login;
    private static String room;
    private static String status;

    public ActiveUser() {
    }

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        ActiveUser.login = login;
    }

    public static String getRoom() {
        return room;
    }

    public static void setRoom(String room) {
        ActiveUser.room = room;
    }

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        ActiveUser.status = status;
    }
}
