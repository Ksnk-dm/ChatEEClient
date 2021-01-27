package ua.kiev.prog;


import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        ActiveUser.setLogin("");
        ActiveUser.setStatus("");
        ActiveUser.setRoom("");
        Scanner scanner = new Scanner(System.in);
        Utils.menu(scanner);
        }
    }


