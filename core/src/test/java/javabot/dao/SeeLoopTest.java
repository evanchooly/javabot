package javabot.dao;

import com.antwerkz.sofia.Sofia;
import javabot.BaseTest;
import javabot.operations.BaseOperationTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.inject.Inject;

@Test
public class SeeLoopTest extends BaseOperationTest {
    @Inject
    private FactoidDao factoidDao;

    public void serial() {
        createCircularSee();
        createCircularSee2();
        createNormalSee();
        followReferencesCorrectly();
    }

    public void createCircularSee() {
        deleteSees();
        testMessage("~see1 is <see>see2", ok);
        testMessage("~see2 is <see>see3", ok);
        testMessage("~see3 is <see>see1", ok);
        testMessage("~see1", Sofia.factoidLoop("<see>see2"));
        deleteSees();
    }

    @BeforeTest
    @AfterTest
    private void deleteSees() {
        factoidDao.delete("test", "see1");
        factoidDao.delete("test", "see2");
        factoidDao.delete("test", "see3");
    }

    public void createCircularSee2() {
        deleteSees();
        testMessage("~see1 is <see>see2", ok);
        testMessage("~see2 is <see>see3", ok);
        testMessage("~see3 is <see>see1", ok);
        testMessage("~see1", Sofia.factoidLoop("<see>see2"));
        deleteSees();
    }

    public void followReferencesCorrectly() {
        deleteSees();
        testMessage("~see1 is Bzzt $who", ok);
        testMessage("~see2 is <see>see1", ok);
        testMessage("~see3 is <see>see2", ok);
        testMessage("~see3", String.format("%s, see1 is Bzzt %s", getTestUser(), getTestUser()));
        deleteSees();
    }

    public void createNormalSee() {
        deleteSees();
        testMessage("~see1 is <see>see2", ok);
        testMessage("~see2 is <see>see3", ok);
        testMessage("~see3 is w00t", ok);
        testMessage("~see1", getTestUser() + ", see3 is w00t");
        deleteSees();
    }
}