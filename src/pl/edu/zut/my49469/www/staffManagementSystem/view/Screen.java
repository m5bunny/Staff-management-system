package pl.edu.zut.my49469.www.staffManagementSystem.view;

import java.util.Scanner;
import java.util.TreeMap;

public class Screen
{
    private String title;
    private String error;
    private String control;
    private final Content main;

    public Screen(String _title, String _error, String _control, Content _main)
    {
        title = _title;
        error = _error;
        control = _control;
        main = _main;
    }

    public void setTitle(String _title) {
        title = _title;
    }
    public void setError(String _error) {
        error = _error;
    }
    public void setControl(String _control) {
        control = _control;
    }

    public void show() throws Exception
    {
        System.out.println(title);
        System.out.println(error);
        main.show();
        System.out.println(control);
    }

    public char getInput()
    {
        Scanner scanner = new Scanner(System.in);
        return scanner.next().charAt(0);
    }
}
