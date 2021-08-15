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

public class ExcluirSimulacaoTest {

	private int id = 10;
	
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
	public void testExcluirSimulacaoInexistente()
	{
		//Retorna o HTTP Status 404 com a mensagem "Simulação não encontrada" 
		//se não existir a simulação pelo ID informado
		String mensagem = "Simulação não encontrada";
		String erro =
		given()
			.pathParam("id", id)
		.when()
			.delete("/{id}")
		.then()
			.statusCode(404)
			.extract()
				.path("mensagem");
		
		Assert.assertEquals(mensagem, erro);
	}
	
	@Test
	public void testExcluirSimulacao()
	{
		//cria simulacao
		//body da simulacao
		String body = 
				  "{\n"
				+ "  \"nome\": \"José Joseano\",\n"
				+ "  \"cpf\": 475841266,\n"
				+ "  \"email\": \"jj@email.com\",\n"
				+ "  \"valor\": 5000,\n"
				+ "  \"parcelas\": 2,\n"
				+ "  \"seguro\": true\n"
				+ "}";
		
		id = 
			given()
				.contentType(ContentType.JSON)
				.body(body)
			.when()
				.post()
			.then()
				.extract()
					.path("id");
		
		//Retorna o HTTP Status 204 se simulação for removida com sucesso
		given()
			.pathParam("id", id)
		.when()
			.delete("/{id}")
		.then()
			.statusCode(204);
	}
}
