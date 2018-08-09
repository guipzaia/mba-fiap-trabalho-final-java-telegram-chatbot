package banco;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Emprestimo {

	private LocalDate dataContratacao;
	private int qtdeMeses;
	private double valorContratado;
	private double valorEmprestimo;
	
	public Emprestimo(double valorContratado,int qtdeMeses) {
		this.dataContratacao = LocalDateTime.now().toLocalDate();
		this.qtdeMeses = qtdeMeses;
		this.valorContratado = valorContratado;
		this.valorEmprestimo = valorContratado + (valorContratado * 0.05 * qtdeMeses);	
	}

	public LocalDate getDataContratacao() {
		return dataContratacao;
	}

	public void setDataContratacao(LocalDate dataContratacao) {
		this.dataContratacao = dataContratacao;
	}

	public int getQtdeMeses() {
		return qtdeMeses;
	}

	public void setQtdeMeses(int qtdeMeses) {
		this.qtdeMeses = qtdeMeses;
	}

	public double getValorEmprestimo() {
		return valorEmprestimo;
	}

	public void setValorEmprestimo(double valorEmprestimo) {
		this.valorEmprestimo = valorEmprestimo;
	}

	public double getValorContratado() {
		return valorContratado;
	}

	public void setValorContratado(double valorContratado) {
		this.valorContratado = valorContratado;
	}

	public LocalDate getDataFinalEmprestimo() {
		return dataContratacao.plusMonths(getQtdeMeses());
	}
}
