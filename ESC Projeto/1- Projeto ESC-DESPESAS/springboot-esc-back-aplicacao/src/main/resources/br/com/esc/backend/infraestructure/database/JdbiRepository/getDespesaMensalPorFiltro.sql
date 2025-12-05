SELECT DISTINCT
    a.id_Despesa,
    a.id_DetalheDespesa,
	UPPER(LTRIM(RTRIM(CASE WHEN a.ds_NomeDespesa = '*EMP' THEN emp.ds_TituloEmprestimo ELSE a.ds_NomeDespesa END))) AS ds_TituloDespesa,
	UPPER(LTRIM(RTRIM(a.ds_NomeDespesa))) AS ds_NomeDespesa,
	REPLACE(a.vl_Limite,',','.') AS vl_Limite,
	a.id_OrdemExibicao,
	a.id_Funcionario,
	a.id_Emprestimo,
	a.id_Consolidacao,
    ISNULL(a.tp_Reprocessar, 'N') AS tp_Reprocessar,
    ISNULL(a.tp_Emprestimo, 'N') AS tp_Emprestimo,
    ISNULL(a.tp_Poupanca, 'N') AS tp_Poupanca,
	ISNULL(a.tp_Anotacao, 'N') AS tp_Anotacao,
	ISNULL(a.tp_DebitoAutomatico, 'N') AS tp_DebitoAutomatico,
	ISNULL(a.tp_Meta, 'N') AS tp_Meta,
	ISNULL(a.tp_LinhaSeparacao, 'N') AS tp_LinhaSeparacao,
	ISNULL(a.tp_DespesaReversa, 'N') as tp_DespesaReversa,
    ISNULL(a.tp_PoupancaNegativa, 'N') as tp_PoupancaNegativa,
	ISNULL(a.tp_Relatorio, 'N') as tp_Relatorio,
	ISNULL(a.tp_DebitoCartao, 'N') as tp_DebitoCartao,
	ISNULL(a.tp_EmprestimoAPagar, 'N') as tp_EmprestimoAPagar,
	ISNULL(a.tp_ReferenciaSaldoMesAnterior, 'N') as tp_ReferenciaSaldoMesAnterior,
	ISNULL(a.tp_VisualizacaoTemp, 'N') as tp_VisualizacaoTemp,
	ISNULL(a.tp_DespesaCompartilhada, 'N') as tp_DespesaCompartilhada,
	ISNULL(a.tp_DespesaConsolidacao, 'N') as tp_DespesaConsolidacao
FROM
    tbd_DespesaMensal a
    LEFT JOIN tbd_Emprestimos emp ON emp.id_Emprestimo = a.id_Emprestimo AND emp.id_Funcionario = a.id_Funcionario
WHERE
    a.id_Despesa = :idDespesa
    AND (:idDetalheDespesa IS NULL OR a.id_DetalheDespesa = :idDetalheDespesa)
    AND a.id_Funcionario = :idFuncionario
GROUP BY
    (CASE WHEN a.ds_NomeDespesa = '*EMP' THEN emp.ds_TituloEmprestimo  ELSE a.ds_NomeDespesa END),a.id_Despesa, a.id_Funcionario, a.vl_Limite, a.id_DetalheDespesa, a.id_Consolidacao,a.tp_Emprestimo,a.id_Emprestimo,a.tp_Poupanca,a.tp_Anotacao,a.ds_NomeDespesa,a.tp_DebitoAutomatico,a.id_OrdemExibicao,a.tp_LinhaSeparacao,a.tp_DespesaReversa,a.tp_PoupancaNegativa,a.tp_Relatorio,a.tp_ReferenciaSaldoMesAnterior,a.tp_DespesaCompartilhada,a.tp_Reprocessar, a.tp_Meta, a.tp_DebitoCartao, a.tp_EmprestimoAPagar, a.tp_VisualizacaoTemp, a.tp_DespesaConsolidacao
ORDER BY
    a.id_OrdemExibicao,a.id_DetalheDespesa
