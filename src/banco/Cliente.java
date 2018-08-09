package banco;

import java.time.LocalDate;

public class Cliente {

	private String nome;
	private long cpf;
	private String rg;
	private LocalDate dataNascimento;
	
	public Cliente(String nome, long cpf, String rg, LocalDate dataNascimento) {
		this.nome = nome;
		this.cpf = cpf;
		this.rg = rg;
		this.dataNascimento = dataNascimento;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public long getCpf() {
		return cpf;
	}

	public void setCpf(long cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	public String getDataNascFormat() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(dataNascimento.getDayOfMonth());
		sb.append("/");
		sb.append(dataNascimento.getMonth());
		sb.append("/");
		sb.append(dataNascimento.getYear());
		
		return sb.toString();
	}

}
