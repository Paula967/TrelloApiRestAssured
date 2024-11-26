package Trello;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
@Feature("Delete a Specific CheckList")
public class Delete_Checklist {

    private Response response;

    @BeforeClass
    public void deleteChecklist() {
        // Validate that checklist_id is initialized
        if (Create_Checklist.checklist_id == null || Create_Checklist.checklist_id.isEmpty()) {
            throw new IllegalStateException("Checklist ID is not initialized. Please ensure Create_Checklist is executed first.");
        }

        System.out.println("Using Checklist ID for Deletion: " + Create_Checklist.checklist_id);

        // Send DELETE request to delete the checklist
        response = given()
                .baseUri("https://api.trello.com")
                .queryParam("key", Create_Board.APIKey)
                .queryParam("token", Create_Board.token)
                .contentType(ContentType.JSON)
                .when()
                .delete("/1/checklists/" + Create_Checklist.checklist_id)
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Story("Test that the Status code of Deleting a Specific CheckList is = 200")
    @Test(description = "Test that the Status code of Deleting a Specific CheckList is = 200")
    public void testStatusCode() {
        // Validate the status code of the DELETE request
        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);
        assertEquals(statusCode, 200, "Expected status code 200 but got: " + statusCode);
    }

    @Story("Test That the Response time Of Deleting a Specific CheckList is less than 3000ms")
    @Test(description = "Test That the Response time Of Deleting a Specific CheckList is less than 3000ms")
    public void testResponseTime() {
        // Validate the response time of the DELETE request
        long responseTime = response.getTime();
        System.out.println("Response Time: " + responseTime + "ms");
        assertTrue(responseTime < 3000, "Response time exceeded 3000ms: " + responseTime);
    }

    @Story("Test That the CheckIList Deleted Successfully")
    @Test(description = "Test That the CheckList Deleted Successfully")
    public void Validate_That_Checklist_is_Deleted_successfully() {
        Get_Checklist getChecklist = new Get_Checklist();
        Response getCheckListResponse = getChecklist.GetChecklist();
        // Verify that the list is deleted by checking the status code
        int getStatusCode = getCheckListResponse.getStatusCode();
        System.out.println("Get Status Code: " + getStatusCode);

        // If list is deleted, the response should return 404 (not found)
        assertEquals(getStatusCode, 404, "Expected status code 404 but got: " + getStatusCode);
    }
}
