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

    public void addWorker(String _id, String _firstName, String _lastName, long _salary, String _businessPhone,
                     String _position, int _commission, long _commissionLimit)
    {
        workersHashMap.put(_id, new Seller(_id, _firstName, _lastName, _salary, _businessPhone, _position, _commission, _commissionLimit));
    }

    public void addWorker(String _id, String _firstName, String _lastName, long _salary, String _businessPhone,
                 long _serviceBonus, String _cartNumber, long _expenseLimit)
    {
        workersHashMap.put(_id, new Director(_id, _firstName, _lastName, _salary, _businessPhone, _serviceBonus, _cartNumber, _expenseLimit));
    }

    public void setWorkersHashMap(HashMap<String, Worker> _workersHashMap)
    {
        workersHashMap = _workersHashMap;
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
    public int getSize()
    {
        return workersHashMap.size();
    }
}