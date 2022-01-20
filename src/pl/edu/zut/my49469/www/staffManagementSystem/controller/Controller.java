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
import java.util.concurrent.*;
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

    ControlSystem getCs()
    {
        return cs;
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

    static boolean verifyId(String _id)
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

    static void showStaff()
    {
        char choose;
        Hashtable<String, Worker> workers = cs.getWorkersHashMap();
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
    static void addEmployee()
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
                        addDirector(id, firstName, lastName, salary, businessPhone, serviceBonus, cartNumber, expenseLimit);
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
                        addSeller(id, firstName, lastName, salary, businessPhone, position, commission, commissionLimit);
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
    static void removeEmployee()
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

    static void backup()
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
                    doSerialize(choose);
                }
                else if (choose == 'o')
                {
                    System.out.printf("%-30s:\t\t", "File name");
                    String filename = scanner.next();
                    doDeserialize(filename);
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

    static void saveGZIP(Worker worker, String fileName) throws IOException
    {
        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file);
             GZIPOutputStream gos = new GZIPOutputStream(fos);
             ObjectOutputStream oos = new ObjectOutputStream(gos))
        {
            oos.writeObject(worker);
        }
    }

    static void  saveZIP(Worker worker, String fileName) throws IOException
    {
        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file);
             ZipOutputStream zos = new ZipOutputStream(fos);)
        {
            ZipEntry ze = new ZipEntry("Backup");
            zos.putNextEntry(ze);
            ObjectOutputStream oos = new ObjectOutputStream(zos);
            oos.writeObject(worker);
            oos.close();
        }
    }

    static Worker readGZIP(File file) throws IOException, ClassNotFoundException
    {
        try(FileInputStream fis = new FileInputStream(file);
            GZIPInputStream gis = new GZIPInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(gis))
        {
            return (Worker)ois.readObject();
        }
    }

    static Worker readZIP(File file) throws IOException, ClassNotFoundException
    {
        File temp = Files.createTempFile("", "").toFile();
        try (FileInputStream fis = new FileInputStream(file);
             ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
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
            return (Worker)ois.readObject();
        }
        finally {
            temp.delete();
        }
    }

    static void verifyWorker(String _id) throws IllegalArgumentException
    {
        if (!verifyId(_id))
            throw new IllegalArgumentException("The id is invalid!");
        if (cs.getWorkersHashMap().get(_id) != null)
            throw new IllegalArgumentException("The worker with this id exist");
    }

    static void addSeller(String _id, String _firstName, String _lastName, long _salary, String _businessPhone,
                             String _position, int _commission, long _commissionLimit) throws IllegalArgumentException
    {
        verifyWorker(_id);
        cs.addWorker(_id, _firstName, _lastName, _salary, _businessPhone, _position, _commission, _commissionLimit);
    }

    static void addDirector(String _id, String _firstName, String _lastName, long _salary, String _businessPhone,
                               long _serviceBonus, String _cartNumber, long _expenseLimi) throws IllegalArgumentException
    {
        verifyWorker(_id);
        cs.addWorker(_id, _firstName, _lastName, _salary, _businessPhone, _serviceBonus, _cartNumber, _expenseLimi);
    }

    static void doRemoveEmployee(String id) throws IllegalArgumentException
    {
        if(cs.getWorkersHashMap().get(id) == null)
            throw new IllegalArgumentException("There is no such a worker, try again");
        cs.getWorkersHashMap().remove(id);
    }

    static void doSerialize(char choose) throws Exception
    {
        if (choose == 'z' || choose == 'g')
        {
            String fileName = LocalDate.now().toString();
            char fileChoose = choose;
            File dir = new File(fileName);
            dir.mkdir();
            String fileExtension = (fileChoose == 'z') ? ".zip" : ".gzip";

            Executor executor = Executors.newFixedThreadPool(10);
            Hashtable<String, Worker> workers = cs.getWorkersHashMap();
            CompletableFuture<Void> [] futures = new CompletableFuture[workers.size()];

            int i = 0;
            for (Worker worker : workers.values())
            {
                String finalFileName = fileName + "/" + i + fileExtension;
                futures[i] = CompletableFuture.runAsync(() -> {
                    try {
                        if (fileChoose == 'z') {
                            saveZIP(worker, finalFileName);
                        } else
                            saveGZIP(worker, finalFileName);
                        } catch (Exception e) {
                            throw new CompletionException(e);
                        }
                    }, executor);
                ++i;
            }
            try {
                CompletableFuture.allOf(futures).get();
            } catch (InterruptedException | ExecutionException e)
            {
                throw e;
            }
            ((ExecutorService) executor).shutdown();
            for (int j = 0; j < futures.length; ++j)
            {
                if (futures[j].isCompletedExceptionally())
                {
                    throw new Exception("Thread error!");
                }
            }
        }
        else
            throw new IllegalArgumentException("There is not such an option, try again");
    }

    static void doDeserialize(String filename)  throws Exception
    {
        String fileExtension;
        if (filename.contains(".gzip"))
        {
            fileExtension = ".gzip";
            filename = filename.substring(0, filename.length() - 5);
        }
        else if(filename.contains(".zip"))
        {
            fileExtension = ".zip";
            filename = filename.substring(0, filename.length() - 4);
        }
        else
            throw new Exception("Wrong file extension");
        Executor executor = Executors.newFixedThreadPool(10);
        try {
            File dir = new File(filename);
            File [] files = dir.listFiles();
            List<CompletableFuture<Worker>> futures = new ArrayList<>();
            for (File file : files)
            {
                if (file.getName().contains(fileExtension))
                {
                    futures.add(CompletableFuture.supplyAsync(
                            () -> {
                                try {
                                    if (fileExtension.equals(".zip"))
                                        return readZIP(file);
                                    else
                                        return readGZIP(file);
                                } catch (Exception e) {
                                    throw new CompletionException(e);
                                }
                            }, executor));
                }
            }
            for (int i = 0; i < futures.size(); ++i)
            {
                if (!futures.get(i).isCompletedExceptionally())
                    cs.addWorker(futures.get(i).get());
                else
                    throw new Exception("Thread error!");
            }
        } finally {
            ((ExecutorService) executor).shutdown();
        }
    }
}