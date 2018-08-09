package bot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;

import banco.Cliente;
import banco.Conta;
import banco.Emprestimo;
import banco.Lancamento;

public class JBankBot extends Thread {
	
	private TelegramBot bot;
	private int chatId;
	private List<Conta> contas;
	private String ultimaMsg;
	
	// Bot e Token
	private final String botNome  = "@JBankBot";
	private final String botToken = "620078088:AAF5BHrtkNcvJKHUfwMuAEVh86IyhPuYNfc";
	
	// Opções Menu principal
	private final String criarConta 		= "Criar Conta";
	private final String modificarConta 	= "Modificar Conta";
	private final String incluirDependente 	= "Incluir Dependente"; 
	private final String exibirDados 		= "Exibir Dados da Conta";
	private final String deposito 			= "Depósito";
	private final String saque 				= "Saque";
	private final String extrato 			= "Extrato";
	private final String emprestimo 		= "Empréstimo";
	private final String saldoDevedor 		= "Saldo Devedor";
	private final String lancamentos 		= "Lançamentos";
	private final String retiradas 			= "Retiradas";
	private final String tarifas 			= "Tarifas";
	private final String ajuda 				= "Ajuda";
	
	// Opções Menu Modificar Conta
	private final String nomeCliente		= "Nome";
	private final String cpfCliente			= "CPF";
	private final String rgCliente			= "RG";
	private final String dataNascCliente	= "Data Nascimento";
	
	public JBankBot() {		
		this.bot = new TelegramBot(botToken);
		this.chatId = 0;
		this.ultimaMsg = "";
		this.contas = new ArrayList<>();
	}
	
	/**
	 * Executa a Thread
	 */
	public void run() {
		
		/**
		 * TESTE
		 */
		contas.add(new Conta(new Cliente("Astolfo", 12345, "37424", LocalDate.parse("10/10/2000", DateTimeFormatter.ofPattern("dd/MM/yyyy")))));
		contas.add(new Conta(new Cliente("Bernardete", 345345, "4761324", LocalDate.parse("11/05/1980", DateTimeFormatter.ofPattern("dd/MM/yyyy")))));
		contas.add(new Conta(new Cliente("Joao", 54353, "5422", LocalDate.parse("07/11/1963", DateTimeFormatter.ofPattern("dd/MM/yyyy")))));
		contas.get(2).addDependente(new Cliente("Maria", 534543, "76565", LocalDate.parse("31/10/1980", DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
		contas.get(2).addDependente(new Cliente("Dolores", 765756, "4238", LocalDate.parse("30/07/1920", DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
		contas.get(2).deposito(100f);
		contas.get(2).deposito(2000f);
		contas.get(2).saque(90f);
		
		while (true) {
			
			String msg = lerMensagem();			
			processaMensagem(msg);
		}
		
	}
	
	/**
	 * Lê mensagem
	 */
	public String lerMensagem() {
		
		List<Update> updates;
		Message mensagem;
		String msgAtual = "";
		
		do {
		
			updates = bot.execute(new GetUpdates().limit(100).offset(0).timeout(0)).updates();
			
			if (updates.size() > 0) {
				
				mensagem = updates.get(updates.size() - 1).message();
				msgAtual = mensagem.text();
				
				// Seta o Id do usuário que está interagindo com o Bot
				if (chatId == 0) {
					chatId = mensagem.from().id();
					
					String msg = "Bem vindo ao JBankBot!\n" +
								 "\nÉ um prazer ter você como nosso cliente." +
								 "\nLorem ipsum dolor sit amet, consectetur adipiscing elit." +
								 "\nQuisque nec metus nec augue molestie aliquam nec eget est." +
								 "\nQuisque sed orci iaculis nunc tempus lacinia. Donec maximus auctor mollis." +
								 "\nAenean rhoncus eleifend mauris." +
								 "\nPhasellus sapien dolor, efficitur in nibh ac, convallis finibus mi." +
								 "\nNam tristique, nisi sed sagittis mattis, tortor mi hendrerit dui, quis egestas felis lacus in ipsum.";
					
					enviarMensagem(chatId, msg);
					mostrarMenu();
				}
			}
				
			try {
				sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		} while (ultimaMsg.equals(msgAtual));
		
		ultimaMsg = msgAtual;
		
		return msgAtual;
	}
	
	/**
	 * Processa mensagem
	 * 
	 * @param mensagem
	 */
	public void processaMensagem(String mensagem) {
		
		int c = 2;
		
		switch (mensagem) {
			
			case criarConta :
				Conta conta = criarConta();
				contas.add(conta);
				mostrarMenu();
				break;
					
			case modificarConta :
				//c = selecionarConta();
				modificarConta(contas.get(c));
				mostrarMenu();
				break;
				
			case incluirDependente :
				//c = selecionarConta();
				incluirDependente(contas.get(c));
				mostrarMenu();
				break;
				
			case exibirDados :
				c = selecionarConta();
				exibirConta(contas.get(c));
				mostrarMenu();
				break;
				
			case deposito :
				//c = selecionarConta();
				depositar(contas.get(c));
				mostrarMenu();
				break;
				
			case saque :
				//c = selecionarConta();
				sacar(contas.get(c));
				mostrarMenu();
				break;
				
			case extrato :
				//c = selecionarConta();
				extrato(contas.get(c));
				mostrarMenu();
				break;
				
			case emprestimo :
				//c = selecionarConta();
				emprestimo(contas.get(c));
				mostrarMenu();
				break;
				
			case saldoDevedor :
				//c = selecionarConta();
				saldoDevedor(contas.get(c));
				mostrarMenu();
				break;

			case lancamentos :
				//c = selecionarConta();
				lancamentos(contas.get(c));
				mostrarMenu();
				break;
				
			case retiradas :
				//c = selecionarConta();
				retiradas(contas.get(c));
				mostrarMenu();
				break;
				
			case tarifas :
				//c = selecionarConta();
				tarifas(contas.get(c));
				mostrarMenu();
				break;
			
			default :
				enviarMensagem(chatId, "Desculpe! Não conseguimos processar a informação");
				mostrarMenu();
				break;
		}
	}
	
	/**
	 * Envia mensagem
	 * 
	 * @param chatId
	 * @param mensagem
	 */
	public void enviarMensagem(long chatId, String mensagem) {
		enviarMensagem(chatId, mensagem, new ForceReply());
	}
	
	/**
	 * Envia Mensagem
	 * 
	 * @param chatId
	 * @param mensagem
	 * @param keyboard
	 */
	public void enviarMensagem(long chatId, String mensagem, Keyboard keyboard) {
		
		SendResponse sr = bot.execute(
				new SendMessage(chatId, mensagem)
			        .parseMode(ParseMode.HTML)
			        .disableWebPagePreview(true)
			        .disableNotification(true)
			        .replyToMessageId(1)
			        .replyMarkup(keyboard)
		        );
		
		if (! sr.isOk()) {
			System.out.println(sr.description() + " (" + sr.errorCode() + ")");
			System.out.println("Chat Id: " + chatId);
		}
	}
	
	/**
	 * Deleta Mensagem
	 * 
	 * @param chatId
	 * @param mensagemId
	 */
	public void deletarMensagem(int chatId, int mensagemId) {
		
		BaseResponse br = bot.execute(new DeleteMessage(chatId, mensagemId));
		
		if (! br.isOk()) {
			System.out.println(br.description() + " (" + br.errorCode() + ")");
			System.out.println("Chat Id: " + chatId + " - Mensagem Id: " + mensagemId);
		}
	}
	
	/**
	 * Mostra o Menu de opções
	 */
	public void mostrarMenu() {
		
		Keyboard keyboard = new ReplyKeyboardMarkup(
				
                new String[]{ criarConta, modificarConta },
                new String[]{ incluirDependente, exibirDados },
                new String[]{ deposito, saque },
                new String[]{ extrato, emprestimo },
                new String[]{ saldoDevedor, lancamentos },                
                new String[]{ retiradas, tarifas },                
                new String[]{ ajuda });
		
		enviarMensagem(chatId, "Em que podemos ajudá-lo(a) agora?", keyboard);
	}
	
	/**
	 * Cria uma nova Conta
	 */
	public Conta criarConta() {
		
		// Obtendo o nome do cliente
		enviarMensagem(chatId, "Informe seu nome completo");
		String nome = lerMensagem();
		
		// Obtendo o CPF do cliente
		long cpf = 0;
		
		do {
		
			try {		
				enviarMensagem(chatId, "Informe seu CPF (apenas números)");
				String cpfString = lerMensagem();
				
				cpf = Long.valueOf(cpfString);
				
			} catch(Exception e) {
				enviarMensagem(chatId, "Desculpe! Não conseguimos processar a informação");
			}
			
		} while (cpf == 0);
		
		// Obtendo o RG do cliente
		enviarMensagem(chatId, "Informe seu RG");
		String rg = lerMensagem();
		
		// Obtendo a data de nascimento do cliente
		LocalDate dataNasc = null;
		
		do {
		
			try {		
				enviarMensagem(chatId, "Informe sua data de nascimento (dd/mm/aaaa)");
				String dataString = lerMensagem();
								
				dataNasc = LocalDate.parse(dataString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				
			} catch(Exception e) {
				enviarMensagem(chatId, "Desculpe! Não conseguimos processar a informação");
			}
			
		} while (dataNasc == null);
		
		// Criando objeto Cliente
		Cliente cliente = new Cliente(nome, cpf, rg, dataNasc);
		
		// Criando a conta
		Conta conta = new Conta(cliente);
		
		String msg = "Sua conta foi criada com sucesso! :)" +
					 "\nAg: " + conta.getNumAg() +
					 "\nCc: " + conta.getNumCc();
		
		enviarMensagem(chatId, msg);
		
		return conta;
	}
	
	/**
	 * Mostra todas as contas e permite que o usuário selecione
	 * 
	 * @return	Índice da conta selecionada
	 */
	public int selecionarConta() {
		
		String[] listaContas = new String[contas.size()];
		
		// Monta lista de contas
		for (int i = 0; i < contas.size() - 1; i++) {
			String text = contas.get(i).getNumAg() + "/" + contas.get(i).getNumCc();
			listaContas[i] = text;
		}
		
		Keyboard keyboard = new ReplyKeyboardMarkup(listaContas);
		
		do {			
			enviarMensagem(chatId, "Selecione a conta", keyboard);
			
			String opcao = lerMensagem();
			
			// Procura opcao selecionada na lista de contas
			for (int i = 0; i < listaContas.length - 1; i++) {
				
				if (opcao.equals(listaContas[i].toString())) {
					return i;
				}
			}
			
			enviarMensagem(chatId, "Desculpe! Não conseguimos processar a informação");
		
		} while (true);
	}
	
	/**
	 * Modifica uma Conta
	 */
	public void modificarConta(Conta conta) {
		
		Keyboard keyboard = new ReplyKeyboardMarkup(
				
	        new String[]{ nomeCliente, cpfCliente },
	        new String[]{ rgCliente, dataNascCliente });
		
		boolean flag = true;
		
		do {
			
			enviarMensagem(chatId, "Qual informação deseja atualizar?", keyboard);
			
			String opcao = lerMensagem();
			
			switch (opcao) {
			
				case nomeCliente :
					enviarMensagem(chatId, "Informe seu nome completo");
					String nome = lerMensagem();
					
					conta.getTitular().setNome(nome);

					flag = false;
					break;
					
				case cpfCliente :
					long cpf = 0;
					do {				
						try {		
							enviarMensagem(chatId, "Informe seu CPF (apenas números)");
							String cpfString = lerMensagem();
							
							cpf = Long.valueOf(cpfString);
							
						} catch(Exception e) {
							enviarMensagem(chatId, "Desculpe! Não conseguimos processar a informação");
						}					
					} while (cpf == 0);
					
					conta.getTitular().setCpf(cpf);

					flag = false;
					break;
			
				case rgCliente :
					enviarMensagem(chatId, "Informe seu RG");
					String rg = lerMensagem();
					
					conta.getTitular().setRg(rg);
					
					flag = false;
					break;
					
				case dataNascCliente :
					LocalDate dataNasc = null;				
					do {
					
						try {		
							enviarMensagem(chatId, "Informe sua data de nascimento (dd/mm/aaaa)");
							String dataString = lerMensagem();
											
							dataNasc = LocalDate.parse(dataString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
							
						} catch(Exception e) {
							enviarMensagem(chatId, "Desculpe! Não conseguimos processar a informação");
						}
						
					} while (dataNasc == null);
					
					conta.getTitular().setDataNascimento(dataNasc);
					
					flag = false;
					break;
					
				default : 
					enviarMensagem(chatId, "Desculpe! Não conseguimos processar a informação");
					break;
			}
			
		} while (flag);
				
		enviarMensagem(chatId, "Informação atualizada com sucesso! :)");
	}
	
	/**
	 * Inclui um dependente a uma Conta
	 * 
	 * @param conta
	 */
	public void incluirDependente(Conta conta) {
		
		// Obtendo o nome do cliente
		enviarMensagem(chatId, "Informe o nome do dependente");
		String nome = lerMensagem();
		
		// Obtendo o CPF do cliente
		long cpf = 0;
		
		do {
		
			try {		
				enviarMensagem(chatId, "Informe o CPF do dependente (apenas números)");
				String cpfString = lerMensagem();
				
				cpf = Long.valueOf(cpfString);
				
			} catch(Exception e) {
				enviarMensagem(chatId, "Desculpe! Não conseguimos processar a informação");
			}
			
		} while (cpf == 0);
		
		// Obtendo o RG do cliente
		enviarMensagem(chatId, "Informe o RG do dependente");
		String rg = lerMensagem();
		
		// Obtendo a data de nascimento do cliente
		LocalDate dataNasc = null;
		
		do {
		
			try {		
				enviarMensagem(chatId, "Informe a data de nascimento do dependente (dd/mm/aaaa)");
				String dataString = lerMensagem();
								
				dataNasc = LocalDate.parse(dataString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				
			} catch(Exception e) {
				enviarMensagem(chatId, "Desculpe! Não conseguimos processar a informação");
			}
			
		} while (dataNasc == null);
		
		// Criando objeto Cliente
		Cliente cliente = new Cliente(nome, cpf, rg, dataNasc);
		
		// Adicionando o dependente à conta
		conta.addDependente(cliente);
		
		enviarMensagem(chatId, "O dependente foi adicionado a conta! :)");
	}
	
	/**
	 * Exibe dados de uma Conta
	 * 
	 * @param conta
	 */
	public void exibirConta(Conta conta) {
		
		String msg1 = "Ag: " + conta.getNumAg() +
					  "\nCc: " + conta.getNumCc();
		enviarMensagem(chatId, msg1);
		
		String msg2 = "Informações do Titular:" +
					  "\nNome: " + conta.getTitular().getNome() +
					  "\nCPF: " + conta.getTitular().getCpf() +
					  "\nRG: " + conta.getTitular().getRg() +
					  "\nData Nascimento: " + conta.getTitular().getDataNascFormat();
		enviarMensagem(chatId, msg2);
		
		// Conta possui Dependentes
		if (conta.getDependentes().size() > 0) {
			
			for (int i = 0; i < conta.getDependentes().size(); i++) {
			
				Cliente dependente = conta.getDependentes().get(i);
				
				String msg3 = "Informações do Dependente " + (i + 1) + ":" +
						  "\nNome: " + dependente.getNome() +
						  "\nCPF: " + dependente.getCpf() +
						  "\nRG: " + dependente.getRg() +
						  "\nData Nascimento: " + dependente.getDataNascFormat();
				enviarMensagem(chatId, msg3);
			}
		}
	}
	
	/**
	 * Deposita um valor na conta
	 * 
	 * @param conta
	 */
	public void depositar(Conta conta) {
		
		double valor = 0f;
		
		do {
		
			try {		
				enviarMensagem(chatId, "Informe o valor a ser depositado");
				String valString = lerMensagem();
								
				valor = Double.parseDouble(valString);
				
			} catch(Exception e) {
				enviarMensagem(chatId, "Desculpe! Não conseguimos processar a informação");
			}
			
		} while (valor == 0f);
		
		conta.deposito(valor);
		enviarMensagem(chatId, "Depósito efetuado com sucesso! :)\nSaldo atual: R$ " + conta.getSaldo());
	}
	
	/**
	 * Saca um valor da conta
	 * 
	 * @param conta
	 */
	public void sacar(Conta conta) {
		
		double valor = 0f;
		
		do {
		
			try {		
				enviarMensagem(chatId, "Informe o valor a ser sacado (será cobrada uma taxa de R$ 2,50)");
				String valString = lerMensagem();
								
				valor = Double.parseDouble(valString);
				
			} catch(Exception e) {
				enviarMensagem(chatId, "Desculpe! Não conseguimos processar a informação");
			}
			
		} while (valor == 0f);
		
		if (conta.saque(valor)) {
			
			enviarMensagem(chatId, "Saque efetuado com sucesso! :)\nSaldo atual: R$ " + conta.getSaldo());
			
		} else {
			enviarMensagem(chatId, "Saldo insuficiente para o saque! :(\nSaldo atual: R$ " + conta.getSaldo());
		}
	}
	
	public void extrato(Conta conta) {
		
		List<Lancamento> extrato = conta.extrato();
		
		if (extrato.size() > 0) {
			
			StringBuilder sb = new StringBuilder();
			
			for(Lancamento lc: extrato) {
				sb.append(lc.getExtrato());
				sb.append("\n");
			}
			
			sb.append("Saldo atual: R$ " + conta.getSaldo());
			
			enviarMensagem(chatId, sb.toString());
			
		} else {
			
			if (conta.getSaldo() < 1f) {
				enviarMensagem(chatId, "Saldo insuficiente para o extrato! :(\nSaldo atual: R$ " + conta.getSaldo());
				
			} else {
				enviarMensagem(chatId, "Não há informções para exibir");
			}
		}
	}
	
	public void emprestimo(Conta conta) {
					
		double valorContratado = 0f;
		
		do {
		
			try {		
				enviarMensagem(chatId, "Informe o valor do empréstimo");		
				String valorStr = lerMensagem();
				
				valorContratado = Long.valueOf(valorStr);
				
				if (valorContratado > (conta.getSaldo() * 40)) {
					
					enviarMensagem(chatId,
							"Valor do empréstimo deve ser menor ou igual a 40x o saldo da sua conta.\nSaldo atual: R$ "
									+ conta.getSaldo());
					
					valorContratado = 0f;
				}
				
			} catch(Exception e) {
				enviarMensagem(chatId, "Desculpe! Não conseguimos processar a informação");
			}
			
		} while (valorContratado == 0f);
		
		int qtdeMeses = 0;
		
		do {
		
			try {		
				enviarMensagem(chatId, "Informe a quantidade de meses do empréstimo");		
				String mesesStr = lerMensagem();
				
				qtdeMeses = Integer.valueOf(mesesStr);
				
				if (qtdeMeses > 36) {
					
					enviarMensagem(chatId, "Quantidade de meses do empréstimo deve ser menor que 3 anos");
					
					qtdeMeses = 0;
				}
				
			} catch(Exception e) {
				enviarMensagem(chatId, "Desculpe! Não conseguimos processar a informação");
			}
			
		} while (qtdeMeses == 0);
		
		Emprestimo emprestimo = new Emprestimo(valorContratado, qtdeMeses);
		
		conta.addLancamento(new Lancamento("Empréstimo", valorContratado, 0f, LocalDateTime.now()));
		conta.addEmprestimo(emprestimo);
		
		String str = "Empréstimo contratado com sucesso! :)" +
				 "\nValor Contratado: R$ " + emprestimo.getValorContratado() +
				 "\nData Contratação: " + emprestimo.getDataContratacao().toString() +
				 "\nQuantidade de Meses: " + emprestimo.getQtdeMeses() +
				 "\nValor Total do Empréstimo: R$ " + emprestimo.getValorEmprestimo() +
				 "\nData Final do Empréstimo: R$ " + emprestimo.getDataFinalEmprestimo().toString();
		
		enviarMensagem(chatId, str);
	}
	
	public void saldoDevedor(Conta conta) {
		
		if (conta.getEmprestimos().size() > 0) {
		
			for (int i = 0; i < conta.getEmprestimos().size(); i++) {
			
				String str = "Empréstimo +" + (i + 1) +
						 "\nValor Contratado: R$ " + conta.getEmprestimos().get(i).getValorContratado() +
						 "\nData Contratação: " + conta.getEmprestimos().get(i).getDataContratacao().toString() +
						 "\nQuantidade de Meses: " + conta.getEmprestimos().get(i).getQtdeMeses() +
						 "\nValor Total do Empréstimo: R$ " + conta.getEmprestimos().get(i).getValorEmprestimo() +
						 "\nData Final do Empréstimo: R$ " + conta.getEmprestimos().get(i).getDataFinalEmprestimo().toString();
			
				enviarMensagem(chatId, str);
			}
			
		} else {
			enviarMensagem(chatId, "Você não possui empréstimos contratados!");
		}
	}
	
	public void lancamentos(Conta conta) {
		
		List<Lancamento> lancamentos = conta.getLancamentos();
		
		if (lancamentos.size() > 0) {
			
			double somaDepositos = 0f;
			double somaSaques 	 = 0f;
			
			int totalDepositos	 = 0;
			int totalSaques		 = 0;
			int totalExtratos	 = 0;
			
			StringBuilder sb = new StringBuilder();
			
			for(Lancamento lc: lancamentos) {
				
				sb.append(lc.toString());
				sb.append("\n");
				
				switch (lc.getDescricao()) {
				
					case "Saque" :
						somaSaques += lc.getValor();
						totalSaques++;
						break;
						
					case "Depósito" :
						somaDepositos += lc.getValor();
						totalDepositos++;
						break;
					
					case "Extrato" :
						totalDepositos++;
						break;
				}
			}
			
			sb.append("\nValor Depositado: R$ " + somaDepositos);
			sb.append("\nValor Sacado: R$ " + somaSaques);
			sb.append("\n\nTotal de Depósitos: " + totalDepositos);
			sb.append("\nTotal de Saques: " + totalSaques);
			sb.append("\nTotal de Extratos: " + totalExtratos);
			
			enviarMensagem(chatId, sb.toString());
			
		} else {
			enviarMensagem(chatId, "Não há informções para exibir");
		}
	}
	
	public void retiradas(Conta conta) {
		
		List<Lancamento> retiradas = conta.tarifas();
		
		if (retiradas.size() > 0) {
			
			double somaRetiradas = 0f;
			StringBuilder sb = new StringBuilder();
			
			for(Lancamento lc: retiradas) {
				
				sb.append(lc.getRetirada());
				sb.append("\n");
				
				somaRetiradas += lc.getTarifa();
			}
			
			sb.append("Total Retiradas: R$ " + somaRetiradas);
			
			enviarMensagem(chatId, sb.toString());
			
		} else {
			enviarMensagem(chatId, "Não há informções para exibir");
		}
	}
	
	public void tarifas(Conta conta) {
		
		List<Lancamento> tarifas = conta.tarifas();
		
		if (tarifas.size() > 0) {
			
			double somaTarifas = 0f;
			StringBuilder sb = new StringBuilder();
			
			for(Lancamento lc: tarifas) {
				
				sb.append(lc.getTarifaServico());
				sb.append("\n");
				
				somaTarifas += lc.getTarifa();
			}
			
			sb.append("Total Tarifas: R$ " + somaTarifas);
			
			enviarMensagem(chatId, sb.toString());
			
		} else {
			enviarMensagem(chatId, "Não há informções para exibir");
		}
	}
	
}