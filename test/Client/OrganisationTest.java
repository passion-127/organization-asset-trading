package Client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrganisationTest {

    Organisation org;
    List<String> assets1;
    List<String> assets2;
    List<String> assets3;
    List<Integer> assetAmount1;
    List<Integer> assetAmount2;
    List<Integer> assetAmount3;
    String orgName;


    // Creating Organisation object
    @BeforeEach @Test
    public void setUpOrganisation() {

        // Parameters for Organisation object
        assets1 = new ArrayList<>();
        assetAmount1 = new ArrayList<>();
        assets2 = new ArrayList<>();
        assetAmount2 = new ArrayList<>();
        assets3 = new ArrayList<>();
        assetAmount3 = new ArrayList<>();

        org = new Organisation();

        org.createOrganisation("Microsoft", 200, assets1, assetAmount1);

        assets2.add("Hardware Resources");
        assetAmount2.add(5);
        assets2.add("Software Licenses");
        assetAmount2.add(15);

        org.createOrganisation("Microsoft2", 250, assets2, assetAmount2);

        org.createOrganisation("Microsoft3", 201, assets3, assetAmount3);

        assets3.add("Computational Resources");
        assetAmount3.add(30);
    }


    // Checks the getAssetsAndAmounts() method
    @Test
    public void getAssetsAndAmountsCheck() {
        orgName = "Microsoft3";
        assertEquals(30, org.getOrganisation(orgName).getAssetsAndAmounts().get("Computational " +
                "Resources"));
    }

    // Checks the getCredits() method
    @Test
    public void getCreditsCheck() {
        orgName = "Microsoft2";
        assertEquals(250, org.getOrganisation(orgName).getCredits());
    }

    // Checks the setCredits() method
    @Test
    public void setCreditsCheck() {
        orgName = "Microsoft2";
        Organisation orgObject = org.getOrganisation(orgName);
        orgObject.setCredits(255);
        assertEquals(255, orgObject.getCredits());
    }

    // Checks the getAssets() method
    @Test
    public void getAssetsCheck() {
        orgName = "Microsoft2";
        Organisation orgObject = org.getOrganisation(orgName);
        assertEquals("Hardware Resources", orgObject.getAssets().get(0));
    }

    // Checks the getAmounts() method
    @Test
    public void getAmountsCheck() {
        orgName = "Microsoft2";
        Organisation orgObject = org.getOrganisation(orgName);
        assertEquals(5, orgObject.getAmounts().get(0));
    }

    // Check changeCredtis() method
    @Test
    public void changeCreditsCheck() throws OrganisationException {
        orgName = "Microsoft2";
        Organisation orgObject = org.getOrganisation(orgName);
        orgObject.changeCredits(125);
        assertEquals(125, orgObject.getCredits());
    }

    // Check changeCredtis() method multiple after multiple changes
    @Test
    public void changeCreditsMultipleCheck() throws OrganisationException {
        orgName = "Microsoft2";
        Organisation orgObject = org.getOrganisation(orgName);
        orgObject.changeCredits(125);
        orgObject.changeCredits(100);
        orgObject.changeCredits(150);
        orgObject.changeCredits(165);
        assertEquals(165, orgObject.getCredits());
    }

    // Checks that exception is thrown if change credits is set to negative number
    @Test
    public void changeCreditsNegativeCheck() throws OrganisationException{
        orgName = "Microsoft";
        Organisation orgObject = org.getOrganisation(orgName);
        assertThrows(OrganisationException.class, () -> {
            orgObject.changeCredits(-50);
        });
    }

    // Checks changeAssetAmounts() method with positive values
    @Test
    public void changeAssetAmountsPositiveCheck() {
        orgName = "Microsoft";
        Organisation orgObject = org.getOrganisation(orgName);
        orgObject.addAssets("Software Licenses", 30);
        orgObject.addAssets("CPU Hours", 120);

        // Amounts passed to changeAssetAmounts() method
        List<Integer> newAmounts = new ArrayList<>();
        newAmounts.add(50);
        newAmounts.add(50);

        // Expected Results
        List<Integer> expectedAmounts = new ArrayList<>();
        expectedAmounts.add(50);
        expectedAmounts.add(50);

        orgObject.changeAssetAmounts(newAmounts);

        assertEquals(expectedAmounts, orgObject.getAmounts());
    }

    // Checks changeAssetAmounts() method if negative value is entered
    @Test
    public void changeAssetAmountsNegativeCheck() {
        orgName = "Microsoft";
        Organisation orgObject = org.getOrganisation(orgName);
        orgObject.addAssets("Software Licenses", 30);
        orgObject.addAssets("CPU Hours", 120);

        // Amounts passed to changeAssetAmounts() method
        List<Integer> newAmounts = new ArrayList<>();
        newAmounts.add(50);
        newAmounts.add(-50);

        // Expected Results
        List<Integer> expectedAmounts = new ArrayList<>();
        expectedAmounts.add(50);
        expectedAmounts.add(120);

        orgObject.changeAssetAmounts(newAmounts);

        assertEquals(expectedAmounts, orgObject.getAmounts());
    }

    // Checks changeAssetAmounts() method if values set to 0
    @Test
    public void changeAssetAmountsZeroCheck() {
        orgName = "Microsoft";
        Organisation orgObject = org.getOrganisation(orgName);
        orgObject.addAssets("Software Licenses", 30);
        orgObject.addAssets("CPU Hours", 120);

        // Amounts passed to changeAssetAmounts() method
        List<Integer> newAmounts = new ArrayList<>();
        newAmounts.add(0);
        newAmounts.add(0);

        // Expected Results
        List<Integer> expectedAmounts = new ArrayList<>();
        expectedAmounts.add(0);
        expectedAmounts.add(0);

        orgObject.changeAssetAmounts(newAmounts);

        assertEquals(expectedAmounts, orgObject.getAmounts());
    }
}

