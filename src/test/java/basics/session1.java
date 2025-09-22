package basics;

import groovy.json.JsonParser;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class session1 {

    public static void main(String[] args){
        /**
        Add place -> update place with new address ->get Place to validate if new address is present in response
         */

        //validate if add place is working as expected
        RestAssured.baseURI="https://rahulshettyacademy.com";
       String response= given().queryParam("key","qaclick123")
                .header("Content-Type","application/json")
                .body("{\n" +
                        "  \"location\": {\n" +
                        "    \"lat\": -38.383494,\n" +
                        "    \"lng\": 33.427362\n" +
                        "  },\n" +
                        "  \"accuracy\": 50,\n" +
                        "  \"name\": \"Frontline house\",\n" +
                        "  \"phone_number\": \"(+91) 983 893 3937\",\n" +
                        "  \"address\": \"29, side layout, cohen 09\",\n" +
                        "  \"types\": [\n" +
                        "    \"shoe park\",\n" +
                        "    \"shop\"\n" +
                        "  ],\n" +
                        "  \"website\": \"http://google.com\",\n" +
                        "  \"language\": \"French-IN\"\n" +
                        "}\n")
                .when().post("/maps/api/place/add/json")
                .then()
                //.log().all()
                .assertThat().statusCode(200)
                .body("scope",equalTo("APP"))
                .header("Server",equalTo("Apache/2.4.52 (Ubuntu)"))
                .extract().asString();
        //print(response);

        JsonPath json=new JsonPath(response);
        String placeId=json.get("place_id");
        print(placeId);


        //update address field of the place
        given()
                //.log().all()
                .queryParam("key","qaclick123")
                .header("Content-Type","application/json")
                .body("{\n" +
                        "\"place_id\":\""+placeId+"\",\n" +
                        "\"address\":\"70 Summer walk, USA\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}")
                .when().put("/maps/api/place/update/json")
                .then().assertThat().statusCode(200)
                .body("msg",equalTo("Address successfully updated"));

        //verify if address is updated or not
        String response1 =given().queryParam("key","qaclick123")
                .queryParam("place_id",placeId)
                .when().get("/maps/api/place/get/json")
                .then().assertThat().statusCode(200)
                .body("address",equalTo("70 Summer walk, USA"))
                .extract().asString();
        print(response1);


        //Assert.assertEquals(actualAddress,expectedaddress);

    }

    public static void print(String s){
        System.out.println(s);
    }


}
