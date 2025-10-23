package com.techbloom.smartrh.records;

import java.math.BigDecimal;

public record HoleriteRecords(
		
		BigDecimal salario,
	    int horasExtras,
	    BigDecimal valorHoraExtra,
	    BigDecimal valorTotalHorasExtras,
	    BigDecimal inss,
	    BigDecimal irrf,
	    BigDecimal totalDescontos,
	    BigDecimal salarioLiquido
	    ) {}
