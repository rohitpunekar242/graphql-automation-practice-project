package graphQL;
import static io.restassured.RestAssured.*;
import org.testng.Assert;

import io.restassured.path.json.JsonPath;

public class GraphQLScript {

	public static void main(String[] args) {
		
		//mutations
		
		String Locationtype = "city";
		String Charactertype = "human";
		
		String mutationResponse = given().log().all().header("Content-Type","application/json")
		.body("{\"query\":\"mutation($locationName:String!,$characterName:String!,$episodeName:String!) \\n{\\n  createLocation(location: {name: $locationName, type: \\\""+Locationtype+"\\\", dimension: \\\"big\\\"}) {\\n    id\\n  }\\n  createCharacter(character: {name: $characterName, type: \\\""+Charactertype+"\\\", status: \\\"active\\\", species: \\\"human\\\", gender: \\\"male\\\", image: \\\"png\\\", originId: 15746, locationId: 15746}) {\\n    id\\n  }\\n  createEpisode(episode: {name: $episodeName, air_date: \\\"Sept 2001\\\", episode: \\\"Deathly Hallows\\\"}) {\\n    id\\n  }\\n  deleteLocations(locationIds:[15748])\\n  {\\n    locationsDeleted\\n  }\\n}\\n\",\"variables\":{\"locationName\":\"mumbai\",\"characterName\":\"rahul\",\"episodeName\":\"manifest\"}}")
		.when().post("https://rahulshettyacademy.com/gq/graphql")
		.then().log().all().extract().response().asString();
		
		JsonPath js1 = new JsonPath(mutationResponse);
		int id = js1.getInt("data.createCharacter.id");
		
		//query
		
		String response = given().log().all().header("Content-Type","application/json")
		.body("{\"query\":\"query \\n{\\n  character(characterId: "+id+") {\\n    name\\n    id\\n    status\\n    type\\n    image\\n    origin {\\n      name\\n      created\\n    }\\n  }\\n}\\n\",\"variables\":null}")
		.when().post("https://rahulshettyacademy.com/gq/graphql")
		.then().log().all().extract().response().asString();
		
		JsonPath js = new JsonPath(response);
		String actualType = js.getString("data.character.type");
		
		Assert.assertEquals(actualType, Charactertype);

	}

}
