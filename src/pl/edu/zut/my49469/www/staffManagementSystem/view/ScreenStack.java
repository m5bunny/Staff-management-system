package pl.edu.zut.my49469.www.staffManagementSystem.view;

import java.util.Stack;

public class ScreenStack
{
    private static ScreenStack instance;
    private Stack<Screen> stack;

    private ScreenStack()
    {
        stack = new Stack<>();
    }

    public static ScreenStack getInstance()
    {
        if (instance == null)
            instance = new ScreenStack();
        return instance;
    }

    public void pushScreen(Screen s)
    {
        stack.push(s);
    }
    public char actCurrent() throws Exception
    {
        System.out.print("\033[H\033[2J");
        stack.get(stack.size() - 1).show();
        System.out.print(":");
        return stack.get(stack.size() - 1).getInput();
    }
    public void addError(String e)
    {
        stack.get(stack.size() - 1).setError(e);
    }
    public void popCurrent()
    {
        stack.pop();
        stack.get(stack.size() - 1).setError("");
    }
}
