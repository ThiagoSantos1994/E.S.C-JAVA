SELECT DISTINCT
	UPPER(LTRIM(RTRIM(CASE WHEN a.ds_NomeDespesa = '*EMP' THEN emp.ds_TituloEmprestimo ELSE a.ds_NomeDespesa END))) AS ds_TituloDespesa,
	UPPER(LTRIM(RTRIM(a.ds_NomeDespesa))) AS ds_NomeDespesa,
	REPLACE(a.vl_Limite,',','.') AS vl_Limite,
	ISNULL(SUM(CAST(REPLACE(REPLACE(REPLACE(b.vl_Total, '.', ''), ',', '.'), '- ', '-') AS DECIMAL(10,2))),0) AS vl_TotalDespesa,
	a.id_DetalheDespesa,
	a.id_Emprestimo,
    a.id_OrdemExibicao,
    ISNULL(a.tp_Emprestimo, 'N') AS tp_Emprestimo,
    ISNULL(a.tp_Poupanca, 'N') AS tp_Poupanca,
	ISNULL(a.tp_Anotacao, 'N') AS tp_Anotacao,
	ISNULL(a.tp_DebitoAutomatico, 'N') AS tp_DebitoAutomatico,
	ISNULL(a.tp_LinhaSeparacao, 'N') AS tp_LinhaSeparacao,
	ISNULL(a.tp_DespesaReversa, 'N') as tp_DespesaReversa,
    ISNULL(a.tp_PoupancaNegativa, 'N') as tp_PoupancaNegativa,
	ISNULL(a.tp_Relatorio, 'N') as tp_Relatorio,
	ISNULL(a.tp_ReferenciaSaldoMesAnterior, 'N') as tp_ReferenciaSaldoMesAnterior,
	ISNULL(a.tp_DespesaCompartilhada, 'N') as tp_DespesaCompartilhada
FROM
    tbd_DespesaMensal a
    LEFT JOIN tbd_DetalheDespesasMensais b ON b.id_Despesa = a.id_Despesa AND b.id_Funcionario = a.id_Funcionario AND b.id_DetalheDespesa = a.id_DetalheDespesa AND b.tp_Anotacao = 'N' AND b.tp_LinhaSeparacao = 'N'
    LEFT JOIN tbd_Emprestimos emp ON emp.id_Emprestimo = a.id_Emprestimo AND emp.id_Funcionario = a.id_Funcionario
WHERE
    a.id_Despesa = :idDespesa
    AND a.id_Funcionario = :idFuncionario
GROUP BY
    (CASE WHEN a.ds_NomeDespesa = '*EMP' THEN emp.ds_TituloEmprestimo  ELSE a.ds_NomeDespesa END),a.vl_Limite, a.id_DetalheDespesa,a.tp_Emprestimo,a.id_Emprestimo,a.tp_Poupanca,a.tp_Anotacao,a.ds_NomeDespesa,a.tp_DebitoAutomatico,a.id_OrdemExibicao,a.tp_LinhaSeparacao,a.tp_DespesaReversa,a.tp_PoupancaNegativa,a.tp_Relatorio,a.tp_ReferenciaSaldoMesAnterior,a.tp_DespesaCompartilhada
ORDER BY
    a.id_OrdemExibicao,a.id_DetalheDespesa
