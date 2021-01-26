package ua.kiev.prog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class Utils {
    private static final String URL = "http://127.0.0.1";
    private static final int PORT = 8080;

    public static String getURL() {
        return URL + ":" + PORT;
    }

    private static String bodyMes(String message) {
        return message;
    }

    private static void sendMessage(String message, String to, boolean isService) {
        String body = message;
        String from = Main.login;
        if (!isService) {
            body = bodyMes(message);
        } else {
            from = "system";
        }

        Message m = new Message(from, to, body, isService);
        try {
            int res = m.send(Utils.getURL() + "/add");
            if (res != 200) {
                System.out.println("error: " + res);
                return;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private static void allUsers() {
        try {
            URL obj = new URL(Utils.getURL() + "/alluser");
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            int res = conn.getResponseCode();
            String messageFromServer = getMessageFromServer(conn);
            System.out.println(messageFromServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getMessageFromServer(HttpURLConnection conn) throws IOException {
        InputStream is = conn.getInputStream();
        byte[] buf = requestBodyToArray(is);
        String strBuf = new String(buf, StandardCharsets.UTF_8);
        return strBuf;
    }

    private static void login(String login, String pass) {

        try {
            URL obj = new URL(Utils.getURL() + "/login?login=" + login + "&password=" + pass);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            int res = conn.getResponseCode();
            if (res == 200) {
                Main.login = login;
                Main.room = "main";
                Main.status = "online";
                System.out.println("Добро пожаловать " + Main.login);
            } else {
                String messageFromServer = getMessageFromServer(conn);
                System.out.println(messageFromServer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void logout() {
        boolean success = false;
        try {
            URL obj = new URL(Utils.getURL() + "/logout?login=" + Main.login);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            int res = conn.getResponseCode();
            if (res == 200) {
                success = true;
            } else {
                String messageFromServer = getMessageFromServer(conn);
                System.out.println(messageFromServer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (success) {
            String mes = Main.login + " вышел";
            Main.login = "";
            System.out.println(mes);

        }
    }

    private static void registration(String login, String pass) {

        try {
            URL obj = new URL(Utils.getURL() + "/reg?login=" + login + "&password=" + pass);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            int res = conn.getResponseCode();
            String messageFromServer = getMessageFromServer(conn);
            System.out.println(messageFromServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] requestBodyToArray(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;
        do {
            r = is.read(buf);
            if (r > 0) bos.write(buf, 0, r);
        } while (r != -1);

        return bos.toByteArray();
    }
public static void help(){
        String text = "\nМеню:" +
                "\nСделайте выбор" +
                "\n"+
                "\n1)Регистрация" +
                "\n2)Авторизация" +
                "\n3)Войти в главный чат" +
                "\n4)Посмотреть всех пользователей" +
                "\n5)Выйти ";

        System.out.println(text);
}

    public static void helpChat(){
        String text="\nДля отправки сообщений в приват пользователю используйте @<Логин>" +
                "\nДля выхода с чата введите \"0\"" +
                "\nДля вызова помощи введи \"help\"";
        System.out.println(text);
    }


    public static void menu(Scanner scanner) {
        while (true) {
            help();
            String text = scanner.nextLine();

            if (text.equals("1")) {
                System.out.println("Введите логин");
                String login = scanner.nextLine();
                System.out.println("Введите пароль");
                String pass = scanner.nextLine();
                Utils.registration(login, pass);
                continue;
            }
            if (text.equals("2")) {
                System.out.println("Введите логин");
                String login = scanner.nextLine();
                System.out.println("Введите пароль");
                String pass = scanner.nextLine();
                login(login, pass);
                continue;
            }
            if (text.equals("3")) {
                sendMessage(Main.login + " вошел в главный чат", "system", true);
                mesUser(scanner);
            }
            if (text.equals("5")) {
                logout();
            }
            if (text.equals("4")) {
                if (Main.login.isEmpty()) {
                    System.out.println("Пройдите авторизацию или регистрацию");
                } else {
                    allUsers();
                }
            }
        }
    }


    private static void mesUser(Scanner scanner) {
        if (Main.login.isEmpty()) {
            System.out.println("Пройдите авторизацию или регистрацию");
        } else {
            helpChat();
            Thread th = new Thread(new GetThread());
            th.setDaemon(true);
            th.start();
            while (true) {
                String mes = scanner.nextLine();
                if (mes.equals("0")) {
                    break;
                }
                if (mes.equals("help")){
                    helpChat();
                }
                if (mes.contains("@")) {
                    String to = mes.contains(" ") ? mes.substring(mes.indexOf("@") + 1, mes.indexOf(" "))
                            : mes.substring(mes.indexOf("@") + 1);
                    Utils.sendMessage(mes, to, false);
                } else {
                    Utils.sendMessage(mes, Main.room, false);
                }
            }

        }
    }
}





