package pl.edu.zut.my49469.www.staffManagementSystem.model;

import java.awt.image.AreaAveragingScaleFilter;
import java.beans.PropertyEditorSupport;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public abstract class Worker implements Serializable
{
    final private String id;
    private String firstName;
    private String lastName;
    private long salary;
    private String businessPhone;

    Worker(String _id, String _firstName, String _lastName, long _salary, String _businessPhone)
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
    final public void setBusinessPhone(String _businessPhone)
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
    final public String getBusinessPhone()
    {
        return businessPhone;
    }

    public abstract ArrayList<ArrayList<String>> getInfo();
}

class Seller extends Worker
{
    private String position;
    private int commission;
    private long commissionLimit;

    public Seller(String _id, String _firstName, String _lastName, long _salary, String _businessPhone,
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
    public ArrayList<ArrayList<String>> getInfo()
    {
        ArrayList<String> titles = new ArrayList<String>();
        ArrayList<String> values = new ArrayList<String>();
        titles.add("id");                           values.add(getId());
        titles.add("First name");                   values.add(getFirstName());
        titles.add("Last name");                    values.add(getLastName());
        titles.add("Position");                     values.add(position);
        titles.add("Salary (PLN)");                 values.add(String.valueOf(getSalary()));
        titles.add("Business phone");               values.add(getBusinessPhone());
        titles.add("Commission");                   values.add(String.valueOf(commission));
        titles.add("Commission limit/month (PLN)"); values.add(String.valueOf(commissionLimit));
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        result.add(titles);
        result.add(values);
        return result;
    }
}

class Director extends Worker
{
    private long serviceBonus;
    private String cartNumber;
    private long expenseLimit;

    public Director(String _id, String _firstName, String _lastName, long _salary, String _businessPhone,
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
    public ArrayList<ArrayList<String>> getInfo()
    {
        ArrayList<String> titles = new ArrayList<String>();
        ArrayList<String> values = new ArrayList<String>();
        titles.add("id");                           values.add(getId());
        titles.add("First name");                   values.add(getFirstName());
        titles.add("Last name");                    values.add(getLastName());
        titles.add("Salary (PLN)");                 values.add(String.valueOf(getSalary()));
        titles.add("Business phone");               values.add(getBusinessPhone());
        titles.add("Service bonus (PLN)");          values.add(String.valueOf(serviceBonus));
        titles.add("Cart number");                  values.add(cartNumber);
        titles.add("Expense limit/month (PLN)");    values.add(String.valueOf(expenseLimit));
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        result.add(titles);
        result.add(values);
        return result;
    }
}
