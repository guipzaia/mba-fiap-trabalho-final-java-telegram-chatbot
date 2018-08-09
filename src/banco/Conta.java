package banco;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Conta {

	// Informações Cadastrais da conta
	private int numAg;
	private int numCc;
	private Cliente titular;
	private List<Cliente> dependentes;
	private List<Lancamento> lancamentos;
	private List<Emprestimo> emprestimos;

	// Informações Financeiras da conta
	private double saldo;

	public Conta(Cliente cliente) {
		this.numAg = new Random().nextInt(9999);
		this.numCc = new Random().nextInt(999999);
		this.titular = cliente;
		this.dependentes = new ArrayList<>();
		this.lancamentos = new ArrayList<>();
		this.emprestimos = new ArrayList<>();
		this.saldo = 0f;
	}

	public int getNumAg() {
		return numAg;
	}

	public void setNumAg(int numAg) {
		this.numAg = numAg;
	}

	public int getNumCc() {
		return numCc;
	}

	public void setNumCc(int numCc) {
		this.numCc = numCc;
	}

	public Cliente getTitular() {
		return titular;
	}

	public void setTitular(Cliente titular) {
		this.titular = titular;
	}

	public List<Cliente> getDependentes() {
		return dependentes;
	}

	public void setDependentes(List<Cliente> dependentes) {
		this.dependentes = dependentes;
	}

	public void addDependente(Cliente cliente) {
		this.dependentes.add(cliente);
	}

	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void setLancamento(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}
	
	public void addLancamento(Lancamento lancamento) {
		this.lancamentos.add(lancamento);
	}

	public List<Emprestimo> getEmprestimos() {
		return emprestimos;
	}

	public void setEmprestimos(List<Emprestimo> emprestimos) {
		this.emprestimos = emprestimos;
	}
	
	public void addEmprestimo(Emprestimo emprestimo) {
		this.emprestimos.add(emprestimo);
	}

	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	
	public void deposito(double valor) {
		this.lancamentos.add(new Lancamento("Depósito", valor, 0f, LocalDateTime.now()));
		this.saldo += valor;
	}
	
	public boolean saque(double valor) {
		
		if ((saldo - valor - 2.50) >= 0f) {
			
			this.saldo -= valor + 2.50;			
			this.lancamentos.add(new Lancamento("Saque", valor, 2.50, LocalDateTime.now()));
			
			return true;
			
		} else {
			return false;
		}
	}

	public List<Lancamento> extrato() {
		
		List<Lancamento> extrato = null;
		
		if (saldo - 1f >= 0f) {
			lancamentos.add(new Lancamento("Extrato", 0f, 1f, LocalDateTime.now()));
			
			extrato = lancamentos.stream()
						.filter(o -> o.getDescricao().equals("Saque") ||
									 o.getDescricao().equals("Depósito"))
						.collect(Collectors.toList());
		}
		
		return extrato;
	}
	
	public List<Lancamento> retiradas() {
		
		return lancamentos.stream()
				.filter(o -> o.getDescricao().equals("Saque"))
				.collect(Collectors.toList());
		
	}

	public List<Lancamento> tarifas() {
		
		return lancamentos.stream()
				.filter(o -> o.getTarifa() > 0f)
				.collect(Collectors.toList());
		
	}
}
