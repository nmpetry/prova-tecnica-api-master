package br.com.sicredi.simulacao.simulacao;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class ConsultarUmaSimulacaoTest {

	private String cpf = "41234875";
	
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
	public void testBuscarUmCpfSemSimulacao()
	{
		String validacao = "CPF " + cpf + " não encontrado";
		//buscar simulacao
		String erro =
		given()
			.pathParam("cpf", cpf)
		.when()
			.get("/{cpf}")
		.then()
			.statusCode(404)
			.extract()
				.path("mensagem");
		
		Assert.assertEquals(validacao, erro);
	}
	
	@Test
	public void testBuscarUmCpfComSimulacao()
	{
		//criar simulacao
		//body da criacao
		String bodyCreateS1 = 
				  "{\n"
				+ "  \"nome\": \"José Fulano\",\n"
				+ "  \"cpf\": " + cpf + ",\n"
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
		
		//buscar simulacao
		String corpo =
		given()
			.pathParam("cpf", cpf)
		.when()
			.get("/{cpf}")
		.then()
			.statusCode(200)
			.extract()
				.asString();
		
		Assert.assertEquals(corpo.isEmpty(), false);
	}
}
