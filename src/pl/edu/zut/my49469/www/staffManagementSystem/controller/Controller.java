package pl.edu.zut.my49469.www.staffManagementSystem.controller;
import pl.edu.zut.my49469.www.staffManagementSystem.model.ControlSystem;
import pl.edu.zut.my49469.www.staffManagementSystem.model.Worker;
import pl.edu.zut.my49469.www.staffManagementSystem.view.*;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.*;
import java.lang.reflect.Executable;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;
import java.util.zip.*;

public class Controller
{
    static Controller instance;
    private static ControlSystem cs;
    private static ScreenStack ss;

    private Controller()
    {
        cs = ControlSystem.getInstance();
        ss = ScreenStack.getInstance();
    }

    public static Controller getInstance()
    {
        if (instance == null)
            instance = new Controller();
        return instance;
    }

    public static void main(String[] args)
    {
        Controller ctrl = Controller.getInstance();
        ss.pushScreen(
                new Screen("Main menu", "", "Chose option by using number (E[x]it)", new Content() {
                    @Override
                    public void show() {
                        System.out.println("1.\tStaff list\n" +
                                           "2.\tAdd employee\n" +
                                           "3.\tRemove employee\n" +
                                           "4.\tBackup data");
                    }
                })
        );
        boolean isEnd = false;
        char choose;
        while (!isEnd) {
            try {
                switch (ss.actCurrent()) {
                    case '1':
                        if (cs.getSize() == 0) {
                            throw new Exception("The staff list is empty, add someone first.");
                        }
                        showStaff();
                        break;
                    case '2':
                        addEmployee();
                        break;
                    case '3':
                        if (cs.getSize() == 0) {
                            throw new Exception("The staff list is empty, add someone first.");
                        }
                        removeEmployee();
                        break;
                    case '4':
                        backup();
                        break;
                    case 'x':
                        isEnd = true;
                        break;
                    default:
                        throw  new Exception("There is not such an option, try again.");
                }
            }
            catch (Exception e)
            {
                ss.addError(e.getMessage());
            }
        }
    }

    private static boolean verifyId(String _id)
    {
        if (_id.length() != 11)
            return false;
        int[] coefficients = { 1, 3, 7, 9, 1, 3, 7, 9, 1, 3 };
        int sum = 0;
        for (int i = 0; i < 10; ++i)
            sum += (_id.charAt(i) - '0') * coefficients[i];
        sum = sum%10 == 0 ? 0 : 10-sum%10;
        return sum == (_id.charAt(10) - '0');
    }

    private static void showStaff()
    {
        char choose;
        HashMap<String, Worker> workers = cs.getWorkersHashMap();
        String[] keyArr = workers.keySet().toArray(new String[workers.keySet().size()]);
        boolean end = false;
        int index = 0;
        choose = 'n';
        do {
            if (choose == 'n')
            {
                if (index < cs.getSize())
                {
                    ArrayList<ArrayList<String>> info = workers.get(keyArr[index]).getInfo();
                    ss.pushScreen(new Screen("Staff list", "", "[N]ext, [B]ack, E[x]it", new Content() {
                        @Override
                        public void show() throws Exception {
                            for (int i = 0; i < info.get(0).size(); ++i)
                                System.out.printf("%-30s:\t\t\t%s%n", info.get(0).get(i), info.get(1).get(i));
                        }
                    }));
                    ++index;
                }
            }
            else if (choose == 'b')
            {
                if (index > 1)
                {
                    ss.popCurrent();
                    --index;
                }
            }
            else if(choose == 'x')
            {
                while (index != 0)
                {
                    ss.popCurrent();
                    --index;
                }
                end = true;
                break;
            }
            try
            {
                choose = ss.actCurrent();
                if (choose != 'b' && choose != 'x' && choose != 'n')
                    throw new Exception("There is not such an option, try again.");
            }
            catch (Exception e)
            {
                ss.addError(e.getMessage());
            }
        }
        while(!end);
    }
    private static void addEmployee()
    {
        char choose;
        boolean end = false;
        ss.pushScreen(new Screen("Add worker", "", "[A]dd new, [B]ack", new Content() {
            @Override
            public void show() throws Exception
            {
                Scanner scanner = new Scanner(System.in);
                System.out.println("[D]irector/[S]eller ([B]ack): ");
                char choose = scanner.next().charAt(0);
                if (choose == 'b')
                    return;
                if (choose != 'd' && choose != 's')
                {
                    throw new Exception("There is no such a kind of worker!");
                }
                System.out.printf("%-30s:\t\t", "id");
                String id = scanner.next();
                if (!verifyId(id))
                    throw new Exception("The id is invalid!");
                if (cs.getWorkersHashMap().get(id) != null)
                    throw new Exception("The worker with this id exist");
                System.out.printf("%-30s:\t\t", "First name");
                String firstName = scanner.next();
                System.out.printf("%-30s:\t\t", "Last name");
                String lastName = scanner.next();
                System.out.printf("%-30s:\t\t", "Salary (PLN)");
                long salary = scanner.nextLong();
                System.out.printf("%-30s:\t\t", "Business phone");
                String businessPhone = scanner.next();
                if (choose == 'd')
                {
                    System.out.printf("%-30s:\t\t", "Service bonus (PLN)");
                    long serviceBonus = scanner.nextLong();
                    System.out.printf("%-30s:\t\t", "Cart number");
                    String cartNumber = scanner.next();
                    System.out.printf("%-30s:\t\t", "Expense limit/month (PLN)");
                    long expenseLimit = scanner.nextLong();
                    System.out.printf("%s\n:", "Save [y] - yes, [n] - no");
                    if (scanner.next().charAt(0) == 'y')
                        cs.addWorker(id, firstName, lastName, salary, businessPhone, serviceBonus, cartNumber, expenseLimit);
                }
                else if (choose == 's')
                {
                    System.out.printf("%-30s:\t\t", "Position");
                    String position = scanner.next();
                    System.out.printf("%-30s:\t\t", "Commission");
                    int commission = scanner.nextInt();
                    System.out.printf("%-30s:\t\t", "Commission limit/month (PLN)");
                    long commissionLimit = scanner.nextLong();
                    System.out.printf("%s\n:", "Save [y]es, [n]o");
                    if (scanner.next().charAt(0) == 'y')
                        cs.addWorker(id, firstName, lastName, salary, businessPhone, position, commission, commissionLimit);
                }
            }
        }));
        do {
            try {
                choose = ss.actCurrent();
                if (choose != 'n' && choose != 'b')
                    throw new Exception("There is not such an option, try again");
                if (choose == 'b')
                    end = true;
                ss.addError("");
            }
            catch (Exception e)
            {
                ss.addError(e.getMessage());
            }
        }
        while (!end);
        ss.popCurrent();
    }
    private static void removeEmployee()
    {
        char choose;
        boolean end = false;
        ss.pushScreen(new Screen("Remove employee", "", "[A]gain, [B]ack", new Content() {
            @Override
            public void show() throws Exception {
                Scanner scanner = new Scanner(System.in);
                System.out.printf("%-30s:\t\t\t", "Enter id ([B]ack)");
                String id = scanner.next();
                if(id.charAt(0)  == 'b')
                    return;
                if(cs.getWorkersHashMap().get(id) == null)
                    throw new Exception("There is no such a worker, try again");
                ArrayList<ArrayList<String>> info = cs.getWorkersHashMap().get(id).getInfo();
                for (int i = 1; i < info.get(0).size(); ++i)
                    System.out.printf("%-30s:\t\t\t%s%n", info.get(0).get(i), info.get(1).get(i));
                System.out.printf("Delete? [y]es\n", "Enter id ([B]ack)");
                if (scanner.next().charAt(0) == 'y')
                    cs.getWorkersHashMap().remove(id);
            }
        }));
        do {
            try {
                choose = ss.actCurrent();
                if (choose != 'a' && choose != 'b')
                    throw new Exception("There is not such an option, try again");
                if (choose == 'b')
                    end = true;
                ss.addError("");
            }
            catch (Exception e)
            {
                ss.addError(e.getMessage());
            }
        }
        while (!end);
        ss.popCurrent();
    }
    private static void backup()
    {
        char choose;
        boolean end = false;
        ss.pushScreen(new Screen("Backup", "", "[B]ack, [A]gain", new Content() {
            @Override
            public void show() throws Exception {
                Scanner scanner = new Scanner(System.in);
                System.out.printf("%-30s:\t\t", "[S]ave/[O]pen ([B]ack)");
                char choose = scanner.next().charAt(0);
                if (choose == 'b')
                    return;
                if (choose == 's')
                {
                    System.out.printf("%-30s:\t\t", "[Z]ip/[G]zip");
                    choose = scanner.next().charAt(0);
                    String fileName = LocalDate.now().toString();
                    if (choose == 'z')
                    {
                        fileName += ".zip";
                        System.out.printf("%-30s:\t\t%s\n", "File name", fileName);
                        System.out.printf("%-30s\t\t\n", "[S]ave/[B]ack");
                        choose = scanner.next().charAt(0);
                        if (choose == 's')
                        {
                            File file = new File(fileName);
                            try (FileOutputStream fos = new FileOutputStream(file);
                                 ZipOutputStream zos = new ZipOutputStream(fos);)
                            {
                                ZipEntry ze = new ZipEntry("Backup");
                                zos.putNextEntry(ze);
                                ObjectOutputStream oos = new ObjectOutputStream(zos);
                                oos.writeObject(cs.getWorkersHashMap());
                                oos.close();
                            }
                        }
                    }
                    else if (choose == 'g')
                    {
                        fileName += ".gzip";
                        System.out.printf("%-30s:\t\t%s\n", "File name:", fileName);
                        System.out.printf("%-30s\t\t\n", "[S]ave/[B]ack");
                        choose = scanner.next().charAt(0);
                        if (choose == 's')
                        {
                            File file = new File(fileName);
                            try (FileOutputStream fos = new FileOutputStream(file);
                                 GZIPOutputStream gos = new GZIPOutputStream(fos);
                                 ObjectOutputStream oos = new ObjectOutputStream(gos))
                            {
                                oos.writeObject(cs.getWorkersHashMap());
                            }
                        }
                    }
                    else
                        throw new Exception("There is not such an option, try again");
                }
                else if (choose == 'o')
                {
                    System.out.printf("%-30s:\t\t", "File name");
                    String filename = scanner.next();
                    try (FileInputStream fis = new FileInputStream(filename))
                    {
                        if (filename.contains(".gzip"))
                        {
                            try(GZIPInputStream gis = new GZIPInputStream(fis);
                                ObjectInputStream ois = new ObjectInputStream(gis))
                            {
                                cs.setWorkersHashMap((HashMap<String, Worker>)ois.readObject());
                            }
                        }
                        else if(filename.contains(".zip"))
                        {
                            File temp = Files.createTempFile("", "").toFile();
                            try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(filename)));
                                 BufferedInputStream bis = new BufferedInputStream(new FileInputStream(temp)))
                            {
                                zis.getNextEntry();
                                FileOutputStream fos = new FileOutputStream(temp);
                                byte[] buffer = new byte[1024];
                                int len;
                                while ((len = zis.read(buffer)) > 0) {
                                    fos.write(buffer, 0, len);
                                }
                                fos.close();
                                ObjectInputStream ois = new ObjectInputStream(bis);
                                cs.setWorkersHashMap((HashMap<String, Worker>)ois.readObject());
                            }
                            temp.delete();
                        }
                    }
                }
                else
                    throw new Exception("There is not such an option, try again");
            }
        }));
        do {
            try {
                choose = ss.actCurrent();
                if (choose != 'a' && choose != 'b')
                    throw new Exception("There is not such an option, try again");
                if (choose == 'b')
                    end = true;
                ss.addError("");
            }
            catch (Exception e)
            {
                ss.addError(e.getMessage());
            }
        }
        while (!end);
        ss.popCurrent();
    }
}