package banco;

import java.time.LocalDateTime;

public class Lancamento {

	private String descricao;
	private double valor;
	private double tarifa;
	private LocalDateTime dataHora;
	
	public Lancamento(String descricao, double valor, double tarifa, LocalDateTime dataHora) {
		this.descricao = descricao;
		this.valor = valor;
		this.tarifa = tarifa;
		this.dataHora = dataHora;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public double getTarifa() {
		return tarifa;
	}

	public void setTarifa(double tarifa) {
		this.tarifa = tarifa;
	}

	public LocalDateTime getDataHora() {
		return dataHora;
	}

	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}
	
	public String getExtrato() {		
		return dataHora + " - " + descricao + ": " + valor + " (" + tarifa + ")";
	}
	
	public String getRetirada() {
		return dataHora + " - " + valor;
	}
	
	public String getTarifaServico() {
		return dataHora + " - " + tarifa;
	}

}
