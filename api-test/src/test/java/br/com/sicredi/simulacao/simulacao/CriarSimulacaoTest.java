package br.com.sicredi.simulacao.simulacao;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CriarSimulacaoTest {
	
	@BeforeClass
	public static void base()
	{
		baseURI = "http://localhost:8080/api/v1";
		basePath = "/simulacoes/";
	}
	
	@Test
	public void testCriarSimulacao()
	{
		//body da simulacao
		String body = 
				  "{\n"
				+ "  \"nome\": \"José Joseano\",\n"
				+ "  \"cpf\": 214411244,\n"
				+ "  \"email\": \"jj@email.com\",\n"
				+ "  \"valor\": 1000,\n"
				+ "  \"parcelas\": 2,\n"
				+ "  \"seguro\": true\n"
				+ "}";
		
		//Enviar simulacao
		given()
			.contentType(ContentType.JSON)
			.body(body)
		.when()
			.post()
		.then()
			.body("nome", is("José Joseano"))
			.body("cpf", is("214411244"))
			.body("email", is("jj@email.com"))
			.body("valor", is(1000))
			.body("parcelas", is(2))
			.body("seguro", is(true))
			.statusCode(201);
	}

	@Test
	public void testCriarSimulacaoComEmailInvalido()
	{
		String validacaoEmail = "E-mail deve ser um e-mail válido";
		//body da simulacao
		String body = 
				  "{\n"
				+ "  \"nome\": \"José Joseano\",\n"
				+ "  \"cpf\": 789456778,\n"
				+ "  \"email\": \"jegr@a\",\n"
				+ "  \"valor\": 1000,\n"
				+ "  \"parcelas\": 2,\n"
				+ "  \"seguro\": true\n"
				+ "}";
		
		//Enviar simulacao
		String erro = 
			given()
				.contentType(ContentType.JSON)
				.body(body)
			.when()
				.post()
			.then()
				.statusCode(400)
				.extract()
					.path("erros.email");
		
		Assert.assertEquals(validacaoEmail, erro);
	}


	@Test
	public void testCriarSimulacaoComCPFEmFormatoErrado()
	{
		//não existe mensagem de erro
		//body da simulacao
		String body = 
				  "{\n"
				+ "  \"nome\": \"José Joseano\",\n"
				+ "  \"cpf\": 789.456.778-22,\n"
				+ "  \"email\": \"jjr@email.com\",\n"
				+ "  \"valor\": 1000,\n"
				+ "  \"parcelas\": 2,\n"
				+ "  \"seguro\": true\n"
				+ "}";
		
		//Enviar simulacao
		 given()
			.contentType(ContentType.JSON)
			.body(body)
		.when()
			.post()
		.then()
			.statusCode(400);	
	}

	
	@Test
	public void testCriarSimulacaoSemAlgumasInformacoes()
	{
		String validacaoNome =     "Nome não pode ser vazio";
		String validacaoCpf =  	   "CPF não pode ser vazio";
		String validacaoParcelas = "Parcelas não pode ser vazio";
		String validacaoValor =    "Valor não pode ser vazio";
		//body da simulacao
		String body = 
				      "{\n"
					+ "  \n"
					+ "  \"email\": \"gr@email.com\",\n"
					+ "  \"seguro\": true\n"
					+ "}";
		
		//Enviar simulacao
		Response response = 
			given()
				.contentType(ContentType.JSON)
				.body(body)
			.when()
				.post();
			response.then()
				.statusCode(400);
		
		String erroNome = response.jsonPath().getString("erros.nome");
		String erroCpf = response.jsonPath().getString("erros.cpf");
		String erroParcelas = response.jsonPath().getString("erros.parcelas");
		String erroValor = response.jsonPath().getString("erros.valor");
		
		Assert.assertEquals(validacaoNome, erroNome);
		Assert.assertEquals(validacaoCpf, erroCpf);
		Assert.assertEquals(validacaoParcelas, erroParcelas);
		Assert.assertEquals(validacaoValor, erroValor);
	}
	
	
	@Test
	public void testCriarSimulacaoComValorAbaixoDe1000()
	{
		//não existe mensagem de erro
		//body da simulacao
		String body = 
				  "{\n"
				+ "  \"nome\": \"José Joseano\",\n"
				+ "  \"cpf\": 745879841,\n"
				+ "  \"email\": \"jj@email.com\",\n"
				+ "  \"valor\": 999,\n"
				+ "  \"parcelas\": 2,\n"
				+ "  \"seguro\": true\n"
				+ "}";
		
		//Enviar simulacao
			given()
				.contentType(ContentType.JSON)
				.body(body)
			.when()
				.post()
			.then()
				.statusCode(400);
	}
	
	@Test
	public void testCriarSimulacaoComValorAcimaDe40000()
	{
		String validacaoValor = "Valor deve ser menor ou igual a R$ 40.000";
		
		//body da simulacao
		String body =
				  "{\n"
				+ "  \"nome\": \"José Joseano\",\n"
				+ "  \"cpf\": 67344764,\n"
				+ "  \"email\": \"jj@email.com\",\n"
				+ "  \"valor\": 40001,\n"
				+ "  \"parcelas\": 2,\n"
				+ "  \"seguro\": true\n"
				+ "}";
		
		//Enviar simulacao
		String erro = 
			given()
				.contentType(ContentType.JSON)
				.body(body)
			.when()
				.post()
			.then()
				.statusCode(400)
				.extract()
					.path("erros.valor");
		
		Assert.assertEquals(validacaoValor, erro);
	}
	
	@Test
	public void testCriarSimulacaoComParcelasMenorQue2()
	{
		String validacaoParcelas = "Parcelas deve ser igual ou maior que 2";
		
		//body da simulacao
		String body = 
				  "{\n"
				+ "  \"nome\": \"José Joseano\",\n"
				+ "  \"cpf\": 67344764,\n"
				+ "  \"email\": \"jj@email.com\",\n"
				+ "  \"valor\": 25000,\n"
				+ "  \"parcelas\": 1,\n"
				+ "  \"seguro\": true\n"
				+ "}";
		
		//Enviar simulacao
		String erro = 
			given()
				.contentType(ContentType.JSON)
				.body(body)
			.when()
				.post()
			.then()
				.statusCode(400)
				.extract()
					.path("erros.parcelas");
		
		Assert.assertEquals(validacaoParcelas, erro);
	}
	
	@Test
	public void testCriarSimulacaoComParcelasMaiorQue48()
	{
		//não existe mensagem de erro
		//body da simulacao
		String body = 
				  "{\n"
				+ "  \"nome\": \"José Joseano\",\n"
				+ "  \"cpf\": 412578544,\n"
				+ "  \"email\": \"jj@email.com\",\n"
				+ "  \"valor\": 20000,\n"
				+ "  \"parcelas\": 49,\n"
				+ "  \"seguro\": true\n"
				+ "}";
		
		//Enviar simulacao
			given()
				.contentType(ContentType.JSON)
				.body(body)
			.when()
				.post()
			.then()
				.statusCode(400);
	}
	
	@Test
	public void testCriarSimulacaoCpfDuplicado()
	{
		String validacaoCpfDuplicado = "CPF duplicado";
		//body da simulacao
		String body = 
				  "{\n"
				+ "  \"nome\": \"José Joseano\",\n"
				+ "  \"cpf\": 475841266,\n"
				+ "  \"email\": \"jj@email.com\",\n"
				+ "  \"valor\": 1000,\n"
				+ "  \"parcelas\": 2,\n"
				+ "  \"seguro\": true\n"
				+ "}";
		
		//Enviar simulacao
		given()
			.contentType(ContentType.JSON)
			.body(body)
		.when()
			.post()
		.then();
		
		String erro = 
				given()
					.contentType(ContentType.JSON)
					.body(body)
				.when()
					.post()
				.then()
					.statusCode(400)
					.extract()
						.path("mensagem");
			
			Assert.assertEquals(validacaoCpfDuplicado, erro);
	}
}
