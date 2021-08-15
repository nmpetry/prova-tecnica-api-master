package br.com.sicredi.simulacao.simulacao;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;

public class AlterarSimulacaoTest {

	private static String cpfDuplicado     = "234911121";
	private static String cpfInexistente = "446551687";
	
	@BeforeClass
	public static void before()
	{
		baseURI = "http://localhost:8080/api/v1";
		basePath = "/simulacoes";
	}
	
	@Test
	public void testAlterarDadosdaSimulacao()
	{
		//body da criacao
		String bodyCreate = 
				  "{\n"
				+ "  \"nome\": \"José Fulano\",\n"
				+ "  \"cpf\": " + cpfDuplicado + ",\n"
				+ "  \"email\": \"jj@email.com\",\n"
				+ "  \"valor\": 1000,\n"
				+ "  \"parcelas\": 2,\n"
				+ "  \"seguro\": true\n"
				+ "}";
		
		//criar simulacao para alteracoes
		given()
			.contentType(ContentType.JSON)
			.body(bodyCreate)
		.when()
			.post()
		.then();
		
		//body da alteracao
		String bodyUpdate = 
				  "{\n"
				+ "  \"nome\": \"José Joseano\",\n"
				+ "  \"email\": \"jta@email.com\",\n"
				+ "  \"valor\": 4500,\n"
				+ "  \"parcelas\": 5,\n"
				+ "  \"seguro\": false\n"
				+ "}";
		
		//alterar simulacao
		given()
			.contentType(ContentType.JSON)
			.body(bodyUpdate)
			.pathParam("cpf", cpfDuplicado)
		.when()
			.put("/{cpf}")
		.then()
			.statusCode(200)
			.body("nome", is("José Joseano"))
			.body("cpf", is(cpfDuplicado))
			.body("email", is("jta@email.com"))
			.body("valor", is(4500))
			.body("parcelas", is(5))
			.body("seguro", is(false));
			
	}
	

	@Test
	public void testAlterarSimulacaoComEmailInvalido()
	{
		String cpf = "274568701";
		//body da criacao
		String bodyCreate = 
				  "{\n"
				+ "  \"nome\": \"José Fulano\",\n"
				+ "  \"cpf\": " + cpf + ",\n"
				+ "  \"email\": \"jj@email.com\",\n"
				+ "  \"valor\": 1000,\n"
				+ "  \"parcelas\": 2,\n"
				+ "  \"seguro\": true\n"
				+ "}";
		
		//criar simulacao para alteracoes
		given()
			.contentType(ContentType.JSON)
			.body(bodyCreate)
		.when()
			.post()
		.then();
		
		String validacaoEmail = "E-mail deve ser um e-mail válido";
		
		//body da alteracao
		String bodyUpdate = 
				  "{\n"
				+ "  \"email\": \"jta@email\",\n"
				+ "  \"parcelas\": 10,\n"
				+ "  \"seguro\": false\n"
				+ "}";
		
		//alterar simulacao
		String erro = 
			given()
				.contentType(ContentType.JSON)
				.body(bodyUpdate)
				.pathParam("cpf", cpf)
			.when()
				.put("/{cpf}")
			.then()
				.statusCode(400)
				.extract()
					.path("erros.email");
		
		Assert.assertEquals(validacaoEmail, erro);
	}

	@Test
	public void testAlterarSimulacaoComCPFEmFormatoErrado()
	{
		String cpf = "47134566";
		//body da criacao
		String bodyCreate = 
				  "{\n"
				+ "  \"nome\": \"José Fulano\",\n"
				+ "  \"cpf\": " + cpf + ",\n"
				+ "  \"email\": \"jj@email.com\",\n"
				+ "  \"valor\": 1000,\n"
				+ "  \"parcelas\": 2,\n"
				+ "  \"seguro\": true\n"
				+ "}";
		
		//criar simulacao para alteracoes
		given()
			.contentType(ContentType.JSON)
			.body(bodyCreate)
		.when()
			.post()
		.then();
		
		//não existe mensagem de erro		
		//body da alteracao
		String bodyUpdate = 
				  "{\n"
				+ "  \"cpf\": 745.315.658-43,\n"
				+ "  \"parcelas\": 5,\n"
				+ "  \"seguro\": false\n"
				+ "}";
		
		//alterar simulacao
		given()
			.contentType(ContentType.JSON)
			.body(bodyUpdate)
			.pathParam("cpf", cpf)
		.when()
			.put("/{cpf}")
		.then()
			.statusCode(400);
	}
	
	@Test
	public void testAlterarSimulacaoComValorAbaixoDe1000()
	{
		String cpf = "999452111";
		//body da criacao
		String bodyCreate = 
				  "{\n"
				+ "  \"nome\": \"José Fulano\",\n"
				+ "  \"cpf\": " + cpf + ",\n"
				+ "  \"email\": \"jj@email.com\",\n"
				+ "  \"valor\": 1000,\n"
				+ "  \"parcelas\": 2,\n"
				+ "  \"seguro\": true\n"
				+ "}";
		
		//criar simulacao para alteracoes
		given()
			.contentType(ContentType.JSON)
			.body(bodyCreate)
		.when()
			.post()
		.then();
		
		//não existe mensagem de erro		
		//body da alteracao
		String bodyUpdate = 
				  "{\n"
				+ "  \"valor\": 900,\n"
				+ "  \"seguro\": false\n"
				+ "}";
		
		//alterar simulacao
		given()
			.contentType(ContentType.JSON)
			.body(bodyUpdate)
			.pathParam("cpf", cpf)
		.when()
			.put("/{cpf}")
		.then()
			.statusCode(400);
	}
	
	@Test
	public void testAlterarSimulacaoComValorAcimaDe40000()
	{
		String cpf = "777111451";
		//body da criacao
		String bodyCreate = 
				  "{\n"
				+ "  \"nome\": \"José Fulano\",\n"
				+ "  \"cpf\": " + cpf + ",\n"
				+ "  \"email\": \"jj@email.com\",\n"
				+ "  \"valor\": 1000,\n"
				+ "  \"parcelas\": 2,\n"
				+ "  \"seguro\": true\n"
				+ "}";
		
		//criar simulacao para alteracoes
		given()
			.contentType(ContentType.JSON)
			.body(bodyCreate)
		.when()
			.post()
		.then();
		
		String validacaoValor = "Valor deve ser menor ou igual a R$ 40.000";
		
		//body da alteracao
		String bodyUpdate = 
				  "{\n"
				+ "  \"valor\": 900,\n"
				+ "  \"seguro\": false\n"
				+ "}";
		
		//alterar simulacao
		String erro = 
			given()
				.contentType(ContentType.JSON)
				.body(bodyUpdate)
				.pathParam("cpf", cpf)
			.when()
				.put("/{cpf}")
			.then()
				.statusCode(400)
				.extract()
					.path("erros.valor");
		
		Assert.assertEquals(validacaoValor, erro);
	}
	
	@Test
	public void testAlterarSimulacaoComParcelasMenorQue2()
	{
		String cpf = "611134521";
		//body da criacao
		String bodyCreate = 
				  "{\n"
				+ "  \"nome\": \"José Fulano\",\n"
				+ "  \"cpf\": " + cpf + ",\n"
				+ "  \"email\": \"jj@email.com\",\n"
				+ "  \"valor\": 1000,\n"
				+ "  \"parcelas\": 2,\n"
				+ "  \"seguro\": true\n"
				+ "}";
		
		//criar simulacao para alteracoes
		given()
			.contentType(ContentType.JSON)
			.body(bodyCreate)
		.when()
			.post()
		.then();
		
		String validacaoParcelas = "Parcelas deve ser igual ou maior que 2";
		
		//body da alteracao
		String bodyUpdate = 
				  "{\n"
				+ "  \"valor\": 900,\n"
				+ "  \"parcelas\": 0,\n"
				+ "  \"seguro\": false\n"
				+ "}";
		
		//alterar simulacao
		String erro = 
			given()
				.contentType(ContentType.JSON)
				.body(bodyUpdate)
				.pathParam("cpf", cpf)
			.when()
				.put("/{cpf}")
			.then()
				.statusCode(400)
				.extract()
					.path("erros.parcelas");
		
		Assert.assertEquals(validacaoParcelas, erro);
	}
	
	@Test
	public void testAlterarSimulacaoComParcelasMaiorQue48()
	{
		String cpf = "222134442";
		//body da criacao
		String bodyCreate = 
				  "{\n"
				+ "  \"nome\": \"José Fulano\",\n"
				+ "  \"cpf\": " + cpf + ",\n"
				+ "  \"email\": \"jj@email.com\",\n"
				+ "  \"valor\": 1000,\n"
				+ "  \"parcelas\": 2,\n"
				+ "  \"seguro\": true\n"
				+ "}";
		
		//criar simulacao para alteracoes
		given()
			.contentType(ContentType.JSON)
			.body(bodyCreate)
		.when()
			.post()
		.then();
		
		//não existe mensagem de erro
		//body da alteracao
		String bodyUpdate = 
				  "{\n"
				+ "  \"email\": \"jfa@email.com\",\n"
				+ "  \"valor\": 900,\n"
				+ "  \"parcelas\": 50,\n"
				+ "  \"seguro\": true\n"
				+ "}";
		
		//alterar simulacao
			given()
				.contentType(ContentType.JSON)
				.body(bodyUpdate)
				.pathParam("cpf", cpf)
			.when()
				.put("/{cpf}")
			.then()
				.statusCode(400);
	}
	
	@Test
	public void testAlterarSimulacaoParaCpfJaExistente()
	{
		String cpf = "2214421474";
		//body da criacao
		String bodyCreate = 
				  "{\n"
				+ "  \"nome\": \"José Fulano\",\n"
				+ "  \"cpf\": " + cpf + ",\n"
				+ "  \"email\": \"jj@email.com\",\n"
				+ "  \"valor\": 1000,\n"
				+ "  \"parcelas\": 2,\n"
				+ "  \"seguro\": true\n"
				+ "}";
		
		//criar simulacao para alteracoes
		given()
			.contentType(ContentType.JSON)
			.body(bodyCreate)
		.when()
			.post()
		.then();
		
		String validacaoCpfDuplicado = "CPF duplicado";
		
		//body da alteracao
		String bodyUpdate = 
				  "{\n"
				+ "  \"cpf\": " + cpfDuplicado +",\n"
				+ "  \"email\": \"jta@email.com\",\n"
				+ "  \"valor\": 4500,\n"
				+ "  \"parcelas\": 5,\n"
				+ "  \"seguro\": false\n"
				+ "}";
		
		//alterar simulacao
		String erro = 
				given()
					.contentType(ContentType.JSON)
					.body(bodyUpdate)
					.pathParam("cpf", cpf)
				.when()
					.put("/{cpf}")
				.then()
					.statusCode(400)
					.extract()
						.path("mensagem");
			
			Assert.assertEquals(validacaoCpfDuplicado, erro);
	}
	
	@Test
	public void testAlterarSimulacaoParaCpfInexistente()
	{
		String validacaoCpfInexistente = "CPF " + cpfInexistente + " não encontrado";
		
		//body da alteracao
		String bodyUpdate = 
				  "{\n"
				+ "  \"email\": \"jta@email.com\",\n"
				+ "  \"parcelas\": 5,\n"
				+ "  \"seguro\": false\n"
				+ "}";
		
		//alterar simulacao
		String erro = 
				given()
					.contentType(ContentType.JSON)
					.body(bodyUpdate)
					.pathParam("cpf", cpfInexistente)
				.when()
					.put("/{cpf}")
				.then()
					.statusCode(404)
					.extract()
						.path("mensagem");
			
			Assert.assertEquals(validacaoCpfInexistente, erro);
	}
}
