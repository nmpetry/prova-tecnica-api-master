package br.com.sicredi.simulacao.simulacao;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;


public class ConsultarTodasSimulacoesTest {

	@BeforeClass
	public static void base()
	{
		baseURI = "http://localhost:8080/api/v1";
		basePath = "/simulacoes";
	}
	
	@Before
	public void delete()
	{
		ArrayList<Integer> ids = new ArrayList();
		//excluir todas as simulacoes antes dos testes
		ids = 
			given()
			.when()
				.get()
			.then()
				.extract()
					.path("id");
	
		if (!ids.isEmpty())
		{
			for (int i=0;i<ids.size();i++)
			{
				int id = ids.get(i);
				System.out.println(id);
				given()
					.pathParam("id", id)
				.when()
					.delete("/{id}")
				.then();
			}
		}
	}
	
	@Test
	public void testBuscarTodasSimulacoesSemSimuacoesCadastradas()
	{
		//Retorna HTTP Status 204 se não existir simulações cadastradas
		//busca simulacoes
		given()
		.when()
			.get()
		.then()
			.statusCode(204);
	}
	
	@Test
	public void testBuscarTodasSimulacoesCadastradas()
	{
		String cpfSimulacao1 = "471241266";
		String cpfSimulacao2 = "721841266";
		
		//criar simulacoes
		
		//body da criacao
		String bodyCreateS1 = 
				  "{\n"
				+ "  \"nome\": \"José Fulano\",\n"
				+ "  \"cpf\": " + cpfSimulacao1 + ",\n"
				+ "  \"email\": \"jj@email.com\",\n"
				+ "  \"valor\": 1000,\n"
				+ "  \"parcelas\": 2,\n"
				+ "  \"seguro\": true\n"
				+ "}";
		
		//criar simulacao
		given()
			.contentType(ContentType.JSON)
			.body(bodyCreateS1)
		.when()
			.post()
		.then();
		
		//body da criacao
		String bodyCreateS2 = 
				  "{\n"
				+ "  \"nome\": \"José Siclano\",\n"
				+ "  \"cpf\": " + cpfSimulacao2 + ",\n"
				+ "  \"email\": \"jsc@email.com\",\n"
				+ "  \"valor\": 5000,\n"
				+ "  \"parcelas\": 2,\n"
				+ "  \"seguro\": true\n"
				+ "}";
		
		//criar simulacao
		given()
			.contentType(ContentType.JSON)
			.body(bodyCreateS2)
		.when()
			.post()
		.then();
		
		//busca simulacoes
		given()
		.when()
			.get()
		.then()
			.statusCode(200)
			.body("cpf", hasItems(cpfSimulacao1,cpfSimulacao2));
	}
}
