package com.techbloom.smartrh.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.techbloom.smartrh.model.Colaborador;
import com.techbloom.smartrh.records.CalculoSalarioRecords;
import com.techbloom.smartrh.records.FaixaImpostoRecords;
import com.techbloom.smartrh.records.HoleriteRecords;
import com.techbloom.smartrh.repository.ColaboradorRepository;

@Service
public class CalcularSalarioService {
	
	 	@Autowired
	    private ColaboradorRepository colaboradorRepository;
	 
	 	private static final List<FaixaImpostoRecords> FAIXAS_INSS = List.of(
	 			
		        new FaixaImpostoRecords(1518.00, 7.5, 0),
		        new FaixaImpostoRecords(2793.87, 9.0, 28.80),
		        new FaixaImpostoRecords(4190.82, 12.0, 135.57),
		        new FaixaImpostoRecords(8381.66, 14.0, 259.17)
		    );
	 	
	 	 private static final List<FaixaImpostoRecords> FAIXAS_IRRF = List.of(
	 	        new FaixaImpostoRecords(2352.00, 0, 0),
	 	        new FaixaImpostoRecords(2826.65, 7.5, 176.15),
	 	        new FaixaImpostoRecords(3751.05, 15.0, 404.78),
	 	        new FaixaImpostoRecords(4664.68, 22.5, 694.54),
	 	        new FaixaImpostoRecords(Double.MAX_VALUE, 27.5, 917.24)
	 	    );
	 	 
	 	 private static final BigDecimal DEDUCAO_DEPENDENTE = new BigDecimal("179.59");
	 	 
	 	 private static final BigDecimal PERCENTUAL_HORA_EXTRA = new BigDecimal("1.5");
	 	 
	 	 private static final int SCALE = 2;
	 	 
	 	 public HoleriteRecords calcularSalario(Long id, CalculoSalarioRecords dadosSalario) {
	 		 
	 		 Colaborador colaborador = buscarColaborador(id);

	         // 2. Calcula valores básicos
	         BigDecimal salarioPorHora = calcularSalarioPorHora(colaborador);
	         BigDecimal valorHoraExtra = salarioPorHora.multiply(PERCENTUAL_HORA_EXTRA);
	         BigDecimal valorTotalHorasExtras = valorHoraExtra.multiply(new BigDecimal(dadosSalario.totalHorasExtras()));
	         
	         // 3. Soma salário com horas extras (necessário para cálculo dos impostos)
	         BigDecimal salarioComHorasExtras = colaborador.getSalario().add(valorTotalHorasExtras);

	         // 4. Calcula os descontos (INSS e IRRF)
	         BigDecimal descontoINSS = calcularINSS(salarioComHorasExtras);
	         BigDecimal descontoIRRF = calcularIRRF(salarioComHorasExtras, descontoINSS, colaborador.getDependentes());

	         // 5. Soma todos os descontos
	         BigDecimal totalDescontos = descontoINSS.add(descontoIRRF).add(dadosSalario.descontos());
	         
	         // 6. Calcula salário líquido (bruto - descontos)
	         BigDecimal salarioLiquido = salarioComHorasExtras.subtract(totalDescontos);

	         // 7. Retorna o holerite com todos os valores calculados
	         return new HoleriteRecords(
	             colaborador.getSalario(),
	             dadosSalario.totalHorasExtras(),
	             valorHoraExtra,
	             valorTotalHorasExtras,
	             descontoINSS,
	             descontoIRRF,
	             totalDescontos,
	             salarioLiquido
	         );
	 	 }
	 	 
	 	 private Colaborador buscarColaborador(Long id) {
	         Optional<Colaborador> colaborador = colaboradorRepository.findById(id);
	         if (colaborador.isEmpty()) {
	             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Colaborador não encontrado");
	         }
	         return colaborador.get();
	     }
	 	 
	 	  private BigDecimal calcularSalarioPorHora(Colaborador colaborador) {
	 	        return colaborador.getSalario()
	 	            .divide(new BigDecimal(colaborador.getHorasMensais()), SCALE,RoundingMode. HALF_UP);
	 	    }
	 	 
	 	 private BigDecimal calcularINSS(BigDecimal salarioBruto) {
	         return buscarFaixaAplicavel(salarioBruto, FAIXAS_INSS);
	         
	 	 }
	 	 
	 	  private BigDecimal calcularIRRF(BigDecimal salarioBruto, BigDecimal descontoINSS, int dependentes) {
	 		  
	 	        // Calcula a dedução total de dependentes
	 	        BigDecimal deducaoDependentes = DEDUCAO_DEPENDENTE.multiply(BigDecimal.valueOf(dependentes));
	 	        
	 	        // Base de cálculo = Salário - INSS - Deduções dependentes
	 	        BigDecimal baseDeCalculo = salarioBruto
	 	            .subtract(descontoINSS)
	 	            .subtract(deducaoDependentes)
	 	            .setScale(SCALE, RoundingMode.HALF_UP);

	 	        return buscarFaixaAplicavel(baseDeCalculo, FAIXAS_IRRF);
	 	    }
	 	  
	 	 private BigDecimal buscarFaixaAplicavel(BigDecimal valor, List<FaixaImpostoRecords> faixas) {
	 		 
	         for (FaixaImpostoRecords faixa : faixas) {
	             // Se o valor é menor ou igual ao limite da faixa, aplica esta faixa
	             if (valor.compareTo(BigDecimal.valueOf(faixa.limite())) <= 0) {
	                 return aplicarFaixa(valor, faixa);
	             }
	         }
	         // Se não enquadrar em nenhuma faixa (muito raro), retorna ZERO
	         return BigDecimal.ZERO.setScale(SCALE, RoundingMode.HALF_UP);
	     }
	 	 
	 	 private BigDecimal aplicarFaixa(BigDecimal base, FaixaImpostoRecords faixa) {
	 		 
	         // 1. Converte alíquota de percentual para decimal (ex: 7.5% = 0.075)
	         BigDecimal aliquota = BigDecimal.valueOf(faixa.aliquota())
	             .divide(new BigDecimal("100"), SCALE, RoundingMode.HALF_UP);
	         
	         // 2. Multiplica base × alíquota
	         BigDecimal valorAliquota = base.multiply(aliquota)
	             .setScale(SCALE, RoundingMode.HALF_UP);
	         
	         // 3. Subtrai a deução da faixa
	         BigDecimal desconto = valorAliquota.subtract(BigDecimal.valueOf(faixa.deducao()));
	         
	         // 4. Se negativo, retorna ZERO; senão, retorna o desconto
	         return desconto.max(BigDecimal.ZERO)
	             .setScale(SCALE, RoundingMode.HALF_UP);
	     }

}
