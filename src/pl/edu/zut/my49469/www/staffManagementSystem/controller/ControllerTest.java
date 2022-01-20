package pl.edu.zut.my49469.www.staffManagementSystem.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    Controller controller = Controller.getInstance();

    @org.junit.jupiter.api.Test
    void getInstance() {
        assertSame(controller, Controller.getInstance());
    }


    @ParameterizedTest
    @ValueSource(strings = {"06241964372",
                            "96051691885",
                            "91101092429",
                            "59101787352",
                            "99011757593",
                            "55120262467",
                            "56072221539",
                            "54052321813",
                            "60101298142",
                            "78051783832"})
    void verifyIdTrue(String id) {
        assertTrue(Controller.verifyId(id));
    }

    @ParameterizedTest
    @ValueSource(strings = {"000241964372",
                            "96051691085",
                            "911010923429",
                            "59101707352",
                            "99010757593",
                            "55120202467",
                            "56072201539",
                            "54052301813",
                            "60101208142",
                            "78050783832"})
    void verifyIdFalse(String id) {
        assertFalse(Controller.verifyId(id));
    }

    @Test
    void addSellerToEmptyMapThrow()
    {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {Controller.addSeller("000241964372", "Yurii",
                "Yurii", 421412414, "123412341234", "Seller", 1231, 1234234);
        });
        assertEquals("The id is invalid!", exception.getMessage());
    }

    @Test
    void addSellerToEmptyMapGood()
    {
        controller.getCs().getWorkersHashMap().clear();
        assertDoesNotThrow(() -> {
            Controller.addSeller("06241964372", "Yurii",
                    "Yurii", 421412414, "123412341234", "Seller", 1231, 1234234);
        });
    }

    @Test
    void addSellerToNotEmptyMapThrow()
    {
        controller.getCs().getWorkersHashMap().clear();
        addSellerToEmptyMapGood();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Controller.addSeller("06241964372", "1",
                    "1", 1, "1", "1", 1, 1);
        });
        assertEquals("The worker with this id exist", exception.getMessage());
    }

    @Test
    void addSellerToNotEmptyMapNoThrow()
    {
        controller.getCs().getWorkersHashMap().clear();
        addSellerToEmptyMapGood();
        assertDoesNotThrow(() -> {
            Controller.addSeller("96051691885", "1",
                    "1", 1, "1", "1", 1, 1);
        });
    }

    @Test
    void addDirectorToEmptyMapGood()
    {
        controller.getCs().getWorkersHashMap().clear();
        assertDoesNotThrow(() -> {
            Controller.addDirector("91101092429", "Yurii",
                    "Yurii", 421412414, "123412341234",123123, "21314124", 1231);
        });
    }

    @Test
    void addDirectorToNotEmptyMapNoThrow()
    {
        controller.getCs().getWorkersHashMap().clear();
        addDirectorToEmptyMapGood();
        assertDoesNotThrow(() -> {
            Controller.addDirector("59101787352", "1",
                    "Yurii", 421412414, "123412341234",123123, "21314124", 1231);

        });
    }


    @Test
    void add10RandWorkers()
    {
        controller.getCs().getWorkersHashMap().clear();
        String [] ids = {"06241964372",
                         "96051691885",
                         "91101092429",
                         "59101787352",
                         "99011757593",
                         "55120262467",
                         "56072221539",
                         "54052321813",
                         "60101298142",
                         "78051783832"};
        for (String id : ids)
        {
            if (Math.round(Math.random()) == 1)
                Controller.addSeller(id, "1", "1", 1, "1", "1", 1, 1);
            else
                Controller.addDirector(id, "1", "1", 1, "1", 1, "1", 1);
        }
        assertEquals(10, controller.getCs().getSize());
    }

    @Test
    void removeEmployeeNoThrow() {
        controller.getCs().getWorkersHashMap().clear();
        add10RandWorkers();
        assertDoesNotThrow(() -> {Controller.doRemoveEmployee("96051691885");});
        assertEquals(9, controller.getCs().getSize());
    }

    @Test
    void removeEmployeThrow() {
        controller.getCs().getWorkersHashMap().clear();
        add10RandWorkers();
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {Controller.doRemoveEmployee("123123");});
        assertEquals("There is no such a worker, try again", e.getMessage());
        assertEquals(10, controller.getCs().getSize());
    }

    @ParameterizedTest
    @ValueSource(chars = {'z', 'g'})
    void serializeNoThrows(char choose)
    {
        controller.getCs().getWorkersHashMap().clear();
        add10RandWorkers();
        assertDoesNotThrow(() -> {Controller.doSerialize(choose);});
        File folder = new File(LocalDate.now().toString());
        String[] entries = folder.list();
        for(String s: entries){
            File currentFile = new File(folder.getPath(),s);
            currentFile.delete();
        }
        folder.delete();
    }

    @ParameterizedTest
    @ValueSource(strings = {".zip", ".gzip"})
    void deserializeNoThrows(String filename)
    {
        controller.getCs().getWorkersHashMap().clear();
        add10RandWorkers();
        if (filename.contains(".zip"))
            assertDoesNotThrow(() -> {Controller.doSerialize('z');});
        else
            assertDoesNotThrow(() -> {Controller.doSerialize('g');});

        String folderName = LocalDate.now().toString();
        controller.getCs().getWorkersHashMap().clear();
        assertDoesNotThrow(() -> {Controller.doDeserialize(folderName + filename);});
        assertEquals(10, controller.getCs().getSize());
        File folder = new File(folderName);
        String[] entries = folder.list();
        for(String s: entries){
            File currentFile = new File(folder.getPath(),s);
            currentFile.delete();
        }
        folder.delete();
    }

    @ParameterizedTest
    @ValueSource(strings = {".zip", ".gzip"})
    void deserializeThrows(String filename) throws Exception
    {
        File folder = new File("bad");
        folder.mkdir();
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream("bad/bad" + filename)))
        {
            byte [] bytes = new byte[32];
            new Random().nextBytes(bytes);
            dos.write(bytes);
        }
        assertThrows(NullPointerException.class, () -> {Controller.doDeserialize("bad/bad" + filename);});
        String[] entries = folder.list();
        for(String s: entries){
            File currentFile = new File(folder.getPath(),s);
            currentFile.delete();
        }
        folder.delete();
    }
}