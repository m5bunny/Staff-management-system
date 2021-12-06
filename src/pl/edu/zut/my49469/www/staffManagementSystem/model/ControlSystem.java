package pl.edu.zut.my49469.www.staffManagementSystem.model;

import java.io.Serializable;
import java.util.HashMap;

public class ControlSystem
{
    private static ControlSystem instance;
    private HashMap<String, Worker> workersHashMap;

    private ControlSystem()
    {
        workersHashMap = new HashMap<>();
    }

    public void addWorker(String _id, String _firstName, String _lastName, long _salary, long _businessPhone,
                     String _position, int _commission, long _commissionLimit)
    {
        workersHashMap.put(_id, new Seller(_id, _firstName, _lastName, _salary, _businessPhone, _position, _commission, _commissionLimit));
    }

    public void addWorker(String _id, String _firstName, String _lastName, long _salary, long _businessPhone,
                 long _serviceBonus, String _cartNumber, long _expenseLimit)
    {
        workersHashMap.put(_id, new Director(_id, _firstName, _lastName, _salary, _businessPhone, _serviceBonus, _cartNumber, _expenseLimit));
    }

    public static ControlSystem getInstance()
    {
        if (instance == null)
            instance = new ControlSystem();
        return instance;
    }
    public HashMap<String, Worker> getWorkersHashMap() {
        return workersHashMap;
    }
}

abstract class Worker implements Serializable
{
    final private String id;
    private String firstName;
    private String lastName;
    private long salary;
    private long businessPhone;

    Worker(String _id, String _firstName, String _lastName, long _salary, long _businessPhone)
    {
        id = _id;
        firstName = _firstName;
        lastName = _lastName;
        salary = _salary;
        businessPhone = _businessPhone;
    }
    final public void setSalary(long _salary)
    {
        salary = _salary;
    }
    final public void setFirstName(String _firstName)
    {
        firstName = _firstName;
    }
    final public void setSecondName(String _lastName)
    {
        lastName = _lastName;
    }
    final public void setBusinessPhone(long _businessPhone)
    {
        businessPhone = _businessPhone;
    }

    final public String getId()
    {
        return id;
    }
    final public String getFirstName()
    {
        return firstName;
    }
    final public String getLastName()
    {
        return lastName;
    }
    final public long getSalary()
    {
        return salary;
    }
    final public long getBusinessPhone()
    {
        return businessPhone;
    }

    public abstract String getInfo();
}

class Seller extends Worker
{
    private String position;
    private int commission;
    private long commissionLimit;

    public Seller(String _id, String _firstName, String _lastName, long _salary, long _businessPhone,
           String _position, int _commission, long _commissionLimit)
    {
        super(_id, _firstName, _lastName, _salary, _businessPhone);
        position = _position;
        commission = _commission;
        commissionLimit = _commissionLimit;
    }

    public void setPosition(String _position)
    {
        position = _position;
    }
    public void setCommission(int _commission)
    {
        commission = _commission;
    }
    public void setCommissionLimit(long _commissionLimit)
    {
        commissionLimit = _commissionLimit;
    }

    public String getPosition()
    {
        return position;
    }
    public int getCommission()
    {
        return commission;
    }
    public long getCommissionLimit()
    {
        return commissionLimit;
    }
    public String getInfo()
    {
        return new String(
                "id\t:\t" + getId() + "\n" +
                        "First name\t:\t" + getFirstName() + "\n" +
                        "Last name\t:\t" + getLastName() + "\n" +
                        "Position\t:\t" + position + "\n" +
                        "Salary (PLN)\t:\t" + getSalary() + "\n" +
                        "Business phone\t:\t" + getBusinessPhone() + "\n" +
                        "Commission\t:\t" + commission + "\n" +
                        "Commission limit/month (PLN)\t:\t" + commissionLimit + "\n"
        );
    }
}

class Director extends Worker
{
    private long serviceBonus;
    private String cartNumber;
    private long expenseLimit;

    public Director(String _id, String _firstName, String _lastName, long _salary, long _businessPhone,
                  long _serviceBonus, String _cartNumber, long _expenseLimit)
    {
        super(_id, _firstName, _lastName, _salary, _businessPhone);
        serviceBonus = _serviceBonus;
        cartNumber = _cartNumber;
        expenseLimit = _expenseLimit;
    }

    public void setServiceBonus(long _serviceBonus)
    {
        serviceBonus = _serviceBonus;
    }
    public void setCartNumber(String _cartNumber)
    {
        cartNumber = _cartNumber;
    }
    public void setExpenseLimit(long _expenseLimit)
    {
        expenseLimit = _expenseLimit;
    }

    public long getServiceBonus()
    {
        return serviceBonus;
    }
    public String getCartNumber()
    {
        return cartNumber;
    }
    public long getExpenseLimit()
    {
        return expenseLimit;
    }
    public String getInfo()
    {
        return new String(
                "id\t:\t" + getId() + "\n" +
                        "First name\t:\t" + getFirstName() + "\n" +
                        "Last name\t:\t" + getLastName() + "\n" +
                        "Salary (PLN)\t:\t" + getSalary() + "\n" +
                        "Business phone\t:\t" + getBusinessPhone() + "\n" +
                        "Service bonus (PLN)\t:\t" + serviceBonus + "\n" +
                        "Cart number\t:\t" + cartNumber + "\n" +
                        "Expense limit/month(PLN)\t:\t" + expenseLimit + "\n"
        );
    }
}